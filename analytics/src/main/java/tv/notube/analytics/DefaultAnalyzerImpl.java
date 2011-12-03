package tv.notube.analytics;

import tv.notube.analytics.analysis.Analysis;
import tv.notube.analytics.analysis.AnalysisDescription;
import tv.notube.analytics.analysis.AnalysisException;
import tv.notube.analytics.analysis.AnalysisResult;
import tv.notube.commons.storage.kvs.KVStore;
import tv.notube.commons.storage.kvs.KVStoreException;
import tv.notube.commons.storage.model.Activity;
import tv.notube.commons.storage.model.ActivityLog;
import tv.notube.commons.storage.model.Query;
import tv.notube.commons.storage.model.QueryException;
import tv.notube.commons.storage.model.fields.StringField;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DefaultAnalyzerImpl extends StorageAnalyzerImpl {

    private Set<Analysis> analyses = new HashSet<Analysis>();

    private final String ANALYSIS = "analysis";

    public DefaultAnalyzerImpl(KVStore kvs, ActivityLog alog) {
        super(kvs, alog);
        loadAnalysis();
    }

    private void loadAnalysis() {
        List<String> analysisKeys;
        analyses = new HashSet<Analysis>();
        try {
            analysisKeys = kvs.search(ANALYSIS);
        } catch (KVStoreException e) {
            throw new RuntimeException("", e);
        }
        for(String analysisKey : analysisKeys) {
            AnalysisDescription analysisDescription;
            try {
                analysisDescription = (AnalysisDescription) kvs.getValue(ANALYSIS, analysisKey);
            } catch (KVStoreException e) {
                throw new RuntimeException("", e);
            }
            try {
                registerAnalysis(analysisDescription);
            } catch (AnalyzerException e) {
                throw new RuntimeException("", e);
            }
        }
    }

    public void registerAnalysis(
            String name,
            String description,
            Query query,
            Class<? extends Analysis> clazz
    ) throws AnalyzerException {
        AnalysisDescription analysisDescription =
                new AnalysisDescription(
                        name,
                        description,
                        query,
                        clazz.getName()
                );
        registerAnalysis(analysisDescription);
    }

    private void registerAnalysis(AnalysisDescription analysisDescription)
            throws AnalyzerException {

        Class<? extends Analysis> clazz;
        try {
            clazz = (Class<? extends Analysis>)
                    Class.forName(analysisDescription.getClassName());
        } catch (ClassNotFoundException e) {
            throw new AnalyzerException("", e);
        }
        Constructor<? extends Analysis> constructor;
        try {
            constructor = clazz.getConstructor(
                    ActivityLog.class,
                    String.class,
                    String.class
            );
        } catch (NoSuchMethodException e) {
            throw new AnalyzerException("", e);
        }
        Analysis analysis;
        try {
            analysis = constructor.newInstance(
                    alog,
                    analysisDescription.getName(),
                    analysisDescription.getDescription()
            );
        } catch (InstantiationException e) {
            throw new AnalyzerException("", e);
        } catch (IllegalAccessException e) {
            throw new AnalyzerException("", e);
        } catch (InvocationTargetException e) {
            throw new AnalyzerException("", e);
        }
        try {
            analysis.registerQuery(analysisDescription.getQuery());
        } catch (AnalysisException e) {
            throw new AnalyzerException("", e);
        }
        if(analyses.contains(analysis)) {
            throw new AnalyzerException(
                    "Analysis '" + analysisDescription.getName() + "' already registered"
            );
        }
        analyses.add(analysis);
        storeAnalysis(analysisDescription);
    }

    private void storeAnalysis(AnalysisDescription analysisDescription) throws AnalyzerException {
        try {
            kvs.setValue(ANALYSIS, analysisDescription.getName(), analysisDescription);
        } catch (KVStoreException e) {
            throw new AnalyzerException("Error while storing analysis '" +
                    analysisDescription.getName() +
                    "'", e
            );
        }
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
            analysisResult.setName(analysis.getName());
            analysisResult.setUser(owner);
            flush(analysis.getName(), owner);
            storeResult(analysisResult, kvs);
        }
    }

    public AnalysisResult getResult(
            String name,
            String username
    ) throws AnalyzerException {
       try {
            return (AnalysisResult) kvs.getValue(
                    name,
                    username
            );
        } catch (KVStoreException e) {
            throw new AnalyzerException(
                    "Error while getting activity '" +
                    name + "'" + "results for user '" + username + "'",
                    e
            );
        }
    }

    public void deregisterAnalysis(String name) throws AnalyzerException {
        try {
            kvs.deleteValue(ANALYSIS, name);
        } catch (KVStoreException e) {
            throw new AnalyzerException("", e);
        }
        loadAnalysis();
    }

    public AnalysisDescription[] getRegisteredAnalysis() throws AnalyzerException {
        AnalysisDescription descriptions[] = new AnalysisDescription[analyses.size()];
        int i = 0;
        for(Analysis analysis : analyses) {
            descriptions[i] = new AnalysisDescription(
                    analysis.getName(),
                    analysis.getDescription(),
                    analysis.getQuery(),
                    analysis.getClass().getName()
            );
            i++;
        }
        return descriptions;
    }

    public void flush(String name, String username) throws AnalyzerException {
        try {
            kvs.deleteValue(name, username);
        } catch (KVStoreException e) {
            throw new AnalyzerException("Error while deleting activity " +
                    "result '" + name + "' for user '" + username + "'", e);
        }
    }

    private void storeResult(AnalysisResult analysisResult, KVStore kvs) throws AnalyzerException {
        StringField exectutedAt = new StringField(
                "executedAt",
                String.valueOf(analysisResult.getExecutedAt()
                .getMillis())
        );
        try {
            kvs.setValue(
                    analysisResult.getName(),
                    analysisResult.getUser(),
                    analysisResult,
                    exectutedAt
            );
        } catch (KVStoreException e) {
            throw new AnalyzerException(
                    "Error while storing analysis results '" +
                    analysisResult.getName() +
                    "' for user '" + analysisResult.getName() + "'",
                    e
            );
        }
    }
}
