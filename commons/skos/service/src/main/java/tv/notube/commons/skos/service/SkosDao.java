package tv.notube.commons.skos.service;

import org.apache.ibatis.session.SqlSession;
import tv.notube.commons.skos.service.mybatis.ConfigurableDao;
import tv.notube.commons.skos.service.mybatis.ConnectionFactory;
import tv.notube.commons.skos.service.mybatis.mapper.SkosMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SkosDao extends ConfigurableDao {

    public SkosDao(Properties properties) {
        super(properties);
    }

    public List<String> selectSkosByResource(String resource) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        SkosMapper mapper = session.getMapper(SkosMapper.class);
        List<String> resourceNames;
        try {
            resourceNames = mapper.selectSkos(resource);
        } finally {
            session.close();
        }
        return resourceNames;
    }

}
