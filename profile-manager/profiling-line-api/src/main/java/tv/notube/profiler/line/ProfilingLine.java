package tv.notube.profiler.line;

/**
 * This class represents a generic line containing different
 * {@link ProfilingLineItem}s.
 *
 * Each {@link ProfilingLineItem} added
 * to the {@link ProfilingLine} must respect
 * the object type returned by the previous {@link ProfilingLineItem}.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public abstract class ProfilingLine {

    protected String name;

    protected String description;

    protected ProfilingLineItem profilingLineItem;

    protected ProfilingLine(String name, String description) {
        this.name = name;
        this.description = description;
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


    public void setProfilingLineItem(ProfilingLineItem profilingLineItem) {
        this.profilingLineItem = profilingLineItem;
    }

    /**
     *
     * @param profilingLineItem
     */
    public void enqueueProfilingLineItem(ProfilingLineItem profilingLineItem) {
        if(this.profilingLineItem == null)
            this.profilingLineItem = profilingLineItem;
        else
            getLastProfilingLineItem().setNextProfilingLineItem(profilingLineItem);        
    }

    public ProfilingLineItem getProfilingLineItem() {
        return profilingLineItem;
    }

    public ProfilingLineItem getLastProfilingLineItem() {
        return recurseOnItems(profilingLineItem, null);
    }

    private ProfilingLineItem recurseOnItems(
            ProfilingLineItem profilingLineItem,
            ProfilingLineItem actual) {
        if(profilingLineItem == null)
            return actual;
        return recurseOnItems(profilingLineItem.getNextProfilingLineItem(), profilingLineItem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProfilingLine that = (ProfilingLine) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ProfilingLine{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", profilingLineItem=" + profilingLineItem +
                '}';
    }

    /**
     * Runs the whole line calling the {@link ProfilingLineItem}
     * registered chain.
     *
     * @param profilingInput the {@link ProfilingInput} to be processed.
     * @return a not <i>null</i> {@link ProfilingResult} .
     * @throws ProfilingLineException raised if shomething goes wrong
     */
    public abstract void run(ProfilingInput profilingInput)
            throws ProfilingLineException;

}
