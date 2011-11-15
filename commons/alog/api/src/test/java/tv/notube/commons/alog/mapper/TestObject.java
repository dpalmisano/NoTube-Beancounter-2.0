package tv.notube.commons.alog.mapper;

import org.joda.time.DateTime;

import java.net.URL;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class TestObject {

    private String string;

    private DateTime datetime = new DateTime();

    private URL url;

    private int integer;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public DateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(DateTime dateTime) {
        this.datetime = dateTime;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public int getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestObject that = (TestObject) o;

        if (integer != that.integer) return false;
        if (datetime != null ? !datetime.equals(that.datetime) : that.datetime != null)
            return false;
        if (string != null ? !string.equals(that.string) : that.string != null)
            return false;
        if (url != null ? !url.equals(that.url) : that.url != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = string != null ? string.hashCode() : 0;
        result = 31 * result + (datetime != null ? datetime.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + integer;
        return result;
    }
}
