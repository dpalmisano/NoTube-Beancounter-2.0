package tv.notube.commons.skos.service.mybatis;

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
import tv.notube.commons.skos.service.mybatis.mapper.SkosMapper;

import java.util.Properties;

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
            configuration.addMapper(SkosMapper.class);
            configuration.getTypeHandlerRegistry().register(JdbcType.BLOB, new ByteArrayTypeHandler());
            sqlMapper = new SqlSessionFactoryBuilder().build(configuration);
        }
        logger.info("SqlSession correctly instantiated");
        return sqlMapper;
    }
}