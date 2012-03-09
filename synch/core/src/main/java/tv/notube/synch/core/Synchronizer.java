package tv.notube.synch.core;

import tv.notube.synch.model.Status;

import java.util.UUID;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface Synchronizer {

    public UUID getToken(String process) throws SynchronizerException;

    public void lock(String process, UUID token) throws SynchronizerException;

    public void release(String process, UUID token) throws SynchronizerException;

    public Status getStatus() throws SynchronizerException;

}
