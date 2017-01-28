package it.balyfix.experiments.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class ServiceExample4Discovery extends AbstractVerticle {

	public static void main(String[] Args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(ServiceExample4Discovery.class.getName());
	}

	@Override
	public void start() {

		String jsonResponse = "{\"servername\": \"Sample server for discovery\", \"status\": { \"alive\": \"true\", \"node\": \"1\" }}";
		Router router = Router.router(vertx);
		router.get("/api").handler(routingContext ->

		{
			routingContext.response().end(jsonResponse);
		});

		vertx.createHttpServer().requestHandler(router::accept).listen(8080, "localhost");

	}

}
