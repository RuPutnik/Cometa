package ru.putink.cometa;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class GeneralController extends Application implements Initializable{
    private Stage generalStage;
    private final String PATH_LAYOUT="layouts/GeneralLayout.fxml";
    private final String PATH_ICON="icon/generalIcon.png";
    private final int DEFAULT_LIMIT_DIGIT=100;
    private final int DEFAULT_COUNT=1000;

    @FXML
    private TextField countGeneration;
    @FXML
    private TextField limitDigitGeneration;
    @FXML
    private Button buildGeneration;
    @FXML
    private LineChart<Integer,Integer> graphicsPane;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;

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
            alertError.setHeaderText("Ошибка загрузки главного окна");
            alertError.setContentText("Макет главного окна недоступен или поврежден");
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
        countGeneration.setOnKeyReleased(new CountGeneration());
        limitDigitGeneration.setOnKeyReleased(new LimitDigit());
        buildGeneration.setOnAction(new Build());

        countGeneration.setText(String.valueOf(DEFAULT_COUNT));
        limitDigitGeneration.setText(String.valueOf(DEFAULT_LIMIT_DIGIT));

        graphicsPane.getXAxis().setAutoRanging(false);
        graphicsPane.getYAxis().setAutoRanging(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(DEFAULT_LIMIT_DIGIT);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(DEFAULT_COUNT);
        countGeneration.setFocusTraversable(false);
        limitDigitGeneration.setFocusTraversable(false);
    }

    public class CountGeneration implements EventHandler<KeyEvent>{
        @Override
        public void handle(KeyEvent event) {
            Integer count;
            try{
                count=Integer.parseInt(countGeneration.getText());
            }catch (NumberFormatException ex){
                count=DEFAULT_COUNT;
                countGeneration.setText(String.valueOf(count));
                Alert invalidSign=new Alert(Alert.AlertType.ERROR);
                invalidSign.setTitle("Ошибка");
                invalidSign.setHeaderText("Ошибка ввода количества итераций");
                invalidSign.setContentText("Вы ввели недопустимый символ!");
                invalidSign.show();
            }
            yAxis.setUpperBound(count);
        }
    }
    public class LimitDigit implements EventHandler<KeyEvent>{
        @Override
        public void handle(KeyEvent event) {
            Integer limit;
            try{
                limit=Integer.parseInt(limitDigitGeneration.getText());
            }catch (NumberFormatException ex){
                limit=DEFAULT_LIMIT_DIGIT;
                limitDigitGeneration.setText(String.valueOf(limit));
                Alert invalidSign=new Alert(Alert.AlertType.ERROR);
                invalidSign.setTitle("Ошибка");
                invalidSign.setHeaderText("Ошибка ввода граничного числа");
                invalidSign.setContentText("Вы ввели недопустимый символ!");
                invalidSign.show();
            }
            xAxis.setUpperBound(limit);
        }
    }
    public class Build implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            int[] seriesNumbers=DataGenerator.createSeriesNumbers(Integer.parseInt(countGeneration.getText()),Integer.parseInt(limitDigitGeneration.getText()));
            XYChart.Series<Integer,Integer> series=DataGenerator.getSeries(seriesNumbers);
            graphicsPane.getData().add(series);
        }
    }
}
