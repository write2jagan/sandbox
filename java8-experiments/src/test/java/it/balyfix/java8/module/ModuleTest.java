package it.balyfix.java8.module;

import org.junit.Assert;
import org.junit.Test;

import it.balyfix.module.ListModule.List;

import static it.balyfix.module.ListModule.*;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ModuleTest {

	@Test
	public void ricursiveTest() {
		List<String> list = list("a", list("b", list("c", (List<String>) EMPTY)));
		Assert.assertEquals("(a, (b, (c, ())))", list.toString());
	}

	@Test
	public void foldLeft() {

		List<String> list = list("hello", list("world", list("functional", list("programming", (List<String>) EMPTY))));
		String foldLeftStr = list.foldLeft("write", new BiFunction<String, String, String>() {
			@Override
			public String apply(String seed, String item) {
				return seed + item;
			}
		});
		Assert.assertEquals("writehelloworldfunctionalprogramming", foldLeftStr);
	}

	@Test
	public void foldRigth() {
		List<String> list = list("hello", list("world", list("functional", list("programming", (List<String>) EMPTY))));
		String foldRigthStr = list.foldRigth("write", new BiFunction<String, String, String>() {
			@Override
			public String apply(String seed, String item) {
				return seed + item;
			}
		});
		Assert.assertEquals("helloworldfunctionalprogrammingwrite", foldRigthStr);
	}

	@Test
	public void forEach() {
		List<String> list = list("hello", list("world", list("functional", list("programming", (List<String>) EMPTY))));
		list.foreach(new Function<String, Boolean>() {

			@Override
			public Boolean apply(String item) {
				System.out.println(item);
				return true;
			}
		});

	}

	@Test
	public void costructorList() {
		java.util.List<String> arrayList = new ArrayList<>();
		arrayList.add("Ciao");
		arrayList.add("Mondo");
		arrayList.add("da Fix");
		List<String> newList = list(arrayList);
		Assert.assertEquals("Ciao, (Mondo, (da Fix, ())))", newList);

	}

}
