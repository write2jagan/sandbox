package it.balyfix.experiments.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

/**
 * Created by fbalicchia on 17/01/2017.
 */
public class FirstSetupInfra extends AbstractVerticle
{


    @Override
    public void start(Future<Void> fut) throws Exception
    {
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

        router.route().blockingHandler(routingContext -> {

        	//Here when can call blocking method as loading and image processing
        	
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/plain");

            System.out.println("request Call");
            // Write to the response and end it
            response.end("Hello World from Vert.x-Web!");
        });

        server.requestHandler(router::accept).listen(8080);
        System.out.println("Complete Call");
        // Load infrastructure component and then call feature complete to bootstrap verticle
        
        Thread.sleep(2000);
        fut.complete();

    }
}
