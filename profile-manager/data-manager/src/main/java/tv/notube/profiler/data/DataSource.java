package tv.notube.profiler.data;

/**
 * This interface models a generic source for data needed to feed
 * the profiler.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface DataSource {

    public void init() throws DataSourceException;

    public void dispose() throws DataSourceException;

    public RawDataSet getRawData() throws DataSourceException;

}
