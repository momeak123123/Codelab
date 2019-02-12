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
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        setScore(0); //初始化全局变量
    }
}





