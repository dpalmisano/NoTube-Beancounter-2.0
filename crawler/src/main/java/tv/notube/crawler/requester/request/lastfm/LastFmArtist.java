package tv.notube.crawler.requester.request.lastfm;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LastFmArtist {

    private String name;

    private String mbid;

    public LastFmArtist(String name, String mbid) {
        this.name = name;
        this.mbid = mbid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    @Override
    public String toString() {
        return "LastFmArtist{" +
                "name='" + name + '\'' +
                ", mbid='" + mbid + '\'' +
                '}';
    }
}
