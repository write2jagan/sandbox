package it.balyfix.java8.lambdas;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import it.balyfix.stock.model.Stock;
import it.balyfix.stock.service.StockService;
import it.balyfix.utils.StringCombiner;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class StringOperation {

	public static String formatStockReducing(List<Stock> stocks) {
		// BEGIN reducing
		String result = stocks
				.stream()
				.map(Stock::getStockName)
				.collect(
						Collectors.reducing(new StringCombiner(", ", "[", "]"),
								name -> new StringCombiner(", ", "[", "]")
										.add(name), StringCombiner::merge))
				.toString();
		// END reducing
		return result;
	}

	@Test
	public void formatStock() {
		List<Stock> init = StockService.getData();
		String formatStockReducing = formatStockReducing(init);

		System.out.println("valore della stringa " + formatStockReducing);

	}

	@Test
	public void checkType() {

		List<String> code = asList("4324523345344388", "4324524408674448",
				"4324523489590143", "4324526917922879", "4324522054592369",
				"4324520141653731", "4324520495889378", "4324520777507300",
				"4324528762557354", "4324528109065368", "4324524891815854",
				"4324529282037604", "4324529497379544", "4324523345019402",
				"4324521021523358", "4324522289179032", "4324529865339658",
				"4324522862984147", "4324521423843248", "4324529316743631",
				"127748352447529", "5127742825333077", "5127742779590490",
				"5127748298459190", "5127742287286037", "5127741356880530",
				"5127744437063860", "5127747130189437", "5127746156229952",
				"5127749254867178", "5127741668678937", "5127742079427518",
				"5127745573661797");

		List<String> strOut = code.stream().filter(str -> {
			final String sub = str.substring(1, 6);
			return "12774".equalsIgnoreCase(sub);
		}).collect(toList());

		strOut.forEach(System.out::println);
	}

}
