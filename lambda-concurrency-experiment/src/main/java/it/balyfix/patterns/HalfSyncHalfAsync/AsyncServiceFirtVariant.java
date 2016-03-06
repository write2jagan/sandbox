package it.balyfix.patterns.HalfSyncHalfAsync;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This class represent async layer for halfsync and hanf_async patter. 
 * There are different approach to implement this pattern this is the first one.
 * this implementation retrieve task and send to a queue for later the job will be executed.
 * 
 * It decouples asyn and syn processing services adding a service simplifying the develop 
 * and avoid performance hit
 * 
 * In the past this pattern was used from BSD UNIX  to structure the concurrent I/O architecture 
 * of application processes and the operating system
 * kernel
 * 
 * 
 * 
 * 
 * @author fbalicchia
 */

public class AsyncServiceFirtVariant {

	private ExecutorService service;

	public AsyncServiceFirtVariant(BlockingQueue<Runnable> queue) {
		service = new ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS, queue,
				new ThreadPoolExecutor.DiscardOldestPolicy());
	}

	public void execute(RequestToManage<String> request) {
		try
		{
			request.onLoad();
		}catch (Exception e)
		{
			request.onError(e);
			return;
		}
		service.submit(new FutureTask<String>(request) {
			@Override
			protected void done() {
				super.done();
				try {
					request.onResult(get());
				} catch (InterruptedException | ExecutionException e) {
					request.onError(e);
				}
			}

		});
	}

}
