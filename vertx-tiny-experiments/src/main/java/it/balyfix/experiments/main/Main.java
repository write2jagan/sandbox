package it.balyfix.experiments.main;

import io.vertx.core.Vertx;
import it.balyfix.experiments.web.WebserverChat;

public class Main {

	public static void main(String[] args) {
		 Vertx vertx = Vertx.vertx();
	        vertx.deployVerticle(WebserverChat.class.getName());

	}

}
