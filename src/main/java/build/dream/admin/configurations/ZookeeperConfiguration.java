package build.dream.admin.configurations;

import build.dream.admin.constants.Constants;
import build.dream.common.utils.ConfigurationUtils;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.UUID;

@Configuration
public class ZookeeperConfiguration {
    @Bean
    public ZooKeeper zooKeeper() throws IOException {
        String zookeeperConnectionString = ConfigurationUtils.getConfiguration(Constants.ZOOKEEPER_CONNECTION_STRING);
        int sessionTimeout = 30000;
        ZooKeeper zooKeeper = new ZooKeeper(zookeeperConnectionString, sessionTimeout, event -> {
            System.out.println(UUID.randomUUID().toString());
            System.out.println(UUID.randomUUID().toString());
        });
        return zooKeeper;
    }
}
