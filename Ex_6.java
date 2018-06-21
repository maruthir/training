package com.apr.rxjava;

import io.reactivex.Flowable;
import io.reactivex.flowables.ConnectableFlowable;
import java.util.concurrent.TimeUnit;
import org.davidmoten.rx.jdbc.Database;
import org.davidmoten.rx.jdbc.tuple.Tuple2;

public class Ex_6 {

    public static void main(String[] args) throws Exception {
        Database db = Database.from("jdbc:mysql://root:admin123@localhost:3306/training", 5);
        Flowable<Tuple2<String, String>> rows = db.select("select name, email_id from users")
                .getAs(String.class, String.class).publish().refCount();

        rows.blockingSubscribe(s -> System.out.println("Mr." + s._1()));

        Flowable<Integer> count = db
                .update("update users set name = ? where id=?")
                .parameters("Maruthi", 1)
                .counts();
        count.blockingSubscribe(s -> System.out.println("Updated record count: " + s));

        rows.blockingSubscribe(s -> System.out.println("Mr." + s._1()));

        TimeUnit.SECONDS.sleep(5);
        db.close();
        System.out.println("Closed DB");
    }
}
