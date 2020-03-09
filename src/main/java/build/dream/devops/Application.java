package build.dream.devops;

import build.dream.devops.utils.JSchUtils;
import com.jcraft.jsch.Session;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@ServletComponentScan
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@MapperScan(basePackages = {"build.dream.common.mappers", "build.dream.devops.mappers"})
public class Application {
    public static void main(String[] args) {
//        SpringApplication.run(Application.class, args);

        String command = "nohup java -jar /usr/local/development/services/service-gateway-0.1.0.jar > /usr/local/development/services/console.log 2>&1 & echo $!";

        Session session = null;
        try {
            session = JSchUtils.createSession("root", "root", "192.168.1.11", 22);
            boolean result = JSchUtils.processExists(session, "14703");
            System.out.println(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JSchUtils.disconnectSession(session);
        }
    }
}