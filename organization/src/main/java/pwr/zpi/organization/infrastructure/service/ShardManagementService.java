package pwr.zpi.organization.infrastructure.service;

import com.microsoft.azure.elasticdb.core.commons.helpers.ReferenceObjectHelper;
import com.microsoft.azure.elasticdb.shard.base.Shard;
import com.microsoft.azure.elasticdb.shard.base.ShardKeyType;
import com.microsoft.azure.elasticdb.shard.base.ShardLocation;
import com.microsoft.azure.elasticdb.shard.base.SqlProtocol;
import com.microsoft.azure.elasticdb.shard.map.ListShardMap;
import com.microsoft.azure.elasticdb.shard.map.ShardMap;
import com.microsoft.azure.elasticdb.shard.mapmanager.ShardMapManager;
import com.microsoft.azure.elasticdb.shard.mapmanager.ShardMapManagerFactory;
import com.microsoft.azure.elasticdb.shard.mapmanager.ShardMapManagerLoadPolicy;
import com.microsoft.azure.elasticdb.shard.utils.StringUtilsLocal;
//import lombok.extern.log4j.Log4j2;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.UUID;

import static pwr.zpi.organization.infrastructure.service.SqlDatabaseService.*;

@Component
@Log4j2
public class ShardManagementService {
    static ListShardMap<Integer> shardMap;
    static ShardMapManager shardMapManager;

    static {
        String shardMapManagerServerName = "localhost";
        String shardMapManagerDatabaseName = "ShardMapManagerDb";
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
        shardMap = createOrGetListShardMap(shardMapManager, "OrganizationId", ShardKeyType.Int32);
    }

    public static boolean createNewOrganization(UUID databaseKey){
        return provisionNewTenant("ORG_" + UUID.randomUUID(), false, databaseKey);
    }

    public static boolean provisionNewTenant(String databaseName, Boolean isGlobal, UUID databaseKey) {
        String shardMapManagerServerName = "localhost";
        String shardMapManagerDatabaseName = "ShardMapManagerDb";
        if (shardMapManager != null) {
            log.info(String.format("Shard Map Manager %s already exists in memory", shardMapManagerServerName));
        }

        String shardMapManagerConnectionString;
        // Create shard map manager database
        if (!databaseExists(shardMapManagerServerName, shardMapManagerDatabaseName)) {
            shardMapManagerConnectionString = createDatabase(shardMapManagerServerName, shardMapManagerDatabaseName);
        }
        else {
            shardMapManagerConnectionString = getConnectionString(shardMapManagerServerName, shardMapManagerDatabaseName);
        }
        if (!StringUtilsLocal.isNullOrEmpty(shardMapManagerConnectionString)) {
            // Create shard map manager
            shardMapManager = createOrGetShardMapManager(shardMapManagerConnectionString);
        }
        //create requested database
        createDatabase(shardMapManagerServerName, databaseName);
        //execute schema initialisation script based on database type, global or tenant
        if (isGlobal) {
            executeSqlScript(shardMapManagerServerName, databaseName, "create.sql");
        } else {
            executeSqlScript(shardMapManagerServerName, databaseName, "create.sql");
        }

        ShardLocation shardLocation = new ShardLocation(shardMapManagerServerName, databaseName, SqlProtocol.Tcp, 1433);
        shardMap = createOrGetListShardMap(shardMapManager, "OrganizationId", ShardKeyType.Int64);
        Shard shard = ShardManagementService.createOrGetShard(shardMap, shardLocation);
        //create shard map pointing for this database
        shardMap.createPointMapping(databaseKey.toString().hashCode(), shard);
        return true;
    }

    private static <T> ListShardMap<T> createOrGetListShardMap(ShardMapManager shardMapManager,
                                                       String shardMapName,
                                                       ShardKeyType keyType) {
        // Try to get a reference to the Shard Map.
        ReferenceObjectHelper<ListShardMap<T>> refListShardMap = new ReferenceObjectHelper<>(null);
        boolean isGetSuccess = shardMapManager.tryGetListShardMap(shardMapName, keyType, refListShardMap);
        ListShardMap<T> shardMap = refListShardMap.argValue;

        if (isGetSuccess && shardMap != null) {
            log.info(String.format("Shard Map %1$s already exists", shardMap.getName()));
        }
        else {
            // The Shard Map does not exist, so create it
            try {
                shardMap = shardMapManager.createListShardMap(shardMapName, keyType);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            log.info(String.format("Created Shard Map %1$s", shardMap != null ? shardMap.getName() : null));
        }

        return shardMap;
    }

    static ShardMapManager createOrGetShardMapManager(String shardMapManagerConnectionString) {
        ShardMapManager shardMapManager = null;
        ReferenceObjectHelper<ShardMapManager> refShardMapManager = new ReferenceObjectHelper<>(shardMapManager);
        boolean shardMapManagerExists = ShardMapManagerFactory.tryGetSqlShardMapManager(shardMapManagerConnectionString,
                ShardMapManagerLoadPolicy.Lazy, refShardMapManager);
        shardMapManager = refShardMapManager.argValue;

        if (shardMapManagerExists) {
            log.info(String.format("Shard Map %s already exists", shardMapManager));
        }
        else {
            shardMapManager = ShardMapManagerFactory.createSqlShardMapManager(shardMapManagerConnectionString);
            log.info(String.format("Created Shard Map %s", shardMapManager));
        }

        return shardMapManager;
    }

    public static Shard createOrGetShard(ShardMap shardMap,
                                         ShardLocation shardLocation) {
        // Try to get a reference to the Shard
        Shard shard = null;
        ReferenceObjectHelper<Shard> refShard = new ReferenceObjectHelper<>(shard);
        boolean shardExists = shardMap.tryGetShard(shardLocation, refShard);
        shard = refShard.argValue;

        if (shardExists) {
            log.info(String.format("Shard %1$s has already been added to the Shard Map", shardLocation.getDatabase()));
        }
        else {
            // The Shard Map does not exist, so create it
            shard = shardMap.createShard(shardLocation);
            log.info(String.format("Added shard %1$s to the Shard Map", shardLocation.getDatabase()));
        }

        return shard;
    }

    public static Connection getConnection(Integer tenantId){
        return shardMap.openConnectionForKey(tenantId, getCredentialsConnectionString());
    }
}
