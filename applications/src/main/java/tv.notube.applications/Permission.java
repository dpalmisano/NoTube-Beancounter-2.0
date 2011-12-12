package tv.notube.applications;

import java.io.Serializable;
import java.util.UUID;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Permission implements Serializable {

    static final long serialVersionUID = 182012396185837510L;

    public enum Action {
        CREATE,
        RETRIEVE,
        UPDATE,
        DELETE
    }

    private UUID resource;

    private boolean permissions[] = { false, false, false, false };

    public Permission(UUID resource) {
        this.resource = resource;
    }

    public UUID getResource() {
        return resource;
    }

    public void setPermission(Action action, boolean b) {
        if(action.equals(Action.CREATE)) {
            permissions[0] = b;
        }
        if(action.equals(Action.RETRIEVE)) {
            permissions[1] = b;
        }
        if(action.equals(Action.UPDATE)) {
            permissions[2] = b;
        }
        if(action.equals(Action.DELETE)) {
            permissions[3] = b;
        }
    }

    public boolean getPermission(Action action) {
        if(action.equals(Action.CREATE)) {
            return permissions[0];
        }
        if(action.equals(Action.RETRIEVE)) {
            return permissions[1];
        }
        if(action.equals(Action.UPDATE)) {
            return permissions[2];
        }
        if(action.equals(Action.DELETE)) {
            return permissions[3];
        }
        throw new IllegalStateException(
                "Action '" + action + "' not supported"
        );
    }

}
