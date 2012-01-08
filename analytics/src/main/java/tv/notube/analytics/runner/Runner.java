package tv.notube.analytics.runner;

import tv.notube.analytics.Analyzer;
import tv.notube.analytics.AnalyzerException;
import tv.notube.analytics.DefaultAnalyzerImpl;
import tv.notube.analytics.analysis.AnalysisDescription;
import tv.notube.analytics.analysis.MethodDescription;
import tv.notube.analytics.analysis.custom.*;
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

import java.util.*;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Runner {

    private static final String TIMEFRAME_ANALYSIS = "timeframe-analysis";

    private static final String ACTIVITY_ANALYSIS = "activity-analysis";

    private static final String WYWOL_ANALYSIS = "wywol-analysis";

    private static String OWNER = "user-manager-%s";

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
            System.out.println("working on " + String.format(OWNER, userId.toString()));
            try {
                analyzer.run(String.format(OWNER, userId.toString()));
            } catch (AnalyzerException e) {
                throw new RuntimeException("Error while getting user IDs", e);
            }
        }
    }

    private static void initAnalyzer(Analyzer analyzer) throws AnalyzerException {
        MethodDescription getAmount;
        getAmount = new MethodDescription(
                "getNumberOfActivitiesByVerb",
                "it returns the total number of activities of the verb specified as parameter",
                new String[] { "java.lang.String" }
        );

        MethodDescription getActivities;
        getActivities = new MethodDescription(
                "getActivities",
                "it returns the activity types the user performed",
                new String[] {}
        );

        MethodDescription getTotalActivities;
        getTotalActivities = new MethodDescription(
                "getTotalActivities",
                "it returns the total number of activities of the user",
                new String[] {}
        );

        MethodDescription getServiceAmount;
        getServiceAmount = new MethodDescription(
                "getNumberOfActivitiesByService",
                "it returns the total number of activities done on a " +
                        "specified service (must be a valid URL)",
                new String[] { "java.net.URL" }
        );

        Set<MethodDescription> aadMds = new HashSet<MethodDescription>();
        aadMds.add(getAmount);
        aadMds.add(getActivities);
        aadMds.add(getTotalActivities);
        aadMds.add(getServiceAmount);
        AnalysisDescription aad = new AnalysisDescription(
                ACTIVITY_ANALYSIS,
                "this analysis gives an overview on the user activities summarizing them",
                getQuery(),
                ActivityAnalysis.class.getCanonicalName(),
                ActivityAnalysisResult.class.getCanonicalName(),
                aadMds
        );

        MethodDescription getStatistics;
        getStatistics = new MethodDescription(
                "getStatistics",
                "this method returns activity statistics day by day of the last month",
                new String[] { "java.lang.Integer" }
        );
        Set<MethodDescription> tfdMds = new HashSet<MethodDescription>();
        tfdMds.add(getStatistics);
        AnalysisDescription tad = new AnalysisDescription(
                TIMEFRAME_ANALYSIS,
                "this analysis summarizes the user activities over time",
                getQuery(),
                TimeFrameAnalysis.class.getCanonicalName(),
                TimeFrameAnalysisResult.class.getCanonicalName(),
                tfdMds
        );

        MethodDescription getPercentage;
        getPercentage = new MethodDescription(
                "getPercentages",
                "returns watch or listen percentages per each time slice 0 to 3",
                new String[] { "java.lang.Integer" }
        );
        MethodDescription getNumbers;
        getNumbers = new MethodDescription(
                "getNumbers",
                "returns watch or listen absolute numbers per each time slice 0 to 3",
                new String[] { "java.lang.Integer" }
        );
        Set<MethodDescription> wywoladmds = new HashSet<MethodDescription>();
        wywoladmds.add(getPercentage);
        wywoladmds.add(getNumbers);
        AnalysisDescription wywolad =  new AnalysisDescription(
                WYWOL_ANALYSIS,
                "returns watch or listen absolute numbers per each time slice 0 to 3",
                getWywolQuery(),
                WhenYouWatchOrListenAnalysis.class.getCanonicalName(),
                WhenYouWatchOrListenAnalysisResult.class.getCanonicalName(),
                wywoladmds
        );
        analyzer.registerAnalysis(aad, true);
        analyzer.registerAnalysis(tad, true);
        analyzer.registerAnalysis(wywolad, true);
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
        query.push(Query.Boolean.OR);
        Field watched = new StringField("verb", "WATCHED");
        query.push(watched, Query.Math.EQ);
        return query;
    }

    private static Query getWywolQuery() {
        Query query = new Query();
        Field tweet = new StringField("verb", "LISTEN");
        query.push(tweet, Query.Math.EQ);
        query.push(Query.Boolean.OR);
        Field listen = new StringField("verb", "WATCHED");
        query.push(listen, Query.Math.EQ);
        return query;
    }

}
