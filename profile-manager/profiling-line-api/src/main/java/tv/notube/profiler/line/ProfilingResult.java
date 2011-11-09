package tv.notube.profiler.line;

/**
 * This class wraps the result of the whole profiling process.
 * 
 * @author Davide Palmisano
 */
public class ProfilingResult<T> {

    private T value;

    public ProfilingResult(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ProfilingResult{" +
                "value=" + value +
                '}';
    }
}
