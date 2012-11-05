package comp4104.imsg.mario.services;

import java.net.Socket;

import comp4104.imsg.core.logging.Logger;
import comp4104.imsg.core.net.IRequest;
import comp4104.imsg.core.net.IResponse;
import comp4104.imsg.mario.Client;
import comp4104.imsg.mario.ClientManager;

/**
 * An abstract service handler implementation.
 */
public abstract class AbstractServiceHandler implements IServiceHandler
{
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.mario.services.IServiceHandler#handleRequest(comp4104.imsg.core.net.IRequest)
	 */
	public abstract void handleRequest(IRequest request);
	
	/**
	 * Writes a response.
	 * @param socket
	 * 		  The socket connection.
	 * @param response
	 * 		  The response.
	 */
	protected void writeResponse(Socket socket, IResponse response)
	{
		try {
			Client client = ClientManager.getInstance().getClient(socket);
			client.writeResponse(response);
		} catch (Throwable t) {
			Logger.log(t);
		}
	}
}