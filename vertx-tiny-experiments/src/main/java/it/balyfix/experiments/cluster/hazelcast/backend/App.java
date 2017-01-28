package it.balyfix.experiments.cluster.hazelcast.backend;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class App {
	public static void main(String[] args) {
		
		ClientConfig clientConfig = new ClientConfig();
		HazelcastInstance hazelcast = HazelcastClient.newHazelcastClient(clientConfig);
		
		ClusterManager manager = new HazelcastClusterManager(hazelcast);
		
		VertxOptions vertxOptions = new VertxOptions();
		vertxOptions.setClustered(true).setClusterManager(manager);
		
		Vertx.clusteredVertx(vertxOptions, res->{
		
			Vertx vertx = res.result();
			
			vertx.eventBus().consumer("backend", msg->{
			
				System.out.println("Backend got message "+msg.body());
			
				msg.reply("REPLY FROM BACKEND FOR MESSAGE "+msg.body());
				
			});
		
		});
		
		
		
	}
}
