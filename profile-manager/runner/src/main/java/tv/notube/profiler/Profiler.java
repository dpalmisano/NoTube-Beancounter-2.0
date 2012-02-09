package tv.notube.profiler;

import org.apache.log4j.Logger;
import tv.notube.commons.model.UserProfile;
import tv.notube.profiler.container.ProfilingLineContainer;
import tv.notube.profiler.container.ProfilingLineContainerException;
import tv.notube.profiler.data.DataManager;
import tv.notube.profiler.data.DataManagerException;
import tv.notube.profiler.data.RawDataSet;
import tv.notube.profiler.line.ProfilingInput;
import tv.notube.profiler.line.ProfilingResult;
import tv.notube.profiler.storage.ProfileStore;
import tv.notube.profiler.storage.ProfileStoreException;

import java.util.List;
import java.util.Map;

/**
 * Main class orchestrating all the profiling process.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Profiler {

    private static Logger logger = Logger.getLogger(Profiler.class);

    private DataManager dataManager;

    private ProfilingLineContainer profilingLineContainer;

    private ProfileStore profileStore;

    public Profiler(
            DataManager dataManager,
            ProfilingLineContainer profilingLineContainer,
            ProfileStore profileStore
    ) {
        this.dataManager = dataManager;
        this.profilingLineContainer = profilingLineContainer;
        this.profileStore = profileStore;
    }

    public void run() throws ProfilerException {
        logger.info("profiling process started");

        Map<String, List<String>> registeredDataKeys;
        try {
            registeredDataKeys = dataManager.getRegisteredKeys();
        } catch (DataManagerException e) {
            final String errMsg = "Error while accessing to the registered keys";
            logger.error(errMsg, e);
            throw new ProfilerException("Error while accessing to the registered keys", e);
        }

        for (String key : registeredDataKeys.keySet()) {
            RawDataSet rawDataSet;
            logger.debug("processing key: " + key);
            try {
                rawDataSet = dataManager.getRawData(key);
            } catch (DataManagerException e) {
                final String errMsg = "Error while accessing raw data for key: " + key + ". Skipping.";
                logger.error(errMsg, e);
                continue;
            }
            while (rawDataSet.hasNext()) {
                Object objectToProfile = rawDataSet.getNext();
                for (String profilingLine : registeredDataKeys.get(key)) {
                    logger.info("Sending data marked with key: '" + key + "' towards '" + profilingLine + "'.");
                    ProfilingResult profilingResult;
                    try {
                        profilingResult = profilingLineContainer.profile(
                                new ProfilingInput(objectToProfile),
                                profilingLine
                        );
                    } catch (ProfilingLineContainerException e) {
                        final String errMsg = "Error while profiling object: '" +
                                objectToProfile + "' on profiling line '" + profilingLine + "'. Skipping.";
                        logger.error(errMsg, e);
                        continue;
                    }
                    UserProfile profileToStore = (UserProfile) profilingResult.getValue();
                    try {
                        String table = profileStore
                                .getNamespaces()
                                .get(key);
                        logger.debug("Going to write stuff using table: " + table.toString());
                        profileStore.deleteUserProfile(profileToStore.getUsername());
                        profileStore.storeUserProfile(profileToStore);
                    } catch (ProfileStoreException e) {
                        final String errMsg = "Error while storing profile: '" + profileToStore + "' on ProfileStore";
                        logger.error(errMsg, e);
                        throw new ProfilerException(errMsg, e);
                    }
                }
            }
        }
    }
}
