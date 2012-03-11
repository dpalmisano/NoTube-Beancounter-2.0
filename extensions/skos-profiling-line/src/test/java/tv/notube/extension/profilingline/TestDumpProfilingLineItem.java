package tv.notube.extension.profilingline;

import org.apache.log4j.Logger;
import tv.notube.commons.model.Interest;
import tv.notube.commons.model.Type;
import tv.notube.commons.model.UserProfile;
import tv.notube.profiler.line.ProfilingLineItem;
import tv.notube.profiler.line.ProfilingLineItemException;

public class TestDumpProfilingLineItem extends ProfilingLineItem {

    private final Logger logger = Logger.getLogger(TestDumpProfilingLineItem.class);

    protected TestDumpProfilingLineItem(String name, String description) {
        super(name, description);
    }

    @Override
    public void execute(Object o) throws ProfilingLineItemException {
        UserProfile up = (UserProfile) o;
        logger.info("profile of: " + up.getUsername() + " done.");
        for (Type type : up.getTypes()) {
            logger.info("Type: " + type.toString());
        }
    }
}