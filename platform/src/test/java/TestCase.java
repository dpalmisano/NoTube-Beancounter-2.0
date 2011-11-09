import org.testng.annotations.Test;
import tv.notube.platform.LoaderInstanceManager;

/**
 * Created by IntelliJ IDEA.
 * User: davide
 * Date: 10/6/11
 * Time: 8:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestCase {

    @Test
    public void test() {
        LoaderInstanceManager.getInstance().getUserManager();
        LoaderInstanceManager.getInstance().getProfileStore();

    }

}
