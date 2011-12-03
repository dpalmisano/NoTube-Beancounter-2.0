package tv.notube.analytics.runner;

import tv.notube.analytics.Analyzer;
import tv.notube.analytics.AnalyzerException;
import tv.notube.analytics.DefaultAnalyzerImpl;
import tv.notube.analytics.analysis.custom.ActivityAnalysis;
import tv.notube.analytics.analysis.custom.TimeFrameAnalysis;
import tv.notube.commons.storage.alog.DefaultActivityLogImpl;
import tv.notube.commons.storage.kvs.KVStore;
import tv.notube.commons.storage.kvs.mybatis.MyBatisKVStore;
import tv.notube.commons.storage.model.ActivityLog;
import tv.notube.commons.storage.model.Query;
import tv.notube.commons.storage.model.fields.Field;
import tv.notube.commons.storage.model.fields.StringField;
import tv.notube.commons.storage.model.fields.serialization.SerializationManager;
import tv.notube.usermanager.DefaultUserManagerImpl;
import tv.notube.usermanager.UserManager;
import tv.notube.usermanager.UserManagerException;
import tv.notube.usermanager.configuration.ConfigurationManager;
import tv.notube.usermanager.configuration.UserManagerConfiguration;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Runner {

    private static final String TIMEFRAME_ANALYSIS = "timeframe-analysis";

    private static final String ACTIVITY_ANALYSIS = "activity-analysis";

    private static String OWNER = "user-manager-";

    private static UserManager userManager;

    private static Analyzer analyzer;

    public static void main(String args[]) {
        String isInitStr = args[0];
        UserManagerConfiguration configuration
                 = ConfigurationManager
                 .getInstance("user-manager.xml")
                 .getUserManagerConfiguration();
        boolean isInit = Boolean.parseBoolean(isInitStr);
        analyzer = getAnalyzer();
        if(isInit) {
            try {
                initAnalyzer(analyzer);
            } catch (AnalyzerException e) {
                throw new RuntimeException(
                        "Error while initializing analyzer",
                        e
                );
            }
        }
        userManager = new DefaultUserManagerImpl(configuration);
        List<UUID> userIds;
        try {
            userIds = userManager.getUsersToCrawled();
        } catch (UserManagerException e) {
            throw new RuntimeException("Error while getting user IDs", e);
        }
        for(UUID userId : userIds) {
            try {
                System.out.println("running on " + userId);
                analyzer.run(String.format(OWNER, userId.toString()));
            } catch (AnalyzerException e) {
                throw new RuntimeException("Error while getting user IDs", e);
            }
        }
    }

    private static void initAnalyzer(Analyzer analyzer) throws AnalyzerException {
        analyzer.registerAnalysis(
                TIMEFRAME_ANALYSIS,
                "this analysis summarizes the user activities in a timeframe",
                getQuery(),
                TimeFrameAnalysis.class
        );
        analyzer.registerAnalysis(
                ACTIVITY_ANALYSIS,
                "this analysis summarizes the user activities",
                getQuery(),
                ActivityAnalysis.class
        );
    }

    private static Analyzer getAnalyzer() {
        Properties prop = new Properties();
        prop.setProperty("url", "jdbc:mysql://moth.notube.tv:3306/alog");
        prop.setProperty("username", "alog");
        prop.setProperty("password", "alog");
        ActivityLog alog = new DefaultActivityLogImpl(prop);
        prop = new Properties();
        prop.setProperty("url", "jdbc:mysql://moth.notube.tv:3306/kvs");
        prop.setProperty("username", "kvs");
        prop.setProperty("password", "kvs");
        KVStore kvs = new MyBatisKVStore(prop, new SerializationManager());
        return new DefaultAnalyzerImpl(kvs, alog);
    }

    private static Query getQuery() {
        Query query = new Query();
        Field tweet = new StringField("verb", "TWEET");
        query.push(tweet, Query.Math.EQ);
        query.push(Query.Boolean.OR);
        Field listen = new StringField("verb", "LISTEN");
        query.push(listen, Query.Math.EQ);
        return query;
    }

}
