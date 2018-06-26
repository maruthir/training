package com.apr.rxjava;

import io.reactivex.Observable;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Ex_15 {

    public static void main(String[] args) throws Exception {
        Observable<String> fileSource = Observable.create(e -> {
            Files.lines(Paths.get("/Users/maruthir/scripts.log")).forEach(e::onNext);
            e.onComplete();
        });
        fileSource.flatMap(line -> Observable.fromArray(line.split(" ")))
                .subscribe(word -> System.out.println(word));
    }
}
