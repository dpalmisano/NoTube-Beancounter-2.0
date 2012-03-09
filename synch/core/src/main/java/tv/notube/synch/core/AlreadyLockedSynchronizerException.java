package tv.notube.synch.core;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class AlreadyLockedSynchronizerException extends SynchronizerException {

    public AlreadyLockedSynchronizerException(String message) {
        super(message);
    }
}
