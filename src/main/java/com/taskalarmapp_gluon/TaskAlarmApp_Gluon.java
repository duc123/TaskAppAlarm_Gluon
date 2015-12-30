package com.taskalarmapp_gluon;

import com.taskalarmapp_gluon.model.Task;
import com.taskalarmapp_gluon.view_controller.MainViewController;
import com.taskalarmapp_gluon.view_controller.TaskEditorController;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

public class TaskAlarmApp_Gluon extends Application {
    
    private Stage primaryStage;
    
    public static void main(String[] args) {
        launch(args);
    }
        
    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        Platform.setImplicitExit(false);
        try {
            SwingUtilities.invokeLater(this::addSystemTray);
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
    
    private void addSystemTray(){
        if(SystemTray.isSupported()){
            SystemTray tray = SystemTray.getSystemTray();
            final String IMAGE_PATH = "picture/smile_ninja.png";
            URL url = this.getClass().getClassLoader().getResource(IMAGE_PATH);
            Image image = null;
            try{
                image = ImageIO.read(url);
            } catch (IOException ex) {
                Logger.getLogger(TaskAlarmApp_Gluon.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            PopupMenu popup = new PopupMenu();
            MenuItem item = new MenuItem("Exit");
            popup.add(item);
            
            TrayIcon trayIcon = new TrayIcon(image, "Task Alarm App", popup);
            
            item.addActionListener(e ->{
                Platform.exit();
                tray.remove(trayIcon);
            });
            
            trayIcon.addActionListener(e -> {
                Platform.runLater(this::showStage);
            });
            
            try{
                tray.add(trayIcon);
            } catch (AWTException ex) {
                Logger.getLogger(TaskAlarmApp_Gluon.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            System.out.println("Tray unavaliable");
        }
    }
    
    private void showStage(){
        if(primaryStage != null){
            primaryStage.show();
            primaryStage.toFront();
        }
    }

}
