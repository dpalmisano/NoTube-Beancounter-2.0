package tv.notube.extension.profilingline.lupedia.http.json;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LupediaEntry {

    private int startOffset;

    private int endOffset;

    private String instanceUri;

    private String instanceClass;

    private String predicateUri;

    private float weight;

    public int getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
    }

    public String getInstanceUri() {
        return instanceUri;
    }

    public void setInstanceUri(String instanceUri) {
        this.instanceUri = instanceUri;
    }

    public String getInstanceClass() {
        return instanceClass;
    }

    public void setInstanceClass(String instanceClass) {
        this.instanceClass = instanceClass;
    }

    public String getPredicateUri() {
        return predicateUri;
    }

    public void setPredicateUri(String predicateUri) {
        this.predicateUri = predicateUri;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "LupediaEntry{" +
                "startOffset=" + startOffset +
                ", endOffset=" + endOffset +
                ", instanceUri='" + instanceUri + '\'' +
                ", instanceClass='" + instanceClass + '\'' +
                ", predicateUri='" + predicateUri + '\'' +
                ", weight=" + weight +
                '}';
    }
}
