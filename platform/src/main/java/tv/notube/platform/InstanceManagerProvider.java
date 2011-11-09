package tv.notube.platform;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.spi.inject.SingletonTypeInjectableProvider;

import javax.ws.rs.ext.Provider;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
@Provider
public class InstanceManagerProvider
        extends SingletonTypeInjectableProvider<InjectParam, InstanceManager> {

    /**
     * Manager instance instance.
     */
    public static InstanceManager instanceManager = new InstanceManager();

    public InstanceManagerProvider() {
        super(InstanceManager.class, instanceManager);
    }

}