package comp4104.imsg.mario.http;

import java.io.IOException;
import java.net.Socket;

import comp4104.imsg.core.logging.Logger;
import comp4104.imsg.core.net.IRequestResponseReader;
import comp4104.imsg.core.net.IRequestResponseWriter;
import comp4104.imsg.core.net.http.HttpRequestResponseReader;
import comp4104.imsg.core.net.http.HttpRequestResponseWriter;
import comp4104.imsg.mario.AbstractAcceptor;
import comp4104.imsg.mario.Client;

/**
 * A concrete implementation of an Http acceptor.
 */
public class HttpAcceptor extends AbstractAcceptor
{
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.mario.AbstractAcceptor#accept(java.net.Socket)
	 */
	public void accept(Socket s)
	{
		try {
			IRequestResponseReader reader = HttpRequestResponseReader.createReader(s);
			IRequestResponseWriter writer = HttpRequestResponseWriter.createWriter(s);
			Client client = new Client(s, dispatcher, writer, reader);
			executorService.execute(client);
			Logger.log("Connection established!");
		} catch (IOException e) {
			Logger.log("Error establishing connection");
			Logger.log(e);
		}
	}
}
