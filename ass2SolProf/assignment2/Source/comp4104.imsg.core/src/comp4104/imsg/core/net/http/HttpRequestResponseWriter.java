package comp4104.imsg.core.net.http;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import comp4104.imsg.core.net.IRequest;
import comp4104.imsg.core.net.IRequestResponseWriter;
import comp4104.imsg.core.net.IResponse;

/**
 * A concrete implementation of <code>IRequestResponseWriter</code> for
 * writing Http output.
 */
public class HttpRequestResponseWriter implements IRequestResponseWriter
{
	private HttpOutputStream outputStream;
	
	private HttpRequestResponseWriter(HttpOutputStream stream)
	{
		this.outputStream = stream;
	}
	
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.core.net.IRequestResponseWriter#writeRequest(comp4104.imsg.core.net.IRequest)
	 */
	public void writeRequest(IRequest request) throws IOException {
		outputStream.write(request);
		outputStream.flush();
	}

	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.core.net.IRequestResponseWriter#writeResponse(comp4104.imsg.core.net.IResponse)
	 */
	public void writeResponse(IResponse response) throws IOException {
		outputStream.write(response);
		outputStream.flush();
	}
	
	/**
	 * Creates and returns a new <code>HttpRequestResponseWriter</code>.
	 * @param socket
	 * 		  The socket associated with the writer.
	 * @return An <code>HttpRequestResponseWriter</code>.
	 * @throws IOException
	 */
	public static IRequestResponseWriter createWriter(Socket socket) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
		HttpOutputStream hos = new HttpOutputStream(bos);
		return new HttpRequestResponseWriter(hos);
	}
}
