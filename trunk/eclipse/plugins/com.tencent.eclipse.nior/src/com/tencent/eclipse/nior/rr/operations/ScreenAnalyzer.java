package com.tencent.eclipse.nior.rr.operations;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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
public class ScreenAnalyzer {
	private List<String> videoNames = new ArrayList<String>();
	private IDevice device;

	public boolean pullVideo() {
		String screenAnalysisDir = Activator.getDefault().getPreferenceStore()
				.getString(Activator.PREF_SCREEN_ANALYZER_DIR);
		// find the latest video
		MultiLineShellReceiver multiLineShellReceiver = new MultiLineShellReceiver();
		List<String> output = multiLineShellReceiver.getOutput();
		try {
			device.executeShellCommand("ls " + NiorConstants.REMOTE_VIDEO_DIR,
					multiLineShellReceiver);
		} catch (Exception e) {
			Activator.logError(e, output.toString());
		}
		String latestVideoName = output.get(output.size() - 2);
		videoNames.add(latestVideoName);
		// pull it out to screenAnalysisDir
		Process process;
		DataOutputStream out = null;
		try {
			String[] cmd = new String[] { "cmd" };
			process = Runtime.getRuntime().exec(cmd);
			OutputStream outputstream = process.getOutputStream();
			out = new DataOutputStream(outputstream);
			new Thread(new StreamDrainer(process.getInputStream())).start();
			new Thread(new StreamDrainer(process.getErrorStream())).start();
			CMDUtil.runCMDLine(out, "adb -s " + device.getSerialNumber() + " pull "
					+ NiorConstants.REMOTE_VIDEO_DIR + latestVideoName + " "
					+ screenAnalysisDir + File.separator
					+ NiorConstants.DIR_VIDEO);
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
		return true;
	}

	public ScreenAnalyzer(IDevice device) {
		super();
		this.device = device;
	}

	public void analysis() {
		String screenAnalysisDir = Activator.getDefault().getPreferenceStore()
				.getString(Activator.PREF_SCREEN_ANALYZER_DIR);
		boolean rs = false;
		Process process;
		DataOutputStream out = null;
		try {
			String[] cmd = new String[] { "cmd" };
			process = Runtime.getRuntime().exec(cmd);
			OutputStream outputstream = process.getOutputStream();
			out = new DataOutputStream(outputstream);
			new Thread(new StreamDrainer(process.getInputStream())).start();
			new Thread(new StreamDrainer(process.getErrorStream())).start();
			// create image from video
			CMDUtil.runCMDLine(out, "cd /d " + screenAnalysisDir);
			CMDUtil.runCMDLine(out, "java -jar ImageCreate.jar");
			// parse image
			CMDUtil.runCMDLine(out, "java -jar CompareAnalysis.jar   2>nul");
			CMDUtil.runCMDLine(out, "exit");

			int returnCode = process.waitFor();
			if (returnCode == 255) {
				rs = false;
			} else {
				rs = true;
			}
		} catch (Exception e) {
			Activator.logError(e, "error doing analysis");
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				Activator.logError(e, "error closing outputstream");
			}
		}
		if (rs) {
			delFiles();
		}
	}

	private void delFiles() {
		String screenAnalysisDir = Activator.getDefault().getPreferenceStore()
				.getString(Activator.PREF_SCREEN_ANALYZER_DIR);
		// del remoteFIles
		MultiLineShellReceiver multiLineShellReceiver = new MultiLineShellReceiver();
		List<String> output = multiLineShellReceiver.getOutput();
		try {
			for (String videoName : videoNames) {
				device.executeShellCommand("rm "
						+ NiorConstants.REMOTE_VIDEO_DIR + videoName,
						multiLineShellReceiver);
			}
		} catch (Exception e) {
			Activator.logError(e, output.toString());
		}

		// del image files
		File imageDir = new File(screenAnalysisDir + File.separator
				+ NiorConstants.DIR_IMAGE);
		File[] images = imageDir.listFiles();// directories
		for (File image : images) {
			// 若有则把文件放进数组，并判断是否有下级目录
			File delFile[] = image.listFiles();
			for (File imageFile : delFile) {
				if (!imageFile.delete()) {
					Activator.logInfo("delete imageFile failed: "
							+ imageFile.getName());
				}
			}
			if (!image.delete()) {
				Activator.logInfo("delete imageDir failed: " + image.getName());
			}
		}

		// del local videos
		File videoDir = new File(screenAnalysisDir + File.separator
				+ NiorConstants.DIR_VIDEO);
		File[] videos = videoDir.listFiles();
		for (File video : videos) {
			if (!video.delete()) {
				Activator.logInfo("delete file failed: " + video.getName());
			}
		}
	}


}
