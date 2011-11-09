package tv.notube.crawler.requester.request.lastfm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LastFmRecentTracksResponse {

    private List<LastFmTrack> tracks = new ArrayList<LastFmTrack>();

    public List<LastFmTrack> getTracks() {
        return tracks;
    }

    public void setTracks(List<LastFmTrack> tracks) {
        this.tracks = tracks;
    }

    public boolean addTrack(LastFmTrack track) {
        return tracks.add(track);
    }

}


