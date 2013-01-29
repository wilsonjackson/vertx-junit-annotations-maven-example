package com.example.verticle;

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.deploy.Verticle;

public class StandAloneVerticle extends Verticle {

    public static final String MESSAGE = "hello there, friend";

    @Override
    public void start() throws Exception {
        System.out.println("Verticle: Starting HTTP server");
        vertx.createHttpServer().requestHandler(new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest request) {
                System.out.println("Verticle: Handling HTTP request");
                request.response.headers().put("Content-length", String.valueOf(MESSAGE.length()));
                request.response.writeBuffer(new Buffer(MESSAGE));
                request.response.end();
            }
        }).listen(8088);
    }
}