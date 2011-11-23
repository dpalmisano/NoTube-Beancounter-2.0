package tv.notube.extension.profilingline;

import tv.notube.commons.model.Interest;
import tv.notube.commons.model.UserProfile;
import tv.notube.profiler.line.ProfilingLineItem;
import tv.notube.profiler.line.ProfilingLineItemException;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ProfileBuildingProfilingLineItem extends ProfilingLineItem {

    public ProfileBuildingProfilingLineItem(String name, String description) {
        super(name, description);
    }

    @Override
    public void execute(Object o) throws ProfilingLineItemException {
        WeightedInterests weightedInterests = (WeightedInterests) o;
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername(weightedInterests.getUser());
        userProfile.setVisibility(UserProfile.Visibility.PUBLIC);
        userProfile.setInterests(weightedInterests.getInterests());
        this.getNextProfilingLineItem().execute(userProfile);
    }
}
