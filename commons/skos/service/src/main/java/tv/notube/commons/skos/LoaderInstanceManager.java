package tv.notube.commons.skos;

import tv.notube.commons.skos.service.Skos;
import tv.notube.commons.skos.service.SkosConfiguration;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LoaderInstanceManager {

    private static LoaderInstanceManager instance =
            new LoaderInstanceManager();

    private Skos skos;

    public static LoaderInstanceManager getInstance() {
        if (instance == null)
            instance = new LoaderInstanceManager();

        return instance;
    }

    private LoaderInstanceManager() {
        SkosConfiguration skosConfiguration = ConfigurationManager
                .getInstance("skos-configuration.xml")
                .getSkosConfiguration();
        skos = new Skos(skosConfiguration);
    }

    public Skos getSkos() {
        return skos;
    }

}
