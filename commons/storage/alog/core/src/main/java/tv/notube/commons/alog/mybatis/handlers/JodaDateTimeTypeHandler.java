package tv.notube.commons.alog.mybatis.handlers;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.joda.time.DateTime;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class JodaDateTimeTypeHandler implements TypeHandler {

    public void setParameter(
            PreparedStatement preparedStatement,
            int i,
            Object o,
            JdbcType jdbcType
    ) throws SQLException {
        DateTime dateTime = (DateTime) o;
        preparedStatement.setLong(i, dateTime.getMillis());
    }

    public Object getResult(ResultSet resultSet, String s) throws SQLException {
        return new DateTime(resultSet.getLong(s));
    }

    public Object getResult(CallableStatement callableStatement, int i)
            throws SQLException {
        return new DateTime(callableStatement.getLong(i));
    }
}
