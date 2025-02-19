package org.example.miniproject.libs.datasource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
public class DynamicDataSource extends AbstractRoutingDataSource
{

    private DataSourceMasterConfig dataSourceMasterConfig;
    private DataSourceSlaveConfig dataSourceSlaveConfig;
    private DynamicDataSourceTargetHolder dynamicDataSourceTargetHolder;

    @Override
    protected Object determineCurrentLookupKey()
    {
        return DynamicDataSourceTargetHolder.get().getTarget();
    }

    public static enum Target
    {
        MASTER,
        SLAVE
    }

    public DynamicDataSource(DynamicDataSourceTargetHolder dynamicDataSourceTargetHolder,
                             DataSourceMasterConfig dataSourceMasterConfig,
                             DataSourceSlaveConfig dataSourceSlaveConfig) {
        this.dataSourceMasterConfig = dataSourceMasterConfig;
        this.dataSourceSlaveConfig = dataSourceSlaveConfig;
        this.dynamicDataSourceTargetHolder = dynamicDataSourceTargetHolder;

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(Target.MASTER, dataSourceMasterDataSource());
        dataSourceMap.put(Target.SLAVE, dataSourceSlaveDataSource());
        this.setTargetDataSources(dataSourceMap);
        this.setDefaultTargetDataSource(dataSourceMasterDataSource());
    }

    public DataSource dataSourceMasterDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(dataSourceMasterConfig.getUrl());
        dataSource.setUsername(dataSourceMasterConfig.getUsername());
        dataSource.setPassword(dataSourceMasterConfig.getPassword());
        return dataSource;
    }

    public DataSource dataSourceSlaveDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(dataSourceSlaveConfig.getUrl());
        dataSource.setUsername(dataSourceSlaveConfig.getUsername());
        dataSource.setPassword(dataSourceSlaveConfig.getPassword());
        return dataSource;
    }

    @Data
    @Component
    @ConfigurationProperties(prefix="datasource.master.datasource")
    public static class DataSourceMasterConfig {
        private String url;
        private String username;
        private String password;
    }

    @Data
    @Component
    @ConfigurationProperties(prefix="datasource.slave.datasource")
    public static class DataSourceSlaveConfig {
        private String url;
        private String username;
        private String password;
    }
}
