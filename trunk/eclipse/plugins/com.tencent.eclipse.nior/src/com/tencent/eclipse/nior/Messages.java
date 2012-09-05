package com.tencent.eclipse.nior;



import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "com.tencent.eclipse.nior.messages"; //$NON-NLS-1$


    public static String NEW_NIOR_RECORD_PAGE_TITLE;
    public static String NEW_NIOR_RECORD_PAGE_DESCRIPTION;
    public static String RECORD;
    public static String STOP;
    public static String RECORDING;
    public static String RECORD_FINISHED;
    
    public static String LABEL_CHOOSE_DEVICE;
    public static String LABEL_DEVICE_NOT_EXISTS;
    public static String LABEL_SCREEN_ANALYZER_LOCATION;
    public static String LABEL_PACKETS_ANALYZER_LOCATION;
    public static String LABEL_MEMEORY_ANALYZER_LOCATION;
    public static String LABEL_CPU_ANALYZER_LOCATION;
    
    public static String LABEL_SCREEN_ANALYSIS;
    public static String LABEL_PACKETS_ANALYSIS;
    public static String LABEL_MEMORY_ANALYSIS;
    public static String LABEL_CPU_ANALYSIS;
    public static String LABEL_REPLAY_FINISHED;
    public static String LABEL_MEMEORY_PKGNAME;

    public static String ALERT_DEVICE_NOT_EXISTS;
    public static String ALERT_NO_DEVICE_CHOOSEN;
    public static String ALERT_NUMBER_REQUIRED;
    public static String ALERT_SCREEN_ANALYZER_DIR_REQUIRED;
    public static String ALERT_PACKETS_ANALYZER_DIR_REQUIRED;
    public static String ALERT_MEMEORY_ANALYZER_DIR_REQUIRED;
    public static String ALERT_CPU_ANALYZER_DIR_REQUIRED;
    public static String ALERT_PKGNAME_REQUIRED;
    
    public static String INFO_CHOOSEN_DEVICE;
    public static String INFO_ENTER_REPLAY_TIMES;
    public static String INFO_REPLAY_PROGRESS;
    public static String INFO_REPLAY_FINISHED;
    
    public static String TITLE_PREFERENCE_PAGE;
    public static String TITLE_REPLAY_INFO;

    public static String NOTICE;
    
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
