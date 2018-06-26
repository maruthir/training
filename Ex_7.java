package com.apr.rxjava;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import java.sql.Connection;
import java.sql.Statement;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.davidmoten.rx.jdbc.Database;
import org.davidmoten.rx.jdbc.tuple.Tuple2;

class UpdateCall implements Callable<Integer> {
    private Connection con;

    UpdateCall(Connection con) {
        this.con = con;
    }

    public Integer call() throws Exception {
        Statement st = con.createStatement();
        int updateCount = st.executeUpdate("update users set name = 'Maruthi' where id=1");
        st.close();
        System.out.println("Updated DB in thread " + " " + Ex_7.tname());
        return updateCount;
    }
}
public class Ex_7 {

    static String tname() {
        return Thread.currentThread().getName();
    }
    public static void main(String[] args) throws Exception {
        Database db = Database.from("jdbc:mysql://root:admin123@localhost:3306/training", 5);
        Flowable<Tuple2<String, String>> rows = db.select("select name, email_id from users")
                .getAs(String.class, String.class).publish().refCount();
        rows.blockingSubscribe(s -> System.out.println("Mr." + s._1() + " " + tname()));
        db.connection().subscribe(con -> {
            Observable.fromCallable(new UpdateCall(con))
                    .doOnNext(i -> System.out.println("Updated records : " + i + " " + tname()))
                    .subscribe(i -> rows.subscribe(s -> System.out.println("Mr." + s._1() + " " + tname())));
        });

        TimeUnit.SECONDS.sleep(5);
        db.close();
        System.out.println("Closed DB" + " " + tname());
    }
}
