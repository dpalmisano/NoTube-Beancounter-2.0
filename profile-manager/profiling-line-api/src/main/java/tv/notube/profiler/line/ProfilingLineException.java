package tv.notube.profiler.line;

/**
 * Raised if something goes wrong during the run of a
 * {@link ProfilingLine}.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ProfilingLineException extends Exception {

    public ProfilingLineException(String message, Exception e) {
        super(message, e);
    }
}
