package tv.notube.usermanager.fields;

import org.joda.time.DateTime;
import tv.notube.kvs.storage.Field;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Author extends Field {

    private static final String NAME = "author";

    public Author(String author) {
        setName(NAME);
        setValue(author);
    }

}
