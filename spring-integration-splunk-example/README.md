Spring Integration with Splunk example
=============================================

This is a basic example of use  of spring integration splunk adapter. I started with it for take confident with API.
The example dependes from 1.5.0.0  splunk SDK and from Splunk Adapter 1.2.0.BUILD-SNAPSHOT, documentation about adapter
can be found at https://github.com/spring-projects/spring-integration-splunk 
 	
At the moment spring integration can consume data in 5 ways

* Blocking
* Non blocking
* Saved search
* Realtime
* Export

At the moment Realtime mode is disable but I found two ticket INTEXT-72 and INTEXT-201 that when they will able into the master adpter will provide 
better option like HTTP Event Collector.   
For this example I've used Blocking Mode as consumer instead for producer side I send a custom Event using submit and index type as data writers provided.



## Running the sample
download splunk 6.3 
####Add index
./splunk add index tinyIndex
./splunk add index someIndex
####Start
./splunk start
Launch TinySpluckProducer main and  TinySpluckConsumer