package tv.notube.profiler.data.datasources;

import tv.notube.commons.model.User;
import tv.notube.profiler.data.DataSource;
import tv.notube.profiler.data.DataSourceException;
import tv.notube.profiler.data.RawDataSet;
import org.apache.log4j.Logger;
import tv.notube.usermanager.UserManager;
import tv.notube.usermanager.UserManagerException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDataSource implements DataSource {

    private UserManager userManager;

    private static Logger logger = Logger.getLogger(UserDataSource.class);

    public UserDataSource(UserManager userManager) {
        this.userManager = userManager;
    }

    public void init() throws DataSourceException {}

    public void dispose() throws DataSourceException {
        userManager = null;
    }

    public RawDataSet getRawData() throws DataSourceException {
        List<UUID> userIds;
        try {
            userIds = userManager.getUsersToBeProfiled();
        } catch (UserManagerException e) {
            final String errMsg = "Error while getting user IDs to be profiled";
            logger.error(errMsg, e);
            throw new DataSourceException(errMsg, e);
        }
        List<User> usersToProfile = new ArrayList<User>();
        for (UUID userId : userIds) {
            User user;
            try {
                user = userManager.getUser(userId);
            } catch (UserManagerException e) {
                final String errMsg = "Error while getting user with ID '" + userId + "'";
                logger.error(errMsg, e);
                throw new DataSourceException(errMsg, e);
            }
            usersToProfile.add(user);
        }
        return new RawDataSet<User>(usersToProfile);
    }

}
