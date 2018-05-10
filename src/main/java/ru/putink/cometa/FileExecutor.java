package ru.putink.cometa;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;

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
                    writer.write("#Series " + a + " name: " + handleNameSeries(series.get(a).getName()));
                    writer.write(getSeriesStringFormat(series.get(a)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public XYChart.Series<Integer,Integer> readSeriesFromFile(){
        return null;
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
}
