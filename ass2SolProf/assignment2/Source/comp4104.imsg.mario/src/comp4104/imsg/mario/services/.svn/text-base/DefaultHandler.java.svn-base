package comp4104.imsg.mario.services;

import comp4104.imsg.core.net.IRequest;
import comp4104.imsg.core.net.IResponse;
import comp4104.imsg.core.net.Response;

/**
 * A default service handler for handling unknown commands.
 */
public class DefaultHandler extends AbstractServiceHandler
{
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.mario.services.AbstractServiceHandler#handleRequest(comp4104.imsg.core.net.IRequest)
	 */
	@Override
	public void handleRequest(IRequest request) {
		IResponse response = new Response();
		response.setStatus(IResponse.NOT_FOUND);
		writeResponse(request.getSocket(), response);
	}
}