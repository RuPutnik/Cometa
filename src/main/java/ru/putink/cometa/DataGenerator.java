package ru.putink.cometa;

import javafx.scene.chart.XYChart;

import java.util.Random;

public class DataGenerator {
    static Random random=new Random();
    public static int[] createSeriesNumbers(int count,int rightLimit){
        int[] series=new int[count];

        for (int a=0;a<count;a++){
            series[a]=getRandomNumber(rightLimit);
        }

        return series;
    }
    private static int getRandomNumber(int rightLimit){
        return random.nextInt(rightLimit);
    }
    public static XYChart.Series<Integer,Integer> getSeries(int[] seriesNumbers){
        XYChart.Series<Integer,Integer> series=null;
        //series.getData().add(new XYChart.Data<Integer, Integer>())

        return series;
    }
}
