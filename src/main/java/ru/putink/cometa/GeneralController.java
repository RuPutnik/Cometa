package ru.putink.cometa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class GeneralController extends Application {
    private Stage generalStage;
    private final String PATH_LAYOUT="layouts/GeneralLayout.fxml";
    private final String PATH_ICON="icon/generalIcon.png";
    public static void start(){
        launch();
    }
    @Override
    public void start(Stage primaryStage){
        renderGeneralWindow(primaryStage);
    }
    private void renderGeneralWindow(Stage stage){
        try {
            Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource(PATH_LAYOUT));
            stage.setScene(new Scene(parent));
            if(new File(PATH_ICON).exists()) {
                generalStage.getIcons().add(new Image(PATH_ICON));
            }
        }catch (IOException|NullPointerException ex){
            Alert alertError=new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Ошибка загрузки");
            alertError.setContentText("При заугрзки главного окна возникла ошибка");
            ex.printStackTrace();
        }
        stage.setWidth(400);
        stage.setHeight(300);
        stage.show();
        generalStage=stage;
    }
}
