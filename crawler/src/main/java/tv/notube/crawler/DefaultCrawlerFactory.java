package tv.notube.crawler;

import tv.notube.crawler.configuration.CrawlerConfiguration;
import tv.notube.usermanager.DefaultUserManagerFactory;
import tv.notube.usermanager.UserManager;
import tv.notube.usermanager.UserManagerFactoryException;
import tv.notube.usermanager.configuration.UserManagerConfiguration;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DefaultCrawlerFactory implements CrawlerFactory {

        private static CrawlerFactory instance;

    public static synchronized CrawlerFactory getInstance(
            CrawlerConfiguration configuration
    ) {
        if(instance == null) {
            instance = new DefaultCrawlerFactory(configuration);
        }
        return instance;
    }

    private DefaultCrawlerFactory(CrawlerConfiguration configuration) {
        try {
            UserManagerConfiguration umc = configuration.getUserManagerConfiguration();
            UserManager userManager = DefaultUserManagerFactory.getInstance(umc).build();
            crawler = new ParallelCrawlerImpl(userManager);
        } catch (UserManagerFactoryException e) {
            throw new RuntimeException("Error while building UserManager", e);
        }
    }

    private Crawler crawler;

    public Crawler build() throws CrawlerFactoryException {
        return crawler;
    }

}
