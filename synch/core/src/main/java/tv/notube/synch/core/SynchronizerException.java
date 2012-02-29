package tv.notube.synch.core;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SynchronizerException extends Exception {

    public SynchronizerException(String msg) {
        super(msg);
    }

    public SynchronizerException(String msg, Exception e) {
        super(msg, e);
    }
}
