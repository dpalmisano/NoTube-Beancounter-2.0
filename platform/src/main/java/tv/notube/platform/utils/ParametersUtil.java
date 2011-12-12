package tv.notube.platform.utils;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ParametersUtil {

    public static void check(Object... signature) {
        int i = 0;
        for(Object o : signature) {
            if(o == null) {
                throw new RuntimeException(
                        "Error: parameter '" + i + "' cannot be null"
                );
            }
            i++;
        }
    }

    public static void check(String... signature) {
        int i = 0;
        for(String s : signature) {
            if(s == null) {
                throw new RuntimeException(
                        "Error: parameter '" + i + "' cannot be null"
                );
            }
            if(s.length() == 0) {
                throw new RuntimeException(
                        "Error: parameter '" + i + "' cannot be an empty String"
                );
            }
            i++;
        }
    }

}
