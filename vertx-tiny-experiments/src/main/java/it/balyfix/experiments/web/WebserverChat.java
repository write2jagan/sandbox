package it.balyfix.experiments.web;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.ext.web.Router;

public class WebserverChat extends AbstractVerticle {

	Logger logger = LoggerFactory.getLogger(WebserverChat.class.getName());

	@Override
	public void start() {
		final Pattern chatUrlPattern = Pattern.compile("/chat/(\\w+)");
		final EventBus eventBus = vertx.eventBus();

		Router router = Router.router(vertx);

		router.get("/").handler(routingContext -> {
			routingContext.response().sendFile("web/chat.html");
		});

		router.getWithRegex(".*\\.(css|js)$").handler(routingContext -> {
			routingContext.response().sendFile(("web/" + new File(routingContext.request().path())));
		});

		vertx.createHttpServer().requestHandler(router::accept).listen(8080, "localhost");

		vertx.createHttpServer().websocketHandler(new Handler<ServerWebSocket>() {
			@Override
			public void handle(final ServerWebSocket ws) {
				final Matcher m = chatUrlPattern.matcher(ws.path());
				if (!m.matches()) {
					ws.reject();
					return;
				}

				final String chatRoom = m.group(1);
				final String id = ws.textHandlerID();
				logger.info("registering new connection with id: {0} for chat-room: {1}", id, chatRoom);

				LocalMap<String, Object> localMap = vertx.sharedData().getLocalMap("chat.room." + chatRoom);

				localMap.put(id, 0);

				ws.closeHandler(new Handler<Void>() {
					@Override
					public void handle(final Void event) {
						logger.info("un-registering connection with id: {0}  from chat-room: {1}", id, chatRoom);
						vertx.sharedData().getLocalMap("chat.room." + chatRoom).remove(id);
					}
				});

				ws.handler(new Handler<Buffer>() {
					@Override
					public void handle(final Buffer data) {

						ObjectMapper m = new ObjectMapper();
						try {
							JsonNode rootNode = m.readTree(data.toString());
							((ObjectNode) rootNode).put("received", new Date().toString());
							String jsonOutput = m.writeValueAsString(rootNode);
							logger.info("json generated: {0}", jsonOutput);
							LocalMap<String, Object> maps = vertx.sharedData().getLocalMap("chat.room." + chatRoom);
							for (String chatter : maps.keySet()) {
								eventBus.send(chatter, jsonOutput);
							}
						} catch (IOException e) {
							ws.reject();
						}
					}
				});

			}
		}).listen(8090);
	}
}
