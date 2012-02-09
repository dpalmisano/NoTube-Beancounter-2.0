package tv.notube.commons.configuration.profiler;

import tv.notube.commons.configuration.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Davide Palmisano (dpalmisano@gmail.com)
 */
public class ProfilerConfiguration extends Configuration {

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

