package tv.notube.commons.alchemyapi;

import java.net.URI;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public abstract class Identified {

    private URI identifier;

    private float relevance;

    protected Identified(URI identifier, float relevance) {
        this.identifier = identifier;
        this.relevance = relevance;
    }

    public URI getIdentifier() {
        return identifier;
    }

    public float getRelevance() {
        return relevance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Identified that = (Identified) o;

        if (identifier != null ? !identifier.equals(that.identifier) : that.identifier != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return identifier != null ? identifier.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Identified{" +
                "identifier=" + identifier +
                ", relevance=" + relevance +
                '}';
    }
}
