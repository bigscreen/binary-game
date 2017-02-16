package com.bigscreen.binarygame.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class AppHelper {

    public static int getRandomNumber(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    public static int getRandomIndex() {
        return getRandomNumber(0, 7);
    }

    public static List<Integer> getUniqueIndexList(int limit) {
        List<Integer> randomList = new ArrayList<>();
        for (int length=0; length<limit; length++) {
            int temp = getRandomIndex();
            if (!isValueExist(temp, randomList)) {
                randomList.add(temp);
            }
        }
        return randomList;
    }

    public static boolean isValueExist(int value, List<Integer> integers) {
        boolean isExist = false;
        for (int integer : integers) {
            if (integer == value) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }
}
