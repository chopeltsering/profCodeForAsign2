package comp4104.imsg.mario;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import comp4104.imsg.core.logging.Logger;
import comp4104.imsg.core.net.IRequest;
import comp4104.imsg.core.net.IRequestResponse;
import comp4104.imsg.core.net.IResponse;
import comp4104.imsg.mario.services.IServiceHandler;
import comp4104.imsg.mario.services.ServiceHandlerAdapter;

/**
 * A dispatcher for the Reactor pattern.
 */
public class Dispatcher
{
	public  static final String DEFAULT_HANDLER_KEY = "defaultHandler";
	private static final int 	MAX_THREADS	= 10;
	
	private HashMap<String,IServiceHandler> 	services;
	private ExecutorService						executorService;
	
	/**
	 * Creates a <code>Dispatcher</code> instance.
	 */
	public Dispatcher() {
		services = new HashMap<String, IServiceHandler>();
		executorService = Executors.newFixedThreadPool(MAX_THREADS);
	}
	
	/**
	 * Register a sevice handler.
	 * @param key
	 * 		  The service handler key.
	 * @param handler
	 * 		  The service handler.
	 */
	public void registerHandler(String key, IServiceHandler handler) {
		services.put(key, new ServiceHandlerAdapter(handler));
	}
	
	/**
	 * Handle a request or response.
	 * @param requestResponse
	 * 		  The request or response to handle.
	 */
	public void handleRequestResponse(IRequestResponse requestResponse) {
		if (requestResponse instanceof IRequest) {
			handleRequest((IRequest)requestResponse);
		} else if (requestResponse instanceof IResponse) {
			handleResponse((IResponse)requestResponse);
		}
	}
	
	private void handleRequest(final IRequest request) {
		final IServiceHandler handler = getHandler(request);
		executorService.execute(new Runnable() {
			public void run() {
				Logger.log("Request received");
				handler.handleRequest(request);
			}
		});
	}
	
	private void handleResponse(IResponse response) {
		Logger.log("Response received");
	}
	
	private IServiceHandler getHandler(IRequest request) {
		if (services.containsKey(request.getCommand())) {
			return services.get(request.getCommand());
		}
		
		return services.get(DEFAULT_HANDLER_KEY);
	}
}