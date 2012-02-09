package tv.notube.usermanager;

import tv.notube.commons.storage.kvs.KVStore;
import tv.notube.commons.storage.model.ActivityLog;
import tv.notube.usermanager.services.auth.ServiceAuthorizationManager;

/**
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public abstract class StorageUserManager implements UserManager {

    protected KVStore kvs;

    protected ActivityLog alog;

    protected ServiceAuthorizationManager sam;

    protected long rate;

    public StorageUserManager(
            KVStore kvs,
            ActivityLog alog,
            ServiceAuthorizationManager sam,
            long rate
    ) {
        this.kvs = kvs;
        this.alog = alog;
        this.sam = sam;
        this.rate = rate;
    }

    public KVStore getKvStore() {
        return kvs;
    }

    public ActivityLog getActivityLog() {
        return alog;
    }

    public ServiceAuthorizationManager getSam() {
        return sam;
    }

    public long getRate() {
        return rate;
    }
}
