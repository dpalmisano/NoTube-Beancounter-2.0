package tv.notube.analytics;

import tv.notube.analytics.analysis.Analysis;
import tv.notube.analytics.analysis.AnalysisDescription;
import tv.notube.analytics.analysis.AnalysisResult;
import tv.notube.commons.storage.model.Query;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface Analyzer {

    public void registerAnalysis(
            String name,
            String description,
            Query query,
            Class<? extends Analysis> clazz
    ) throws AnalyzerException;

    public void run(String owner)
        throws AnalyzerException;

    public AnalysisResult getResult(
            String name,
            String username
    ) throws AnalyzerException;

    public void deregisterAnalysis(String name) throws AnalyzerException;

    public AnalysisDescription[] getRegisteredAnalysis() throws AnalyzerException;

    public void flush(String name, String username) throws AnalyzerException;
}
