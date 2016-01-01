/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taskalarmapp_gluon.view_controller;

import com.taskalarmapp_gluon.TaskAlarmApp_Gluon;
import com.taskalarmapp_gluon.model.Task;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void setDialogStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }
    
    public void setMainApp(TaskAlarmApp_Gluon mainApp){
        this.mainApp = mainApp;
    }
    
    @FXML
    private void handleAddButton(){
        Task task = new Task();
        boolean okClicked =  mainApp.showTaskEditor(task);
        if(okClicked){
            danhsachTask.getItems().add(task);
        }
    }
    
    @FXML
    private void handleEditButton(){
        int index = danhsachTask.getSelectionModel().getSelectedIndex();
        if(index != -1){
            Task task = danhsachTask.getItems().get(index);
            mainApp.showTaskEditor(task);
            
        }
        else{
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
    private void handleDeleteButton(){
        int index = danhsachTask.getSelectionModel().getSelectedIndex();
        if(index != -1){
            danhsachTask.getItems().remove(index);
        }
        else{
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
    private void handleExitButton(){
        mainApp.exit();
    }
}
