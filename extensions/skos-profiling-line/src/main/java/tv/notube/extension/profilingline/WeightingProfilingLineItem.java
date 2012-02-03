package tv.notube.extension.profilingline;

import tv.notube.commons.model.Interest;
import tv.notube.commons.model.UserProfile;
import tv.notube.commons.model.activity.Activity;
import tv.notube.profiler.line.ProfilingLineItem;
import tv.notube.profiler.line.ProfilingLineItemException;

import java.net.URI;
import java.util.*;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class WeightingProfilingLineItem extends ProfilingLineItem {

    public WeightingProfilingLineItem(String name, String description) {
        super(name, description);
    }

    @Override
    public void execute(Object o) throws ProfilingLineItemException {
        RawData intermediate = (RawData) o;
        Map<Activity, List<URI>> linkedActivities
                = intermediate.getLinkedActivities();

        Map<URI, List<Activity>> activitiesByInterest
                = new HashMap<URI, List<Activity>>();

        for (Activity activity : linkedActivities.keySet()) {
            List<URI> activityInterests = linkedActivities.get(activity);
            for(URI activityInterest : activityInterests) {
                if(activitiesByInterest.containsKey(activityInterest)) {
                    activitiesByInterest.get(activityInterest).add(activity);
                } else {
                    List<Activity> interestActivities =
                            new ArrayList<Activity>();
                    interestActivities.add(activity);
                    activitiesByInterest.put(
                            activityInterest,
                            interestActivities
                    );
                }
            }
        }

        Set<Interest> wInterests = new HashSet<Interest>();
        int activitiesNumber = linkedActivities.keySet().size();
        float initialWeight = 1.0f / activitiesNumber;
        for(URI interest : activitiesByInterest.keySet()) {
            Interest interestObj = new Interest();
            float w = initialWeight;
            interestObj.setWeight(w);
            interestObj.setResource(interest);
            interestObj.setVisible(true);
            interestObj.setActivities(uniq(activitiesByInterest.get(interest)));
            wInterests.add(interestObj);
        }
        WeightedInterests weightedInterests = new WeightedInterests(
                intermediate.getUsername(),
                normalize(wInterests)
                );
        this.getNextProfilingLineItem().execute(weightedInterests);
    }

    private List<Activity> uniq(List<Activity> activities) {
        Set<Activity> unique = new HashSet<Activity>(activities);
        return Arrays.asList(unique.toArray(new Activity[unique.size()]));
    }

    private Set<Interest> normalize(Set<Interest> wInterests) {
        // a = avarage
        // took only the ones over the avarage
        float a = 0.0f;
        for(Interest i : wInterests) {
            a += i.getWeight();
        }
        a = a / wInterests.size();
        Set<Interest> overs = new HashSet<Interest>();
        int activitiesNumber = 0;
        for (Interest i : wInterests) {
            if(i.getWeight() >= a) {
                overs.add(i);
                activitiesNumber += i.getActivities().size();
            }
        }
        float initialWeight = 1f / activitiesNumber;
        Set<Interest> result = new HashSet<Interest>();
        for(Interest i : overs) {
            i.setWeight(initialWeight * i.getActivities().size());
            result.add(i);
        }
        return result;
    }
}
