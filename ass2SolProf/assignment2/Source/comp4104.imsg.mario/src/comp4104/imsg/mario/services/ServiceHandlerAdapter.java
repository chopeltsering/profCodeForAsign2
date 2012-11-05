package comp4104.imsg.mario.services;

import comp4104.imsg.core.logging.Logger;
import comp4104.imsg.core.net.IRequest;
import comp4104.imsg.core.net.IResponse;
import comp4104.imsg.core.net.Response;

/**
 * An adapter to wrap all handlers that throw an exception to ensure
 * at least a server error response is sent back to the client.
 */
public class ServiceHandlerAdapter extends AbstractServiceHandler
{
	private IServiceHandler 	serviceHandler;

	/**
	 * Creates a new service handler adapter.
	 * @param serviceHandler
	 * 		  The service handler to wrap.
	 */
	public ServiceHandlerAdapter(IServiceHandler serviceHandler) {
		this.serviceHandler = serviceHandler;
	}
	
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.mario.services.AbstractServiceHandler#handleRequest(comp4104.imsg.core.net.IRequest)
	 */
	@Override
	public void handleRequest(IRequest request) {
		try {
			serviceHandler.handleRequest(request);
		} catch (Throwable t) {
			Logger.log(t);
			handleException(request, t);
		}
	}
	
	private void handleException(IRequest request, Throwable t)
	{
		IResponse response = new Response();
		response.setStatus(IResponse.SERVER_ERROR);
		response.setBody(t.getMessage().getBytes());
		writeResponse(request.getSocket(), response);
	}
}