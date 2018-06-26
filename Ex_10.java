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

import io.netty.buffer.ByteBuf;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.netty.examples.ExamplesEnvironment;
import io.reactivex.netty.protocol.http.server.HttpServer;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


/**
 * An HTTP "Hello World" server.
 *
 * This server sends a response with "Hello World" as the content for any request that it recieves.
 */
public final class Ex_10 {

    public static void main(final String[] args) {

        ExamplesEnvironment env = ExamplesEnvironment.newEnvironment(Ex_10.class);
        HttpServer<ByteBuf, ByteBuf> server;

        server = HttpServer.newServer()
                .start((req, resp) -> {
                            return resp.writeString(getResponse(req));
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

    public static rx.Observable getResponse(HttpServerRequest req) {
//        Observable<String> fileSource = Observable.create(e -> {
//            Files.lines(Paths.get("/Users/maruthir/scripts.log")).forEach(e::onNext);
//            e.onComplete();
//        }).map(s -> s + "<br>").filter(s -> s.length() > 50).startWith("<html><body>");

        Flowable<String> fileFlow = Flowable.generate(
                () -> new BufferedReader(new FileReader("/Users/maruthir/scripts.log")),
                (reader, emitter) -> {
                    final String line = reader.readLine();
                    if (line != null) {
                        emitter.onNext(line);
                    } else {
                        emitter.onComplete();
                    }
                },
                reader -> reader.close()
        );
        return rx.Observable.create(e -> {
            fileFlow.subscribe(res -> e.onNext("" + res));
            e.onCompleted();
        });
    }
}
