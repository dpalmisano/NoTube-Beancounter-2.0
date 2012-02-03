package tv.notube.commons.skos;

import tv.notube.commons.skos.service.Skos;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class InstanceManager {

    private Skos skosService;

    public InstanceManager() {
        skosService = LoaderInstanceManager.getInstance().getSkos();
    }

    public Skos getSkos() {
        return skosService;
    }
}
