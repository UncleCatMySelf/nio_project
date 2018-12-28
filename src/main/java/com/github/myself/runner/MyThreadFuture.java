package com.github.myself.runner;

import java.util.concurrent.Callable;

/**
 * Created by MySelf on 2018/12/28.
 */
public class MyThreadFuture implements Callable {

    private String name;

    public MyThreadFuture(String name) {
        this.name = name;
    }

    @Override
    public Object call() throws Exception {
        System.out.println("这是一个子线程 BY " + name);
        try {
            Thread.sleep(5000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return "success";
    }
}
