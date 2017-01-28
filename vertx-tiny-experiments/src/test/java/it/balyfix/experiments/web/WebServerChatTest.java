package it.balyfix.experiments.web;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import it.balyfix.experiments.web.WebserverChat;

@RunWith(VertxUnitRunner.class)
public class WebServerChatTest {

	private Vertx vertx;

    @Before
    public void setUp(TestContext context) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(WebserverChat.class.getName(),
            context.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }
	
    
    @Test
    public void testChat(TestContext context) {
        final Async async = context.async();
        
        vertx.createHttpClient().getNow(8080, "localhost", "/",
            response -> {
                response.handler(body -> {
                	context.assertNotNull(body.toString());
                    async.complete();
                });
            });
    }
    
    
    
	
	
}
