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
    private final static int HOUR_MIN = 0;
    private final static int MINUTE_MAX = 60;
    private final static int MINUTE_MIN = 0;
    private final static int SECOND_MAX = 60;
    private final static int SECOND_MIN = 0;

    @FXML
    private CustomTextField taskNameTextField;
    @FXML
    private Spinner<Integer> spinHour;
    @FXML
    private Spinner<Integer> spinMinute;
    @FXML
    private Spinner<Integer> spinSecond;

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
        spinHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(HOUR_MIN, HOUR_MAX, 8, 1));
        spinHour.setEditable(true);
        spinHour.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                String text = spinHour.getEditor().getText();
                int value;
                try {
                    value = Integer.parseInt(text);
                    spinHour.getValueFactory().setValue(value);
                    spinHour.getEditor().setText(String.valueOf(value));
                } catch (NumberFormatException ex) {
                    spinHour.getValueFactory().setValue(0);
                    spinHour.getEditor().setText(String.valueOf(0));
                }
            }
        });
        //--------------------------------------------
        //Minute Spinner
        spinMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(MINUTE_MIN, MINUTE_MAX, 8, 1));
        spinMinute.setEditable(true);
        spinMinute.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                String text = spinMinute.getEditor().getText();
                int value;
                try {
                    value = Integer.parseInt(text);
                    spinMinute.getValueFactory().setValue(value);
                    spinHour.getEditor().setText(String.valueOf(value));
                } catch (NumberFormatException ex) {
                    spinMinute.getValueFactory().setValue(0);
                    spinMinute.getEditor().setText(String.valueOf(0));
                }
            }
        });
        //--------------------------------------------
        //Second Spinner
        spinSecond.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(SECOND_MIN, SECOND_MAX, 8, 1));
        spinSecond.setEditable(true);
        spinSecond.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                String text = spinSecond.getEditor().getText();
                int value;
                try {
                    value = Integer.parseInt(text);
                    spinSecond.getValueFactory().setValue(value);
                    spinSecond.getEditor().setText(String.valueOf(value));
                } catch (NumberFormatException ex) {
                    spinSecond.getValueFactory().setValue(0);
                    spinSecond.getEditor().setText(String.valueOf(0));
                }
            }
        });
        //--------------------------------------------
    }

    public void setTask(Task task) {
        this.task = task;
        taskNameTextField.setText(task.getName());
//        hour.getEditor().setText(String.valueOf(task.getTime().getHour()));
//        minute.getEditor().setText(String.valueOf(task.getTime().getMinute()));
//        second.getEditor().setText(String.valueOf(task.getTime().getSecond()));
        spinHour.getValueFactory().setValue(task.getTime().getHour());
        spinMinute.getValueFactory().setValue(task.getTime().getMinute());
        spinSecond.getValueFactory().setValue(task.getTime().getSecond());
    }

    @FXML
    private void handleOkButton() {
        if (isValid()) {
            task.setName(taskNameTextField.getText());
            LocalTime timer = LocalTime.of(spinHour.getValue() == 24 ? 23 : spinHour.getValue(), spinMinute.getValue() == 60 ? 59 : spinMinute.getValue(), spinSecond.getValue() == 60 ? 59 : spinSecond.getValue());
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
        StringBuilder errorMessage = new StringBuilder();
        if (taskNameTextField.getText() == null || taskNameTextField.getText().trim().length() == 0) {
            errorMessage.append("No valid task name\n");
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
            return false;
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }
}
