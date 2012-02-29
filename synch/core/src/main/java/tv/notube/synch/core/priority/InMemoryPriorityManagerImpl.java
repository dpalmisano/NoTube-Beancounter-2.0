package tv.notube.synch.core.priority;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class InMemoryPriorityManagerImpl implements PriorityManager {

    private Map<String, UUID> tokens = new HashMap<String, UUID>();

    private Queue<UUID> queue;

    public InMemoryPriorityManagerImpl(int size) {
        queue = new ArrayBlockingQueue<UUID>(size);
    }

    public synchronized UUID askToken(String process) throws PriorityManagerException {
        if(tokens.containsKey(process)) {
            return tokens.get(process);
        } else {
            UUID token = UUID.randomUUID();
            tokens.put(process, token);
            queue.add(token);
            return token;
        }
    }

    public void revokeToken(String process) throws PriorityManagerException {
        UUID token = tokens.remove(process);
        if(queue.contains(token)) {
            queue.remove(token);
        }
    }

    public synchronized UUID next() throws PriorityManagerException {
        return queue.peek();
    }
}
