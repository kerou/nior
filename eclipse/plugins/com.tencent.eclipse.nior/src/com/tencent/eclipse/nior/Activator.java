package com.tencent.eclipse.nior;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.android.ddmlib.AndroidDebugBridge;


/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.tencent.eclipse.nior"; //$NON-NLS-1$
	
	public static final String PREF_SCREEN_ANALYZER_DIR = PLUGIN_ID+".screenanalyzer.dir";
	public static final String PREF_PACKETS_ANALYZER_DIR = PLUGIN_ID+".packetsanalyzer.dir";
	public static final String PREF_MEMEORY_ANALYZER_DIR = PLUGIN_ID+".memeoryanalyzer.dir";
	public static final String PREF_CPU_ANALYZER_DIR = PLUGIN_ID+".cpuanalyzer.dir";
	
	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
//		AndroidDebugBridge.createBridge(AdtPrefs.getPrefs().getOsSdkFolder()+"platform-tools"+File.separator+"adb.exe",false);
		AndroidDebugBridge.getBridge();
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	  /**
     * Logs an exception to the default Eclipse log.
     * <p/>
     * The status severity is always set to ERROR.
     *
     * @param exception the exception to log.
     * @param format The format string, like for {@link String#format(String, Object...)}.
     * @param args The arguments for the format string, like for
     * {@link String#format(String, Object...)}.
     */
    public static void logError(Throwable exception, String msg) {
        Status status = new Status(IStatus.ERROR, PLUGIN_ID, msg, exception);
        getDefault().getLog().log(status);
    }
    
    public static void logInfo(String msg) {
        Status status = new Status(IStatus.INFO, PLUGIN_ID, msg);
        getDefault().getLog().log(status);
    }

}
