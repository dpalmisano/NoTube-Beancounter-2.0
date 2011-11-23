package tv.notube.commons.skos.service.mybatis.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface SkosMapper {

    public List<String> selectSkos(
            @Param("resource") String resource
    );

}
