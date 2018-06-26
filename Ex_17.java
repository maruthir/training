package com.apr.rxjava;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class Ex_17 {

    public static void main(String[] args) throws Exception {
        Flowable.range(1, 10000)
                .doOnNext(i -> System.out.println("Sending item: " + i))
                .observeOn(Schedulers.io())
                .subscribe(i -> {
                    TimeUnit.MILLISECONDS.sleep(20);
                    System.out.println("Processed item: " + i);
                });
        TimeUnit.SECONDS.sleep(5);
    }
}
