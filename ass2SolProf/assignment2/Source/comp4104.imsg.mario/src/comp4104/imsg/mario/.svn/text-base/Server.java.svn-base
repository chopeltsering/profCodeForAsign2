package comp4104.imsg.mario;

import java.io.FileReader;
import java.io.IOException;
import java.io.InvalidClassException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

import comp4104.imsg.mario.object.ObjectAcceptor;
import comp4104.imsg.mario.services.DefaultHandler;
import comp4104.imsg.mario.services.IServiceHandler;

/**
 * The messaging server.
 */
public class Server
{
	private static final String SERVER_CONFIG_FILE		= "server.config";
	private static final String HANDLER_CONFIG_KEY		= "handlerConfig";
	private static final String CHAT_LOG_KEY			= "chatLog";
	private static final String CHAT_LOG_FILE			= "chat.log";
	private static final String ACCEPTOR_KEY			= "acceptor";
	private static final String	SERVER_PORT_KEY 		= "port";
	private static final String SERVER_PORT				= "8080";
	
	private String			configFile;
	private ServerSocket	serverSocket;
	
	private boolean			started;
	private ReentrantLock	startLock;
	
	Dispatcher				dispatcher;
	IAcceptor				acceptor;
	
	/**
	 * Creates a new instance of <code>Server</code>
	 */
	public Server(String configFile)
	{
		this.started = false;
		this.configFile = configFile;
		this.startLock = new ReentrantLock(); 
		this.dispatcher = null;
		this.acceptor = null;
	}
	
	/**
	 * Starts the server.
	 */
	public void start() throws Exception
	{
		// acquire the start/stop lock
		startLock.lock();
		
		try {
			if (!isStarted()) {
				// start the server
				setStarted(true);
				
				// initialize the server
				initialize();
			}
		}
		finally {
			// release the start/stop lock
			startLock.unlock();
		}
		
		// begin run-loop
		run();
	}
	
	/**
	 * Stops the server.
	 */
	public void stop()
	{
		// acquire the start/stop lock
		startLock.lock();
		
		try {
			if (isStarted()) {
				try {
					// stop processing requests
					setStarted(false);
					serverSocket.close();
					acceptor.complete();
				} catch (IOException ioe) {
				}
			}
		} finally {
			// release the start/stop lock
			startLock.unlock();
		}
	}
	
	/**
	 * Sets the started server flag.
	 * @param started The value for the flag
	 * 				  <code>true</code> if started,
	 * 				  <code>false</code> otherwise.
	 */
	protected synchronized void setStarted(boolean started)
	{
		this.started = started;
	}
	
	/**
	 * Checks if the server is started.
	 * 
	 * @return <code>true</code> if the server is started,
	 * 		   <code>false<code> otherwise.
	 */
	protected synchronized boolean isStarted()
	{
		return started;
	}
	
	/**
	 * Initializes the server.
	 */
	private void initialize() throws Exception
	{
		Properties props = new Properties();
		props.load(new FileReader(configFile));
		
		// initialize the executor service
		serverSocket = new ServerSocket(getServerPort(props));
		dispatcher = new Dispatcher();
		acceptor = createAcceptor(props);
		acceptor.setDispatcher(dispatcher);
		
		// load/register handlers
		loadHandlers(props);
		
		// set up the chat log
		String logFile = props.getProperty(CHAT_LOG_KEY, CHAT_LOG_FILE);
		ClientManager.getInstance().setLogFile(logFile);
	}
	
	private void loadHandlers(Properties props) throws Exception
	{
		dispatcher.registerHandler(Dispatcher.DEFAULT_HANDLER_KEY, new DefaultHandler());
		
		String handlerConfigFile = props.getProperty(HANDLER_CONFIG_KEY, "handler.config");
		Properties handlerProps = new Properties();
		handlerProps.load(new FileReader(handlerConfigFile));
		
		for (Object key : handlerProps.keySet()) {
			String keyString = (String)key;
			IServiceHandler handler = loadHandler(handlerProps.getProperty(keyString));
			dispatcher.registerHandler(keyString.toLowerCase(), handler);
		}
	}
	
	private IServiceHandler loadHandler(String className)
	 throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvalidClassException
	{
		Class<?> handlerClass = Class.forName(className);
		if (handlerClass != null && IServiceHandler.class.isAssignableFrom(handlerClass)) {
			return (IServiceHandler)handlerClass.newInstance();
		}
		
		throw new InvalidClassException(className);
	}
	
	private IAcceptor createAcceptor(Properties props) 
	 throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvalidClassException
	{
		String className = props.getProperty(ACCEPTOR_KEY, ObjectAcceptor.class.getName());
		Class<?> acceptorClass = Class.forName(className);
		if (acceptorClass != null && IAcceptor.class.isAssignableFrom(acceptorClass)) {
			return (IAcceptor)acceptorClass.newInstance();
		}
		
		throw new InvalidClassException(className);
	}
	
	private int getServerPort(Properties props)
	{
		return Integer.parseInt(props.getProperty(SERVER_PORT_KEY, SERVER_PORT));
	}
	
	/**
	 * The server run loop.
	 */
	private void run()
	{
		while (isStarted()) {
			try {
				Socket con = serverSocket.accept();
				acceptor.accept(con);
			} catch (IOException ioe) {
			}
		}
	}
	
	public static void main(String[] args) {
		if (args.length <= 1) {
			String configFile = args.length > 0 ? args[0] : SERVER_CONFIG_FILE;
			Server server = new Server(configFile);
			try {
				server.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}