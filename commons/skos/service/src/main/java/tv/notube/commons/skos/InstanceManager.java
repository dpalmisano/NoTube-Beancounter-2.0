package tv.notube.commons.skos;

import tv.notube.commons.skos.service.Skos;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class InstanceManager {

    private Skos skosService;

    public InstanceManager() {
        skosService = new Skos();
    }

    public Skos getSkos() {
        return skosService;
    }
}
