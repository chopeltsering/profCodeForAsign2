package comp4104.imsg.core;

/**
 * Lamport clock.
 */
public class Lamport
{
	private long timestamp = 0;
	private static Lamport lamport = new Lamport();
	
	private Lamport(){}
	
	/**
	 * Gets the lamport clock
	 * @return The <code>Lamport</code> singleton
	 */
	public static Lamport getInstance(){
		return lamport;
	}
	
	/**
	 * Ticks the clock.
	 */
	public synchronized void tick(){
		timestamp++;
	}
	
	/**
	 * Updates the clock with a received time stamp action
	 * @param sentTimestamp
	 * 		  The time stamp that was received
	 */
	public synchronized void receiveAction(long sentTimestamp){
		timestamp = Math.max(timestamp, sentTimestamp);
	}
	
	/**
	 * Gets the clock value.
	 * @return The clock&quot;s value
	 */
	public synchronized long getValue(){
		return timestamp;
	}
}
