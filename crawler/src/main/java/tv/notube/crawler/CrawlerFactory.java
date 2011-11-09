package tv.notube.crawler;


/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface CrawlerFactory {

     public Crawler build() throws CrawlerFactoryException;

}
