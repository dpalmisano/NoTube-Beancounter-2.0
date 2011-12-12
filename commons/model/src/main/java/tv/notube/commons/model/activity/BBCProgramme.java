package tv.notube.commons.model.activity;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class BBCProgramme extends tv.notube.commons.model.activity.Object {

    private URL picture;

    private List<String> genres = new ArrayList<String>();

    private List<String> actors = new ArrayList<String>();

    private String mediumSynopsis;

    public URL getPicture() {
        return picture;
    }

    public void addGenre(String genre) {
        genres.add(genre);
    }

    public void addActor(String actor) {
        genres.add(actor);
    }

    public void setPicture(URL picture) {
        this.picture = picture;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public String getMediumSynopsis() {
        return mediumSynopsis;
    }

    public void setMediumSynopsis(String mediumSynopsis) {
        this.mediumSynopsis = mediumSynopsis;
    }
}
