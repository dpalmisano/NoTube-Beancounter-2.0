package tv.notube.commons.alog.mybatis.handlers;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

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
public class UUIDTypeHandler implements TypeHandler {

    public void setParameter(
            PreparedStatement preparedStatement,
            int i,
            Object o,
            JdbcType jdbcType
    ) throws SQLException {
        UUID uuid = (UUID) o;
        preparedStatement.setString(i, uuid.toString());
    }

    public Object getResult(ResultSet resultSet, String s) throws SQLException {
        return UUID.fromString(resultSet.getString(s));
    }

    public Object getResult(CallableStatement callableStatement, int i) throws SQLException {
        return UUID.fromString(callableStatement.getString(i));
    }
}
