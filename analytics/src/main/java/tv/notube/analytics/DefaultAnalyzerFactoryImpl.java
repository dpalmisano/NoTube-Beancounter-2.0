package tv.notube.analytics;

import tv.notube.commons.configuration.Configurations;
import tv.notube.commons.configuration.ConfigurationsException;
import tv.notube.commons.configuration.analytics.AnalysisDescription;
import tv.notube.commons.configuration.analytics.AnalyticsConfiguration;
import tv.notube.commons.configuration.storage.StorageConfiguration;
import tv.notube.commons.storage.alog.DefaultActivityLogImpl;
import tv.notube.commons.storage.kvs.KVStore;
import tv.notube.commons.storage.kvs.mybatis.MyBatisKVStore;
import tv.notube.commons.storage.model.ActivityLog;
import tv.notube.commons.storage.model.fields.serialization.SerializationManager;

import java.util.Set;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DefaultAnalyzerFactoryImpl {

    private static DefaultAnalyzerFactoryImpl instance;

    private static final String ANALYZER_CONF = "analyzer-configuration.xml";

    private static final String STORAGE_CONF = "storage-configuration.xml";

    public static DefaultAnalyzerFactoryImpl getInstance(boolean fromScratch) {
        if(instance == null) {
            instance = new DefaultAnalyzerFactoryImpl(fromScratch);
        }
        return instance;
    }

    private Analyzer analyzer;

    public DefaultAnalyzerFactoryImpl(boolean fromScratch) {
        AnalyticsConfiguration analyticsConfiguration;
        try {
             analyticsConfiguration = Configurations
                    .getConfiguration(
                    ANALYZER_CONF,
                    AnalyticsConfiguration.class
            );
        } catch (ConfigurationsException e) {
            final String errMsg = "Error while loading configuration";
            throw new RuntimeException(errMsg, e);
        }
        StorageConfiguration storageConfiguration;
        try {
            storageConfiguration = Configurations.getConfiguration(
                    STORAGE_CONF,
                    StorageConfiguration.class
            );
        } catch (ConfigurationsException e) {
            final String errMsg = "Error while loading configuration for kvs";
            throw new RuntimeException(errMsg, e);
        }

        KVStore kvs = getKVS(storageConfiguration);
        ActivityLog alog = getAlog(storageConfiguration);
        analyzer = new DefaultAnalyzerImpl(kvs, alog);
        try {
            initAnalyzer(analyzer, analyticsConfiguration, fromScratch);
        } catch (AnalyzerException e) {
            final String errMsg = "Error while initializing the analyzer for";
            throw new RuntimeException(errMsg, e);
        }
    }

    private void initAnalyzer(
            Analyzer analyzer,
            AnalyticsConfiguration analyticsConfiguration,
            boolean fromScratch
    ) throws AnalyzerException {
        Set<AnalysisDescription> analysisDescriptions = analyticsConfiguration
                .getAnalysisDescriptions();
        for(AnalysisDescription analysisDescription : analysisDescriptions) {
            analyzer.registerAnalysis(analysisDescription, fromScratch);
        }
    }

    private KVStore getKVS(StorageConfiguration storageConfiguration) {
        return new MyBatisKVStore(
                storageConfiguration.getKvsProperties(),
                new SerializationManager()
        );
    }

    private ActivityLog getAlog(StorageConfiguration storageConfiguration) {
        return new DefaultActivityLogImpl(storageConfiguration.getActivityLogProperties());
    }

    public Analyzer build() {
        return analyzer;
    }

}
