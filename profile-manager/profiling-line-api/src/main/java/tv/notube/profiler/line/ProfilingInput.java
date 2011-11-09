package tv.notube.profiler.line;

/**
 * This object is responsible of wrapping the object to be fed
 * as input to the whole profiling process.
 * 
 * @author Davide Palmisano (dpalmisano@gmail.com)
 */
public class ProfilingInput<T> {

    private T value;

    public ProfilingInput(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ProfilingInput{" +
                "value=" + value +
                '}';
    }
}
