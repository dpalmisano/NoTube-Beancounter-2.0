package tv.notube.profiler.container.defaults;

import tv.notube.profiler.line.ProfilingLineItem;
import tv.notube.profiler.line.ProfilingLineItemException;
import tv.notube.profiler.line.ProfilingResult;

/**
 * This {@link ProfilingLineItem} is the last
 * item of every pipeline by default. And it's able to instantiate a valid
 * {@link ProfilingResult}.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DumpProfilingLineContainerItem<T> extends ProfilingLineItem {

    private ProfilingResult result;

    private T inputType;

    public DumpProfilingLineContainerItem(String name, String description, T inputType) {
        super(name, description);
        this.inputType = inputType;
    }

    public void execute(Object input) throws ProfilingLineItemException {
        T value = (T) input;
        result = new ProfilingResult(value);
    }

    public ProfilingResult getProfile() {
        if(result == null)
            throw new IllegalStateException("Maybe the item has not been executed yet?");
        return result;
    }
}
