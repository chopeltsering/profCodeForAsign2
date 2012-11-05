package comp4104.imsg.core.net;

/**
 * A model of a response.
 */
public interface IResponse extends IRequestResponse
{
	public static final int OK 					= 200;
	public static final int BAD_REQUEST			= 400;
	public static final int NOT_AUTHORIZED 		= 401;
	public static final int NOT_FOUND			= 404;
	public static final int SERVER_ERROR 		= 500;
	
	/**
	 * Gets the response status.
	 * @return The response status.
	 */
	public int getStatus();
	
	/**
	 * Sets the response status.
	 * @param status
	 * 		  The response status.
	 */
	public void setStatus(int status);	
}