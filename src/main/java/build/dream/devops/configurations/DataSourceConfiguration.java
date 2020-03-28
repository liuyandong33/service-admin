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

    @Primary
    @Bean(name = "routingDataSource")
    public DataSource routingDataSource() {
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        targetDataSources.put("devops", hikariDevopsDataSource());
        targetDataSources.put("test-zd1-catering-log", hikariTestZd1CateringLogDataSource());
        targetDataSources.put("beta-zd1-catering-log", hikariBetaZd1CateringLogDataSource());
        targetDataSources.put("production-zd1-catering-log", hikariProductionZd1CateringLogDataSource());
        return new RoutingDataSource(hikariDevopsDataSource(), targetDataSources);
    }
}
