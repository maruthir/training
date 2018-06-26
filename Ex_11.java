package com.apr.rxjava;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.flowables.ConnectableFlowable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.davidmoten.rx.jdbc.Database;
import org.davidmoten.rx.jdbc.tuple.Tuple2;

public class Ex_11 {

    public static void main(String[] args) throws Exception {
        Database db = Database.from("jdbc:mysql://root:admin123@localhost:3306/training", 5);
        Flowable<Integer> sales = db.select("select sales from sales")
                .getAs(Integer.class);
        Flowable<Integer> sales2 = db.select("select sales from sales2")
                .getAs(Integer.class);
        Flowable<Integer> allSales = sales.concatWith(sales2);

        Flowable<String> months = db.select("select month from sales")
                .getAs(String.class);
        Flowable<String> months2 = db.select("select month from sales2")
                .getAs(String.class);
        //months.mergeWith(months2).subscribe(System.out::println);

        Flowable<String> allMonths = months.concatWith(months2);

        ConnectableFlowable<Integer> allSalesFlow = allSales.publish();
        allSalesFlow.scan((acc, next) -> acc + next)
                        .zipWith(allMonths, (sale, mon) -> mon + " -> " + sale)
                        .subscribe(System.out::println);
        allSalesFlow.connect();

        Single<Integer> totalSales = allSalesFlow.reduce(0, (prev, next) -> prev + next);
        totalSales.subscribe(i -> System.out.println("Total sales for year = " + i));



















//
//        Flowable<String> months = db.select("select month from sales")
//                .getAs(String.class);
//        Flowable<String> months2 = db.select("select month from sales2")
//                .getAs(String.class);
//
//        //months.mergeWith(months2).subscribe(System.out::println);
//        Flowable<String> allMonths = months.concatWith(months2);
//
//        ConnectableFlowable<Integer> allSalesFlow = allSales.publish();
//        allSalesFlow.scan((acc, next) -> acc + next)
//                .zipWith(allMonths, (sale, mon) -> mon + " -> " + sale)
//                .subscribe(System.out::println);
//
//        Single<Integer> totalSales = allSalesFlow.reduce(0, (prev, next) -> prev + next);
//        totalSales.subscribe(i -> System.out.println("Total sales for year = " + i));
//
//        allSalesFlow.connect();


        TimeUnit.SECONDS.sleep(5);
        db.close();
        System.out.println("Closed DB");
    }
}
