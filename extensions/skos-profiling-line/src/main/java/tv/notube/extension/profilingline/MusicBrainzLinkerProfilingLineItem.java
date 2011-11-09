package tv.notube.extension.profilingline;

import tv.notube.commons.model.activity.Activity;
import tv.notube.commons.model.activity.Song;
import tv.notube.extension.profilingline.musicbrainz.MusicBrainzLookup;
import tv.notube.extension.profilingline.musicbrainz.MusicBrainzLooupException;
import tv.notube.extension.profilingline.musicbrainz.MusicBrainzNotResolvableException;
import tv.notube.profiler.line.ProfilingLineItem;
import tv.notube.profiler.line.ProfilingLineItemException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MusicBrainzLinkerProfilingLineItem extends ProfilingLineItem {

    private static final String LASTFM = "http://last.fm";

    private MusicBrainzLookup musicBrainz;

    public MusicBrainzLinkerProfilingLineItem(String name, String description) {
        super(name, description);
        musicBrainz = new MusicBrainzLookup();
    }

    @Override
    public void execute(Object o) throws ProfilingLineItemException {
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
                if (activity.getContext().getService().equals(new URL(LASTFM))) {
                    Song song = (Song) activity.getObject();
                    URI resource;
                    try {
                        resource = musicBrainz.resolve(song.getMbid());
                    } catch (MusicBrainzNotResolvableException e) {
                        // just skip this activity
                        continue;
                    } catch (MusicBrainzLooupException e) {
                        // just skip this activity
                        continue;
                    }
                    intermediate.addLinkedActivity(activity, resource);
                    activitiesToBeRemoved.add(activity);
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException("URL '" + LASTFM + "' is not well formed", e);
            }
        }
        for(Activity activity : activitiesToBeRemoved) {
            intermediate.removeActivity(activity);
        }
        super.getNextProfilingLineItem().execute(intermediate);
    }
}
