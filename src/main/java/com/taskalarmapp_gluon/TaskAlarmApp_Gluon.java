package com.taskalarmapp_gluon;

import com.taskalarmapp_gluon.model.Task;
import com.taskalarmapp_gluon.model.TaskListWrapper;
import com.taskalarmapp_gluon.view_controller.MainViewController;
import com.taskalarmapp_gluon.view_controller.TaskEditorController;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class TaskAlarmApp_Gluon extends Application {

    private Stage primaryStage;
    private SystemTray tray = null;
    private TrayIcon trayIcon = null;
    private MainViewController mainViewController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        Platform.setImplicitExit(false);
        stage.setScene(new Scene(createView().orElse(new AnchorPane())));
        stage.setTitle("Task Alarm App");
        stage.sizeToScene();
        stage.show();

    }

    private Optional<Parent> createView() {
        Parent root;
        try {
            SwingUtilities.invokeLater(this::addSystemTray);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view_controller/MainView.fxml"));
            root = loader.load();

            mainViewController = loader.getController();
            mainViewController.setMainApp(this);
            mainViewController.setDialogStage(primaryStage);
            
            File file = getTaskFilePath();
            if (file != null) {
                loadTaskDataFromFile(file);
            }
        } catch (IOException ex) {
            Logger.getLogger(TaskAlarmApp_Gluon.class.getName()).log(Level.SEVERE, null, ex);
            return Optional.empty();
        }
        return Optional.ofNullable(root);
    }

    public boolean showTaskEditor(Task task) {
        try {
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

    private void addSystemTray() {
        if (SystemTray.isSupported()) {
            tray = SystemTray.getSystemTray();
            final String IMAGE_PATH = "picture/smile_ninja.png";
            URL url = this.getClass().getClassLoader().getResource(IMAGE_PATH);
            Image image = null;
            try {
                image = ImageIO.read(url);
            } catch (IOException ex) {
                Logger.getLogger(TaskAlarmApp_Gluon.class.getName()).log(Level.SEVERE, null, ex);
            }

            PopupMenu popup = new PopupMenu();
            MenuItem item = new MenuItem("Exit");
            popup.add(item);

            trayIcon = new TrayIcon(image, "Task Alarm App", popup);

            item.addActionListener(e -> exit());

            trayIcon.addActionListener(e -> {
                Platform.runLater(this::showStage);
            });

            try {
                tray.add(trayIcon);
            } catch (AWTException ex) {
                //Logger.getLogger(TaskAlarmApp_Gluon.class.getName()).log(Level.SEVERE, null, ex);

            }
        } else {
            System.out.println("Tray unavaliable");
        }
    }

    private void showStage() {
        if (primaryStage != null) {
            primaryStage.show();
            primaryStage.toFront();
        }
    }

    public void exit() {
        Platform.exit();
        if (tray != null) {
            if (trayIcon != null) {
                tray.remove(trayIcon);
            }
        }
        System.exit(0);
    }

    public File getTaskFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(TaskAlarmApp_Gluon.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    public void setTaskFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(TaskAlarmApp_Gluon.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());
            primaryStage.setTitle("Task Alarm App - " + file.getName());
        } else {
            prefs.remove("filePath");
            primaryStage.setTitle("Task Alarm App");
        }
    }

    public void loadTaskDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(TaskListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();
            TaskListWrapper wrapper = (TaskListWrapper) um.unmarshal(file);
            mainViewController.setDanhSachTasks(wrapper.getTasks());
            setTaskFilePath(file);
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());
            alert.showAndWait();
        }
    }

    public void saveTaskDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(TaskListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            TaskListWrapper wrapper = new TaskListWrapper();
            wrapper.setTasks(mainViewController.getDanhSachTasks());

            m.marshal(wrapper, file);

            setTaskFilePath(file);
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());
            alert.showAndWait();
        }
    }

}
