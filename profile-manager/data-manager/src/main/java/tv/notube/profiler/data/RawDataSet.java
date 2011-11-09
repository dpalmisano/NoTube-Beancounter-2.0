package tv.notube.profiler.data;

import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class RawDataSet<T> {

    private List<T> items;

    private int index;

    public RawDataSet(List<T> items) {
        this.items = items;
        index = 0;
    }

    public synchronized boolean hasNext() {
        if(index < items.size())
            return true;
        return false;
    }

    public synchronized T getNext() {
        if(index >= items.size())
            return null;
        T item = items.get(index);
        index++;
        return item;
    }

    public synchronized int size() {
        return items.size();
    }

}
