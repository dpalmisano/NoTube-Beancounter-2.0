package tv.notube.analytics;

import tv.notube.commons.storage.kvs.KVStore;
import tv.notube.commons.storage.model.ActivityLog;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public abstract class StorageAnalyzerImpl implements Analyzer {

    protected KVStore kvs;

    protected ActivityLog alog;

    public StorageAnalyzerImpl(KVStore kvs, ActivityLog alog) {
        this.kvs = kvs;
        this.alog = alog;
    }

}
