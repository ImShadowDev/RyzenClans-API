package dev.imshadow.api;

public final class RyzenClansProvider {

    private static IRyzenClansAPI instance;

    private RyzenClansProvider() {}

    public static IRyzenClansAPI get() {
        if (instance == null) {
            throw new IllegalStateException("RyzenClans API is not available. Is the plugin enabled?");
        }
        return instance;
    }

    public static boolean isAvailable() {
        return instance != null;
    }

    // Called by RyzenClan in onEnable/onDisable — do not use from external plugins
    public static void register(IRyzenClansAPI api) {
        instance = api;
    }

    public static void unregister() {
        instance = null;
    }
}
