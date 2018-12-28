package com.github.myself.runner;

/**
 * Created by MySelf on 2018/12/28.
 */
public class MyThreadRunnable implements Runnable {

    private String name;

    public MyThreadRunnable(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("这是一个子线程 BY " + name);
    }
}
