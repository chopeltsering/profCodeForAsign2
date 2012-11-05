package comp4104.imsg.luigi;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import comp4104.imsg.core.Lamport;
import comp4104.imsg.core.model.Message;
import comp4104.imsg.core.net.Request;
import comp4104.imsg.core.net.http.HttpInputStream;
import comp4104.imsg.core.net.http.HttpOutputStream;
import comp4104.imsg.luigi.SocketProxy.ConnectionType;

public class Connector implements Runnable{
        private SocketProxy socket;
        private PrintWriter httpWriter;
        private ObjectOutputStream objectWriter;
        private AtomicBoolean running;
        private Request login;

        private LinkedBlockingQueue<Request> messages;
        /**
         * A request sending Thread Runnable
         * @param socket
         * @param messages
         * @param running
         * @param login
         */
        public Connector(SocketProxy socket, LinkedBlockingQueue<Request> messages, AtomicBoolean running, Request login){
                this.socket = socket;
                this.messages = messages;
                this.running = running;
                this.login = login;
        }

        /**
         * send the Requests when they're put in the message queue
         */
        public void run(){
                
        		long timeout = 0;
        			try{
        				ObjectOutputStream sender =null;
        				HttpOutputStream httpSender=null;
        				if(socket.getConnectionType() == ConnectionType.OBJECT){
        					sender = new ObjectOutputStream(socket.getOutputStream());
        				} else{
        					httpSender = new HttpOutputStream(socket.getOutputStream());
        				}
        				Message m = null;
        				while(running.get()) {
        					Request r = messages.take(); 
        					if(r == null)
        						continue;
        					Lamport.getInstance().tick();
        					if(socket.getConnectionType() == ConnectionType.OBJECT){
        						
        						r.addHeader("lamport", Long.toString(Lamport.getInstance().getValue()));
        						
        						sender.writeObject(r);
        						
        					}else if(socket.getConnectionType() == ConnectionType.HTTP){
        						
        						r.addHeader("lamport", Long.toString(Lamport.getInstance().getValue()));
        						httpSender.write(r);
        						
        					}
        				}
        			}catch (InterruptedException ie){}
        			catch (IOException ie){
        				System.out.println("1");
        				System.err.println("Connector Cannot connect to server. Retrying");
        				try{
        					Thread.sleep(100);
        					socket.close();
        					//System.err.println("Server seems to be dead. Someone should check on that");
                			return;
        					
        				}catch (InterruptedException ie2){
        					return;
        				}
        			} catch (NullPointerException npe){
        				System.err.println("Connector Cannot connect to server. Retrying");
        				System.out.println("2");
        				try{
        					Thread.sleep(100);
        					socket.close();
        					return;
        					
        				}catch (InterruptedException ie2){
        					return;
        				}
        			}
        		
        }
}
