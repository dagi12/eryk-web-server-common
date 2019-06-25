package pl.softra.common.db.context.initializer;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;
import pl.edu.amu.wmi.util.MyStringUtil;
import pl.softra.model.base.DatabaseType;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
@Primary
public class DataBaseConfig implements InitializingBean, DataBaseConfigHolder {

    private final DataSource dataSource;
    private DatabaseType databaseType;
    private String username;
    private String password;
    private String databaseName;
    private Integer port;
    private String host;

    @Autowired
    public DataBaseConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public boolean isMssql() {
        return DatabaseType.MSSQL == databaseType;
    }

    @Override
    public boolean isOracle() {
        return DatabaseType.ORACLE == databaseType;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (dataSource instanceof DriverManagerDataSource) {
            DriverManagerDataSource driverManagerDataSource = (DriverManagerDataSource) dataSource;
            setUsername(driverManagerDataSource.getUsername());
            setPassword(driverManagerDataSource.getPassword());
        }

        try (Connection connection = dataSource.getConnection()) {
            setHost(MyStringUtil.hostFromDataSourceUrl(connection.getMetaData().getURL()));
            setDatabaseName(connection.getCatalog());
            String dataBaseTypeString = connection.getMetaData().getDatabaseProductName();
            databaseType = DatabaseType.fromString(dataBaseTypeString);
        }
    }

}
