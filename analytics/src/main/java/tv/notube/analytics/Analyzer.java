package tv.notube.analytics;

import tv.notube.analytics.analysis.AnalysisResult;
import tv.notube.commons.configuration.analytics.AnalysisDescription;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface Analyzer {

    public void registerAnalysis(
            AnalysisDescription analysisDescription,
            boolean persist
    ) throws AnalyzerException;

    public AnalysisDescription getAnalysisDescription(String name) throws AnalyzerException;

    public void run(String owner) throws AnalyzerException;

    public AnalysisResult getResult(
            String name,
            String username
    ) throws AnalyzerException;

    public void deregisterAnalysis(String name) throws AnalyzerException;

    public AnalysisDescription[] getRegisteredAnalysis() throws AnalyzerException;

    public void flush(String name, String username) throws AnalyzerException;

}
