package tv.notube.crawler.requester.request.lastfm;

import org.joda.time.DateTime;

import java.net.URL;

public class LastFmTrack {

    private LastFmArtist artist;

    private String name;

    private URL url;

    private DateTime date;

    public LastFmArtist getArtist() {
        return artist;
    }

    public void setArtist(LastFmArtist artist) {
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LastFmTrack)) return false;

        LastFmTrack that = (LastFmTrack) o;

        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "LastFmTrack{" +
                "artist=" + artist +
                ", name='" + name + '\'' +
                ", url=" + url +
                ", date=" + date +
                '}';
    }
}
