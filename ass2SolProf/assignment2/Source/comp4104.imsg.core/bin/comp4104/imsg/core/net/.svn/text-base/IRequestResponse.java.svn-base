package comp4104.imsg.core.net;

import java.io.Serializable;
import java.net.Socket;
import java.util.Set;

/**
 * A base interface for requests and responses.
 */
public interface IRequestResponse extends Serializable
{
	/**
	 * Gets the socket.
	 * @return The socket.
	 */
	public Socket getSocket();
	
	/**
	 * Sets the socket.
	 * @param s
	 * 		  The socket.
	 */
	public void setSocket(Socket s);
	
	/**
	 * Adds a header.
	 * @param key
	 * 		  The header key.
	 * @param value
	 * 		  The header value.
	 */
	public void addHeader(String key, String value);
	
	/**
	 * Removes a header.
	 * @param key
	 * 		  The header key.
	 */
	public void removeHeader(String key);
	
	/**
	 * Gets a header.
	 * @param key
	 * 		  The header key.
	 * @return The header value.
	 */
	public String getHeader(String key);
	
	/**
	 * Gets a set representation of all header keys.
	 * @return A set of header keys.
	 */
	public Set<String> headerKeys();
	
	/**
	 * Gets the body.
	 * @return The body bytes.
	 */
	public byte[] getBody();
	
	/**
	 * Sets the body.
	 * @param body
	 * 		  The body bytes.
	 */
	public void setBody(byte[] body);
}