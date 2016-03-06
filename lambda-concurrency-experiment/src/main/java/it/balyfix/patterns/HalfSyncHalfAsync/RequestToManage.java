package it.balyfix.patterns.HalfSyncHalfAsync;

import java.util.concurrent.Callable;

public interface RequestToManage<T> extends Callable<T> {

	public String onResult(T result);

	void onLoad();

	void onError(Exception e);

}
