package comp4104.imsg.mario;

import java.io.IOException;
import java.net.Socket;

import comp4104.imsg.core.Lamport;
import comp4104.imsg.core.logging.Logger;
import comp4104.imsg.core.net.IRequest;
import comp4104.imsg.core.net.IRequestResponse;
import comp4104.imsg.core.net.IRequestResponseReader;
import comp4104.imsg.core.net.IRequestResponseWriter;
import comp4104.imsg.core.net.IResponse;

/**
 * Represents a client connection to the server.
 */
public class Client implements Runnable
{
	private Socket socket;
	private Dispatcher dispatcher;
	private IRequestResponseWriter writer;
	private IRequestResponseReader reader;
	
	/**
	 * Creates a new <code>Client</code> connection instance.
	 * @param socket
	 * 		  The socket connection.
	 * @param dispatcher
	 * 		  The dispatcher.
	 * @param writer
	 * 		  The request/response writer.
	 * @param reader
	 * 		  The request/response reader.
	 */
	public Client(Socket socket, Dispatcher dispatcher, IRequestResponseWriter writer, IRequestResponseReader reader) {
		this.dispatcher = dispatcher;
		this.writer = writer;
		this.reader = reader;
		this.socket = socket;
	}
	
	/**
	 * Gets the socket connection.
	 * @return The socket connection.
	 */
	public Socket getSocket() {
		return socket;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		ClientManager.getInstance().registerClient(this, socket);
		
		try {
			while (!socket.isClosed()) {
				IRequestResponse requestResponse = reader.readRequestResponse();
				if (requestResponse != null) {
					requestResponse.setSocket(socket);
				}
				
				// update the lamport
				updateLamport(requestResponse);
				
				// dispatch the request or response
				dispatcher.handleRequestResponse(requestResponse);
			}
		}
		catch (Exception e) {
			Logger.log(e);
		}
		finally {
			ClientManager.getInstance().unregisterClient(this);
			try { socket.close(); }
			catch (IOException ioe) {}
		}
	}
	
	private void updateLamport(IRequestResponse requestResponse) {
		try {
			long timestamp = Long.parseLong(requestResponse.getHeader("lamport"));
			Lamport.getInstance().receiveAction(timestamp);
		} catch (Throwable t) {
			Logger.log(t);
		}
	}
	
	/**
	 * Writes a request to the client connection.
	 * @param request
	 * 		  The request.
	 * @throws Exception
	 */
	public synchronized void writeRequest(IRequest request) throws Exception {
		request.addHeader("lamport", Long.toString(Lamport.getInstance().getValue()));
		writer.writeRequest(request);
	}
	
	/**
	 * Writes a response to the client connection.
	 * @param response
	 * 		  The response.
	 * @throws Exception
	 */
	public synchronized void writeResponse(IResponse response) throws Exception {
		response.addHeader("lamport", Long.toString(Lamport.getInstance().getValue()));
		writer.writeResponse(response);
	}
}