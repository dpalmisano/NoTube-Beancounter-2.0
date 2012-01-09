package tv.notube.analytics.analysis.custom;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tv.notube.analytics.analysis.AnalysisException;
import tv.notube.analytics.analysis.AnalysisResult;
import tv.notube.commons.storage.alog.DefaultActivityLogImpl;
import tv.notube.commons.storage.model.ActivityLog;
import tv.notube.commons.storage.model.Query;
import tv.notube.commons.storage.model.fields.Field;
import tv.notube.commons.storage.model.fields.StringField;

import java.util.Properties;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class WhenYouWatchOrListenAnalysisTestCase {

    private WhenYouWatchOrListenAnalysis analysis;

    @BeforeTest
    public void setUp() throws AnalysisException {
        Properties prop = new Properties();
        prop.setProperty("url", "jdbc:mysql://moth.notube.tv:3306/alog");
        prop.setProperty("username", "alog");
        prop.setProperty("password", "alog");
        ActivityLog alog = new DefaultActivityLogImpl(prop);
        analysis = new WhenYouWatchOrListenAnalysis(
                alog,
                "wywol-analysis",
                "this analysis computes when a user listens or watches"
        );

        Query query = new Query();
        Field tweet = new StringField("verb", "LISTEN");
        query.push(tweet, Query.Math.EQ);
        query.push(Query.Boolean.OR);
        Field listen = new StringField("verb", "WATCHED");
        query.push(listen, Query.Math.EQ);
        analysis.registerQuery(query);
    }

    @Test
    public void testRun() throws AnalysisException {
        AnalysisResult analysisResult =
                analysis.run("user-manager-d894906e-c0be-4b2f-9049-eb56a614c3f2");
        WhenYouWatchOrListenAnalysisResult tar =
                (WhenYouWatchOrListenAnalysisResult) analysisResult;
        Assert.assertNotNull(tar);
    }

}
