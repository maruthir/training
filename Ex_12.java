package com.apr.rxjava;

import io.reactivex.Flowable;
import java.util.concurrent.TimeUnit;
import org.davidmoten.rx.jdbc.Database;
import org.davidmoten.rx.jdbc.tuple.Tuple2;

public class Ex_12 {

    public static void main(String[] args) throws Exception {
        Database db = Database.from("jdbc:mysql://root:admin123@localhost:3306/training", 5);
        Flowable<Tuple2<String, String>> rows = db.select("select name, email_id from users")
                .getAs(String.class, String.class);

        rows.sorted((tup1, tup2) -> tup1._1().compareTo(tup2._1()))
                .distinct(tup -> tup._1())
                .blockingSubscribe(s -> System.out.println("Mr." + s._1()));

        TimeUnit.SECONDS.sleep(5);
        db.close();
        System.out.println("Closed DB");
    }
}
