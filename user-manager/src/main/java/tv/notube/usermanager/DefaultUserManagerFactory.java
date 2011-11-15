package tv.notube.usermanager;

import tv.notube.usermanager.configuration.UserManagerConfiguration;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DefaultUserManagerFactory implements UserManagerFactory {

    private static UserManagerFactory instance;

    public static synchronized UserManagerFactory getInstance(
            UserManagerConfiguration configuration
    ) {
        if(instance == null) {
            instance = new DefaultUserManagerFactory(configuration);
        }
        return instance;
    }

    private DefaultUserManagerFactory(UserManagerConfiguration configuration) {
        userManager = new DefaultUserManagerImpl(configuration);
    }

    private UserManager userManager;

    public UserManager build() throws UserManagerFactoryException {
        return userManager;
    }
}
