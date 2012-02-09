package tv.notube.profiler;

import tv.notube.profiler.line.ProfilingLineItem;
import tv.notube.profiler.line.ProfilingLineItemException;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class FakeProfilingLineItem extends ProfilingLineItem {

    public FakeProfilingLineItem(String name, String description) {
        super(name, description);
    }

    @Override
    public void execute(Object input) throws ProfilingLineItemException {
        throw new UnsupportedOperationException("NIY");
    }
}
