package tv.notube.applications;

import java.util.UUID;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface ApplicationsManager {

    public String registerApplication(
            Application application
    ) throws ApplicationsManagerException;

    public Application getApplication(String name)
            throws ApplicationsManagerException;

    public Application getApplicationByApiKey(String name)
            throws ApplicationsManagerException;

    public void grantPermission(String name, Permission permission)
        throws ApplicationsManagerException;

    public void grantPermission(
            String name,
            UUID resource,
            Permission.Action action
    ) throws ApplicationsManagerException;

    public void deregisterApplication(String name)
            throws ApplicationsManagerException;

    public boolean isAuthorized(
            String apiKey,
            UUID resource,
            Permission.Action action
    ) throws ApplicationsManagerException;

    public boolean isAuthorized(String apiKey)
            throws ApplicationsManagerException;
}
