package tv.notube.synch.service;

import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import tv.notube.synch.core.LoggableSynchronizer;
import tv.notube.synch.core.Synchronizer;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class InstanceManager {

    private LoggableSynchronizer synchronizer;

    public InstanceManager() {
        synchronizer = LoaderInstanceManager.getInstance().getSynchronizer();
    }

    public LoggableSynchronizer getSynchronizer() {
        return synchronizer;
    }
}
