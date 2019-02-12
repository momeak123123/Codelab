package com.example.momeak.codelab;

import android.app.Application;

public class MyApp extends Application{
    private int score = 0;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }



    private int set = 0;

    public int getScoret() {
        return set;
    }

    public void setScoret(int set) {
        this.set = set;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        setScore(0); //初始化全局变量
        setScoret(0);
    }
}





