package tv.notube.extension.profilingline;

import tv.notube.commons.alchemyapi.*;
import tv.notube.commons.model.activity.Activity;
import tv.notube.commons.model.activity.Tweet;
import tv.notube.extension.profilingline.tagdef.TagDef;
import tv.notube.extension.profilingline.tagdef.TagDefException;
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

    // TODO (high) this should be passed as configuration parameter.
    private static final String API_KEY = "04490000a72fe7ec5cb3497f14e77f338c86f2fe";

    private static String TWITTER = "http://twitter.com";

    private AlchemyAPI alchemyAPI;

    public TwitterLinkerProfilingLineItem(String name, String description) {
        super(name, description);
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
                    // get resources from tweet text
                    try {
                        resources = getResources(tweet.getText());
                    } catch (AlchemyAPIException e) {
                        throw new ProfilingLineItemException("Error while " +
                                "calling AlchemyAPI", e);
                    }
                    // get resources from tweet eventual urls
                    for(URL url : tweet.getUrls()) {
                        resources.addAll(getResources(url));
                    }
                    // get resources from tweet eventual hashtags
                    for(String hashTag : tweet.getHashTags()) {
                        resources.addAll(getResourcesFromHashTag(hashTag));
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

   private List<URI> getResourcesFromHashTag(String hashTag) throws ProfilingLineItemException {
        TagDef tagDef = new TagDef();
        List<String> defs;
        try {
            defs = tagDef.getDefinitions(hashTag);
        } catch (TagDefException e) {
            throw new ProfilingLineItemException(
                    "Error while accessing to TagDef for '" + hashTag + "'",
                    e
            );
        }
        List<URI> resources = new ArrayList<URI>();
        for (String def : defs) {
            AlchemyAPIResponse alchemyAPIResponse;
            try {
                alchemyAPIResponse = alchemyAPI.getNamedEntities(def);
            } catch (AlchemyAPIException e) {
                throw new ProfilingLineItemException("", e);
            }
            for (Identified identified : alchemyAPIResponse.getIdentified()) {
                NamedEntity namedEntity = (NamedEntity) identified;
                resources.add(namedEntity.getIdentifier());
            }
            try {
                alchemyAPIResponse = alchemyAPI.getRankedConcept(def);
            } catch (AlchemyAPIException e) {
                throw new ProfilingLineItemException("", e);
            }
            for (Identified identified : alchemyAPIResponse.getIdentified()) {
                Concept concept = (Concept) identified;
                resources.add(concept.getIdentifier());
            }
        }
        return resources;
    }

    private List<URI> getResources(URL url) throws ProfilingLineItemException {
        AlchemyAPIResponse alchemyAPIResponse;
        List<URI> resources = new ArrayList<URI>();
        try {
            alchemyAPIResponse = alchemyAPI.getNamedEntities(url);
        } catch (AlchemyAPIException e) {
            throw new ProfilingLineItemException("", e);
        }
        for(Identified identified : alchemyAPIResponse.getIdentified()) {
            NamedEntity namedEntity = (NamedEntity) identified;
            resources.add(namedEntity.getIdentifier());
        }
        try {
            alchemyAPIResponse = alchemyAPI.getRankedConcept(url);
        } catch (AlchemyAPIException e) {
            throw new ProfilingLineItemException("", e);
        }
        for(Identified identified : alchemyAPIResponse.getIdentified()) {
            Concept concept = (Concept) identified;
            resources.add(concept.getIdentifier());
        }
        return resources;
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
