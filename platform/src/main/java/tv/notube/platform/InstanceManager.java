package tv.notube.platform;

import tv.notube.profiler.storage.ProfileStore;
import tv.notube.usermanager.UserManager;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class InstanceManager {

    private UserManager userManager;

    private ProfileStore profileStore;

    public InstanceManager() {
        userManager = LoaderInstanceManager.getInstance().getUserManager();
        profileStore = LoaderInstanceManager.getInstance().getProfileStore();
    }

    UserManager getUserManager() {
        return userManager;
    }

    public ProfileStore getProfileStore() {
        return profileStore;
    }
}