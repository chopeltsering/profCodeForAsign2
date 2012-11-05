package comp4104.imsg.luigi;
import java.io.IOException;
import java.net.*;
import java.io.*;

public class SocketProxy {
	private Socket socket;
	private InetAddress host;
	private int port;
	private ConnectionType connectionType = ConnectionType.OBJECT;
	
	public enum ConnectionType{OBJECT, HTTP};
	/**
	 * Create a socket proxy both the connector and acceptor can share
	 * @param host
	 * @param port
	 */
	public SocketProxy(InetAddress host, int port){
		this.host = host;
		this.port = port;
		try{
			socket = new Socket(host,port);
		}catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to: " + host);
            
		}
	}
	/**
	 * reconnect to the server if a connection
	 * @throws IOException
	 */
	public synchronized void reconnect() throws IOException{
		
		socket.close();
		socket = new Socket(host,port);
		
	}
	/**
	 * return the sockets output stream
	 * @return
	 */
	public synchronized OutputStream getOutputStream(){
		try{
			return socket.getOutputStream();
		} catch(IOException ie){}
		
		return null;
	}
	/**
	 * return the sockets input stream
	 * @return
	 */
	public synchronized InputStream getInputStream(){
		try{
			return socket.getInputStream();
		} catch(IOException ie){}
		return null;
	}
	
	/**
	 * close the socket
	 */
	public synchronized void close(){
		try {
			socket.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	
	}
	
	/**
	 * return the connection type currently being used
	 * @return
	 */
	public synchronized ConnectionType getConnectionType(){
		return connectionType;
	}
	/**
	 * switch the connection type being used
	 */
	public synchronized void switchConnectionType(){
		if(connectionType == ConnectionType.OBJECT)
			connectionType = ConnectionType.HTTP;
		else
			connectionType = ConnectionType.OBJECT;
	}
}
