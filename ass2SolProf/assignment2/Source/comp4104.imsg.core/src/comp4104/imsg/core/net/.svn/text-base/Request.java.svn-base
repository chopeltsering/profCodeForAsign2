package comp4104.imsg.core.net;

import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

/**
 * Concrete implementation of a <code>IRequest</code>
 */
public class Request implements IRequest
{
	private static final long serialVersionUID = 3374770933897676366L;
	
	private transient Socket		socket;
	private String					command;
	private HashMap<String,String> 	headers;
	private byte[] 					body;
	
	/**
	 * Creates a new <code>Request</code> instance.
	 */
	public Request() {
		this.headers = new HashMap<String,String>();
		this.command = null;
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
	 * @see comp4104.imsg.core.net.IRequest#getCommand()
	 */
	public String getCommand() {
		return command;
	}
	
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.core.net.IRequest#setCommand(java.lang.String)
	 */
	public void setCommand(String command) {
		this.command = command;
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