package tv.notube.analytics.analysis;

import tv.notube.commons.storage.model.Query;

import java.io.Serializable;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class AnalysisDescription implements Serializable {

    public String name;

    public String description;

    private Query query;

    private String className;

    public AnalysisDescription(
            String name,
            String description,
            Query query,
            String className
    ) {
        this.name = name;
        this.description = description;
        this.query = query;
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Query getQuery() {
        return query;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnalysisDescription that = (AnalysisDescription) o;

        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
