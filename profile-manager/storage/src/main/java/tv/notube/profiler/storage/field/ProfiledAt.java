package tv.notube.profiler.storage.field;

import org.joda.time.DateTime;
import tv.notube.kvs.storage.Field;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ProfiledAt extends Field {

    public static final String NAME = "profiled_at";

    public ProfiledAt() {
        setName(NAME);
        setValue(Long.toString(System.currentTimeMillis()));
    }

    public ProfiledAt(DateTime dateTime) {
        setName(NAME);
        setValue(Long.toString(dateTime.toInstant().getMillis()));
    }

}
