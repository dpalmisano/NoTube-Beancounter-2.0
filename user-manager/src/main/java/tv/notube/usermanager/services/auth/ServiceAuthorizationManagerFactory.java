package tv.notube.usermanager.services.auth;

import tv.notube.commons.configuration.auth.ServiceAuthorizationManagerConfiguration;
import tv.notube.commons.model.auth.AuthHandler;
import tv.notube.commons.model.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 *
 */
public class ServiceAuthorizationManagerFactory {

    private static ServiceAuthorizationManagerFactory instance;

    public static ServiceAuthorizationManagerFactory getInstance(
            ServiceAuthorizationManagerConfiguration samc
    ) {
        if(instance == null) {
            instance = new ServiceAuthorizationManagerFactory(samc);
        }
        return instance;
    }

    private ServiceAuthorizationManager sam;

    private ServiceAuthorizationManagerFactory(
            ServiceAuthorizationManagerConfiguration samc
    ) {
        sam = new DefaultServiceAuthorizationManager();
        for(Service service : samc.getServices()) {
            Class<? extends AuthHandler> handlerClass = samc.getServiceHandler(service.getName());
            AuthHandler handler = getHandlerInstance(handlerClass, service);
            try {
                sam.addHandler(service, handler);
            } catch (ServiceAuthorizationManagerException e) {
                final String errMsg = "Error while registering handler for " +
                        "service [" + service.getName() + "]";
                throw new RuntimeException(errMsg, e);
            }
        }
    }

    private AuthHandler getHandlerInstance(
            Class<? extends AuthHandler> handlerClass,
            Service service
    ) {
        Constructor<AuthHandler> constructor;
        try {
            constructor = (Constructor<AuthHandler>) handlerClass.getConstructor(Service.class);
        } catch (NoSuchMethodException e) {
            final String errMsg = "cannot find auth handler constructor for" +
                    "for service [" + service.getName() + "]";
            throw new RuntimeException(errMsg, e);
        }
        try {
            return constructor.newInstance(service);
        } catch (InstantiationException e) {
            final String errMsg = "Error while instantiating authhandler for " +
                    "service [" + service.getName() + "]";
            throw new RuntimeException(errMsg, e);
        } catch (IllegalAccessException e) {
            final String errMsg = "Error while accessing authhandler " +
                    "constructor for service [" + service.getName() + "]";
            throw new RuntimeException(errMsg, e);
        } catch (InvocationTargetException e) {
            final String errMsg = "Error while invoking authhandler for " +
                    "service [" + service.getName() + "]";
            throw new RuntimeException(errMsg, e);
        }
    }

    public ServiceAuthorizationManager build() {
        return sam;
    }

}
