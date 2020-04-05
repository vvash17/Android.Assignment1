package com.example.myapplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ContentStorage {

    private Map<String, Integer> maxOccurances;
    private String[][] pictureGuide;
    private List<String> pictureNames;
    private int verticalSize, horizontalSize;


    public ContentStorage(String pictureNames, int verticalSize, int horizontalSize) {
        init(pictureNames, verticalSize, horizontalSize);
    }
    //receives coordinates of pic, returns its name
    public String getPictureNameByCoordinates(String coordinates) {
        String[] coordinateStrings = coordinates.split(";");
        int verticalCoordinate = Integer.parseInt(coordinateStrings[0]);
        int horizontalCoordinate = Integer.parseInt(coordinateStrings[1]);
        return pictureGuide[verticalCoordinate]
                [horizontalCoordinate];
    }
    //initializes structure
    private void init(String pictureNames, int verticalSize, int horizontalSize) {
        this.horizontalSize = horizontalSize;
        this.verticalSize = verticalSize;
        pictureGuide = new String[verticalSize][horizontalSize];
        loadPictureNames(pictureNames);
        loadPictureGuide();
    }
    //loads picture names
    private void loadPictureNames(String pictureNames) {
        String[] pictureNameArray = pictureNames.split(";");
        this.pictureNames = Arrays.asList(pictureNameArray);
        this.maxOccurances = new HashMap<>();
        for (String pictureName : pictureNameArray) maxOccurances.put(pictureName, 2);
    }
    //picks picture name randomly
    private String pickPictureName() {
        Random random = new Random();
        int position = random.nextInt(pictureNames.size());
        String result = pictureNames.get(position);
        maxOccurances.put(result, maxOccurances.get(result) - 1);
        if (maxOccurances.get(result) == 0) {
            updatePictureNames(result);
        }
        return result;
    }

    private void updatePictureNames(String pictureName){
        List<String> newPicNames = new ArrayList<>();
        for(String picName : this.pictureNames){
            if(!picName.equals(pictureName))newPicNames.add(picName);
        }
        this.pictureNames = newPicNames;
    }
    private void loadPictureGuide() {
        for (int i = 0; i < verticalSize; i++) {
            for (int j = 0; j < horizontalSize; j++) {
                pictureGuide[i][j] = pickPictureName();
            }
        }
    }
}
