package tv.notube.commons.model.activity;

import com.google.gson.annotations.Expose;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Tweet extends tv.notube.commons.model.activity.Object {

    @Expose
    private String text;

    @Expose
    private Set<String> hashTags = new HashSet<String>();

    @Expose
    private List<URL> urls = new ArrayList<URL>();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<String> getHashTags() {
        return hashTags;
    }

    public void setHashTags(Set<String> hashTags) {
        this.hashTags = hashTags;
    }

    public List<URL> getUrls() {
        return urls;
    }

    public void setUrls(List<URL> urls) {
        this.urls = urls;
    }

    public boolean addHashTag(String s) {
        return hashTags.add(s);
    }

    public boolean addUrl(URL url) {
        return urls.add(url);
    }
}
