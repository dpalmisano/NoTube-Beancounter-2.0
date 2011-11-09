package tv.notube.profiler.line;

/**
 * Raised if something goes wrong within the execution
 * of a {@link ProfilingLineItem}.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ProfilingLineItemException extends Exception {
	
	public ProfilingLineItemException(String message, Exception e) {
		super(message, e);
	}
	
}
