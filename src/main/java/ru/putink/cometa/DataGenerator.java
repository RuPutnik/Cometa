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
        int[] countsNumbers=getCountsNumbersInArray(seriesNumbers);
        for (int a=0;a<seriesNumbers.length;a++) {
            series.getData().add(new XYChart.Data<Integer, Integer>(a,countsNumbers[a]));
        }

        return series;
    }
    //рассчитывает сколько раз встречалось каждое из чисел от 0 до размера массива и распологает их в соотвествующем порядке
    // --пример--
    // 0 - 2 раза
    // 1 - 5 раз
    // 2 - 2 раза
    // 3 - 8 раз
    // 4 - 4 раза и т.д.
    private static int[] getCountsNumbersInArray(int[] seriesNumber){
        int[] arrayCounts=new int[seriesNumber.length];

        return arrayCounts;
    }
}
