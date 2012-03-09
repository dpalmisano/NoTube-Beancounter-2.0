package tv.notube.platform;

import org.testng.annotations.Test;
import tv.notube.analytics.Analyzer;
import tv.notube.usermanager.UserManager;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class InstanceManagerTestCase {

    @Test
    public void test() {
        Analyzer analyzer = LoaderInstanceManager.getInstance().getAnalyzer();
        UserManager userManager = LoaderInstanceManager.getInstance()
                .getUserManager();
    }

}
