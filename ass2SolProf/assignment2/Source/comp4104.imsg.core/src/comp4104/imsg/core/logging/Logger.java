package comp4104.imsg.core.logging;

/**
 * An extremely simple logging facade.
 */
public class Logger
{
	private Logger() {
	}
	
	/**
	 * Logs a message.
	 * @param message
	 * 		  The message to log.
	 */
	public static void log(String message) {
		System.out.println(message);
	}
	
	/**
	 * Logs a <code>Throwable</code> object.
	 * @param e
	 * 		  The <code>Throwable</code> to log. 
	 */
	public static void log(Throwable e) {
		e.printStackTrace();
	}
}