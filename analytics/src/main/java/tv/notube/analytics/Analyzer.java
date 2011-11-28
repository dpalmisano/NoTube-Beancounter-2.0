package tv.notube.analytics;

import tv.notube.analytics.analysis.Analysis;
import tv.notube.analytics.analysis.AnalysisResult;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface Analyzer {

    public void registerAnalysis(Analysis analysis)
        throws AnalyzerException;

    public void run(String owner)
        throws AnalyzerException;

    public AnalysisResult getResult(String name) throws AnalyzerException;


}
