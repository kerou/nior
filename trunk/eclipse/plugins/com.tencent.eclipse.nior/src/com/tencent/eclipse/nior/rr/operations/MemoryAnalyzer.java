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
public class MemoryAnalyzer {
	private IDevice device;
	private Timer myTimer = new Timer();
	private String resultFile;
	public MemoryAnalyzer(IDevice device) {
		super();
		this.device = device;
	}

	/**
	 * 开始启动定时器，定时采集memory数据，并写文件
	 * @param delay 
	 * @param period 
	 * @param pkgName 
	 */
	public void start(long delay, long period, final String pkgName) {
		final String cmd="dumpsys meminfo "+pkgName; 
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		final String tmpFileName = "mem_" + simpleDateFormat.format(date)+".txt";
		myTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				MultiLineShellReceiver multiLineShellReceiver = new MultiLineShellReceiver();
				List<String> output = multiLineShellReceiver.getOutput();
				try {
					//check the progress alive
					System.out.println(output);
					device.executeShellCommand("pidof "+pkgName, multiLineShellReceiver);
					if (output.size()>=1&&StringUtil.isNumeric(output.get(0))) {
						output.clear();
						device.executeShellCommand(cmd, multiLineShellReceiver);
						resultFile = Activator.getDefault()
								.getPreferenceStore()
								.getString(Activator.PREF_MEMEORY_ANALYZER_DIR)
								+ File.separator + tmpFileName;
						System.out.println(output);
						MemCpuUtils.writeLog(resultFile, output, true, null);
					}
				} catch (Exception e) {
					Activator.logError(e, output.toString());
				}
			}
		}, delay, period);
	}
	
	
	public void stop(){
		myTimer.cancel();
		MemCpuUtils.booStarted = false;
		MemCpuUtils.strNativeSize = null;
		MemCpuUtils.strDalvikSize = null;
		MemCpuUtils.strNativeAllocatedSize = null;
		MemCpuUtils.strDalvikAllocatedSize = null;
		MemCpuUtils.strNativePrivdirty = null;
		MemCpuUtils.strDalvikPrivdirty = null;
		MemCpuUtils.strNativePSS = null;
		MemCpuUtils.strDalvikPSS = null;
		MemCpuUtils.strTotalPSS = null;
	}
	
}
