package com.apr.rxjava;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.concurrent.TimeUnit;

public class Ex_18 {

    public static void main(String[] args) throws Exception {
        // APPROACH 1
        Flowable.create(e -> {
            for(int i=0; i< 10000; i++) {
                if (e.isCancelled()) return;
                e.onNext(i);
            }
            e.onComplete();
        }, BackpressureStrategy.LATEST)
        .doOnNext(i -> System.out.println("Sending item: " + i))
        .observeOn(Schedulers.io())
        .subscribe(i -> {
            TimeUnit.MILLISECONDS.sleep(20);
            System.out.println("Processed item: " + i);
        });
        TimeUnit.SECONDS.sleep(5);

        // APPROACH 2
        LineNumberReader lineReader = new LineNumberReader(
                new InputStreamReader(new FileInputStream("/users/maruthir/scripts.log")));

        Flowable.generate(() -> lineReader.readLine(), (prevLine, e) -> {
            String line = lineReader.readLine();
            if (line != null) {
                e.onNext(line);
            } else {
                lineReader.close();
                e.onComplete();
            }
        }).doOnNext(i -> System.out.println("Sending line: " + i))
        .observeOn(Schedulers.io())
        .subscribe(i -> {
            TimeUnit.MILLISECONDS.sleep(200);
            System.out.println("Processed line: " + i);
        });
        TimeUnit.SECONDS.sleep(20);
    }
}
