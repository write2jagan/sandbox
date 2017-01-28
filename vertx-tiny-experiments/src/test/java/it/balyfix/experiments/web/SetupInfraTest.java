package it.balyfix.experiments.web;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import it.balyfix.experiments.web.FirstSetupInfra;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by fbalicchia on 17/01/2017.
 */
@RunWith(VertxUnitRunner.class)
public class SetupInfraTest
{
    private Vertx vertx;

    @Before
    public void setUp(TestContext context) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(FirstSetupInfra.class.getName(),
            context.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testMyRoute(TestContext context) {
        final Async async = context.async();



        vertx.createHttpClient().getNow(8080, "localhost", "/",
            response -> {
                response.handler(body -> {
                    context.assertTrue(body.toString().contains("World"));
                    async.complete();
                });
            });
    }


}
