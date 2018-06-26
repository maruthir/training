/*
 * Copyright 2016 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.reactivex.netty.examples.http.helloworld;

import static rx.Observable.just;

import io.netty.buffer.ByteBuf;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.netty.examples.ExamplesEnvironment;
import io.reactivex.netty.protocol.http.server.HttpServer;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import java.sql.Connection;
import java.sql.Statement;
import java.util.concurrent.Callable;
import org.davidmoten.rx.jdbc.Database;
import org.davidmoten.rx.jdbc.tuple.Tuple2;


/**
 * An HTTP "Hello World" server.
 *
 * This server sends a response with "Hello World" as the content for any request that it recieves.
 */
public final class Ex_8 {

    public static void main(final String[] args) {

        ExamplesEnvironment env = ExamplesEnvironment.newEnvironment(Ex_8.class);
        Database db = Database.from("jdbc:mysql://root:admin123@localhost:3306/training", 5);

        HttpServer<ByteBuf, ByteBuf> server;

        server = HttpServer.newServer()
                .start((req, resp) -> {
                            return resp.writeString(getResponse(req, db));
                        }
                );
        /*Wait for shutdown if not called from the client (passed an arg)*/
        if (env.shouldWaitForShutdown(args)) {
            server.awaitShutdown();
        }

        /*If not waiting for shutdown, assign the ephemeral port used to a field so that it can be read and used by
        the caller, if any.*/
        env.registerServerAddress(server.getServerAddress());
    }

    public static rx.Observable getResponse(HttpServerRequest req, Database db) {
        String op = (String)req.getQueryParameters().get("operation");
        if (op == null || op.equals("")) {
            db.connection().subscribe(con -> {
                Observable.fromCallable(new UpdateCall(con))
                        .subscribe(i -> System.out.println("Updated records : " + i));
            });
            System.out.println("Request process complete");
            return rx.Observable.empty();
        }
        return rx.Observable.empty();
    }

    static class UpdateCall implements Callable<Integer> {

        private Connection con;

        UpdateCall(Connection con) {
            this.con = con;
        }

        @Override
        public Integer call() throws Exception {
            Statement st = con.createStatement();
            int updateCount = st.executeUpdate("update users set name = 'Maruthi' where id=1");
            st.close();
            System.out.println("Updated DB in thread ");
            return updateCount;
        }
    }
}
