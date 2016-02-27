Yet another GELF Logger - A GELF Appender for Log4j 
====================================================================

Why ya-gelf-logger
-------------

This is very simple GELF appender implemented in Java for the Log4j. This is a [GELFJ](vhttps://github.com/t0xa/gelfj.git) fork with
a heavy refactoring with addition of a layer to manage Service Provider Interface to intended to be implemented or extended by a third party   

obviously the Following transports are supported:

 * TCP
 * UDP
 * AMQP

but in the near future I think to add Kafka and Cassandra Sender.
That said, this is a simple experiment to have light and pluggable appender, at moment the best solution as Architeture and completeness that I found around is [logstash-gelf](https://github.com/mp911de/logstash-gelf.git) 
