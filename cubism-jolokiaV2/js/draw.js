var context = cubism.context()
            .serverDelay(0)
            .clientDelay(0)
            .step(1e3)
            .size(960);

function getHeapMemory(name) {
          var values = [],
              last;

            return context.metric(function(start, stop, step, callback) {
            start = +start, stop = +stop;
            if (isNaN(last)) last = start;
            while (last < stop) {
                last += step;
                d3.json("http://127.0.0.1:8080/jolokia/read/java.lang:type=Memory", function(data) {
                    eval("values.push(data.value.HeapMemoryUsage." + name + ")");
                });
            }
            callback(null, values = values.slice((start - stop) / step));
            }, name);
        }
        
function loadContext(){
	
	var memList = ["used", "committed", "init", "max"];
        var heapUsed = [];

        for (var i = 0; i < memList.length; i++) {
            heapUsed.push(getHeapMemory(memList[i]));
        }

        d3.select("#memory").call(function(div) {

          div.append("div")
              .attr("class", "axis")
              .call(context.axis().orient("top"));

          div.selectAll(".horizon")
              .data(heapUsed)
              .enter().append("div")
              .attr("class", "horizon")
              .call(context.horizon()
                .height(50));
        });

        context.on("focus", function(i) {
              d3.selectAll(".value").style("right", i == null ? null : context.size() - i + "px");
        });

	
	
	
	}
        
