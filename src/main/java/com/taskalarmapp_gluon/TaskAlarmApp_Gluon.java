package com.taskalarmapp_gluon;

import com.taskalarmapp_gluon.model.Task;
import com.taskalarmapp_gluon.view_controller.MainViewController;
import com.taskalarmapp_gluon.view_controller.TaskEditorController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TaskAlarmApp_Gluon extends Application {
    
    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view_controller/MainView.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            
            MainViewController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(primaryStage);
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Task Alarm App");
            stage.sizeToScene();
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public boolean showTaskEditor(Task task){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view_controller/TaskEditor.fxml"));
            Parent root = loader.load();
            
            TaskEditorController controller = loader.getController();
            controller.setTask(task);
            
            Scene scene = new Scene(root);
            
            Stage stage = new Stage();
            stage.setTitle("Task Editor");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            stage.sizeToScene();
            stage.setScene(scene);
            
            controller.setDialogStage(stage);
            
            stage.showAndWait();
            
            return controller.isOkClicked();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        
    }

}
