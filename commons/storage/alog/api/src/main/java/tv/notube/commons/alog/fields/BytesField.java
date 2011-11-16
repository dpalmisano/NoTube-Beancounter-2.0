package tv.notube.commons.alog.fields;

import java.io.Serializable;

/**
 * A {@link Field} implementation to handle
 * {@link Serializable} objects.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class BytesField extends Field<Bytes> {

    public BytesField(String name, Bytes value) {
        super(name, value);
    }
}
