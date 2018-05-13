package ru.putink.cometa;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

//Класс отвечает за сохранение и чтение серий данных из файла
public class FileExecutor{
    public void saveToFile(ObservableList<XYChart.Series<Integer,Integer>> series){
        FileChooser chooser=new FileChooser();
        chooser.setInitialDirectory(new File("C:\\"));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("txt","*.txt"));
        File file=chooser.showSaveDialog(new Stage());
        if(file!=null) {
            try (FileWriter writer = new FileWriter(file)) {
                for (int a = 0; a < series.size(); a++) {
                    writer.write("#"+handleNameSeries(series.get(a).getName()));
                    writer.write(getSeriesStringFormat(series.get(a)));
                }
            } catch (IOException e) {
                e.printStackTrace();
                Alert errorSave=new Alert(Alert.AlertType.ERROR);
                errorSave.setTitle("Ошибка");
                errorSave.setHeaderText("Ошибка сохранения серий");
                errorSave.setContentText("При сохранении серий в файл возникла ошибка!");
                errorSave.show();
            }
        }
    }
    public ArrayList<XYChart.Series<Integer,Integer>> readSeriesFromFile(){
        XYChart.Series<Integer,Integer> series=null;
        ArrayList<XYChart.Series<Integer, Integer>> seriesList=new ArrayList<XYChart.Series<Integer,Integer>>();
        FileChooser chooser=new FileChooser();
        chooser.setInitialDirectory(new File("C:\\"));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("txt","*.txt"));
        File file=chooser.showOpenDialog(new Stage());
        String XLine="";
        String YLine="";
        if(file!=null){
            try(BufferedReader reader=new BufferedReader(new FileReader(file))){
                String line;
                int numberData=0;//0-имя серии, 1 - значения по оси Х, 2 - значения по оси Y
                while ((line=reader.readLine())!=null){
                    if(numberData==3) {
                        numberData=0;
                    }

                    if(numberData==0){
                        series=new XYChart.Series<>();
                        if(line.charAt(0)=='#') {
                            series.setName(line.replace("#", ""));
                        }
                    }else if(numberData==1){
                        XLine=line;
                    }else if(numberData==2){
                        YLine=line;
                        Integer[] x=stringToArray(XLine);
                        Integer[] y=stringToArray(YLine);

                        for (int a=0;a<x.length;a++){
                            series.getData().add(new XYChart.Data<>(x[a],y[a]));
                        }
                        seriesList.add(series);
                    }

                    numberData++;
                }
            }catch (IOException io){
                io.printStackTrace();
                Alert errorRead=new Alert(Alert.AlertType.ERROR);
                errorRead.setTitle("Ошибка");
                errorRead.setHeaderText("Ошибка чтения серий");
                errorRead.setContentText("При чтении серий из файла возникла ошибка!");
                errorRead.show();
            }
        }
        return seriesList;
    }
    private String getSeriesStringFormat(XYChart.Series<Integer, Integer> series){
        StringBuilder line=new StringBuilder();
        line.append("\n");
        for (int a=0;a<series.getData().size();a++){
            line.append(series.getData().get(a).getXValue()).append(" ");
        }
        line.append("\n");
        for (int b=0;b<series.getData().size();b++){
            line.append(series.getData().get(b).getYValue()).append(" ");
        }
        line.append("\n");
        return line.toString();
    }
    //Удаляет из названия все переносы на следующую строку
    private String handleNameSeries(String nameSeries){
       return nameSeries.replace("\n"," ");
    }
    private Integer[] stringToArray(String lineOfIntegers){
        String str1=lineOfIntegers;//получаем содержимое текстового поля
        Integer[] integerArray = new Integer[str1.split(" ").length];
        String[] numbersArray=str1.split(" ");
        for (int i=0; i<numbersArray.length; i++)
        {
            integerArray[i]=Integer.parseInt(numbersArray[i]);//получаем символ из строки по его номеру
            //и записываем его как элемент массива
        }
        return integerArray;
    }
}
