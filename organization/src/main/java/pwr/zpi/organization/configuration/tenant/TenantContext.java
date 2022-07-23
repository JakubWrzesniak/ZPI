package pwr.zpi.organization.configuration.tenant;

public abstract class TenantContext {

    public static final String DEFAULT_TENANT_ID = "public";
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<String>();

    public static void setCurrentTenant(String tenant) {
        currentTenant.set(tenant);
    }

    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    public static void clear() {
        currentTenant.remove();
    }
}