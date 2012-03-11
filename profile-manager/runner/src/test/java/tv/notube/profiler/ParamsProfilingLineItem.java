package tv.notube.profiler;

import tv.notube.profiler.line.ProfilingLineItem;
import tv.notube.profiler.line.ProfilingLineItemException;

import java.util.Map;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ParamsProfilingLineItem extends ProfilingLineItem {

    public ParamsProfilingLineItem(
            String name,
            String description,
            Map<String, String> parameters
    ) {
        super(name, description, parameters);
    }

    @Override
    public void execute(Object input) throws ProfilingLineItemException {
        throw new UnsupportedOperationException("NIY");
    }
}
