package com.apr.rxjava;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;

public class Ex_14 {

    private static final int IMG_WIDTH = 300;
    private static final int IMG_HEIGHT = 300;
    private static final String IMG_PATH = "/Users/maruthir/Documents/Pictures";
    private static final String IMG_EXTN = ".jpg";

    public static void main(String[] args) throws Exception {
        Files.list(Paths.get(IMG_PATH))
                .parallel()
                .filter(p -> p.getFileName().toString().toLowerCase().contains(IMG_EXTN))
                .peek(p -> {
                    System.out.println("Processing file: " + p);
                })
                .map(path -> {
                    try {
                        System.out.println("Reading image in: " + Thread.currentThread().getName());
                        return ImageIO.read(path.toFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(bi -> bi != null)
                .map(bi -> {
                    System.out.println("Processing image in: " + Thread.currentThread().getName());
                    int type = bi.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bi.getType();
                    return resizeImage(bi, type);
                }).forEach(bi -> {
                    try {
                        System.out.println("Writing image in: " + Thread.currentThread().getName());
                        ImageIO.write(bi, IMG_EXTN.replaceAll("\\.", ""), new File(IMG_PATH + "/out" + IMG_EXTN));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        Observable<Path> paths = Observable.create(e -> {
            Files.list(Paths.get(IMG_PATH)).forEach(e::onNext);
            e.onComplete();
        });
        long start = System.currentTimeMillis();
        paths.filter(p -> p.getFileName().toString().toLowerCase().contains(IMG_EXTN))
                .doOnNext(p -> System.out.println("Processing file in rx: " + p))
                //.observeOn(Schedulers.io())
                .map(path -> {
                    try {
                        System.out.println("Reading image in: " + Thread.currentThread().getName());
                        return ImageIO.read(path.toFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                //.observeOn(Schedulers.computation())
                .filter(bi -> bi != null)
                .map(bi -> {
                    System.out.println("Processing image in: " + Thread.currentThread().getName());
                    int type = bi.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bi.getType();
                    return resizeImage(bi, type);
                })
                //.observeOn(Schedulers.io())
                .subscribeOn(Schedulers.computation())
                .subscribe(bi -> {
                    try {
                        System.out.println("Writing image in: " + Thread.currentThread().getName());
                        ImageIO.write(bi, IMG_EXTN.replaceAll("\\.", ""), new File(IMG_PATH + "/out" + IMG_EXTN));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        TimeUnit.SECONDS.sleep(10);
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type) {
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
        g.dispose();
        return resizedImage;
    }
}
