package ru.putink.cometa;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class GeneralController extends Application implements Initializable{
    private DataGenerator generator;
    private Stage generalStage;
    private final String PATH_LAYOUT="layouts/GeneralLayout.fxml";
    private final String PATH_ICON="icon/generalIcon.png";
    private final int DEFAULT_LIMIT_NUMBER=100;
    private final int DEFAULT_COUNT=1000;
    private int limitNumber=DEFAULT_LIMIT_NUMBER;
    private int countNumber=DEFAULT_COUNT;

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
    @FXML
    private TextField generatedValues;
    @FXML
    private TextField countEachValues;
    @FXML
    private TextField fieldX1;
    @FXML
    private TextField fieldX2;
    @FXML
    private TextField fieldY1;
    @FXML
    private TextField fieldY2;
    @FXML
    private Button saveToFile;

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
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();
        stage.setResizable(false);
        stage.setTitle("Cometa");
        generalStage=stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generator=new DataGenerator();

        countGeneration.setOnKeyReleased(new CountGeneration());
        limitDigitGeneration.setOnKeyReleased(new LimitDigit());
        buildGeneration.setOnAction(new Build());
        fieldX1.setOnKeyReleased(new FieldXYCoordinates(fieldX1,xAxis,false));
        fieldX2.setOnKeyReleased(new FieldXYCoordinates(fieldX2,xAxis,true));
        fieldY1.setOnKeyReleased(new FieldXYCoordinates(fieldY1,yAxis,false));
        fieldY2.setOnKeyReleased(new FieldXYCoordinates(fieldY2,yAxis,true));

        countGeneration.setText(String.valueOf(DEFAULT_COUNT));
        limitDigitGeneration.setText(String.valueOf(DEFAULT_LIMIT_NUMBER));

        graphicsPane.getXAxis().setAutoRanging(false);
        graphicsPane.getYAxis().setAutoRanging(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(DEFAULT_LIMIT_NUMBER);
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
                if(event.getText().equals("-")) throw new NumberFormatException();
            }catch (NumberFormatException ex){
                count=DEFAULT_COUNT;
                countGeneration.setText(String.valueOf(count));
                Alert invalidSign=new Alert(Alert.AlertType.ERROR);
                invalidSign.setTitle("Ошибка");
                invalidSign.setHeaderText("Ошибка ввода количества итераций");
                invalidSign.setContentText("Вы ввели недопустимый символ!");
                invalidSign.show();
            }
                countNumber=count;
        }
    }
    public class LimitDigit implements EventHandler<KeyEvent>{
        @Override
        public void handle(KeyEvent event) {
            Integer limit;
            try{
                limit=Integer.parseInt(limitDigitGeneration.getText());
                if(event.getText().equals("-")) throw new NumberFormatException();
            }catch (NumberFormatException ex){
                limit=DEFAULT_LIMIT_NUMBER;
                limitDigitGeneration.setText(String.valueOf(limit));
                Alert invalidSign=new Alert(Alert.AlertType.ERROR);
                invalidSign.setTitle("Ошибка");
                invalidSign.setHeaderText("Ошибка ввода граничного числа");
                invalidSign.setContentText("Вы ввели недопустимый символ!");
                invalidSign.show();
            }
                limitNumber=limit;
        }
    }
    public class FieldXYCoordinates implements EventHandler<KeyEvent>{
        private TextField field;
        private NumberAxis axis;
        private boolean upper;
        public FieldXYCoordinates(TextField field,NumberAxis axis,boolean upper){
            this.field=field;
            this.axis=axis;
            this.upper=upper;
        }
        @Override
        public void handle(KeyEvent event) {
            Integer value;
            try{
                value=Integer.parseInt(field.getText());
                if(event.getText().equals("-")) throw new NumberFormatException();
                if(upper){
                    axis.setUpperBound(value);
                }else {
                    axis.setLowerBound(value);
                }
            }catch (NumberFormatException ex){
                Alert invalidSign=new Alert(Alert.AlertType.ERROR);
                invalidSign.setTitle("Ошибка");
                invalidSign.setHeaderText("Ошибка ввода координаты");
                invalidSign.setContentText("Координата должна быть целым положительным числом!");
                invalidSign.show();
                if(upper){
                    field.setText(String.valueOf((int)axis.getUpperBound()));
                }else {
                    field.setText(String.valueOf((int)axis.getLowerBound()));
                }
            }

        }
    }
    public class Build implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {
            int[] seriesNumbers=generator.createSeriesNumbers(Integer.parseInt(countGeneration.getText()),Integer.parseInt(limitDigitGeneration.getText()));
            generatedValues.setText(Arrays.toString(seriesNumbers));
            countEachValues.setText(Arrays.toString(generator.getCountsNumbersInArray(seriesNumbers,Integer.parseInt(limitDigitGeneration.getText()))));
            XYChart.Series<Integer,Integer> series=generator.getSeries(seriesNumbers,Integer.parseInt(limitDigitGeneration.getText()));
            graphicsPane.getData().add(series);
            xAxis.setUpperBound(limitNumber);
            yAxis.setUpperBound(countNumber);
            xAxis.setLowerBound(0);
            yAxis.setLowerBound(0);
            fieldX1.setText("0");
            fieldX2.setText(String.valueOf(limitNumber));
            fieldY1.setText("0");
            fieldY2.setText(String.valueOf(countNumber));
          //graphicsPane.setLegendSide(Side.RIGHT);
        }
    }
}
