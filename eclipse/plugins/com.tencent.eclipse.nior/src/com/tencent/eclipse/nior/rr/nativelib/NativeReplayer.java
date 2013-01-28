package com.tencent.eclipse.nior.rr.nativelib;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import com.android.ddmlib.IDevice;
import com.tencent.eclipse.nior.Activator;
import com.tencent.eclipse.nior.NiorConstants;
import com.tencent.eclipse.nior.RRException;
import com.tencent.eclipse.nior.rr.MultiLineShellReceiver;
import com.tencent.eclipse.nior.rr.NiorReplayer;

public class NativeReplayer extends NiorReplayer {
	private IDevice device;
	
	public NativeReplayer(IDevice device) {
		super();
		this.device = device;
	}

	@Override
	protected boolean doStop() {
		// TODO kill the progress
		return !checkProcessExists();
	}

	private boolean checkProcessExists() {
		return false;
	}

	@Override
	protected boolean isReplaying() {
		return checkProcessExists();
	}

	@Override
	protected void doReplay(String fileName,int times) {
		try {
			for (int i = 0; i < times; i++) {
				device.executeShellCommand(NiorConstants.REPLAYER_PATH+" "+NiorConstants.REMOTE_BASE_DIR+fileName, new MultiLineShellReceiver(),0);
			}
		} catch (Exception e) {
			Activator.logError(e, "execute replay failed");
		} 
	}

	@Override
	protected boolean isReady() {
		return false;
	}

	@Override
	protected void init() {
		// push the replay file to the device
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);  
		URL url = bundle.getEntry(NiorConstants.NATIVE_EVENT_REPLAY);
		try {
			url = FileLocator.toFileURL(url);
			device.pushFile(url.getPath(),NiorConstants.REPLAYER_PATH);
			//chmod for permission reason
			device.executeShellCommand("su -c ' chmod 555 "+NiorConstants.REPLAYER_PATH+" '",  new MultiLineShellReceiver());
		} catch (Exception e) {
			String msg = "failed pushing replayer to android device";
			Activator.logError(e, msg);
			throw new RRException(msg, e);
		}

	}

}
