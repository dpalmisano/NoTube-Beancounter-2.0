package tv.notube.usermanager.fields;

import tv.notube.kvs.storage.Field;

/**
 * @author Davide Palmisano (dpalmisano@gmail.com)
 */
public class Username extends Field {

    public static final String NAME = "username";

    public Username(String username) {
        setName(NAME);
        setValue(username);
    }

}
