package comp4104.imsg.mario;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * An abstract base implementation of the acceptor pattern.
 */
public abstract class AbstractAcceptor implements IAcceptor
{
	protected Dispatcher			dispatcher;
	protected ExecutorService		executorService;
	
	/**
	 * Creates a new <code>AbstractAcceptor</code> instance.
	 */
	public AbstractAcceptor() {
		this.executorService = Executors.newCachedThreadPool();
	}
	
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.mario.IAcceptor#setDispatcher(comp4104.imsg.mario.Dispatcher)
	 */
	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}
	
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.mario.IAcceptor#complete()
	 */
	public void complete() {
		executorService.shutdown();		
		while (!executorService.isTerminated()) {
			try {
				executorService.awaitTermination(0, TimeUnit.MILLISECONDS);
			} catch (InterruptedException ie) {}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see comp4104.imsg.mario.IAcceptor#accept(java.net.Socket)
	 */
	public abstract void accept(Socket s);
}
