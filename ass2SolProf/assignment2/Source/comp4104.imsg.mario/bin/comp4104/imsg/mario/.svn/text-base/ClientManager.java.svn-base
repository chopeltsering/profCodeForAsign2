package comp4104.imsg.mario;

import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import comp4104.imsg.core.Commands;
import comp4104.imsg.core.Lamport;
import comp4104.imsg.core.logging.Logger;
import comp4104.imsg.core.model.Message;
import comp4104.imsg.core.net.IRequest;
import comp4104.imsg.core.net.Request;

/**
 * Client manager encapsulates and manages client communication
 * and IM logic.
 */
public class ClientManager
{	
	private static final String SERVER_NAME = "Server";
	
	private static ClientManager 				instance;
	
	private FileWriter							logWriter;
	private ConcurrentHashMap<String, String>	usernamesMap;
	private ConcurrentHashMap<Socket, Client> 	socketClientMap;
	private ConcurrentHashMap<String, Client> 	usernameClientMap;
	private ConcurrentHashMap<Client, String>	clientUsernameMap;
	
	private ClientManager() {
		this.usernamesMap = new ConcurrentHashMap<String, String>();
		this.socketClientMap = new ConcurrentHashMap<Socket, Client>();
		this.usernameClientMap = new ConcurrentHashMap<String, Client>();
		this.clientUsernameMap = new ConcurrentHashMap<Client, String>();
	}
	
	/**
	 * Gets the singleton <code>ClientManager</code>
	 * @return The singleton <code>ClientManager</code>.
	 */
	public static ClientManager getInstance() {
		if (instance == null) {
			instance = new ClientManager();
		}
		
		return instance;
	}
	
	/**
	 * Gets a client mapped to a specified socket connection.
	 * @param s
	 * 		  The socket connection.
	 * @return A <code>Client</code>.
	 */
	public Client getClient(Socket s) {
		return socketClientMap.get(s);
	}
	
	/**
	 * Gets a client mapped to a specified username.
	 * @param username
	 * 		  The username string.
	 * @return The <code>Client</code>.
	 */
	public Client getClient(String username) {
		return usernameClientMap.get(username);
	}
	
	/**
	 * Checks if a user is logged in with username.
	 * @param username
	 * 		  The username string.
	 * @return <code>true</code> if logged in, <code>false</code> otherwise.
	 */
	public boolean isLoggedIn(String username) {
		return username != null &&
			   usernameClientMap.containsKey(username);
	}
	
	/**
	 * Checks if a <code>Client</code> is logged in.
	 * @param client
	 * 		  The client.
	 * @return <code>true</code> if logged in, <code>false</code> otherwise.
	 */
	public boolean isLoggedIn(Client client) {
		return usernameClientMap.containsValue(client);
	}
	
	/**
	 * Registers a client.
	 * @param client
	 * 		  The client to register.
	 * @param s
	 * 		  The socket connection.
	 * @return <code>true</code> on success.
	 */
	public boolean registerClient(Client client, Socket s) {
		if (!socketClientMap.contains(s)) {
			socketClientMap.put(s, client);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Unregisters a client.
	 * @param client
	 * 		  The client to unregister.
	 * @return <code>true</code> on success.
	 */
	public boolean unregisterClient(Client client) {
		boolean unregistered = false;
		if (socketClientMap.containsKey(client.getSocket())) {
			socketClientMap.remove(client.getSocket());
			unregistered = true;
		}
		
		if (clientUsernameMap.containsKey(client)) {
			String username = clientUsernameMap.get(client);
			clientUsernameMap.remove(client);
			usernameClientMap.remove(username);
			usernamesMap.remove(username.toLowerCase());
			sendLogoutMessage(username);
			unregistered = true;
		}
		
		return unregistered;
	}
	
	/**
	 * Logs a client into the system.
	 * @param client
	 * 		  The client to login.
	 * @param username
	 * 		  The username to map to the client.
	 * @return <code>true</code> on success.
	 */
	public boolean login(Client client, String username) {
		username = username != null ? username : "";
		if (!usernamesMap.containsKey(username.toLowerCase()) && 
			!SERVER_NAME.equalsIgnoreCase(username)) {
			usernamesMap.put(username.toLowerCase(), username);
			usernameClientMap.put(username, client);
			clientUsernameMap.put(client, username);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Broadcasts a login message to all users in the chat.
	 * @param username
	 * 		  The username string.
	 */
	public void sendLoginMessage(String username) {
		String body = String.format("%s logged in.", username);
		Message message = new Message(SERVER_NAME, body);
		for (String recipient : clientUsernameMap.values()) {
			message.addRecipient(recipient);
		}
		
		sendMessage(message);
	}
	
	/**
	 * Broadcasts a logout message to all users in the chat.
	 * @param username
	 * 		  The username string.
	 */
	public void sendLogoutMessage(String username) {
		String body = String.format("%s logged out.", username);
		Message message = new Message(SERVER_NAME, body);
		for (String recipient : clientUsernameMap.values()) {
			message.addRecipient(recipient);
		}
		
		sendMessage(message);
		sendUpdatedClientList();
	}
	
	/**
	 * Sends an updated client list.
	 */
	public void sendUpdatedClientList() {
		String users = "";
		for (String user : clientUsernameMap.values()) {
			users += "," + user;
		}
		users = users.length() > 0 ? users.substring(1) : "";
		
		IRequest request = new Request();
		request.setBody(users.getBytes());
		request.setCommand(Commands.USERS);
		
		for (Client client : usernameClientMap.values()) {
			try {
				client.writeRequest(request);
			} catch (Exception e) {
				Logger.log(e);
			}
		}
	}
	
	/**
	 * Sends a message.
	 * @param message
	 * 		  The message to send.
	 */
	public void sendMessage(Message message) {
		Lamport.getInstance().tick();
		Request request = new Request();
		request.setCommand(Commands.MESSAGE);
		request.setBody(message.getBytes());
		
		// send the message to each recipient
		for (String recipient : message.getRecipients()) {
			if (usernameClientMap.containsKey(recipient)) {
				try {
					Client client = usernameClientMap.get(recipient);
					client.writeRequest(request);
				} catch (Exception e) {
					Logger.log(e);
				}
			}
		}
		
		log(message);
	}
	
	/**
	 * Logs a message.
	 * @param message
	 * 		  The message to log.
	 */
	public void log(Message message) {
		try {
			String body = message.getBody();
			String sender = message.getSender();
			long timestamp = System.currentTimeMillis();
			long lamport = Lamport.getInstance().getValue();
			String entry = String.format("%d %5d %s: %s\n", timestamp, lamport, sender, body);
			logWriter.write(entry);
			logWriter.flush();
		} catch (IOException ioe) {
			Logger.log(ioe);
		}
	}
	
	/**
	 * Sets the log file to use.
	 * @param file
	 * 		  The log file.
	 * @throws IOException
	 */
	public void setLogFile(String file) throws IOException {
		if (logWriter != null) {
			logWriter.flush();
			logWriter.close();
		}
		
		logWriter = new FileWriter(file);
	}
}