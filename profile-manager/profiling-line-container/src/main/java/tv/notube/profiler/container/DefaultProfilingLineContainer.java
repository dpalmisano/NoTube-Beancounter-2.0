package tv.notube.profiler.container;

import tv.notube.profiler.container.defaults.DumpProfilingLineContainerItem;
import org.apache.log4j.Logger;
import tv.notube.profiler.line.ProfilingInput;
import tv.notube.profiler.line.ProfilingLine;
import tv.notube.profiler.line.ProfilingLineException;
import tv.notube.profiler.line.ProfilingResult;

import java.util.*;

/**
 * Default implementation of {@link ProfilingLineContainer}.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DefaultProfilingLineContainer implements ProfilingLineContainer {

    private static Logger logger = Logger.getLogger(DefaultProfilingLineContainer.class);

    private List<ProfilingLine> lines = new ArrayList<ProfilingLine>();

    private Map<String, DumpProfilingLineContainerItem> dumpers =
            new HashMap<String, DumpProfilingLineContainerItem>();

    public Set getProfilingLineNames() {
        Set result = new HashSet<String>();
        for(ProfilingLine profilingLine : lines) {
            result.add(profilingLine.getName());
        }
        return result;
    }

    public int getNumberOfProfilingLines() {
        return lines.size();
    }

    public void addProfilingLine(ProfilingLine profilingLine)
            throws ProfilingLineContainerException {
        DumpProfilingLineContainerItem dumper =
                new DumpProfilingLineContainerItem("dumper-item", "dumper item", null);
        profilingLine.getLastProfilingLineItem().setNextProfilingLineItem(dumper);
        dumpers.put(profilingLine.getName(), dumper);
        lines.add(profilingLine);
    }

    public void removeProfilingLine(String profilingLineName)
            throws ProfilingLineContainerException {
        lines.remove(profilingLineName);
    }

    public ProfilingResult profile(ProfilingInput profilingInput, String profilingLineName)
            throws ProfilingLineContainerException {
        ProfilingLine profilingLine = getProfilingLine(profilingLineName);
        DumpProfilingLineContainerItem dump;
        try {
            dump = dumpers.get(profilingLine.getName());
            logger.info("Profiling started on line: '" + profilingLineName + "'");
            profilingLine.run(profilingInput);
        } catch (ProfilingLineException e) {
            final String errMsg = "Profiling started on line: '" + profilingLineName + "'";
            logger.error(errMsg, e);
            throw new ProfilingLineContainerException(errMsg, e);
        }
        return dump.getProfile();
    }

    public ProfilingLine getProfilingLine(String profilingLineName)
            throws ProfilingLineContainerException {
        for(ProfilingLine profilingLine : lines) {
            if(profilingLine.getName().equals(profilingLineName))
                return profilingLine;
        }
        final String errMsg = "ProfilingLine: '" + profilingLineName + "' not found";
        logger.error(errMsg);
        throw new ProfilingLineContainerException(errMsg);
    }
    
}
