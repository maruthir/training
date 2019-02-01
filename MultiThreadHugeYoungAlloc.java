package com.mydomain;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Single thread app allocates a lot of young objects with almost nothing going to old gen
 */
public class MultiThreadHugeYoungAlloc {

    public static void main(String args[]) throws Exception {
        long start = System.currentTimeMillis();
        ExecutorService es = Executors.newFixedThreadPool(8);

        for (int i = 0; i < 8; i++) {
            es.submit(new AllocRunnable());
        }

        es.shutdown();
        es.awaitTermination(50, TimeUnit.SECONDS);
        System.out.println("Duration : " + (System.currentTimeMillis() - start));
        Thread.sleep(300000);
    }

    static class AllocRunnable implements Runnable {

        @Override
        public void run() {
            int k = 0;
            try {
                for (int i = 0; i < 100000000; i++) {
                    String temp = new String("Str" + i);
                    k += temp.length();
                }
                for (int i = 0; i < 100000000; i++) {
                    URL u = new URL("http://something.co" + i);
                    k += u.getPort();
                }
            } catch (MalformedURLException e) {

            }
        }
    }
}
