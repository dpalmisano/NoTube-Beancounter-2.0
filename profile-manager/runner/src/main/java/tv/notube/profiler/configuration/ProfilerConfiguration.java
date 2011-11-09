package tv.notube.profiler.configuration;

import tv.notube.profiler.data.DataManagerConfiguration;
import tv.notube.profiler.storage.ProfileStoreConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Davide Palmisano (dpalmisano@gmail.com)
 */
public class ProfilerConfiguration {

    private DataManagerConfiguration dataManagerConfiguration;

    private ProfileStoreConfiguration profileStoreConfiguration;

    public List<ProfilingLineDescription> plDescriptions
            = new ArrayList<ProfilingLineDescription>();

    public DataManagerConfiguration getDataManagerConfiguration() {
        return dataManagerConfiguration;
    }

    public void setDataManagerConfiguration(DataManagerConfiguration dataManagerConfiguration) {
        this.dataManagerConfiguration = dataManagerConfiguration;
    }

    public ProfileStoreConfiguration getProfileStoreConfiguration() {
        return profileStoreConfiguration;
    }

    public void setProfileStoreConfiguration(ProfileStoreConfiguration profileStoreConfiguration) {
        this.profileStoreConfiguration = profileStoreConfiguration;
    }

    public List<ProfilingLineDescription> getPlDescriptions() {
        return plDescriptions;
    }

    public void setPlDescriptions(List<ProfilingLineDescription> plDescriptions) {
        this.plDescriptions = plDescriptions;
    }

    public boolean add(ProfilingLineDescription profilingLineDescription) {
        return plDescriptions.add(profilingLineDescription);
    }
}

