package tv.notube.synch.core.priority;

import java.util.UUID;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface PriorityManager {

    public UUID askToken(String process) throws PriorityManagerException;

    public void revokeToken(String process) throws PriorityManagerException;

    public UUID next() throws PriorityManagerException;

}
