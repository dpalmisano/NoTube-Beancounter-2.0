package tv.notube.extension.profilingline;

import tv.notube.commons.alchemyapi.*;
import tv.notube.commons.model.activity.Activity;
import tv.notube.commons.model.activity.Tweet;
import tv.notube.extension.profilingline.lupedia.DefaultLupediaImpl;
import tv.notube.extension.profilingline.lupedia.Lupedia;
import tv.notube.profiler.line.ProfilingLineItem;
import tv.notube.profiler.line.ProfilingLineItemException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class TwitterLinkerProfilingLineItem extends ProfilingLineItem {

    private static final String API_KEY = "04490000a72fe7ec5cb3497f14e77f338c86f2fe";

    private static String TWITTER = "http://twitter.com";

    private Lupedia lupedia;

    private AlchemyAPI alchemyAPI;

    public TwitterLinkerProfilingLineItem(String name, String description) {
        super(name, description);
        lupedia = new DefaultLupediaImpl();
        alchemyAPI = new AlchemyAPI(API_KEY);
    }

    @Override
    public void execute(java.lang.Object o) throws ProfilingLineItemException {
        RawData intermediate = (RawData) o;
        if(intermediate.getActivities().size() == 0) {
            // just push the object down, there's nothing to profile here
            super.getNextProfilingLineItem().execute(intermediate);
            return;
        }
        List<Activity> activities = intermediate.getActivities();
        List<Activity> activitiesToBeRemoved = new ArrayList<Activity>();
        for (Activity activity : activities) {
            try {
                if (activity.getContext().getService().equals(new URL(TWITTER))) {
                    Tweet tweet = (Tweet) activity.getObject();
                    List<URI> resources;
                    try {
                        resources = getResources(tweet.getText());
                    } catch (AlchemyAPIException e) {
                        throw new ProfilingLineItemException("Error while " +
                                "calling AlchemyAPI", e);
                    }
                    intermediate.addLinkedActivity(activity, resources);
                    activitiesToBeRemoved.add(activity);
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException("URL '" + TWITTER + "' is not well formed", e);
            }
        }
        for(Activity activity : activitiesToBeRemoved) {
            intermediate.removeActivity(activity);
        }
        super.getNextProfilingLineItem().execute(intermediate);
    }

    private List<URI> getResources(String text) throws AlchemyAPIException {
        List<URI> result = new ArrayList<URI>();
        AlchemyAPIResponse alchemyAPIResponse;
        alchemyAPIResponse = alchemyAPI.getNamedEntities(text);
        List<Identified> identifieds = alchemyAPIResponse.getIdentified();
        for (Identified identified : identifieds) {
            NamedEntity namedEntity = (NamedEntity) identified;
            result.add(namedEntity.getIdentifier());
        }
        alchemyAPIResponse = alchemyAPI.getRankedConcept(text);
        identifieds = alchemyAPIResponse.getIdentified();
        for (Identified identified : identifieds) {
            Concept concept = (Concept) identified;
            result.add(concept.getIdentifier());
        }
        return result;
    }


}
