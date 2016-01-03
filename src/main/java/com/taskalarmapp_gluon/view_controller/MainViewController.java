/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taskalarmapp_gluon.view_controller;

import com.taskalarmapp_gluon.TaskAlarmApp_Gluon;
import com.taskalarmapp_gluon.model.Task;
import com.taskalarmapp_gluon.model.TaskUtil;
import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static java.util.stream.Collectors.*;
import javafx.application.Platform;

/**
 * FXML Controller class
 *
 * @author duc
 */
public class MainViewController implements Initializable {

    private TaskAlarmApp_Gluon mainApp;
    private Stage primaryStage;

    @FXML
    private ListView<Task> danhsachTask;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Thread t1 = new Thread(() -> {
            System.out.println("In thread t1");
            while(true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("In while loop");
                if(!danhsachTask.getItems().isEmpty()){
                    System.out.println("Check timer");
                    long sleepTime = checkTimer();
                    System.out.println("Sleep time: " + sleepTime);
                    try {
                        if(sleepTime != 0){
                            System.out.println("Thread Sleep for " + sleepTime);
                            Thread.sleep(sleepTime);
                            System.out.println("After Sleep");
                            Platform.runLater(this::alertTask);
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }
        });
        t1.start();
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
        mainApp.exit();
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
            long sleep = Duration.between(now, LocalTime.of(23, 59,59)).toMinutes();
            return sleep*60*1000;
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
    
    private void alertTask(){
        LocalTime now = LocalTime.now();
        StringBuilder tasks = new StringBuilder();
        danhsachTask.getItems().stream()
                .filter((t) -> t.getTime().getHour() == now.getHour()
                && t.getTime().getMinute() == now.getMinute())
                .forEach(t -> {
                    tasks.append(t.getName());
                    tasks.append("\n");
                });
        if(tasks.length() != 0){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(primaryStage);
        alert.setTitle("Ring Ring!!!!");
        alert.setHeaderText("It's time to do something!!!");
        alert.setContentText(tasks.toString());
        alert.showAndWait();
        }
    }
}
