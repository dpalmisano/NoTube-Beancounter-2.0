package tv.notube.usermanager.fields;

import org.joda.time.DateTime;
import tv.notube.kvs.storage.Field;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LastWrite extends Field {

    public static final String NAME = "last_write";

    public LastWrite() {
        setName(NAME);
        setValue(Long.toString(System.currentTimeMillis()));
    }

    public LastWrite(DateTime dateTime) {
        setName(NAME);
        setValue(Long.toString(dateTime.toInstant().getMillis()));
    }

}
