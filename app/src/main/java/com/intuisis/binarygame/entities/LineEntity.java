package com.intuisis.binarygame.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gallant on 02/03/15.
 */
public class LineEntity {

    public static final int GAME_MODE_ONE = 1;
    public static final int GAME_MODE_TWO = 2;

    private int type;
    private int[] lines;
    private int result;

    public LineEntity() {
        lines = new int[8];
        result = 0;
    }

    public int[] getLines() {
        return lines;
    }

    public void setLines(int[] lines) {
        this.lines = lines;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "LineEntity{" +
                "type=" + type +
                ", lines=" + Arrays.toString(lines) +
                ", result=" + result +
                '}';
    }
}
