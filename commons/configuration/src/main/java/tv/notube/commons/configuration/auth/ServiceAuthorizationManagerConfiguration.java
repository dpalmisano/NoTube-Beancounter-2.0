package tv.notube.commons.configuration.auth;

import tv.notube.commons.configuration.Configuration;
import tv.notube.commons.model.auth.AuthHandler;
import tv.notube.commons.model.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ServiceAuthorizationManagerConfiguration extends Configuration {

     private Map<Service, Class<? extends AuthHandler>> services =
            new HashMap<Service, Class<? extends AuthHandler>>();

    public Set<Service> getServices() {
        return services.keySet();
    }

    public Class<? extends AuthHandler> getServiceHandler(String name) {
        for(Service service : services.keySet()) {
            if(service.getName().equals(name)) {
                return services.get(service);
            }
        }
        throw new IllegalArgumentException(
                "AuthHandler for service [" + name + "] not found"
        );
    }

    public void addService(Service service, Class<? extends AuthHandler> handlerClass) {
        services.put(service, handlerClass);
    }

}
