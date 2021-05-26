package ch.epfl.tchu.thomas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainingSet {

    private final Map<List<Double>, List<Double>> dataMap;

    public TrainingSet(String path, int dataSize) {

        dataMap = new HashMap<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

            for (int i = 0; i < dataSize; i++) {
                String[] splitLine = bufferedReader.readLine().split(" ", -1);

                List<Double> inputData = toDoubleList(splitLine[0]);
                List<Double> outputData = toDoubleList(splitLine[1]);

                dataMap.put(inputData, outputData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private List<Double> toDoubleList(String str) {
        List<Double> doubleList = new ArrayList<>();

        for (String s : str.split(",")) {
            doubleList.add(((double) Integer.parseInt(s)));
        }

        return doubleList;
    }

    public Map<List<Double>, List<Double>> dataMap() {
        return dataMap;
    }


}


