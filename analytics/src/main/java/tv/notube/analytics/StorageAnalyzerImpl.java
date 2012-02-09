package tv.notube.analytics;

import tv.notube.commons.configuration.analytics.AnalysisDescription;
import tv.notube.commons.storage.kvs.KVStore;
import tv.notube.commons.storage.model.ActivityLog;

import java.util.HashMap;
import java.util.Map;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public abstract class StorageAnalyzerImpl implements Analyzer {

    protected Map<String, AnalysisDescription> analysisDescriptions = new
            HashMap<String, AnalysisDescription>();

    protected KVStore kvs;

    protected ActivityLog alog;

    public StorageAnalyzerImpl(KVStore kvs, ActivityLog alog) {
        this.kvs = kvs;
        this.alog = alog;
    }

    public AnalysisDescription getAnalysisDescription(String name)
            throws AnalyzerException {
        return analysisDescriptions.get(name);
    }

}
