package it.balyfix.experiments.cluster.hazelcast.frontend;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import java.util.concurrent.atomic.AtomicInteger;

public class App {

	public static void main(String[] args) {
		
		VertxOptions options = new VertxOptions();
		
		Vertx.clusteredVertx(options, res->{
		
			Vertx vertx = res.result();
			EventBus eb = vertx.eventBus();
			
			AtomicInteger counter = new AtomicInteger();
			
			vertx.setPeriodic(1000, foo->{
			
				int i = counter.incrementAndGet();
				
				System.out.println("Now sending message "+i);
				
				
				eb.send("backend", i, new DeliveryOptions().setSendTimeout(1000), reply -> {
				
					if(reply.failed()){
						System.out.println("ERROR: No reply for message "+i);
					} else {
						System.out.println("SUCCESS: Got reply for message "+i+": "+reply.result().body());
					}
				});
				
				
			
			});
			
		});
		
	}
}
