package comp4104.imsg.core.model;
import java.io.*;
import java.util.*;

/**
 * A common model for an instant message.
 */
public class Message implements Serializable
{
	private static final long serialVersionUID = 9158354690299818453L;
	
	private String sender;
	private String body;
	private ArrayList<String> recipients;
	
	/**
	 * Creates a new <code>Message</code> instance.
	 * @param sender
	 * 		  The sender of the message.
	 * @param body
	 * 		  The message body.
	 */
	public Message(String sender, String body) {
		this.recipients = new ArrayList<String>();
		this.sender = sender;
		this.body = body;
	}
	
	/**
	 * Adds a recipient to the list of recipients.
	 * @param recipient
	 * 		  The recipient to add.
	 */
	public void addRecipient(String recipient) {
		if (!recipients.contains(recipient)) {
			recipients.add(recipient);
		}
	}
	
	/**
	 * Gets the <code>List</code> of recipients.
	 * @return The recipients. 
	 */
	public List<String> getRecipients() {
		return Collections.unmodifiableList(recipients);
	}
	
	/**
	 * Clears the message&quot;s recipients.
	 */
	public void clearRecipients() {
		recipients.clear();
	}
	
	/**
	 * Gets the body of the message.
	 * @return The message body.
	 */
	public String getBody() {
		return body;
	}
	
	/**
	 * Gets the sender of the message.
	 * @return The name of the sender.
	 */
	public String getSender() {
		return sender;
	}
	
	/**
	 * Gets the message as a serialized byte array.
	 * @return The message as a serialized byte array.
	 */
	public byte[] getBytes() {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(this);
			oos.close();
			return bos.toByteArray();
		} catch (IOException ioe) {
			return null;
		}
	}
	
	/**
	 * Creates a message from a serialized byte representation.
	 * @param bytes 
	 * 		  The byte array representation of the message.
	 * @return A new instance of <code>Message</code>
	 * @throws Exception
	 */
	public static Message createMessage(byte[] bytes) throws Exception {
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(bis);
		Object msgObj = ois.readObject();
		if (msgObj instanceof Message) {
			return (Message)msgObj;
		}
		
		throw new IllegalArgumentException("bytes is not a Message.");
	}
}