package tv.notube.extension.profilingline;

import tv.notube.commons.model.User;
import tv.notube.profiler.line.ProfilingLineItem;
import tv.notube.profiler.line.ProfilingLineItemException;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class InitProfilingLineItem extends ProfilingLineItem {

    public InitProfilingLineItem(String name, String description) {
        super(name, description);
    }

    @Override
    public void execute(Object o) throws ProfilingLineItemException {
        User user = (User) o;
        RawData rd = new RawData(user.getUsername());
        rd.setActivities(user.getActivities());
        super.getNextProfilingLineItem().execute(rd);
    }
}
