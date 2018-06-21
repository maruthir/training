package com.apr.rxjava;

import io.reactivex.Observable;
import java.util.concurrent.TimeUnit;

public class Ex_1 {

    public static void main(String[] args) throws Exception {
//        Observable.interval(1, TimeUnit.SECONDS)
//                .subscribe(t -> System.out.println(t + " on " + Thread.currentThread().getName()));
//
//        TimeUnit.SECONDS.sleep(5);

//        Observable.interval(1, TimeUnit.SECONDS)
//                .blockingSubscribe(t -> System.out.println(t + " on " + Thread.currentThread().getName()));

        Observable<Long> sourceObs = Observable.create(e -> {
            e.onNext(3L);
            e.onNext(4L);
            e.onNext(5L);
            e.onComplete();
        });

        sourceObs.subscribe(t -> System.out.println(t + " on " + Thread.currentThread().getName()));
        sourceObs.subscribe(t -> System.out.println(t + " on " + Thread.currentThread().getName()));
    }
}
