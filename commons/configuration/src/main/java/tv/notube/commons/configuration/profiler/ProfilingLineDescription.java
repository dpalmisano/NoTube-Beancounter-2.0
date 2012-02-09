package tv.notube.commons.configuration.profiler;

import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ProfilingLineDescription {

    private String name;

    private String description;

    private String clazz;

    private ProfilingLineItemDescription[] pliDescriptions;

    public ProfilingLineDescription(
            String name,
            String description,
            String clazz,
            List<ProfilingLineItemDescription> pliDescriptions
    ) {
        this.name = name;
        this.description = description;
        this.clazz = clazz;
        this.pliDescriptions = pliDescriptions.toArray(
                new ProfilingLineItemDescription[pliDescriptions.size()]
        );
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

    public ProfilingLineItemDescription[] getPliDescriptions() {
        return pliDescriptions;
    }

    public void setPliDescriptions(ProfilingLineItemDescription[] pliDescriptions) {
        this.pliDescriptions = pliDescriptions;
    }
}