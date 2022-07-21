package pwr.zpi.organization.datasource;

/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license. See LICENSE file in the project root for full license information.
 */

import com.microsoft.azure.elasticdb.core.commons.transientfaulthandling.RetryPolicy;
import com.microsoft.azure.elasticdb.shard.sqlstore.SqlConnectionStringBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import pwr.zpi.organization.OrganizationApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Helper methods for interacting with SQL Databases.
 */
@Log4j2
public final class SqlDatabaseUtils {

    @Value("${DATABASE_USERNAME}")
    private static String username;

    @Value("${DATABASE_PASSWORD}")
    private static String password;

    @Value("${DB_EDITION}")
    private static String dbEdition;

    /**
     * SQL master database name.
     */
    private static final String MASTER_DATABASE_NAME = "master";

    /**
     * Regular expression for go tokens.
     */
    private static final String GO_TOKEN = "go";

    /**
     * Regular expression for comment lines.
     */
    private static final String COMMENT_LINE_TOKEN = "--";

    /**
     * Returns true if we can connect to the database.
     */
//    static boolean tryConnectToSqlDatabase() {
//        String serverName = Configuration.getShardMapManagerServerName();
//        String connectionString = Configuration.getConnectionString(serverName, MASTER_DATABASE_NAME);
//
//        Connection conn = null;
//        try {
//            log.info("Connecting to Azure Portal...");
//            conn = DriverManager.getConnection(connectionString);
//            ConsoleUtils.writeInfo("Connection Successful... Server Name: " + serverName);
//        }
//        catch (Exception e) {
//            ConsoleUtils.writeWarning("Failed to connect to SQL database with connection string:");
//            System.out.printf("\n%1$s\n" + "\r\n", connectionString);
//            ConsoleUtils.writeWarning("If this connection string is incorrect, please update the" + "Configuration file.\r\nException message: %s",
//                    e.getMessage());
//            return false;
//        }
//        finally {
//            connFinally(conn);
//        }
//        return true;
//    }

    private static void connFinally(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
            else {
                log.warn("Returned Connection was either null or already closed.");
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    static boolean databaseExists(String serverName,
            String dbName) {
        String connectionString = getConnectionString(serverName, dbName);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connectionString);
            String query = "select count(*) from sys.databases where name = '" + dbName + "';";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                return rs.next();
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        catch (Exception e) {
            log.warn("Failed to connect to SQL database with connection string:");
            System.out.printf("\n%1$s\n" + "\r\n", connectionString);
            log.warn(String.format("If this connection string is incorrect, please update the" + "Configuration file.\r\nException message: %s",
                    e.getMessage()));
            return false;
        }
        finally {
            connFinally(conn);
        }
        return true;
    }

    static public String createDatabase(String server,
            String db) {
        log.info(String.format("Creating database %s", db));
        Connection conn = null;
        String connectionString = getConnectionString(server, "master");
        String dbConnectionString = "";
        try {
            conn = DriverManager.getConnection(connectionString);
            String query = "SELECT CAST(SERVERPROPERTY('EngineEdition') AS NVARCHAR(128))";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    if (rs.getInt(1) == 5) {
                        query = String.format("CREATE DATABASE %1$s (EDITION = '%2$s')", bracketEscapeName(db), dbEdition);
                        stmt.executeUpdate(query);
                        dbConnectionString = getConnectionString(server, db);
                        while (!databaseIsOnline(DriverManager.getConnection(dbConnectionString), db)) {
                            log.info(String.format("Waiting for database %s to come online...", db));
                            TimeUnit.SECONDS.sleep(5);
                        }
                        log.info(String.format("Database %s is online", db));
                    }
                    else {
                        query = String.format("CREATE DATABASE %1$s", bracketEscapeName(db));
                        stmt.executeUpdate(query);
                        dbConnectionString = getConnectionString(server, db);
                    }
                }
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        catch (Exception e) {
            log.warn("Failed to connect to SQL database with connection string:");
            System.out.printf("\n%1$s\n" + "\r\n", connectionString);
            log.warn(String.format("If this connection string is incorrect, please update the" + "Configuration file.\r\nException message: %s",
                    e.getMessage()));
        }
        finally {
            connFinally(conn);
        }
        return dbConnectionString;
    }

    private static boolean databaseIsOnline(Connection conn,
            String db) {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM sys.databases WHERE name = '" + db + "' and state = 0");
            return (rs.next() && rs.getInt(1) > 0);
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void executeSqlScript(String server,
            String db,
            String schemaFile) {
        log.info("Executing script %s", schemaFile);
        try (Connection conn = DriverManager.getConnection(getConnectionString(server, db))) {
            try (Statement stmt = conn.createStatement()) {
                // Read the commands from the sql script file
                ArrayList<String> commands = readSqlScript(schemaFile);

                for (String cmd : commands) {
                    stmt.execute(cmd);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<String> readSqlScript(String scriptFile) {
        ArrayList<String> commands = new ArrayList<>();
        try (BufferedReader tr = new BufferedReader(new FileReader(scriptFile))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = tr.readLine()) != null) {
                if (!line.endsWith(COMMENT_LINE_TOKEN)) {
                    if (line.endsWith(";")) {
                        sb.append(line).append(System.lineSeparator());
                        commands.add(sb.toString());
                        sb = new StringBuilder();
                    }
                    else {
                        sb.append(line).append(System.lineSeparator());
                    }
                }
            }
        }
        catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
        return commands;
    }

    static RetryPolicy getSqlRetryPolicy() {
        return RetryPolicy.getDefaultFixed();
    }

//    static void dropDatabase(String server,
//            String db) {
//        ConsoleUtils.writeInfo("Dropping database %s", db);
//        Connection conn = null;
//        String connectionString = Configuration.getConnectionString(server, MASTER_DATABASE_NAME);
//        try {
//            conn = DriverManager.getConnection(connectionString);
//            String query = "SELECT CAST(SERVERPROPERTY('EngineEdition') AS NVARCHAR(128))";
//            try (Statement stmt = conn.createStatement()) {
//                ResultSet rs = stmt.executeQuery(query);
//                if (rs.next()) {
//                    query = rs.getInt(1) == 5 ? String.format("DROP DATABASE %1$s", bracketEscapeName(db))
//                            : String.format("ALTER DATABASE %1$s SET SINGLE_USER WITH ROLLBACK IMMEDIATE" + "\r\nDROP DATABASE %1$s",
//                                    bracketEscapeName(db));
//                    stmt.executeUpdate(query);
//                }
//            }
//            catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//        catch (Exception e) {
//            ConsoleUtils.writeWarning("Failed to connect to SQL database with connection string:");
//            System.out.printf("\n%1$s\n" + "\r\n", connectionString);
//            ConsoleUtils.writeWarning("If this connection string is incorrect, please update the" + "Configuration file.\r\nException message: %s",
//                    e.getMessage());
//        }
//        finally {
//            connFinally(conn);
//        }
//    }

    /**
     * Escapes a SQL object name with brackets to prevent SQL injection.
     */

    public static String getConnectionString(String serverName,
                                             String database) {
        SqlConnectionStringBuilder connStr = new SqlConnectionStringBuilder(getCredentialsConnectionString());
        connStr.setDataSource(serverName);
        connStr.setDatabaseName(database);
        return connStr.toString();
    }

    static String getCredentialsConnectionString() {

        // Get Integrated Security from the app.config file.
        // If it exists, then parse it (throw exception on failure), otherwise default to false.
        String integratedSecurityString = "false";
        boolean integratedSecurity = integratedSecurityString != null && Boolean.parseBoolean(integratedSecurityString);

        SqlConnectionStringBuilder connStr = new SqlConnectionStringBuilder();
        connStr.setUser("sa");
        connStr.setPassword("Stud@1234");
        connStr.setIntegratedSecurity(integratedSecurity);
        // connStr.setApplicationName(properties.getProperty("CONN_APP_NAME"));
        connStr.setConnectTimeout(30);
        return connStr.toString();
    }


    private static String bracketEscapeName(String sqlName) {
        return '[' + sqlName.replace("]", "]]") + ']';
    }
}