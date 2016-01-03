/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taskalarmapp_gluon.view_controller;

import com.taskalarmapp_gluon.TaskAlarmApp_Gluon;
import com.taskalarmapp_gluon.model.Task;
import com.taskalarmapp_gluon.model.TaskUtil;
import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static java.util.stream.Collectors.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * FXML Controller class
 *
 * @author duc
 */
public class MainViewController implements Initializable {

    private TaskAlarmApp_Gluon mainApp;
    private Stage primaryStage;
    private Thread timeThread;
    private final BooleanProperty started;

    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;

    @FXML
    private ListView<Task> danhsachTask;

    public MainViewController() {
        this.started = new SimpleBooleanProperty(false);
        //stopButton.setDisable(true);

    }

    public List<Task> getDanhSachTasks() {
        return danhsachTask.getItems();
    }

    public void setDanhSachTasks(List<Task> tasks) {
        danhsachTask.getItems().clear();
        danhsachTask.getItems().addAll(tasks);
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        started.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            startButton.setDisable(newValue);
            stopButton.setDisable(!newValue);
            addButton.setDisable(newValue);
            deleteButton.setDisable(newValue);
            editButton.setDisable(newValue);
        });
//        started.set(false);
//        stopButton.setDisable(true);
        if (!danhsachTask.getItems().isEmpty()) {
            handleStartButton();
        } else {
            handleStartButton();
            handleStopButton();
        }
    }

    public void setDialogStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setMainApp(TaskAlarmApp_Gluon mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleAddButton() {
        Task task = new Task();
        boolean okClicked = mainApp.showTaskEditor(task);
        if (okClicked) {
            danhsachTask.getItems().add(task);
        }
    }

    @FXML
    private void handleEditButton() {
        int index = danhsachTask.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            Task task = danhsachTask.getItems().get(index);
            mainApp.showTaskEditor(task);

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.WINDOW_MODAL);
            alert.initOwner(primaryStage);
            alert.setTitle("Edit Error");
            alert.setHeaderText("You did not select a task");
            alert.setContentText("Please select a task");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleDeleteButton() {
        int index = danhsachTask.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            danhsachTask.getItems().remove(index);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.WINDOW_MODAL);
            alert.initOwner(primaryStage);
            alert.setTitle("Delete Error");
            alert.setHeaderText("You did not select a task");
            alert.setContentText("Please select a task");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleExitButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(primaryStage);
        alert.setTitle("Hold it a minute!!");
        alert.setHeaderText("You are about to exit the app");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> answer = alert.showAndWait();
        //System.out.println(answer.get().getText());
        if (answer.get().getText().equalsIgnoreCase("yes")) {
            mainApp.exit();
        }
    }

    @FXML
    private void handleStartButton() {
        Thread t1 = new Thread(() -> {
            System.out.println("In thread t1");
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    //Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                    break;
                }
                System.out.println("In while loop");
                if (!danhsachTask.getItems().isEmpty()) {
                    System.out.println("Check timer");
                    long sleepTime = checkTimer();
                    System.out.println("Sleep time: " + sleepTime);
                    try {
                        if (sleepTime != 0) {
                            System.out.println("Thread Sleep for " + sleepTime);
                            Thread.sleep(sleepTime);
                            System.out.println("After Sleep");
                            Platform.runLater(this::alertTask);
                        }
                    } catch (InterruptedException ex) {
                        //Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                        break;
                    }

                }
            }
        });
        t1.start();
        started.set(true);
        timeThread = t1;
    }

    @FXML
    private void handleStopButton() {
        timeThread.interrupt();
        started.set(false);
    }

    @FXML
    private void handleNew() {
        danhsachTask.getItems().clear();
        mainApp.setTaskFilePath(null);
    }

    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML file (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            mainApp.loadTaskDataFromFile(file);
        }
    }

    @FXML
    private void handleSave() {
        File taskFile = mainApp.getTaskFilePath();
        if (taskFile != null) {
            mainApp.saveTaskDataToFile(taskFile);
        } else {
            handleSaveAs();
        }
    }

    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.saveTaskDataToFile(file);
        }
    }

    private long checkTimer() {
        LocalTime now = LocalTime.now();

        List<Task> tasksAfterNow = danhsachTask.getItems().stream()
                .filter(t -> t.getTime().getHour() >= now.getHour()
                        && t.getTime().getMinute() > now.getMinute())
                .collect(toList());
        if (tasksAfterNow.isEmpty()) {
//            Optional<Task> task = danhsachTask.getItems().stream()
//                    .collect(minBy(TaskUtil::compareTaskByTime));
            long sleep = Duration.between(now, LocalTime.of(23, 59, 59)).toMinutes();
            return sleep * 60 * 1000;
        } else {
            Optional<Task> task = tasksAfterNow.stream()
                    .collect(minBy(TaskUtil::compareTaskByTime));
            System.out.println("The task: " + task.get());
            System.out.println("Now: " + now);
            long sleep = Duration.between(now, task.get().getTime()).toMillis();
            System.out.println("Sleep: " + sleep);
            return sleep;
        }

    }

    private void alertTask() {
        LocalTime now = LocalTime.now();
        StringBuilder tasks = new StringBuilder();
        danhsachTask.getItems().stream()
                .filter((t) -> t.getTime().getHour() == now.getHour()
                        && t.getTime().getMinute() == now.getMinute())
                .forEach(t -> {
                    tasks.append(t.getName());
                    tasks.append("\n");
                });
        if (tasks.length() != 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(primaryStage);
            alert.setTitle("Ring Ring!!!!");
            alert.setHeaderText("It's time to do something!!!");
            alert.setContentText(tasks.toString());
            alert.showAndWait();
        }
    }

}
