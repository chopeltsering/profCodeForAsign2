package comp4104.imsg.core.net;

import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

/**
 * A concrete implementation of a <code>IResponse</code>
 */
public class Response implements IResponse
{
	private static final long serialVersionUID = 2106864160710014822L;
	
	private transient Socket 		socket;
	private int						status;
	private HashMap<String,String> 	headers;
	private byte[] 					body;
	
	/**
	 * Creates a new <code>Response</code> instance.
	 */
	public Response() {
		this.status = 0;
		this.headers = new HashMap<String,String>();
		this.socket = null;
		this.body = null;				
	}
	
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.core.net.IRequestResponse#getSocket()
	 */
	public Socket getSocket() {
		return socket;
	}
	
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.core.net.IRequestResponse#setSocket(java.net.Socket)
	 */
	public void setSocket(Socket s) {
		this.socket = s;
	}
	
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.core.net.IRequestResponse#getBody()
	 */
	public byte[] getBody() {
		return body;
	}
	
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.core.net.IRequestResponse#setBody(byte[])
	 */
	public void setBody(byte[] body) {
		this.body = body;
	}
	
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.core.net.IResponse#getStatus()
	 */
	public int getStatus() {
		return status;
	}
	
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.core.net.IResponse#setStatus(int)
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.core.net.IRequestResponse#addHeader(java.lang.String, java.lang.String)
	 */
	public void addHeader(String key, String value) {
		headers.put(key, value);
	}
	
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.core.net.IRequestResponse#removeHeader(java.lang.String)
	 */
	public void removeHeader(String key) {
		headers.remove(key);
	}
	
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.core.net.IRequestResponse#getHeader(java.lang.String)
	 */
	public String getHeader(String key) {
		return headers.get(key);
	}
	
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.core.net.IRequestResponse#headerKeys()
	 */
	public Set<String> headerKeys() {
		return headers.keySet();
	}
}