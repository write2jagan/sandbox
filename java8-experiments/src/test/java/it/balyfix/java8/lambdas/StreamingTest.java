package it.balyfix.java8.lambdas;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import it.balyfix.stock.model.Filter;
import it.balyfix.stock.model.Stock;
import it.balyfix.stock.service.StockService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamingTest {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Test
	public void oldAggregation() {

		List<Stock> init = StockService.getData();

		Map<String, List<Stock>> aggregateOldStyle = new HashMap<String, List<Stock>>();

		for (Stock item : init) {
			if (aggregateOldStyle.get(item.getStockName()) == null) {
				aggregateOldStyle.put(item.getStockName(),
						new ArrayList<Stock>());
			}
			aggregateOldStyle.get(item.getStockName()).add(item);
		}

		Assert.assertTrue(aggregateOldStyle.size() == 3);

	}

	@Test
	public void newAggregation() {

		Supplier<String> message = () -> "New logger style";
		log.debug(message.get());

		List<Stock> init = StockService.getData();
		Map<String, List<Stock>> collect = init.stream().collect(
				groupingBy(stock -> stock.getStockName()));
		Assert.assertTrue(collect.size() == 3);

	}

	@Test
	public void oldFind() {

		List<Stock> init = StockService.getData();

		List<Stock> result = new ArrayList<Stock>();

		for (Stock item : init) {
			if ("stocka".equalsIgnoreCase(item.getStockName())) {
				result.add(item);
			}
		}

		Assert.assertTrue(result.size() == 2);

	}

	@Test
	public void find() {

		List<Stock> init = StockService.getData();
		List<Stock> result = init
				.stream()
				.filter((Stock s) -> s.getStockName()
						.equalsIgnoreCase("stocka")).collect(toList());
		Assert.assertTrue(result.size() == 2);

	}
	
	
	

	@Test
	public void findFiltered() {

		List<Stock> init = StockService.getData();
		
		List<Filter> filters = init.stream()
				.filter(item -> item.getStockCode().isPresent())
				.map(stock -> {
					Filter answer = new Filter();
					answer.setStock(stock);
					return answer;
				}).collect(Collectors.toList());
		
		
		Assert.assertTrue(filters.size() == 4);
		

	}

	@Test
	public void findwithLogging() {

		// for debug is necessary to put a break point inside a peek into the
		// Consumer body

		List<Stock> init = StockService.getData();
		List<Stock> result = init
				.stream()
				.filter((Stock s) -> s.getStockName()
						.equalsIgnoreCase("stocka"))
				.peek(s -> log.info("retrive Stock {} ", s.getStockName()))
				.collect(toList());
		Assert.assertTrue(result.size() == 2);

	}

}
