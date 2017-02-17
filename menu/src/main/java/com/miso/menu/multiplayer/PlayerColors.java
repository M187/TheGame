package com.miso.menu.multiplayer;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal.hornak on 2/17/2017.
 */

public class PlayerColors {

    private static List<Integer> colorList;

    public static List<Integer> getAllColors(){
        if (colorList == null) {
            colorList = new ArrayList<>();
            colorList.add(Color.GRAY);
            colorList.add(Color.RED);
            colorList.add(Color.YELLOW);
            colorList.add(Color.GREEN);
            colorList.add(Color.CYAN);
            colorList.add(Color.MAGENTA);
            colorList.add(Color.BLUE);
            colorList.add(Color.WHITE);
        }
        return colorList;
    }

}
