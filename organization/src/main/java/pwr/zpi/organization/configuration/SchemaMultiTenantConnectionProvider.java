package pwr.zpi.organization.configuration;

import lombok.extern.log4j.Log4j2;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.stereotype.Service;
import pwr.zpi.organization.infrastructure.datasource.ShardManagementService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static pwr.zpi.organization.configuration.tenant.TenantContext.DEFAULT_TENANT_ID;
import static pwr.zpi.organization.infrastructure.datasource.SqlDatabaseService.getConnectionString;

@Log4j2
@Service
public class SchemaMultiTenantConnectionProvider implements MultiTenantConnectionProvider {


    @Override
    public Connection getAnyConnection() throws SQLException {
        return DriverManager.getConnection(getConnectionString("localhost", "PUB"));
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantId) throws SQLException {
        Connection conn = null;
        try {
            if(tenantId.equals(DEFAULT_TENANT_ID)){
                conn = getAnyConnection();
            } else {
                conn = ShardManagementService.getConnection(tenantId.hashCode());
            }
        } catch (Exception e) {
            log.error("error: ", e);
        }
        return conn;
    }

    @Override
    public void releaseConnection(String s, Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class aClass) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }
}