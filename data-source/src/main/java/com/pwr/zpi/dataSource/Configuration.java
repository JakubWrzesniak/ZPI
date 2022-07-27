package com.pwr.zpi.dataSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license. See LICENSE file in the project root for full license information.
 */

import com.microsoft.azure.elasticdb.shard.sqlstore.SqlConnectionStringBuilder;

/**
 * Provides access to app.config settings, and contains advanced configuration settings.
 */
final class Configuration {

    private static Properties properties = loadProperties();

    static Properties loadProperties() {
        InputStream inStream = Configuration.class.getClassLoader().getResourceAsStream("resources.properties");
        Properties prop = new Properties();
        if (inStream != null) {
            try {
                prop.load(inStream);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return prop;
    }

    public static String getShardMapManagerServerName() {
        return properties.getProperty("CONN_SERVER_NAME");
    }

    public static String getShardMapManagerDatabaseName() {
        return properties.getProperty("CONN_DB_NAME");
    }

    static String getShardMapName() {
        return properties.getProperty("ORGANIZATION_SHARD_MAP_NAME");
    }

    static String getDatabaseEdition() {
        return properties.getProperty("DB_EDITION");
    }

    static String getOrgShardNameFormat(){
        return properties.getProperty("ORGANIZATION_SHAR_NAME_FORMAT");
    }

    public static String getSQLScriptFileName() {
        return properties.getProperty("INITIAL_SHARD_SCRIPT");
    }

    static String getConnectionString(String serverName,
            String database) {
        SqlConnectionStringBuilder connStr = new SqlConnectionStringBuilder(getCredentialsConnectionString());
        connStr.setDataSource(serverName);
        connStr.setDatabaseName(database);
        connStr.setIntegratedSecurity(true);
        System.out.println(connStr);
        return connStr.toString();
    }

    static String getCredentialsConnectionString() {

        // Get Integrated Security from the app.config file.
        // If it exists, then parse it (throw exception on failure), otherwise default to false.
        String integratedSecurityString = "true";
        boolean integratedSecurity = true;

        SqlConnectionStringBuilder connStr = new SqlConnectionStringBuilder();
        connStr.setUser(properties.getProperty("CONN_USER"));
        connStr.setPassword(properties.getProperty("CONN_PASSWORD"));
        connStr.setIntegratedSecurity(integratedSecurity);
        connStr.setApplicationName(properties.getProperty("CONN_APP_NAME"));
        connStr.setConnectTimeout(30);
        return connStr + "encrypt=true;trustServerCertificate=true;";
    }
}