package com.example.guozhaotong.demo.test;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello world!");
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.schedule(runnable, 1000, TimeUnit.MILLISECONDS);
        executor.schedule(runnable, 2000, TimeUnit.MILLISECONDS);
        executor.schedule(runnable, 3000, TimeUnit.MILLISECONDS);
        executor.schedule(runnable, 4000, TimeUnit.MILLISECONDS);

    }

//    public long timedTask(int year, int month, int day, int hour, int minute, int second) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(year, month - 1, day, hour, minute, second);
//        Date time = calendar.getTime();
//        Date now = new Date();
//        long timeDifference = time.getTime()-now.getTime();
//        System.out.println(timeDifference);
//    }
}
