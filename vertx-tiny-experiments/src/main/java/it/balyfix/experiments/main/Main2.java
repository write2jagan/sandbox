package it.balyfix.experiments.main;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;

public class Main2 {

	public static void main(String[] args) {
		
		VertxOptions vertxOption = new VertxOptions();
		Vertx vertx = Vertx.factory.vertx(vertxOption);
		HttpServer httpServer = vertx.createHttpServer();
		httpServer.requestHandler(req -> req.response().end("<h1>hello from main</h1>")).listen(8080);
		
		
		

	}

}
