package comp4104.imsg.luigi;

import javax.swing.*;

import comp4104.imsg.core.Lamport;
import comp4104.imsg.core.model.Message;
import comp4104.imsg.core.net.Request;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;


public class GUI extends JFrame {
    private JTextArea transcript;
    private JButton sendButton;
    private JList userList;
    private JTextField chatBox;
    private Vector<String> users;
    private String userName = "Bryan";
    private LinkedBlockingQueue<Request> sendQueue;
    private JLabel userLabel;
    /**
     * Set up the gui to allow the user to chat
     * @param title
     * @param sendQueue
     * @param userName
     */
    public GUI(String title, LinkedBlockingQueue<Request> sendQueue, String userName) {
    		super(title);
    		this.sendQueue = sendQueue;
    		this.userName = userName;
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            userLabel = new JLabel("User: " + userName);
            panel.add(userLabel);
            users = new Vector<String>();
            users.add("You are alone");
            userList = new JList(users);
            panel.add(userList);
            transcript = new JTextArea(10, 50);
            
            transcript.setEditable(false);

            JScrollPane aScrollPane = new JScrollPane(transcript);
            panel.add(aScrollPane);
            chatBox = new JTextField();
            JScrollPane anotherScrollPane = new JScrollPane(chatBox);
            panel.add(anotherScrollPane);
            panel.add(chatBox);
            
            sendButton = new JButton("Send");
            sendButton.addActionListener(new SendMessage());
            panel.add(sendButton);
            
            this.setContentPane(panel);
            
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.pack();
    }
    /**
     * Append the msg to the chat window
     * @param msg
     */
    public synchronized void append(String msg) {
    		
            transcript.append(msg + "\n");        
    }
    /**
     * Allow the Acceptor to update the list of users
     * @param names
     */
    public synchronized void updateUserList(String names){
    	String[] nameArray= names.split(",");
    	int selected = userList.getSelectedIndex();
    	users.clear();
    	for(String u : nameArray){
    		if(!u.equals(userName))
    			users.add(u);
    	}
    	userList.setListData(users);
    	//userList = new JList(users);
    	userList.setSelectedIndex(selected);
    	this.repaint();
    }
        	
    /**
     * Listener for the send button, to let the user sent a message
     * @author bryan
     *
     */
    private class SendMessage implements ActionListener
    {  public void actionPerformed(ActionEvent event)
       {  
    		try{
    			if(userList.getSelectedIndex() == -1){
    				throw new IllegalArgumentException("Must select a user to send a message to");
    			}
    		 
    		Message m = new Message(userName, chatBox.getText());
    		Request r = new Request();
    		Lamport.getInstance().tick();
    		m.addRecipient((String)userList.getSelectedValue());
    		System.out.println(m.getRecipients().get(0));
    		r.setCommand(comp4104.imsg.core.Commands.MESSAGE);
    		System.out.println(m.getSender()+ " " + m.getBody());
    		r.setBody(m.getBytes());
    		sendQueue.offer(r);
    		chatBox.setText("");
    		} catch(IllegalArgumentException ie){
				JOptionPane.showMessageDialog(null, ie.getMessage());
    		}
       }
    }
}
