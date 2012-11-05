package comp4104.imsg.mario;

import java.net.Socket;

/**
 * An acceptor interface for adhering to acceptor/connector
 * design.
 */
public interface IAcceptor
{
	/**
	 * Accepts a new socket connection.
	 * @param s
	 * 		  A socket connection.
	 */
	public void accept(Socket s);
	
	/**
	 * Completes a connection.
	 */
	public void complete();
	
	/**
	 * Sets the <code>Dispatcher</code> to dispatch
	 * connection events to.
	 * @param dispatcher
	 * 		  The dispatcher
	 */
	public void setDispatcher(Dispatcher dispatcher);
}
