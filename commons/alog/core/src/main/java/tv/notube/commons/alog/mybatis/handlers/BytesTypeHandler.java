package tv.notube.commons.alog.mybatis.handlers;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import tv.notube.commons.alog.fields.Bytes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class BytesTypeHandler implements TypeHandler {

    public void setParameter(
            PreparedStatement preparedStatement,
            int i,
            Object o,
            JdbcType jdbcType
    ) throws SQLException {
        Bytes bytes = (Bytes) o;
        preparedStatement.setBytes(i, bytes.getBytes());
    }

    public Object getResult(ResultSet resultSet, String s) throws SQLException {
        Bytes bytes = new Bytes();
        bytes.setBytes(resultSet.getBytes(s));
        return bytes;
    }

    public Object getResult(CallableStatement callableStatement, int i) throws SQLException {
        Bytes bytes = new Bytes();
        bytes.setBytes(callableStatement.getBytes(i));
        return bytes;
    }
}
