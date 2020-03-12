package build.dream.devops.mappers;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ServiceMapper {
    List<Map<String, Object>> listServiceNodes(@Param("serviceId") Long serviceId);

    Long deleteJavaOptions(@Param("serviceId") Long serviceId);

    Long updateServiceNodeStatus(@Param("status") Integer status, @Param("id") Long id);

    Long updateServiceNodePid(@Param("pid") String pid, @Param("id") Long id);

    List<Map<String, Object>> findAllServiceNodes();
}
