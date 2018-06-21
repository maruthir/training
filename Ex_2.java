package com.apr.rxjava;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.TimeUnit;

public class Ex_2 {

    public static void main(String[] args) throws Exception {
        Observable<Long> sourceObs = Observable.create(e -> {
            e.onNext(3L);
            e.onNext(4L);
            e.onNext(5L);
            e.onComplete();
        });


        sourceObs.subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {
                System.out.println("Subscribed");
            }

            @Override
            public void onNext(@NonNull Long aLong) {
                System.out.println(aLong);
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Completed");
            }
        });
    }
}
