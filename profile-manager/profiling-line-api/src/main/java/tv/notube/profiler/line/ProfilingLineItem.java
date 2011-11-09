package tv.notube.profiler.line;

/**
 * This class represents a generic {@link ProfilingLine} item
 * able to manipulate an object.
 *
 * This class is intended to be extended to execute specific tasks.
 *  
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public abstract class ProfilingLineItem {

    private String name;

    private String description;

    protected ProfilingLineItem next;

    protected ProfilingLineItem(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setId(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNextProfilingLineItem(ProfilingLineItem profilingLineItem) {
        this.next = profilingLineItem;
    }

    public ProfilingLineItem getNextProfilingLineItem() {
        return next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProfilingLineItem that = (ProfilingLineItem) o;

        if (name != that.name) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "ProfilingLineItem{" +
                "name=" + name +
                ", description='" + description + '\'' +
                '}';
    }

    public abstract void execute(Object input)
            throws ProfilingLineItemException;

}
