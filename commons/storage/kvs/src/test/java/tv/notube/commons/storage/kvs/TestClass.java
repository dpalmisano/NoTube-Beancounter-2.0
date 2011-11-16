package tv.notube.commons.storage.kvs;

import java.io.Serializable;
import java.net.URL;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class TestClass implements Serializable {

    private String string;

    private URL url;

    private boolean bool;

    private long millis;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public long getMillis() {
        return millis;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestClass)) return false;

        TestClass testClass = (TestClass) o;

        if (url != null ? !url.equals(testClass.url) : testClass.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TestClass{" +
                "string='" + string + '\'' +
                ", url=" + url +
                ", bool=" + bool +
                ", millis=" + millis +
                '}';
    }
}
