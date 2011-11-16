package tv.notube.commons.storage.model.fields;

import org.joda.time.DateTime;

/**
 * {@link Field} implementation to handle {@link DateTime} objects.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DatetimeField extends Field<DateTime> {

    public DatetimeField(String name, DateTime value) {
        super(name, value);
    }
}
