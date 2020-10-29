package com.example.mpandroidchart;

public class DataPoint {
    private float xValue;
    private float yValue;

    public DataPoint(){}
    public DataPoint(float x, float y){
        this.xValue =x;
        this.yValue =y;
    }
    //Getters
    public float getxValue(){
        return xValue;
    }
    public float getyValue() {
        return yValue;
    }
    //Setters
    public void setxValue(float xValue) {
        this.xValue = xValue;
    }

    public void setyValue(float yValue) {
        this.yValue = yValue;
    }
}
