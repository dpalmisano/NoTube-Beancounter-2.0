package tv.notube.profiler.container;

import tv.notube.profiler.line.ProfilingInput;
import tv.notube.profiler.line.ProfilingLine;
import tv.notube.profiler.line.ProfilingResult;

import java.util.Set;

/**
 * This interface defines the main behavior of a
 * {@link ProfilingLine} container.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface ProfilingLineContainer {

    public Set getProfilingLineNames();

    public int getNumberOfProfilingLines();

    public void addProfilingLine(ProfilingLine profilingLine)
            throws ProfilingLineContainerException;

    public void removeProfilingLine(String profilingLineName)
            throws ProfilingLineContainerException;

    public ProfilingLine getProfilingLine(String profilingLineName)
        throws ProfilingLineContainerException;

    public ProfilingResult profile(ProfilingInput profilingInput, String profilingLineName)
            throws ProfilingLineContainerException;
    
}
