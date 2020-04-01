package build.dream.devops.configurations;

import build.dream.devops.jdbc.RoutingDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfiguration {
    /*@Bean
    @ConfigurationProperties(prefix = "datasource.druid")
    public DataSource druidDataSource() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }*/

    @Bean
    @ConfigurationProperties(prefix = "datasource.hikari.devops")
    public DataSource hikariDevopsDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.hikari.test-zd1-catering-log")
    public DataSource hikariTestZd1CateringLogDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.hikari.beta-zd1-catering-log")
    public DataSource hikariBetaZd1CateringLogDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.hikari.production-zd1-catering-log")
    public DataSource hikariProductionZd1CateringLogDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.hikari.test-platform-log")
    public DataSource hikariTestPlatformLogDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.hikari.beta-platform-log")
    public DataSource hikariBetaPlatformLogDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.hikari.production-platform-log")
    public DataSource hikariProductionPlatformLogDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.hikari.test-devops-log")
    public DataSource hikariTestDevopsLogDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.hikari.beta-devops-log")
    public DataSource hikariBetaDevopsLogDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.hikari.production-devops-log")
    public DataSource hikariProductionDevopsLogDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "routingDataSource")
    public DataSource routingDataSource() {
        DataSource defaultTargetDataSource = hikariDevopsDataSource();
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        targetDataSources.put("devops", defaultTargetDataSource);
        targetDataSources.put("test-zd1-catering-log", hikariTestZd1CateringLogDataSource());
        targetDataSources.put("beta-zd1-catering-log", hikariBetaZd1CateringLogDataSource());
        targetDataSources.put("production-zd1-catering-log", hikariProductionZd1CateringLogDataSource());
        targetDataSources.put("test-platform-log", hikariTestPlatformLogDataSource());
        targetDataSources.put("beta-platform-log", hikariBetaPlatformLogDataSource());
        targetDataSources.put("production-platform-log", hikariProductionPlatformLogDataSource());
        targetDataSources.put("test-devops-log", hikariTestDevopsLogDataSource());
        targetDataSources.put("beta-devops-log", hikariBetaDevopsLogDataSource());
        targetDataSources.put("production-devops-log", hikariProductionDevopsLogDataSource());
        return new RoutingDataSource(defaultTargetDataSource, targetDataSources);
    }
}
