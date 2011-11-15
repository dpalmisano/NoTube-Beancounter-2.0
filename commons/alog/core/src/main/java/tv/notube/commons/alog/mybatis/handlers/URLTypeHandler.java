package tv.notube.commons.alog.mybatis.handlers;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class URLTypeHandler implements TypeHandler {

    public void setParameter(
            PreparedStatement preparedStatement,
            int i,
            Object o,
            JdbcType jdbcType
    ) throws SQLException {
        URL url = (URL) o;
        preparedStatement.setString(i, url.toString());
    }

    public Object getResult(ResultSet resultSet, String s) throws SQLException {
        try {
            return new URL(resultSet.getString(s));
        } catch (MalformedURLException e) {
            throw new SQLException("Value '" + s + "' doesn't seems a valid " +
                    "URL", e);
        }
    }

    public Object getResult(CallableStatement callableStatement, int i) throws SQLException {
        try {
            return new URL(callableStatement.getString(i));
        } catch (MalformedURLException e) {
            throw new SQLException("Value '" + i + "' doesn't seems a valid " +
                    "URL", e);

        }
    }

}
