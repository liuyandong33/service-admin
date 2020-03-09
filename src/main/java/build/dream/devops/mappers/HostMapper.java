package build.dream.devops.mappers;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface HostMapper {
    List<Map<String, Object>> listHosts(@Param("offset") Integer offset, @Param("maxResults") Integer maxResults);

    Long countHosts();
}
