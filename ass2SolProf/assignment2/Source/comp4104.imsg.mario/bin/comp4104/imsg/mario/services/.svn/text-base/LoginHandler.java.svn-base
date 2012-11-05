package comp4104.imsg.mario.services;

import comp4104.imsg.core.net.IRequest;
import comp4104.imsg.core.net.IResponse;
import comp4104.imsg.core.net.Response;
import comp4104.imsg.mario.Client;
import comp4104.imsg.mario.ClientManager;

/**
 * A login handler for handling login requests.
 */
public class LoginHandler extends AbstractServiceHandler
{
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.mario.services.AbstractServiceHandler#handleRequest(comp4104.imsg.core.net.IRequest)
	 */
	public void handleRequest(IRequest request) {
		String username = new String(request.getBody());
		Client client = ClientManager.getInstance().getClient(request.getSocket());
		boolean success = ClientManager.getInstance().login(client, username);
		if (success) {
			// send the response
			IResponse response = new Response();
			response.setStatus(IResponse.OK);
			writeResponse(request.getSocket(), response);
			
			// send the updated client list
			// to all clients
			ClientManager.getInstance().sendUpdatedClientList();
			ClientManager.getInstance().sendLoginMessage(username);
		}
		else {
			// username already in use
			IResponse response = new Response();
			response.setStatus(IResponse.NOT_AUTHORIZED);
			response.setBody("Duplicate or invalid username".getBytes());
			writeResponse(request.getSocket(), response);
		}
	}
}