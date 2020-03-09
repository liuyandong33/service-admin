package build.dream.devops.mappers;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ServiceMapper {
    List<Map<String, Object>> listServiceNodes(@Param("serviceId") Long serviceId);

    Long deleteJavaOperations(@Param("serviceId") Long serviceId);

    Long deleteServiceConfigurations(@Param("serviceId") Long serviceId);

    List<Map<String, Object>> findAllServiceNodes();

    Long updateServiceNodeStatusAndPid(@Param("status") Integer status, @Param("pid") String pid, @Param("id") Long id);
}