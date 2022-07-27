package pwr.zpi.organization.infrastructure.config;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.pwr.zpi.dataSource.TenantDataSource;
import lombok.extern.log4j.Log4j2;
import net.bytebuddy.implementation.bytecode.Throw;
import org.hibernate.context.TenantIdentifierMismatchException;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static pwr.zpi.organization.infrastructure.config.tenant.TenantContext.DEFAULT_TENANT_ID;

@Log4j2
@Service
public class SchemaMultiTenantConnectionProvider implements MultiTenantConnectionProvider {

    @Autowired
    TenantDataSource tenantDataSource;

    @Override
    public Connection getAnyConnection() throws SQLException {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setEncrypt(true);
        dataSource.setTrustServerCertificate(true);
        dataSource.setServerName("localhost");
        dataSource.setDatabaseName("PUB");
        return dataSource.getConnection("sa", "Stud@1234");
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
                conn = tenantDataSource.getConnectionToDatabase(tenantId.hashCode());
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