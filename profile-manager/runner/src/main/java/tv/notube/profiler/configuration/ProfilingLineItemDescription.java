package tv.notube.profiler.configuration;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ProfilingLineItemDescription {

    private String name;

    private String description;

    private String clazz;

    public ProfilingLineItemDescription(
            String name,
            String description,
            String clazz
    ) {
        this.name = name;
        this.description = description;
        this.clazz = clazz;
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
}
