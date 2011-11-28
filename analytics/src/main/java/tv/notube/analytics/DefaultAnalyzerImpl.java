package tv.notube.analytics;

import tv.notube.analytics.analysis.Analysis;
import tv.notube.analytics.analysis.AnalysisException;
import tv.notube.analytics.analysis.AnalysisResult;
import tv.notube.commons.storage.kvs.KVStore;
import tv.notube.commons.storage.model.ActivityLog;

import java.util.HashSet;
import java.util.Set;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DefaultAnalyzerImpl extends StorageAnalyzerImpl {

    private Set<Analysis> analyses = new HashSet<Analysis>();

    public DefaultAnalyzerImpl(KVStore kvs, ActivityLog alog) {
        super(kvs, alog);
    }

    public void registerAnalysis(Analysis analysis) throws AnalyzerException {
        if(!analyses.contains(analysis)) {
            analyses.add(analysis);
        }
        throw new AnalyzerException("Analysis '" + analysis + "' already " +
                "registered");
    }

    public void run(String owner) throws AnalyzerException {
        for(Analysis analysis : analyses) {
            AnalysisResult analysisResult;
            try {
                analysisResult = analysis.run(owner);
            } catch (AnalysisException e) {
                throw new AnalyzerException(
                        "Error while running analysis '" + analysis + "'",
                        e
                );
            }
            storeResult(analysisResult, kvs);
        }
    }

    private void storeResult(AnalysisResult analysisResult, KVStore kvs) {
        //To change body of created methods use File | Settings | File Templates.
    }
}
