package com.tencent.eclipse.nior.rr.operations;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.android.ddmlib.IDevice;
import com.tencent.eclipse.nior.Activator;
import com.tencent.eclipse.nior.NiorConstants;
import com.tencent.eclipse.nior.rr.MultiLineShellReceiver;
import com.tencent.eclipse.nior.util.CMDUtil;
import com.tencent.eclipse.nior.util.StreamDrainer;

/**
 * @author rainliwang
 *
 */
public class PacketsAnalyzer {
	private IDevice device;

	public PacketsAnalyzer(IDevice device) {
		super();
		this.device = device;
	}

	public void pullPacketFile() {
		String packetsAnalysisDir = Activator.getDefault().getPreferenceStore()
				.getString(Activator.PREF_PACKETS_ANALYZER_DIR);
		// find the latest tcpdump file
		MultiLineShellReceiver multiLineShellReceiver = new MultiLineShellReceiver();
		List<String> output = multiLineShellReceiver.getOutput();
		try {
			device.executeShellCommand("ls " + NiorConstants.REMOTE_TCPDUMP_DIR,
					multiLineShellReceiver);
		} catch (Exception e) {
			Activator.logError(e, output.toString());
		}
		//pull all the files out then delete them in the mobile phone
		// pull it out to packetsAnalysisDir
		Process process;
		DataOutputStream out = null;
		try {
			String[] cmd = new String[] { "cmd" };
			process = Runtime.getRuntime().exec(cmd);
			OutputStream outputstream = process.getOutputStream();
			out = new DataOutputStream(outputstream);
			new Thread(new StreamDrainer(process.getInputStream())).start();
			new Thread(new StreamDrainer(process.getErrorStream())).start();
			for (String tcpdumpFileName : output) {
				CMDUtil.runCMDLine(out, "adb -s " + device.getSerialNumber() + " pull "
						+ NiorConstants.REMOTE_TCPDUMP_DIR + tcpdumpFileName + " "
						+ packetsAnalysisDir);
			}
			CMDUtil.runCMDLine(out, "exit");
			process.waitFor();
		} catch (Exception e) {
			Activator.logError(e, out.toString());
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				Activator.logError(e, "error closing outputstream");
			}
		}
	}

}
