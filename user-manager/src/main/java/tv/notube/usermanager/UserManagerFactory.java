package tv.notube.usermanager;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface UserManagerFactory {

    public UserManager build() throws UserManagerFactoryException;

}
