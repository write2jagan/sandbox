Spring Integration with Apache Kafka example
=============================================
This example get a hit from old example [link] (https://github.com/spring-projects/spring-integration-extensions/tree/master/samples/kafka).
After migration to maven, I've refactor configuration to use new SI new API.

I've used version 1.2.1.RELEASE that depends from  Kafka client api 0.8.2.1, Inbound channel Adpter use HighLevel consumer API to retrive message from Kafka 
that IHMO are very straightforward and easy to use respect Simple Consumer API that of simple probably as only the name.


## Running the sample

Start Apache Zookeeper and Apache Kafka according to the documentation for the Apache Kafka project and create 

	bin/zookeeper-server-start.sh config/zookeeper.properties
	bin/kafka-server-start.sh config/server.properties
	
	bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test1
	bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test2
	bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic regextopic2
	bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic regextopic1

	run main for producer or consumer
