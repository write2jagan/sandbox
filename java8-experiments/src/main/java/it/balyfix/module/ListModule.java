package it.balyfix.module;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ListModule {

	public static interface List<T> {

		public abstract T head();

		public abstract List<T> tail();

		public abstract Boolean isEmpty();

		public abstract List<T> filter(Function<T, Boolean> f);

		public abstract <T2> List<T2> map(Function<T, T2> f);

		public abstract <T2> T2 foldLeft(T2 seed, BiFunction<T2, T, T2> f);

		public abstract <T2> T2 foldRigth(T2 seed, BiFunction<T, T2, T2> f);

		public abstract void foreach(Function<T, Boolean> f);
		

	}

	public static final class NonEmptyList<T> implements List<T> {
		@Override
		public T head() {
			Optional<T> headOptional = Optional.of(_head);
			return headOptional.isPresent() ? headOptional.get() : null  ;
		}

		@Override
		public List<T> tail() {
			Optional<List<T>> tailOptional = Optional.of(_tail);
			return tailOptional.isPresent() ? tailOptional.get() : (List<T>) EMPTY;
		}

		@Override
		public Boolean isEmpty() {
			return false;
		}

		protected NonEmptyList(T head, List<T> tail) {
			this._head = head;
			this._tail = tail;
		}

		private final T _head;

		private final List<T> _tail;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((_head == null) ? 0 : _head.hashCode());
			result = prime * result + ((_tail == null) ? 0 : _tail.hashCode());
			return result;
		}

		@Override
		public String toString() {
			return "(" + head() + ", " + tail() + ")";
		}

		@Override
		public List<T> filter(Function<T, Boolean> f) {

			if (f.apply(head())) {
				return list(head(), tail().filter(f));
			}
			else {
				return tail().filter(f);
			}

		}

		@Override
		public <T2> List<T2> map(Function<T, T2> f) {
			return list(f.apply(head()), tail().map(f));

		}

		@Override
		public <T2> T2 foldLeft(T2 seed, BiFunction<T2, T, T2> f) {
			return tail().foldLeft(f.apply(seed, head()), f);

		}

		@Override
		public <T2> T2 foldRigth(T2 seed, BiFunction<T, T2, T2> f) {
			return f.apply(head(), tail().foldRigth(seed, f));
		}

		@Override
		public void foreach(Function<T, Boolean> f) {
			f.apply(head());
			tail().foreach(f);
		}

	}

	public static class EmptyListHasNoHead extends RuntimeException {
	}

	public static class EmptyListHasNoTail extends RuntimeException {
	}

	public static final List<? extends Object> EMPTY = new List<Object>() {

		@Override
		public Object head() {
			throw new EmptyListHasNoHead();
		}

		@Override
		public List<Object> tail() {
			throw new EmptyListHasNoTail();
		}

		@Override
		public Boolean isEmpty() {
			return true;
		}

		@Override
		public String toString() {
			return "()";
		}

		@Override
		public List<Object> filter(Function<Object, Boolean> f) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T2> List<T2> map(Function<Object, T2> f) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T2> T2 foldLeft(T2 seed, BiFunction<T2, Object, T2> f) {
			return seed;
		}

		@Override
		public <T2> T2 foldRigth(T2 seed, BiFunction<Object, T2, T2> f) {
			// TODO Auto-generated method stub
			return seed;
		}

		@Override
		public void foreach(Function<Object, Boolean> f) {
			// TODO Auto-generated method stub

		}

	};

	public static List<? extends Object> emptyList() {
		return EMPTY;
	}

	public static <T> List<T> list(T head, List<T> tail) {
		return new NonEmptyList<T>(head, tail);
	}
	
	
	public static <T> List<T> list(java.util.List<T> list) {
		
		if(list.isEmpty())
		{
			return (List<T>) EMPTY;
		}
		if(list.size() == 1)
		{
			return new NonEmptyList<T>(list.get(0), (List<T>) EMPTY);
		}else
		{
			return new NonEmptyList<T>(list.get(0), list(list.subList(1, list.size())));
		}
		
	}
	

}
