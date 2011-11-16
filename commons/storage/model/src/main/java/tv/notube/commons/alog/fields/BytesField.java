package tv.notube.commons.alog.fields;

/**
 * A {@link Field} implementation to handle
 * {@link java.io.Serializable} objects.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class BytesField extends Field<Bytes> {

    public BytesField(String name, Bytes value) {
        super(name, value);
    }
}
