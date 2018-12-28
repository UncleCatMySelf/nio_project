package com.github.myself.runner;

import java.util.concurrent.Callable;

/**
 * 返回结果并可能引发异常的任务。实现者定义一个没有调用 {@code 调用} 的参数的方法。
 * Created by MySelf on 2018/12/28.
 */
public class MyThreadFuture implements Callable {

    private String name;

    public MyThreadFuture(String name) {
        this.name = name;
    }

    /**
     * call: 计算结果, 或在无法执行的情况下引发异常。
     * @return
     * @throws Exception
     */
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
