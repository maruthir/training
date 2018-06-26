package com.apr.rxjava;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class Misc {

    public static void main(String[] args) throws Exception {
        Observable<String> linesFromFile = Observable.create(e -> {
            LineNumberReader lineReader = new LineNumberReader(
                    new InputStreamReader(new FileInputStream("/users/maruthir/scripts.log")));
            String line;
            while((line = lineReader.readLine())!=null) {
                if (e.isDisposed()) {
                    System.out.println("Disposing observable");
                    lineReader.close();
                    e.onComplete();
                    return;
                }
                e.onNext(line);
            }
            e.onComplete();
        });
        linesFromFile.subscribe(new Observer<String>() {
            Disposable disposable;
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {
                this.disposable = disposable;
            }

            @Override
            public void onNext(@NonNull String s) {
                System.out.println(s);
                disposable.dispose();
            }

            @Override
            public void onError(@NonNull Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });

        Observable.range(1, 100).window(10).subscribe(obs -> obs.subscribe(System.out::println));
        Observable.range(1, 100).window(10).flatMap(obs -> obs).subscribe(System.out::println);

        Observable.interval(200, TimeUnit.MILLISECONDS).throttleLast(1, TimeUnit.SECONDS)
                .subscribe(System.out::println);

        TimeUnit.SECONDS.sleep(10);
    }
}
