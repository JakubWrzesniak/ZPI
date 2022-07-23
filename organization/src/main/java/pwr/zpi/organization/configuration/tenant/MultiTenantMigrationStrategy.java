package pwr.zpi.organization.configuration.tenant;


//import com.google.common.annotations.Beta;
//import org.flywaydb.core.Flyway;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//import pwr.zpi.organization.infrastructure.datasource.SqlDatabaseService;
//import pwr.zpi.organization.infrastructure.entity.pub.Organization;
//import pwr.zpi.organization.infrastructure.adpter.secondary.OrganizationsRepository;
//
//import javax.annotation.PostConstruct;

//@Configuration
//public class MultiTenantMigrationStrategy {
//
//    @Autowired
//    private OrganizationsRepository organizationsRepository;
//
//    @Bean
//    public FlywayMigrationStrategy migrateStrategy() {
//        return flyway -> {
//            var orgs = organizationsRepository.findAll().stream().map(Organization::getId).toList();
//            String pubConString = SqlDatabaseService.getConnectionString("localhost", "PUB");
//            Flyway.configure().dataSource(pubConString, "sa", "Stud@1234").locations("pwr.zpi.organization.infrastructure.entity.pub").load();
//            flyway.migrate();
//            orgs.forEach(oid ->{
//                String conString = SqlDatabaseService.getConnectionString("localhost", "ORG_" + oid);
//                Flyway.configure().dataSource(conString, "sa", "Stud@1234").locations("pwr.zpi.organization.infrastructure.entity.org").load();
//                flyway.migrate();
//            });
//        };
//    }
//}
