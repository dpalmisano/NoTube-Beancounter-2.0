package tv.notube.commons.configuration.profiler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ProfilingLineItemDescription {

    private String name;

    private String description;

    private String clazz;

    private Map<String, String> parameters = new HashMap<String, String>();

    public ProfilingLineItemDescription(
            String name,
            String description,
            String clazz
    ) {
        this.name = name;
        this.description = description;
        this.clazz = clazz;
    }

    public ProfilingLineItemDescription(
            String name,
            String description,
            String clazz,
            Map<String, String> parameters
    ) {
        this(name, description, clazz);
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return "ProfilingLineItemDescription{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", clazz='" + clazz + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
