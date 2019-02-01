package com.mydomain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Single thread app allocates a lot of young objects with almost nothing going to old gen
 */
public class MultiThreadBuildingOldAlloc {

    public static void main(String args[]) throws Exception {
        long start = System.currentTimeMillis();
        ExecutorService es = Executors.newFixedThreadPool(8);

        for (int i = 0; i < 8; i++) {
            es.submit(new MultiThreadBuildingOldAlloc.AllocRunnable());
        }
        es.shutdown();
        es.awaitTermination(50, TimeUnit.SECONDS);
        System.out.println("Duration : " + (System.currentTimeMillis() - start));
    }

    static class AllocRunnable implements Runnable {

        List<String> str = new ArrayList<>();

        @Override
        public void run() {
            int k = 0;
            for (int i = 0; i < 100000000; i++) {
                str.add(new String("Str" + i));
                String temp = new String("Str" + i);
                k += temp.length();
            }
        }
    }
}
