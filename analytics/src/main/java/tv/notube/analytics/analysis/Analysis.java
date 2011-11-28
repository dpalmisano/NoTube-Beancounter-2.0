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

    public void registerQuery(Query query) throws AnalysisException {
        this.query = query;
    }

    public abstract AnalysisResult run(String owner) throws AnalysisException;


}
