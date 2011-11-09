package tv.notube.usermanager.services.auth;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class AuthHandlerException extends Exception {

    public AuthHandlerException(String message, Exception e) {
        super(message, e);
    }
}
