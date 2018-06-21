package com.apr.rxjava;

import org.davidmoten.rx.jdbc.Database;

public class Ex_3 {

    public static void main(String[] args) throws Exception {
//        Database db = Database.from("jdbc:derby://localhost:1527//Users/maruthir/Documents/Training/derby/mydb", 5);
//        Database db = Database.from("jdbc:derby://localhost:1527/C:/wherever/mydb", 5);
        Database db = Database.from("jdbc:mysql://root:admin123@localhost:3306/training", 5);
        db.select("select name from users")
                .getAs(String.class)
                .subscribe(s -> System.out.println(Thread.currentThread().getName() + ":" + s));
    }
}
