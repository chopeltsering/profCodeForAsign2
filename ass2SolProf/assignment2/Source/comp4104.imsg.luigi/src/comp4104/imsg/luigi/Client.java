package comp4104.imsg.luigi;
import java.net.*;
import java.io.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import comp4104.imsg.core.model.Message;
import comp4104.imsg.core.net.Request;
import comp4104.imsg.core.net.Response;

import java.util.Properties;

public class Client{
        private InetAddress host;
        private Socket s;
        //private Message m;
        private LinkedBlockingQueue<Request> outMessages = new LinkedBlockingQueue<Request>();
        private LinkedBlockingQueue<Response> inMessages = new LinkedBlockingQueue<Response>();
        private AtomicBoolean running = new AtomicBoolean(true);
        private Properties props;
        private String name;
        private int port;
        private Request login;
        /**
         * Create a new client
         */
        public Client(){
                s = new Socket();
                
                try{
                        host = InetAddress.getLocalHost();
                }
                catch (UnknownHostException e) {
                        System.err.println("Don't know about host: " + host);
                        System.exit(1);
                }
        }
        /**
         * load configuation properties from a file
         * @param file
         */
        public void loadPropertiesFile(String file){
        	props = new Properties();
        	try {
				props.load(new FileReader(file));
				name = props.getProperty("name");
				host = InetAddress.getByName(props.getProperty("host"));
				port = Integer.parseInt(props.getProperty("port"));
			} catch (FileNotFoundException e) {
				System.out.println("File does not exist. Exitting");
				System.exit(1);
			} catch (IOException e) {
				System.out.println("File cannot be read. Exitting");
				System.exit(1);
			}
        }
        /**
         * load a set of default configuration properties
         */
        public void defaultProperties(){
        	name = "Bryan";
        	try {
				host = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				System.out.println("Java exceptions suck");
			}
        	port = 8080;
        }
        /**
         * Send the inital login request, and handle changes in protocol
         */
        public void send(){
                        SocketProxy s = new SocketProxy(host,port);
                        login();
                        GUI test = new GUI("Chat client",outMessages, name);
                    	Thread connector = new Thread(new Connector(s, outMessages, running, login));
                        Thread acceptor = new Thread(new Acceptor(s, inMessages, running, test));
                        connector.start();
                        acceptor.start();
                    	test.setVisible(true);
                    	while(true){
                    		try{
                    			acceptor.join();
                    			running.set(false);
                    			connector.interrupt();
                    			connector.join();
                    			s.switchConnectionType();
                    			try{
                    				s.reconnect();
                    			} catch(IOException ie){continue;}
                    			outMessages.clear();
                    			outMessages.offer(login);
                    			System.out.println("Restarting threads.");
                    			running.set(true);
                    			connector = new Thread(new Connector(s, outMessages, running, login));
                    			acceptor = new Thread(new Acceptor(s, inMessages, running, test));
                    			connector.start();
                    			acceptor.start();
                        		
                    		} catch(InterruptedException ie){}
                    	}
        }
        /**
         * put a login request into the send queue
         */
        private void login(){
        	//Message m = new Message(name, name);
        	Request r = new Request();
        	r.setBody(name.getBytes());
        	r.setCommand(comp4104.imsg.core.Commands.LOGIN);
        	login = r;
            outMessages.offer(r);
            	
        }
        /**
         * Load the user name, ip and port from a config file
         * @param args
         * @return
         */
        public String getConfigFile(String[] args){
        	for(int i = 0; i < args.length; i++){
                if(args[i].equals("-c") && i+1 < args.length){
                	System.out.println("here in -c area");    // code put by me
                        return args[i+1];
                }
        	}
        	return null;
        }
        /**
         * Parse the user name, ip and port from the command line arguments
         */
        public boolean getNameAndServer(String[] args){
        	name = null;
        	host = null;
        	port = -1;
        	for(int i = 0; i < args.length; i++){
                if(args[i].equals("-name") && i+1 < args.length){
                	name= args[i+1];
                }else if(args[i].equals("-ip") && i+1 < args.length){
                	try {
        				host = InetAddress.getByName(args[i+1]);
        			} catch (UnknownHostException e) {
        				System.out.println("Java exceptions suck");
        			}
                } else if(args[i].equals("-port") && i+1 < args.length){
                	port = Integer.parseInt(args[i+1]);
                }
                	
        	}
        	if(name == null || host == null || port == -1)
        		return true;
        	return false;
        }
        /**
         * Entry point
         * @param args
         */
        public static void main(String[] args){
        	for(String  c :args){
        		System.out.println(c +args.length); // code put by me
        	}
        	
                Client c = new Client();
                String str = c.getConfigFile(args); //get the config file if one is given on the command line 
                if(str != null)
                	c.loadPropertiesFile(str);
                else if(c.getNameAndServer(args)){
                	System.err.println("Usage: -c 'path to config file' | -name NAME -ip IPADDRESS -port PORTNUMBER");
                	System.exit(1);
                }
                c.send();
        }
}
