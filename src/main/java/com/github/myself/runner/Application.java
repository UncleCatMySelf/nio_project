package com.github.myself.runner;

import java.util.concurrent.*;

/**
 * Created by MySelf on 2018/12/28.
 */
public class Application {

    public static void main(String[] args) {
        // 1、FutureTask
        //{@link 执行器}, 提供管理终止的方法和可以生成 {@link future} 以跟踪一个或多个异步任务的进度的方法。
        //newCachedThreadPool：创建一个线程池, 该线程池可根据需要创建新线程, 但在以前构造的线程可用时将重用这些线程。
        // 这些池通常会提高执行许多短期异步任务的程序的性能。对 {@code 执行} 的调用将重用以前构造的线程 (如果可用)。
        // 如果没有可用的现有线程, 则将创建一个新线程并将其添加到池中。60秒内未使用的线程将被终止并从缓存中删除。
        // 因此, 保持空闲足够长的池不会消耗任何资源。请注意,
        // 可以使用 {@link 愿应用程序可执行函数创建具有类似属性但详细信息 (例如, 超时参数) 的池。
        ExecutorService executorService = Executors.newCachedThreadPool();

        //可取消的异步计算。 此类提供 {@link future} 的基本实现, 其中包含启动和取消计算的方法、查看计算是否已完成的查询, 以及
        // 检索计算的结果。 只有在计算完成后才能检索结果; 只有在计算完成后, 才能检索结果。如果计算尚未完成, 则 {@code get} 方法将被阻止。
        // 计算完成后, 无法重新启动或取消计算 (除非使用 {@link #runAndReset} 调用计算)。
        // 构造方法：创建一个 {@code 未来任务}, 该操作将在运行时执行给定的 {@code 可调用}。
        final FutureTask fun = new FutureTask(new MyThreadFuture("Future"));


        // 在将来的某个时间执行给定的命令。 该命令可以在新线程、池线程或调用线程中执行, 由 {@code 执行器} 实现的自由裁量权。
        executorService.execute(fun);

//        fun.run(); FutureTask确保仅执行一次

        try {
            fun.get(3000, TimeUnit.MILLISECONDS);
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
            fun.cancel(true);
        }catch (TimeoutException e){
            e.printStackTrace();
            fun.cancel(true);
            System.out.println("项目超时");
            //TODO 重新执行
        }

        // 2、Runnable
        //<code> 可运行的 </code> 接口应由其实例打算由线程执行的任何类实现。该类必须定义不调用参数的方法 <code> 运行 </code>。
        MyThreadRunnable runnable = new MyThreadRunnable("Runnable");
        runnable.run();
        runnable.run();

        // 3、Thread

        MyThread myThread = new MyThread("Thread");
        myThread.run();
        myThread.run();

    }

}
