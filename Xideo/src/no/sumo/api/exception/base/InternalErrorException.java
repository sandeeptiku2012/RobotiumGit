package no.sumo.api.exception.base;

/**
 * This exception is thrown when something went wrong that shouldn't go wrong. For example if there is a missing algorithm or encoding, or 
 * other issues that the api-user should not have to worry about. If an InternalErrorException is thrown, it generally means we have some
 * debugging to do.
 * @author <a href="mailto:laf@tv2.no">Laila Frotjold </a>
 * @version $Revision: 1 $
 */
public class InternalErrorException extends RuntimeException {

	private static final long serialVersionUID = -1313696929071546478L;

	public InternalErrorException (String message)
	{
		super(message);
	}
	
	public InternalErrorException (Throwable cause)
	{
		super(cause);
	}
	
	public InternalErrorException(String message, Throwable cause) {
		super(message, cause);
	}
}
