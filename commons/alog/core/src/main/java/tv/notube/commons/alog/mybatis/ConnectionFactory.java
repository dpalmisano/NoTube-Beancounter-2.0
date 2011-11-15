package tv.notube.commons.alog.mybatis;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.ByteArrayTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import tv.notube.commons.alog.fields.Bytes;
import tv.notube.commons.alog.mybatis.handlers.JodaDateTimeTypeHandler;
import tv.notube.commons.alog.mybatis.handlers.URLTypeHandler;
import tv.notube.commons.alog.mybatis.handlers.UUIDTypeHandler;
import tv.notube.commons.alog.mybatis.mapper.ActivityLogMapper;

import java.net.URL;
import java.util.Properties;
import java.util.UUID;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ConnectionFactory {

    private static Logger logger = Logger.getLogger(ConnectionFactory.class);

    private static SqlSessionFactory sqlMapper;

    public static SqlSessionFactory getSession(Properties properties) {
        if (sqlMapper == null) {
            PooledDataSource pds = new PooledDataSource(
                    "com.mysql.jdbc.Driver",
                    properties.getProperty("url"),
                    properties.getProperty("username"),
                    properties.getProperty("password")
            );
            TransactionFactory transactionFactory = new JdbcTransactionFactory();
            Environment environment =
                    new Environment("development", transactionFactory, pds);
            Configuration configuration = new Configuration(environment);
            configuration.addMapper(ActivityLogMapper.class);
            configuration.getTypeHandlerRegistry().register(DateTime.class,
                    new JodaDateTimeTypeHandler());
            configuration.getTypeHandlerRegistry().register(UUID.class,
                    new UUIDTypeHandler());
            configuration.getTypeHandlerRegistry().register(URL.class,
                    new URLTypeHandler());
            configuration.getTypeHandlerRegistry().register(
                    JdbcType.BLOB,
                    new ByteArrayTypeHandler());
            sqlMapper = new SqlSessionFactoryBuilder().build(configuration);
        }
        logger.info("SqlSession correctly instantiated");
        return sqlMapper;
    }
}