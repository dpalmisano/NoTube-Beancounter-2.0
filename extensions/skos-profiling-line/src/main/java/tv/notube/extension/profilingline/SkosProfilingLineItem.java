package tv.notube.extension.profilingline;

import tv.notube.commons.model.activity.Activity;
import tv.notube.extension.profilingline.skos.SkosResolver;
import tv.notube.extension.profilingline.skos.SkosResolverException;
import tv.notube.profiler.line.ProfilingLineItem;
import tv.notube.profiler.line.ProfilingLineItemException;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SkosProfilingLineItem extends ProfilingLineItem {

    private SkosResolver skosResolver;

    public SkosProfilingLineItem(String name, String description) {
        super(name, description);
        skosResolver = new SkosResolver();
    }

    public void execute(Object o) throws ProfilingLineItemException {
        RawData intermediate = (RawData) o;
        Map<Activity, List<URI>> linkedActivities
                = intermediate.getLinkedActivities();

        Map<URI, List<Activity>> skosForActivities =
                new HashMap<URI, List<Activity>>();

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
            Map<URI, List<Activity>> skosForActivities
    ) {
        if(skosForActivities.containsKey(skos)) {
            skosForActivities.get(skos).add(activity);
            return;
        }
        List<Activity> activities = new ArrayList<Activity>();
        activities.add(activity);
        skosForActivities.put(skos, activities);
    }
}