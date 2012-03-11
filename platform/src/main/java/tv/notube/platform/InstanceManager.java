package tv.notube.platform;

import tv.notube.analytics.Analyzer;
import tv.notube.applications.ApplicationsManager;
import tv.notube.crawler.Crawler;
import tv.notube.profiler.Profiler;
import tv.notube.profiler.storage.ProfileStore;
import tv.notube.usermanager.UserManager;

import java.util.Random;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class InstanceManager {

    private UserManager userManager;

    private ProfileStore profileStore;

    private Analyzer analyzer;

    private Random recommender;

    private ApplicationsManager applicationsManager;

    private Crawler crawler;

    private Profiler profiler;

    public InstanceManager() {
        userManager = LoaderInstanceManager.getInstance().getUserManager();
        profileStore = LoaderInstanceManager.getInstance().getProfileStore();
        analyzer = LoaderInstanceManager.getInstance().getAnalyzer();
        recommender = LoaderInstanceManager.getInstance().getRecommender();
        applicationsManager = LoaderInstanceManager.getInstance().getApplicationsManager();
        crawler = LoaderInstanceManager.getInstance().getCrawler();
        profiler = LoaderInstanceManager.getInstance().getProfiler();
    }

    UserManager getUserManager() {
        return userManager;
    }

    public ProfileStore getProfileStore() {
        return profileStore;
    }

    public Analyzer getAnalyzer() {
        return analyzer;
    }

    public Random getRecommender() {
        return recommender;
    }

    public ApplicationsManager getApplicationManager() {
        return applicationsManager;
    }

    public Crawler getCrawler() {
        return crawler;
    }

    public Profiler getProfiler() {
        return profiler;
    }
}