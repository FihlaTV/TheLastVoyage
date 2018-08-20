package com.alter.thelastvoyage;

import androidx.multidex.MultiDexApplication;

/**
 * Application base class. It contains the variables, constants and methods used across the application.
 * When the application runs, the first thing it executes is this base class that extends
 * the Android Application super class whose function is to maintain a global application state.
 * Static variables that need to be shared all over the application are here as well as system information,
 * like Android version, and various methods called by the system, like when the overall system is running low on memory.
 *
 * @see android.app.Application
 *
 * @author Vitor Ribeiro
 */
public class TheLastVoyage extends MultiDexApplication {

    /** Application instance. */
    private static TheLastVoyage instance;

    /** Version code. */
    private static String version = "";

    /** Version name. */
    private static String versionName = "";

    /**
     * Check if app is in debug mode.
     * @return true if it is, false otherwise.
     */
    public static boolean isDebug() {
        switch (BuildConfig.BUILD_TYPE) {
            case "release":
                return false;
            case "debug":
            default:
                return true;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Get current version
//        version = ParsingUtils.intToString(OSUtils.getVersionCode(this));
//        versionName = OSUtils.getVersionName(this);
    }

    /**
     * Called when the overall system is running low on memory,
     * and actively running processes should trim their memory usage.
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // Hint at system gc
        System.gc();
        // Clear image cache
//        new GlideMemoryCleanAsyncTask(this).execute();
    }

    /**
     * Get app instance.
     * @return app instance.
     */
    public static TheLastVoyage getInstance() {
        return instance;
    }

    /**
     * Get version code.
     * @return version code.
     */
    public static String getVersion() {
        return version;
    }

    /**
     * Get version name.
     * @return version name.
     */
    public static String getVersionName() {
        return versionName;
    }

}
