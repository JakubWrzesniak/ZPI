package com.pwr.zpi.dataSource;

import com.microsoft.azure.elasticdb.shard.base.*;
import com.microsoft.azure.elasticdb.shard.map.ListShardMap;
import com.microsoft.azure.elasticdb.shard.mapmanager.ShardManagementErrorCategory;
import com.microsoft.azure.elasticdb.shard.mapmanager.ShardManagementErrorCode;
import com.microsoft.azure.elasticdb.shard.mapmanager.ShardManagementException;
import com.microsoft.azure.elasticdb.shard.mapmanager.ShardMapManager;
import com.microsoft.azure.elasticdb.shard.store.StoreShard;
import com.microsoft.azure.elasticdb.shard.store.StoreShardMap;
import com.microsoft.azure.elasticdb.shard.utils.StringUtilsLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.util.UUID;

import static com.pwr.zpi.dataSource.Configuration.getConnectionString;
import static com.pwr.zpi.dataSource.ShardManagementUtils.*;
import static com.pwr.zpi.dataSource.SqlDatabaseUtils.*;

public class TenantDataSource {
    private static TenantDataSource INSTANCE = null;
    private static ShardMapManager shardMapManager = null;

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final ShardKeyType shardKeyType = ShardKeyType.Int32;

    private TenantDataSource(String shardMapManagerServerName, String shardMapManagerDatabaseName){
        String shardMapManagerConnectionString;
        if (!databaseExists(shardMapManagerServerName, shardMapManagerDatabaseName)) {
            shardMapManagerConnectionString = createDatabase(shardMapManagerServerName, shardMapManagerDatabaseName);
        }
        else {
            shardMapManagerConnectionString = getConnectionString(shardMapManagerServerName, shardMapManagerDatabaseName);
        }
        if (!StringUtilsLocal.isNullOrEmpty(shardMapManagerConnectionString)) {
            shardMapManager = createOrGetShardMapManager(shardMapManagerConnectionString);
        }
    }
    
    public static TenantDataSource get(){
        final String shardMapManagerServerName = Configuration.getShardMapManagerServerName();
        final String shardMapManagerDatabaseName = Configuration.getShardMapManagerDatabaseName();
        if(null == INSTANCE){
            INSTANCE = new TenantDataSource(shardMapManagerServerName, shardMapManagerDatabaseName);
        } else {
            shardMapManager = tryGetShardMapManager(shardMapManagerServerName, shardMapManagerDatabaseName);
        }
        return INSTANCE;
    }

    public void createOrganizationDatabase(Object organizationKey){
        String organizationNameFormat = Configuration.getOrgShardNameFormat();
        String databaseName = String.format(organizationNameFormat, UUID.randomUUID());
        createOrganizationDatabase(databaseName, organizationKey);
    }
    private void createOrganizationDatabase(String databaseName, Object organizationKey){
        final String shardMapManagerServerName = Configuration.getShardMapManagerServerName();
        ListShardMap<Integer> shardMap = createOrGetListShardMap(shardMapManager, Configuration.getShardMapName(), shardKeyType);
        if(isShardWithIdExists(shardMap, organizationKey)){
            ConsoleUtils.writeColor(ConsoleColor.Red, "Organization with tenantId %s already exists", organizationKey);
            throw new ShardManagementException(ShardManagementErrorCategory.Validation, ShardManagementErrorCode.ShardAlreadyExists, String.format("Shard map for id %s already exists", organizationKey));
        }
        createDatabase(shardMapManagerServerName, databaseName);
        executeSqlScript(shardMapManagerServerName, databaseName, Configuration.getSQLScriptFileName());
        
        ShardLocation shardLocation = new ShardLocation(shardMapManagerServerName, databaseName, SqlProtocol.Tcp, 1433);
        Shard shard = createOrGetShard(shardMap, shardLocation);
        shardMap.createPointMapping(organizationKey.hashCode(), shard);
        ConsoleUtils.writeColor(ConsoleColor.Green, "Shar for %s is created", databaseName);
    }

    public void dropOrganizationDatabase(Object tenantId){
        ListShardMap<Integer> shardMap = shardMapManager.getListShardMap(Configuration.getShardMapName(), shardKeyType);
        Shard shard = shardMap.getMappingForKey(tenantId.hashCode()).getShard();
        if(shard == null){
            ConsoleUtils.writeWarning("Shard doesn't exists");
            return;
        }
        ShardLocation shardLocation = shard.getLocation();
        setMappingPointOffline(shardMap, tenantId.hashCode());
        removeMapping(shardMap, tenantId.hashCode());
        removeShard(shardMap, shardLocation);
        SqlDatabaseUtils.dropDatabase(shard.getLocation().getDataSource(), shard.getLocation().getDatabase());
        ConsoleUtils.writeColor(ConsoleColor.Green,"Database is successfully removed %s", shardLocation.getDatabase());
        log.info("Shard was removed successfully");
    }

    public Connection getConnectionToDatabase(int tenantId){
        ListShardMap<Integer> listShardMap = shardMapManager.getListShardMap(Configuration.getShardMapName(), shardKeyType);

        if (listShardMap == null) {
            ConsoleUtils.writeWarning("Shard Map Manager has been created, but the Shard Map has not been created");
            return null;
        }
        return listShardMap.openConnectionForKey(tenantId, Configuration.getCredentialsConnectionString());
    }

    public boolean isShardWithIdExists(ListShardMap<Integer> shardMap, Object tenantId){
        if(shardMap != null)
            return shardMap.getMappings().stream().anyMatch(m -> m.getValue().equals(tenantId));
        return false;
    }
}
