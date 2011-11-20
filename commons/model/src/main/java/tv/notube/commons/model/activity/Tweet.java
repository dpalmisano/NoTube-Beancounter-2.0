package tv.notube.commons.model.activity;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Tweet extends tv.notube.commons.model.activity.Object {

    private String text;

    private List<String> hashTags = new ArrayList<String>();

    private List<URL> urls = new ArrayList<URL>();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getHashTags() {
        return hashTags;
    }

    public void setHashTags(List<String> hashTags) {
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
