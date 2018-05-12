package ru.putink.cometa;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;

import java.util.Random;

public class DataGenerator {
    Random random=new Random();
    public int[] createSeriesNumbers(int count,int rightLimit){
        int[] series=new int[count];

        for (int a=0;a<series.length;a++){
            series[a]=getRandomNumber(rightLimit+1);
        }
      //  System.out.println(Arrays.toString(series));
       // System.out.println(Arrays.toString(getCountsNumbersInArray(series,rightLimit)));

        return series;
    }
    private int getRandomNumber(int rightLimit){
        return random.nextInt(rightLimit);
    }
    public XYChart.Series<Integer,Integer> getSeries(int[] seriesNumbers,int rightLimit){
        XYChart.Series<Integer,Integer> series=new XYChart.Series<>();
        series.setName(GeneralController.COUNT_SERIES+")Кол-во генераций чисел при "+seriesNumbers.length+" итерациях и границе "+rightLimit);

        int[] countsNumbers=getCountsNumbersInArray(seriesNumbers,rightLimit);
        for (int a=0;a<countsNumbers.length;a++) {
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
    public int[] getCountsNumbersInArray(int[] seriesNumber,int rightLimit){
        int[] arrayCounts=new int[rightLimit+1];

        for (int a=0;a<arrayCounts.length;a++){
            for (int aSeriesNumber : seriesNumber) {
                if (aSeriesNumber == a) {
                    arrayCounts[a]++;
                }
            }
        }
        return arrayCounts;
    }
}
