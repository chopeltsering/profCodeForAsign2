package comp4104.imsg.core.net.object;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import comp4104.imsg.core.net.IRequest;
import comp4104.imsg.core.net.IRequestResponseWriter;
import comp4104.imsg.core.net.IResponse;

/**
 * A concrete implementation of <code>IRequestResponseWriter</code> for
 * writing object stream output.
 */
public class ObjectRequestResponseWriter implements IRequestResponseWriter
{
	private ObjectOutputStream outputStream;
	
	private ObjectRequestResponseWriter(ObjectOutputStream stream)
	{
		this.outputStream = stream;
	}
	
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.core.net.IRequestResponseWriter#writeRequest(comp4104.imsg.core.net.IRequest)
	 */
	public void writeRequest(IRequest request) throws IOException {
		outputStream.writeObject(request);
		outputStream.flush();
	}
	
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.core.net.IRequestResponseWriter#writeResponse(comp4104.imsg.core.net.IResponse)
	 */
	public void writeResponse(IResponse response) throws IOException {
		outputStream.writeObject(response);
		outputStream.flush();
	}
	
	/**
	 * Creates and returns a new <code>ObjectRequestResponseWriter</code>.
	 * @param socket
	 * 		  The socket associated with the writer.
	 * @return An <code>ObjectRequestResponseWriter</code>.
	 * @throws IOException
	 */
	public static IRequestResponseWriter createWriter(Socket socket) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		return new ObjectRequestResponseWriter(oos);
	}
}
