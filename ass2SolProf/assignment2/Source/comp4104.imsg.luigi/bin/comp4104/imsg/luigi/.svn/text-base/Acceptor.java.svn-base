package comp4104.imsg.luigi;
import java.io.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.SwingUtilities;

import comp4104.imsg.core.Lamport;
import comp4104.imsg.core.model.Message;
import comp4104.imsg.core.net.IRequest;
import comp4104.imsg.core.net.IRequestResponse;
import comp4104.imsg.core.net.IResponse;
import comp4104.imsg.core.net.Request;
import comp4104.imsg.core.net.Response;
import comp4104.imsg.core.net.http.HttpInputStream;
import comp4104.imsg.luigi.SocketProxy.ConnectionType;

public class Acceptor implements Runnable{
        private SocketProxy socket;
        private LinkedBlockingQueue<Response> messageOutput;
        private HttpInputStream httpReader;
        private ObjectInputStream objectReader;
        
        private AtomicBoolean running;
        private GUI gui;
        /**
         * Listen for requests and responses from the server and pass them to the gui if the user should see them
         * @param socket
         * @param messageOutput
         * @param running
         * @param gui
         */
        public Acceptor(SocketProxy socket, LinkedBlockingQueue<Response> messageOutput, AtomicBoolean running, GUI gui){
                this.socket = socket;
                this.messageOutput = messageOutput;
                this.running = running;
                this.gui = gui;
        }
        /**
         * Return the next request or response, or null if a problem occurs
         * @param in
         * @return
         * @throws IOException
         */
        public IRequestResponse getMessageFromStream(InputStream in) throws IOException{
                if(socket.getConnectionType() == ConnectionType.OBJECT){
                	if (objectReader == null) {
                		try { objectReader = new ObjectInputStream(in); } 
                		catch(Throwable t) { throw new IOException(t); }
                	}
                }else if (socket.getConnectionType() == ConnectionType.HTTP){
                	if (httpReader == null) {
                		try { httpReader = new HttpInputStream(new BufferedInputStream(in)); } catch(Throwable t) {}
                	}
                }
                while(running.get()){
                        try{
                        		IRequestResponse r = null;
                        		if(socket.getConnectionType() == ConnectionType.OBJECT){
                        			r = (IRequestResponse)objectReader.readObject();
                        		} else if(socket.getConnectionType() == ConnectionType.HTTP){
                        			r = (IRequestResponse)httpReader.readRequestResponse();
                        		} 
                        		
                        		return r;
                                
                        }  catch(java.lang.ClassNotFoundException cnf){
                                

                        } catch(java.io.StreamCorruptedException sce){
                                System.out.println("switching protocols");
                                throw new IOException("");
                        }
                }
                return null;
        }
        
        
        public void run(){
                IRequestResponse r = null;
                while(running.get()){
                        try {
							r = getMessageFromStream(socket.getInputStream());
						} catch (IOException io){
            				System.err.println("Acceptor Cannot connect to server. Retrying");
            				try {
								Thread.sleep(100);
								socket.close();
								return;
							} catch (InterruptedException e) {
								
								e.printStackTrace();
								return;
							}	
            			}
                        if(r != null){
                        		//messageOutput.offer(m);
                        		try{
                        			if(r instanceof Response){
                        				
                        				Response resp = (Response)r;
                        				String lam = resp.getHeader("lamport");
                        				if(lam != null)
                        					Lamport.getInstance().receiveAction(Long.parseLong(lam));
                        				if(resp.getStatus() == IResponse.OK){
                        					//dont need to do much when things go right
                        				}else if (resp.getStatus() == IResponse.NOT_FOUND){
                        					gui.append("Not Found");
                        				} else if (resp.getStatus() == IResponse.BAD_REQUEST){
                        					String err = new String(resp.getBody());
                        					gui.append(err);
                        				} else if (resp.getStatus() == IResponse.NOT_AUTHORIZED){
                        					String err = new String(resp.getBody());
                        					gui.append(err);
                        				} else if (resp.getStatus() == IResponse.SERVER_ERROR){
                        					String err = new String(resp.getBody());
                        					gui.append(err);
                        				}
                        				
                        				
                        				//gui.append(m.getSender() + ": " +  m.getBody() + "\n");
                        			} else if(r instanceof Request){
                        				Request req = (Request)r;
                        				//gui.append(Message.createMessage(resp.getBody()).getBody() + "\n");
                        				String lam = req.getHeader("lamport");
                        				if(lam != null)
                        					Lamport.getInstance().receiveAction(Long.parseLong(lam));
                        				final String str = new String(req.getBody());
                        				
                        				if(req.getCommand().equals(comp4104.imsg.core.Commands.USERS)){
                        					SwingUtilities.invokeLater(new Runnable(){
                        						public void run(){
                        							gui.updateUserList(str);
                        						}
                        					});
                        				}else if(req.getCommand().equals(comp4104.imsg.core.Commands.MESSAGE)){
                        					try{
                        						Message m = Message.createMessage(req.getBody());
                        						gui.append(m.getSender() + ": " + m.getBody());
                        					} catch(IllegalArgumentException iae){
                        						iae.printStackTrace();
                        					}
                        					
                        				}
                        			}
                        		} catch (NullPointerException npe){
                    				System.err.println("Cannot connect to server. Retrying");
                    				try {
										Thread.sleep(100);
										socket.close();
										return;
									} catch (InterruptedException e) {
										
										e.printStackTrace();
										return;
									}
                    				
                    			}
                        		catch (Exception e){
                        			e.printStackTrace();
                        			//System.exit(1);
                        		}
                        }else
                        	return;
                }
                
        }
}
