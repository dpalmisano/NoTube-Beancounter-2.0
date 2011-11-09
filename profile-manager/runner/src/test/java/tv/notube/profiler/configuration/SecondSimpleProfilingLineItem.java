package tv.notube.profiler.configuration;

import tv.notube.profiler.line.ProfilingLineItem;
import tv.notube.profiler.line.ProfilingLineItemException;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SecondSimpleProfilingLineItem extends ProfilingLineItem {

    public SecondSimpleProfilingLineItem(String name, String description) {
        super(name, description);
    }

    @Override
    public void execute(Object input) throws ProfilingLineItemException {
        // noop
    }
}
