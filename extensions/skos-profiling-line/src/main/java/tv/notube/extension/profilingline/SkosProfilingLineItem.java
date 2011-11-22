package tv.notube.extension.profilingline;

import tv.notube.commons.model.activity.Activity;
import tv.notube.commons.skos.SkosResolverClient;
import tv.notube.commons.skos.SkosResolverException;
import tv.notube.profiler.line.ProfilingLineItem;
import tv.notube.profiler.line.ProfilingLineItemException;

import java.net.URI;
import java.util.*;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SkosProfilingLineItem extends ProfilingLineItem {

    private SkosResolverClient skosResolver;

    public SkosProfilingLineItem(String name, String description) {
        super(name, description);
        skosResolver = new SkosResolverClient();
    }

    public void execute(Object o) throws ProfilingLineItemException {
        RawData intermediate = (RawData) o;
        Map<Activity, List<URI>> linkedActivities
                = intermediate.getLinkedActivities();

        Map<URI, Set<Activity>> skosForActivities =
                new HashMap<URI, Set<Activity>>();

        for(Activity activity : linkedActivities.keySet()) {
            List<URI> resources = linkedActivities.get(activity);
            for(URI resource : resources) {
                List<URI> skoss;
                try {
                    skoss = skosResolver.getSkos(resource);
                } catch (SkosResolverException e) {
                    throw new ProfilingLineItemException("", e);
                }
                for(URI skos : skoss) {
                    addSkos(skos, activity, skosForActivities);
                }
            }
        }
        UnweightedInterests result = new UnweightedInterests(
                intermediate.getUsername(),
                skosForActivities
        );
        super.getNextProfilingLineItem().execute(result);
    }

    private void addSkos(
            URI skos,
            Activity activity,
            Map<URI, Set<Activity>> skosForActivities
    ) {
        if(skosForActivities.containsKey(skos)) {
            skosForActivities.get(skos).add(activity);
            return;
        }
        Set<Activity> activities = new HashSet<Activity>();
        activities.add(activity);
        skosForActivities.put(skos, activities);
    }
}
