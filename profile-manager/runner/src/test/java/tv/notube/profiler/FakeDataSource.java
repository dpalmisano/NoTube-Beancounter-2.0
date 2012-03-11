package tv.notube.profiler;

import tv.notube.profiler.data.DataSource;
import tv.notube.profiler.data.DataSourceException;
import tv.notube.profiler.data.RawDataSet;

import java.util.UUID;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class FakeDataSource implements DataSource{

    public void init() throws DataSourceException {
        throw new UnsupportedOperationException("NIY");
    }

    public void dispose() throws DataSourceException {
        throw new UnsupportedOperationException("NIY");
    }

    public RawDataSet getRawData() throws DataSourceException {
        throw new UnsupportedOperationException("NIY");
    }

    @Override
    public RawDataSet getRawData(UUID userId) throws DataSourceException {
        throw new UnsupportedOperationException("NIY");
    }
}
