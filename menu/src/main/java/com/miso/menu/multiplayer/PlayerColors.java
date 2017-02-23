package com.miso.menu.multiplayer;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal.hornak on 2/17/2017.
 */

public class PlayerColors {

    public int getMyColor() {
        return myColor;
    }

    public void setMyColor(int myColor) {
        this.myColor = myColor;
    }

    private volatile int myColor;

    private List<MyColor> colorList;

    {
        colorList = new ArrayList<>();
        colorList.add(new MyColor(Color.GRAY));
        colorList.add(new MyColor(Color.RED));
        colorList.add(new MyColor(Color.YELLOW));
        colorList.add(new MyColor(Color.GREEN));
        colorList.add(new MyColor(Color.CYAN));
        colorList.add(new MyColor(Color.MAGENTA));
        colorList.add(new MyColor(Color.BLUE));
        colorList.add(new MyColor(Color.WHITE));
    }


    public List<MyColor> getAllColors() {
        return colorList;
    }

    public int getNextAvailableColor(){
        for (MyColor color : colorList){
            if (color.available) return color.colorCode;
        }
        return 0;
    }

    public static class MyColor {

        public int colorCode;
        public boolean available = true;

        public MyColor(int colorCode) {
            this.colorCode = colorCode;
        }

        @Override
        public boolean equals(Object object) {

            if (object instanceof MyColor) {
                if (((MyColor) object).colorCode == this.colorCode) {
                    return true;
                }
            }
            return false;
        }
    }

    public void hostEvent(MyColor chosenColor) {
        chosenColor.available = false;
    }

    public void unHostEvent() {
        for (MyColor myColor : colorList) {
            myColor.available = true;
        }
    }

    public void changeColor(MyColor oldColor, MyColor newColor) {
        oldColor.available = true;
        newColor.available = false;
    }

    public boolean isColorAvailable(MyColor color){
        return color.available;
    }

    public void makeColorUnavailable(MyColor color){
        color.available = false;
    }

    public void makeColorAvailable(MyColor color){
        color.available = true;
    }
}
