package com.tencent.eclipse.nior.rr.nativelib;

import java.net.URL;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import com.android.ddmlib.IDevice;
import com.tencent.eclipse.nior.Activator;
import com.tencent.eclipse.nior.NiorConstants;
import com.tencent.eclipse.nior.RRException;
import com.tencent.eclipse.nior.rr.MultiLineShellReceiver;
import com.tencent.eclipse.nior.rr.NiorRecorder;
import com.tencent.eclipse.nior.rr.Record;
import com.tencent.eclipse.nior.util.StringUtil;

/**
 * 
 * we use a c program to record the linux input event from /dev/input/eventX file
 * @author rainliwang
 *
 */
public class NativeRecorder extends NiorRecorder{
	
	private IDevice device;
	private String recordFileName;

	
	public NativeRecorder(IDevice device,String recordFileName) {
		super();
		this.device = device;
		this.recordFileName=recordFileName;
	}

	@Override
	protected boolean isRecording() {
		return checkProcessExists();
	}

	/**
	 * check if the recorder progress is still running
	 * @return true if the progress is running
	 */
	@Deprecated
	private boolean checkProcessExists() {
		try {
			OneLineReceiver receiver = new OneLineReceiver();
			device.executeShellCommand("ps |grep record|grep -v grep|awk -F\" \" '{print $2}'",  receiver);
			if(StringUtil.isNumeric(receiver.getInfo())) return true;
		} catch (Exception e) {
			Activator.logError(e, "execute record failed");
			e.printStackTrace();
		} 
		return false;
	}
	@Override
	protected Record doRecord() {
		try {
			device.executeShellCommand(NiorConstants.RECORDER_PATH+" "+NiorConstants.REMOTE_BASE_DIR+recordFileName, new MultiLineShellReceiver(),0);
		} catch (Exception e) {
			Activator.logError(e, "execute record failed");
			e.printStackTrace();
		} 
		return null;
	}

	@Override
	protected boolean isReady() {
		//check if the recorder file is already there
		//TODO
		return false;
	}

	/* (non-Javadoc)
	 * @see com.tencent.mqq.qmc.core.RRRecorder#init()
	 */
	@Override
	protected void init() {
		//push the record file to the device
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);  
		URL url = bundle.getEntry(NiorConstants.NATIVE_EVENT_RECORD);
		try {
			url = FileLocator.toFileURL(url);
			device.pushFile(url.getPath(),NiorConstants.RECORDER_PATH);
			//chmod for permission reason
			device.executeShellCommand("su -c ' chmod 555 "+NiorConstants.RECORDER_PATH+" '",  new MultiLineShellReceiver());
		} catch (Exception e) {
			String msg = "failed pushing recorder to android device. url: "+url.toString();
			Activator.logError(e, msg);
			throw new RRException(msg, e);
		}
		
	}

	@Override
	protected boolean doStop() {
		try {
//			device.executeShellCommand("ps |grep record|grep -v grep|awk -F\" \" '{print $2}'|xargs kill",   new RecordAndReplayShellReceiver());
			
			MultiLineShellReceiver receiver = new MultiLineShellReceiver();
			device.executeShellCommand("cat /data/local/nior/pid.txt", receiver);
			String pid = receiver.getOutput().get(0);
			device.executeShellCommand("kill -9 "+pid, receiver);
		}  catch (Exception e) {
			Activator.logError(e, "execute record failed");
			e.printStackTrace();
		} 
		return true;
	}


}
