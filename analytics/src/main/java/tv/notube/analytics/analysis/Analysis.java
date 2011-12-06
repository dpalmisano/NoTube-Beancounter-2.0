package tv.notube.analytics.analysis;

import tv.notube.commons.storage.model.Query;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public abstract class Analysis {

    private String name;

    private String description;

    protected Query query;

    public Analysis(String name, String description) {
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

    public void registerQuery(Query query) throws AnalysisException {
        this.query = query;
    }

    public Query getQuery() {
        return query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Analysis analysis = (Analysis) o;

        if (name != null ? !name.equals(analysis.name) : analysis.name != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Analysis{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", query=" + query +
                '}';
    }

    public abstract AnalysisResult run(String owner) throws AnalysisException;

    public abstract AnalysisDescription getAnalysisDescription();

}
