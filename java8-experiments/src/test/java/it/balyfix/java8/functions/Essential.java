package it.balyfix.java8.functions;

import it.balyfix.stock.model.Stock;
import it.balyfix.stock.service.StockService;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

public class Essential {

	@Test
	public void predicate() {
		Stock stock = new Stock("Stock1");
		stock.setIsValid(true);
		Predicate<Stock> validStock = (Stock t) -> t.isValid();

		Stock stock2 = new Stock("Stock3");
		stock2.setIsValid(false);
		Assert.assertFalse(validStock.test(stock2));
	}

	@Test
	public void predicateWithMethod() {

		List<Stock> stockList = new ArrayList<Stock>();

		Stock stock = new Stock("Stock0");
		stockList.add(stock);
		stock.setIsValid(true);
		stock.setStockId(123);
		Stock stock1 = new Stock("Stock1");
		stock1.setIsValid(true);
		stock1.setStockId(456);
		stockList.add(stock1);
		Stock stock2 = new Stock("Stock2");
		stock2.setIsValid(true);
		stock2.setStockId(789);
		stockList.add(stock2);

		Predicate<Stock> validStock = (Stock t) -> t.isValid() == true;
		Stock stock3 = new Stock("Stock3");
		stock3.setIsValid(false);
		stock3.setStockId(5575);
		stockList.add(stock3);
		List<Integer> checkValid = checkValid(stockList.stream(), validStock);
		Assert.assertEquals(3, checkValid.size());

		List<Integer> invalid = checkValid(stockList.stream(),
				validStock.negate());
		Assert.assertEquals(1, invalid.size());

	}

	private List<Integer> checkValid(Stream<Stock> stockStream,
			Predicate<Stock> stockPredicate) {

		return stockStream.filter(stockPredicate)
				.map(stock -> stock.getStockId()).collect(Collectors.toList());
	}

	@Test
	public void function() {
		Function<Integer, Double> centigradeToFahrenheitInt = x -> new Double(
				(x * 9 / 5) + 32);
		Double apply = centigradeToFahrenheitInt.apply(30);
		Assert.assertEquals(86, apply, 0.0000000000000001);
	}

	@Test
	public void supplier() {
		ThreadLocal<SimpleDateFormat> simpleDate = ThreadLocal
				.withInitial(() -> new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss.SSSZ"));

	}

	@Test
	public void compareSumOfSquare() {

		int availableProcessors = Runtime.getRuntime().availableProcessors();
		IntStream range = IntStream.range(0, Integer.MAX_VALUE);
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		sequentialSumOfSquare(range);
		stopWatch.stop();
		long sequentialSumOfSquare = stopWatch.getNanoTime();
		System.out.println("sequentialSumOfSquare in Ns "
				+ sequentialSumOfSquare);
		stopWatch.reset();
		stopWatch.start();
		IntStream range2 = IntStream.range(0, Integer.MAX_VALUE);
		parallelSumOfSquare(range2);
		stopWatch.stop();
		long parallelSumOfSquare = stopWatch.getNanoTime();
		System.out.println("parallelSumOfSquare in Ns " + parallelSumOfSquare);

		if (availableProcessors > 1) {
			Assert.assertTrue(parallelSumOfSquare <= sequentialSumOfSquare);
		} else {
			Assert.assertTrue(parallelSumOfSquare > sequentialSumOfSquare);
		}

	}

	@Test
	public void parallelSum() {
		IntStream range = IntStream.range(0, Integer.MAX_VALUE);
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		parallelSumOfSquare(range);
		stopWatch.stop();
		System.out.println("valure stop watch ns " + stopWatch.getNanoTime());

	}

	private int sequentialSumOfSquare(IntStream stream) {

		return (int) stream.map(x -> x * x).count();

	}

	@Test
	public void multiplyThrough() {

		List<Integer> numbers = asList(1, 2, 3);
		BinaryOperator<Integer> mul = (x, y) -> x * y;
		Integer result = 5 * numbers.parallelStream().reduce(1, mul);

		Assert.assertTrue(result == 30);

	}

	@Test
	public void sumTotalItems() {

		List<Stock> data = StockService.getData();

		double sum = data.stream().map(stock ->{Double value = stock.getDouble(); return value == null ? Double.valueOf(0):value.doubleValue();}).reduce(Double.valueOf(0), (x,y) -> x + y);

		Assert.assertTrue(sum > 0);

	}

	private int parallelSumOfSquare(IntStream stream) {

		return (int) stream.parallel().map(x -> x * x).count();

	}

}
