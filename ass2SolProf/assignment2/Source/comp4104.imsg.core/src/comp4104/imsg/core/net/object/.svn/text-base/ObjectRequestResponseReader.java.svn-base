package comp4104.imsg.core.net.object;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import comp4104.imsg.core.net.IRequestResponse;
import comp4104.imsg.core.net.IRequestResponseReader;
import comp4104.imsg.core.net.Request;
import comp4104.imsg.core.net.Response;

/**
 * A concrete implementation of <code>IRequestResponseReader</code> for
 * reading object input streams.
 */
public class ObjectRequestResponseReader implements IRequestResponseReader
{
	private ObjectInputStream inputStream;
	
	private ObjectRequestResponseReader(ObjectInputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.core.net.IRequestResponseReader#readRequestResponse()
	 */
	public IRequestResponse readRequestResponse() throws IOException, ClassNotFoundException {
		Object obj = inputStream.readObject();
		if (obj != null && (obj instanceof Request || obj instanceof Response)) {
			return (IRequestResponse)obj;
		}
		
		return null;
	}
	
	/**
	 * Creates and returns a new <code>ObjectRequestResponseReader</code>.
	 * @param socket
	 * 		  The socket associated with the reader.
	 * @return An <code>ObjectRequestResponseReader</code>.
	 * @throws IOException
	 */
	public static ObjectRequestResponseReader createReader(Socket socket) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
		ObjectInputStream ois = new ObjectInputStream(bis);
		return new ObjectRequestResponseReader(ois);
	}
}