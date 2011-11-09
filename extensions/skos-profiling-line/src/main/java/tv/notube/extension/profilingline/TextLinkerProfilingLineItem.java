package tv.notube.extension.profilingline;

import tv.notube.commons.model.activity.Activity;
import tv.notube.commons.model.activity.Tweet;
import tv.notube.extension.profilingline.lupedia.DefaultLupediaImpl;
import tv.notube.extension.profilingline.lupedia.Lupedia;
import tv.notube.extension.profilingline.lupedia.LupediaException;
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
public class TextLinkerProfilingLineItem extends ProfilingLineItem {

    private static String TWITTER = "http://twitter.com";

    private Lupedia lupedia;

    public TextLinkerProfilingLineItem(String name, String description) {
        super(name, description);
        lupedia = new DefaultLupediaImpl();
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
                        resources = lupedia.getResources(tweet.getText());
                    } catch (LupediaException e) {
                        throw new ProfilingLineItemException("", e);
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


}
