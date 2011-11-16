package tv.notube.commons.alog.fields;

import java.net.URL;

/**
 * A {@link Field} implementation to handle {@link java.net.URL}.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class URLField extends Field<URL> {

    public URLField(String name, URL value) {
        super(name, value);
    }
}
