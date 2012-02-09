package tv.notube.analytics.runner;

import tv.notube.analytics.*;
import tv.notube.usermanager.*;

import java.util.*;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Runner {

    private static String OWNER = "user-manager-%s";

    private static UserManager userManager;

    private static Analyzer analyzer;

    public static void main(String args[]) {
        String isInitStr = args[0];
        boolean isInit = Boolean.parseBoolean(isInitStr);

        if(isInit) {
            try {
                initAnalyzer(analyzer);
            } catch (AnalyzerException e) {
                throw new RuntimeException(
                        "Error while initializing analyzer",
                        e
                );
            }
        } else {
            analyzer = DefaultAnalyzerFactoryImpl.getInstance(false).build();
        }
        try {
            userManager = DefaultUserManagerFactory.getInstance().build();
        } catch (UserManagerFactoryException e) {
            final String errMsg = "Error while building user manager";
            throw new RuntimeException(errMsg, e);
        }
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
        analyzer = DefaultAnalyzerFactoryImpl.getInstance(true).build();
    }

}
