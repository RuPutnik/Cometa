package ru.putink.cometa;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GeneralController extends Application implements Initializable{
    private FileExecutor fileExecutor;
    private DataGenerator generator;
    private Stage generalStage;
    private final String PATH_LAYOUT="layouts/GeneralLayout.fxml";
    private final String PATH_ICON="icon/icon.png";
    private final int DEFAULT_LIMIT_NUMBER=100;
    private final int DEFAULT_COUNT=1000;
    private int limitNumber=DEFAULT_LIMIT_NUMBER;
    private int countNumber=DEFAULT_COUNT;
    public static int COUNT_SERIES=1;

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
    @FXML
    private Button readFromFile;
    @FXML
    private TextField numberSeriesField;
    @FXML
    private Button deleteSeries;

    public static void start(){
        launch();
    }
    @Override
    public void start(Stage primaryStage){
        renderGeneralWindow(primaryStage);
    }
    private void renderGeneralWindow(Stage stage){
        try {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(PATH_LAYOUT)));
            stage.setScene(new Scene(parent));
            generalStage=stage;
            try{
                generalStage.getIcons().add(new Image(PATH_ICON));
            }catch (NullPointerException ex){
                Alert alertError=new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Ошибка загрузки");
                alertError.setHeaderText("Ошибка загрузки иконки");
                alertError.setContentText("Иконка программы недоступна или повреждена");
                ex.printStackTrace();
            }
        }catch (IOException|NullPointerException ex){
            Alert alertError=new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Ошибка загрузки");
            alertError.setHeaderText("Ошибка загрузки главного окна");
            alertError.setContentText("Макет главного окна недоступен или поврежден");
            ex.printStackTrace();
        }
        generalStage.setWidth(800);
        generalStage.setHeight(600);
        generalStage.show();
        generalStage.setResizable(false);
        generalStage.setTitle("Cometa");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generator=new DataGenerator();
        fileExecutor=new FileExecutor();

        countGeneration.setOnKeyReleased(new CountGeneration());
        limitDigitGeneration.setOnKeyReleased(new LimitDigit());
        buildGeneration.setOnAction(new Build());
        saveToFile.setOnAction(new SaveSeries());
        readFromFile.setOnAction(new LoadSeries());
        fieldX1.setOnKeyReleased(new FieldXYCoordinates(fieldX1,xAxis,false));
        fieldX2.setOnKeyReleased(new FieldXYCoordinates(fieldX2,xAxis,true));
        fieldY1.setOnKeyReleased(new FieldXYCoordinates(fieldY1,yAxis,false));
        fieldY2.setOnKeyReleased(new FieldXYCoordinates(fieldY2,yAxis,true));
        deleteSeries.setOnAction(new DeleteSeries());

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
    private void updateInfoPoints(){
        for (int c=0;c<graphicsPane.getData().size();c++) {
            ObservableList<XYChart.Data<Integer, Integer>> dataList = ((XYChart.Series<Integer, Integer>) graphicsPane.getData().get(c)).getData();
            dataList.forEach(data -> {
                Node node = data.getNode();
                Tooltip tooltip = new Tooltip("Значение: " + data.getXValue().toString() + '\n' + "Количество: " + data.getYValue().toString());
                Tooltip.install(node, tooltip);
            });
        }
    }
    private void updateNumbersSeries(){
        for (int b=1;b<graphicsPane.getData().size()+1;b++){
            String oldName=graphicsPane.getData().get(b-1).getName();
            graphicsPane.getData().get(b-1).setName(b+oldName.substring(1));
        }
    }
    public class SaveSeries implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {
           fileExecutor.saveToFile(graphicsPane.getData());
        }
    }
    public class LoadSeries implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {
            ArrayList<XYChart.Series<Integer, Integer>> allSeries=fileExecutor.readSeriesFromFile();
            graphicsPane.getData().addAll(allSeries);
            COUNT_SERIES=COUNT_SERIES+allSeries.size();
            updateNumbersSeries();
            updateInfoPoints();
        }
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
        FieldXYCoordinates(TextField field, NumberAxis axis, boolean upper){
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
            generatedValues.setText(generatedValues.getText()+" ; "+Arrays.toString(seriesNumbers));
            countEachValues.setText(countEachValues.getText()+" ; "+Arrays.toString(generator.getCountsNumbersInArray(seriesNumbers,Integer.parseInt(limitDigitGeneration.getText()))));
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
            saveToFile.setDisable(false);
            COUNT_SERIES++;

           updateInfoPoints();
        }
    }
    private class DeleteSeries implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {
            int numberDeleteSeries=Integer.parseInt(numberSeriesField.getText());
            try {
                if (graphicsPane.getData().get(numberDeleteSeries-1) == null){
                    throw new IndexOutOfBoundsException();
                }else {
                    graphicsPane.getData().remove(numberDeleteSeries-1);
                    COUNT_SERIES--;
                    updateNumbersSeries();
                }
            }catch (IndexOutOfBoundsException ex){
                Alert invalidNumberSeries = new Alert(Alert.AlertType.ERROR);
                invalidNumberSeries.setTitle("Ошибка");
                invalidNumberSeries.setHeaderText("Ошибка ввода номера серии");
                invalidNumberSeries.setContentText("Серия под данным номером не существует!");
                invalidNumberSeries.show();
            }catch (NumberFormatException ex){
                Alert invalidNumberSeries = new Alert(Alert.AlertType.ERROR);
                invalidNumberSeries.setTitle("Ошибка");
                invalidNumberSeries.setHeaderText("Ошибка ввода номера серии");
                invalidNumberSeries.setContentText("Введенный текст не является числом!");
                invalidNumberSeries.show();
            }
        }
    }
}
