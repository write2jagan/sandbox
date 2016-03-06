package it.balyfix.patterns.HalfSyncHalfAsync;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;
import org.mockito.InOrder;


public class RunnerHalfSyncHalfAsync {
	
	@Test
	public void simpleExecute() throws Exception
	{
		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(10);
		AsyncServiceFirtVariant asyncService = new AsyncServiceFirtVariant(queue);
		final RequestToManage<String> request = mock(RequestToManage.class);
		String response = "Done";
		when(request.call()).thenReturn(response);
		asyncService.execute(request);
		verify(request, timeout(3000)).onResult(eq(response));
	    final InOrder inOrder = inOrder(request);
	    inOrder.verify(request, times(1)).onLoad();
	    inOrder.verify(request, times(1)).call();
	    inOrder.verify(request, times(1)).onResult(response);
	    verifyNoMoreInteractions(request);
		
	}
	
	
	@Test
	public void singleException() throws Exception
	{
		final AsyncServiceFirtVariant service = new AsyncServiceFirtVariant(new LinkedBlockingQueue<>());
	    final RequestToManage<String> request = mock(RequestToManage.class);
	    final IllegalStateException exception = new IllegalStateException();
	    doThrow(exception).when(request).onLoad();
	    service.execute(request);
	    verify(request, timeout(2000)).onError(eq(exception));
	    final InOrder inOrder = inOrder(request);
	    inOrder.verify(request, times(1)).onLoad();
	    inOrder.verify(request, times(1)).onError(exception);
	    verifyNoMoreInteractions(request);

	}
	
	
	
	
	

}
