package com.tencent.eclipse.nior.rr.operations;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.android.ddmlib.IDevice;
import com.tencent.eclipse.nior.Activator;
import com.tencent.eclipse.nior.rr.MultiLineShellReceiver;
import com.tencent.eclipse.nior.util.StringUtil;

/**
 * @author rainliwang
 *
 */
public class CpuAnalyzer {
	private IDevice device;

	public CpuAnalyzer(IDevice device) {
		super();
		this.device = device;
	}

	private Timer myTimer = new Timer();
	private String resultFile;

	/**
	 * 开始启动定时器，定时采集cpu数据，并写文件
	 * @param delay 
	 * @param period 
	 * @param pkgName 
	 */
	public void start(long delay, long period, final String pkgName) {
		final String cmd="top -n 1 -d 0 | grep " + pkgName; 
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		final String tmpFileName = "cpu_" + simpleDateFormat.format(date)+".txt";
		myTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				MultiLineShellReceiver multiLineShellReceiver = new MultiLineShellReceiver();
				List<String> output = multiLineShellReceiver.getOutput();
				
				try {
					//check the progress alive
					device.executeShellCommand("pidof "+pkgName, multiLineShellReceiver);
					if (output.size()>=1&&StringUtil.isNumeric(output.get(0))) {
						output.clear();
						device.executeShellCommand(cmd, multiLineShellReceiver);
						resultFile = Activator.getDefault()
								.getPreferenceStore()
								.getString(Activator.PREF_CPU_ANALYZER_DIR)
								+ File.separator + tmpFileName;
						MemCpuUtils
								.writeLog(resultFile, output, false, pkgName);
					}
				} catch (Exception e) {
					Activator.logError(e, output.toString());
				}
			}
		}, delay, period);
	}
	
	
	public void stop(){
		myTimer.cancel();
	}
	

}
