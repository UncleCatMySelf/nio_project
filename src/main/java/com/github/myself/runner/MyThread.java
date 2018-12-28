package com.github.myself.runner;

/**
 * Create by UncleCatMySelf in 21:18 2018\12\27 0027
 */
public class MyThread extends Thread {

    public MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        System.out.println("一个子线程 BY " + getName());
    }
}
