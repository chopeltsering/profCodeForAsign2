package comp4104.imsg.mario.services;

import comp4104.imsg.core.logging.Logger;
import comp4104.imsg.core.model.Message;
import comp4104.imsg.core.net.IRequest;
import comp4104.imsg.core.net.IResponse;
import comp4104.imsg.core.net.Response;
import comp4104.imsg.mario.ClientManager;

/**
 * A message handler for handling chat message requests.
 */
public class MessageHandler extends AbstractServiceHandler
{
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.mario.services.AbstractServiceHandler#handleRequest(comp4104.imsg.core.net.IRequest)
	 */
	@Override
	public void handleRequest(IRequest request) {
		try {
			// deserialize the message
			Message message = Message.createMessage(request.getBody());
			
			// make sure the user is logged in
			if (ClientManager.getInstance().isLoggedIn(message.getSender())) {
				// write the response
				IResponse response = new Response();
				response.setStatus(IResponse.OK);
				writeResponse(request.getSocket(), response);
				
				// send out the message
				message.addRecipient(message.getSender());
				ClientManager.getInstance().sendMessage(message);
			}
			else {
				IResponse response = new Response();
				response.setStatus(IResponse.NOT_AUTHORIZED);
				response.setBody("Not logged in.".getBytes());
				writeResponse(request.getSocket(), response);
			}
		} catch (Exception e) {
			Logger.log(e);
			IResponse response = new Response();
			response.setStatus(IResponse.BAD_REQUEST);
			response.setBody("Bad message format".getBytes());
			writeResponse(request.getSocket(), response);
		}
	}
}