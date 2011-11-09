package tv.notube.extension.profilingline;

import tv.notube.commons.model.Interest;
import tv.notube.commons.model.UserProfile;
import tv.notube.commons.model.activity.Activity;
import tv.notube.profiler.line.ProfilingLineItem;
import tv.notube.profiler.line.ProfilingLineItemException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class WeightingProfilingLineItem extends ProfilingLineItem {

    public WeightingProfilingLineItem(String name, String description) {
        super(name, description);
    }

    @Override
    public void execute(Object o) throws ProfilingLineItemException {
        UnweightedInterests uw = (UnweightedInterests) o;
        Map<URI, List<Activity>> interests = uw.getInterests();
        int activitiesNumber = 0;
        for(URI uri : interests.keySet()) {
            activitiesNumber += interests.get(uri).size();
        }
        float initialWeight = 1.0f / activitiesNumber;
        UserProfile userProfile = new UserProfile();
        userProfile.setVisibility(UserProfile.Visibility.PUBLIC);
        userProfile.setUsername(uw.getUser());
        List<Interest> wInterests = new ArrayList<Interest>();
        for(URI uri : interests.keySet()) {
            Interest interest = new Interest();
            float w = initialWeight * interests.get(uri).size();
            interest.setWeight(w);
            interest.setResource(uri);
            interest.setVisible(true);
            interest.setActivities(interests.get(uri));
            wInterests.add(interest);
        }
        userProfile.setInterests(normalize(wInterests));

        this.getNextProfilingLineItem().execute(userProfile);

    }

    private List<Interest> normalize(List<Interest> wInterests) {
        // a = avarage
        // took only the ones over the avarage
        float a = 0.0f;
        for(Interest i : wInterests) {
            a += i.getWeight();
        }
        a = a / wInterests.size();
        List<Interest> overs = new ArrayList<Interest>();
        int activitiesNumber = 0;
        for (Interest i : wInterests) {
            if(i.getWeight() >= a) {
                overs.add(i);
                activitiesNumber += i.getActivities().size();
            }
        }
        float initialWeight = 1f / activitiesNumber;
        List<Interest> result = new ArrayList<Interest>();
        for(Interest i : overs) {
            i.setWeight(initialWeight * i.getActivities().size());
            result.add(i);
        }
        return result;
    }
}
