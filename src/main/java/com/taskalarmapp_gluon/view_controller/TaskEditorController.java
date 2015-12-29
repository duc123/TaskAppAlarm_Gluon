/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taskalarmapp_gluon.view_controller;

import com.taskalarmapp_gluon.model.Task;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomTextField;

/**
 * FXML Controller class
 *
 * @author duc
 */
public class TaskEditorController implements Initializable {

    private final static int HOUR_MAX = 24;
    private final static int HOUR_MIN = 1;
    private final static int MINUTE_MAX = 60;
    private final static int MINUTE_MIN = 0;
    private final static int SECOND_MAX = 60;
    private final static int SECOND_MIN = 0;

    @FXML
    private CustomTextField taskNameTextField;
    @FXML
    private Spinner<Integer> hour;
    @FXML
    private Spinner<Integer> minute;
    @FXML
    private Spinner<Integer> second;

    private Task task;
    private Stage dialogStage;
    private boolean okClicked = false;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupSpinner();

    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private void setupSpinner() {

        //Hour Spinner
        hour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(HOUR_MIN, HOUR_MAX, 8, 1));
        hour.setEditable(true);
        hour.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                String text = hour.getEditor().getText();
                int value;
                try {
                    value = Integer.parseInt(text);
                    hour.getValueFactory().setValue(value);
                    hour.getEditor().setText(String.valueOf(value));
                } catch (NumberFormatException ex) {
                    hour.getValueFactory().setValue(0);
                    hour.getEditor().setText(String.valueOf(0));
                }
            }
        });
        //--------------------------------------------
        //Minute Spinner
        minute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(MINUTE_MIN, MINUTE_MAX, 8, 1));
        minute.setEditable(true);
        minute.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                String text = minute.getEditor().getText();
                int value;
                try {
                    value = Integer.parseInt(text);
                    minute.getValueFactory().setValue(value);
                    hour.getEditor().setText(String.valueOf(value));
                } catch (NumberFormatException ex) {
                    minute.getValueFactory().setValue(0);
                    minute.getEditor().setText(String.valueOf(0));
                }
            }
        });
        //--------------------------------------------
        //Second Spinner
        second.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(SECOND_MIN, SECOND_MAX, 8, 1));
        second.setEditable(true);
        second.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                String text = second.getEditor().getText();
                int value;
                try {
                    value = Integer.parseInt(text);
                    second.getValueFactory().setValue(value);
                    second.getEditor().setText(String.valueOf(value));
                } catch (NumberFormatException ex) {
                    second.getValueFactory().setValue(0);
                    second.getEditor().setText(String.valueOf(0));
                }
            }
        });
        //--------------------------------------------
    }

    public void setTask(Task task) {
        this.task = task;
        taskNameTextField.setText(task.getName());
        hour.getEditor().setText(String.valueOf(task.getTime().getHour()));
        minute.getEditor().setText(String.valueOf(task.getTime().getMinute()));
        second.getEditor().setText(String.valueOf(task.getTime().getSecond()));
    }

    @FXML
    private void handleOkButton() {
        if (isValid()) {
            task.setName(taskNameTextField.getText());
            LocalTime timer = LocalTime.of(hour.getValue() - 1, minute.getValue() - 1, second.getValue() - 1);
            task.setTime(timer);
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancelButton() {
        okClicked = false;
        dialogStage.close();
    }

    private boolean isValid() {
        String errorMessage = "";
        if (taskNameTextField.getText() == null || taskNameTextField.getText().trim().length() == 0) {
            errorMessage += "No valid task name\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }
}
