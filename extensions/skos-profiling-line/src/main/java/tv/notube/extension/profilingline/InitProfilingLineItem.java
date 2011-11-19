package tv.notube.extension.profilingline;

import tv.notube.commons.model.UserActivities;
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
        UserActivities userActivities = (UserActivities) o;
        RawData rd = new RawData(userActivities.getUsername());
        rd.setActivities(userActivities.getActivities());
        super.getNextProfilingLineItem().execute(rd);
    }
}
