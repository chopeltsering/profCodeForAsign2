package comp4104.imsg.core.net.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;

import comp4104.imsg.core.net.IRequestResponse;
import comp4104.imsg.core.net.IRequestResponseReader;

/**
 * A concrete implementation of a request / response reader for
 * reading http requests.
 * 
 */
public class HttpRequestResponseReader implements IRequestResponseReader
{
	private HttpInputStream inputStream;
	
	private HttpRequestResponseReader(HttpInputStream inputStream)
	{
		this.inputStream = inputStream;
	}
	
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.core.net.IRequestResponseReader#readRequestResponse()
	 */
	public IRequestResponse readRequestResponse() throws IOException
	{
		return inputStream.readRequestResponse();
	}
	
	/**
	 * Creates and returns a new <code>HttpRequestResponseReader</code>.
	 * @param socket
	 * 		  The socket associated with the reader.
	 * @return An <code>HttpRequestResponseReader</code>.
	 * @throws IOException
	 */
	public static HttpRequestResponseReader createReader(Socket socket) throws IOException
	{
		BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
		HttpInputStream his = new HttpInputStream(bis);
		return new HttpRequestResponseReader(his);
	}
}
