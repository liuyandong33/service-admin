package build.dream.admin.mappers;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ServiceMapper {
    List<Map<String, Object>> listServiceNodes(@Param("serviceId") Long serviceId);
}
