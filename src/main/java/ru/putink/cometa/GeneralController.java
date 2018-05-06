package ru.putink.cometa;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GeneralController extends Application implements Initializable{
    private Stage generalStage;
    private final String PATH_LAYOUT="layouts/GeneralLayout.fxml";
    private final String PATH_ICON="icon/generalIcon.png";

    @FXML
    private TextField countGeneration;
    @FXML
    private TextField limitDigitGeneration;
    @FXML
    private Button buildGeneration;
    @FXML
    private LineChart<NumberAxis,NumberAxis> graphicsPane;

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
        stage.setWidth(700);
        stage.setHeight(500);
        stage.show();
        stage.setResizable(false);
        stage.setTitle("Cometa");
        generalStage=stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
