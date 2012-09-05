package com.tencent.eclipse.nior.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.console.MessageConsoleStream;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.tencent.eclipse.nior.Activator;
import com.tencent.eclipse.nior.Messages;
import com.tencent.eclipse.nior.console.ConsoleFactory;
import com.tencent.eclipse.nior.rr.NiorReplayer;
import com.tencent.eclipse.nior.rr.nativelib.NativeReplayer;
import com.tencent.eclipse.nior.rr.operations.CpuAnalyzer;
import com.tencent.eclipse.nior.rr.operations.MemoryAnalyzer;
import com.tencent.eclipse.nior.rr.operations.PacketsAnalyzer;
import com.tencent.eclipse.nior.rr.operations.ScreenAnalyzer;

public class Replay implements IObjectActionDelegate {
	private Shell shell;
	

	private IWorkbenchPart targetPart;
	private IDevice targetDevice;

	@Override
	public void run(IAction action) {
		//collect user requirements
		ReplayDialog replayDialog= new ReplayDialog(getShell());
		ReplayInfo replayInfo;
		int returnCode = replayDialog.open();
		if (Window.CANCEL == returnCode) {
			return;
		} else {
			replayInfo=replayDialog.getReplayInfo();
		}
		// replay the record for given times
		// first get the device and record file, if device is not connected,alert the user
		String deviceSerialNo = "";
		String rcdFile = "";
		ISelection selection = targetPart.getSite().getSelectionProvider()
				.getSelection();
		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection)
					.getFirstElement();
			if (element instanceof IFile) {
				IFile iFile = (IFile) element;
				InputStream contents = null;
				try {
					contents = iFile.getContents();
				} catch (CoreException e1) {
					Activator.logError(e1, "get nior file content error: "
							+ iFile.getFullPath().toString());
					e1.printStackTrace();
				}
				BufferedReader in = new BufferedReader(new InputStreamReader(
						contents));
				try {
					deviceSerialNo = in.readLine();
					rcdFile = in.readLine();
				} catch (Exception e) {
					Activator.logError(e, "read nior file error: "
							+ iFile.getFullPath().toString());
				} finally {
					try {
						in.close();
					} catch (IOException e) {
						Activator.logError(e,
								"close readbuffer error,nior file: "
										+ iFile.getFullPath().toString());
					}
				}
			}
		}

		IDevice[] devices = AndroidDebugBridge.getBridge().getDevices();
		targetDevice = null;
		for (int i = 0; i < devices.length; i++) {
			if (!"".equals(deviceSerialNo)
					&& deviceSerialNo.equals(devices[i].getSerialNumber())) {
				targetDevice = devices[i];
				break;
			}
		}
		// check device
		if (targetDevice == null) {
			
			MessageDialog.openInformation(getShell(),
                    Messages.LABEL_DEVICE_NOT_EXISTS, Messages.ALERT_DEVICE_NOT_EXISTS);
			return;
		}
		replayInfo.setRecordFile(rcdFile);
		ReplayThread replayThread = new ReplayThread(replayInfo);
		//check screenAnalysis
		if(replayInfo.isScreenAnalysis()){
			
		}
		//check packet grasp
		
		//check cpu & memory grasp
		
		replayThread.start();
	}
	
	class ReplayThread extends Thread{
		ReplayInfo  replayInfo;
		
		public ReplayThread(ReplayInfo replayInfo) {
			super();
			this.replayInfo = replayInfo;
		}


		@Override
		public void run() {
			int replayTimes = replayInfo.getReplayTimes();
			String recordFile = replayInfo.getRecordFile();
			NiorReplayer niorReplayer = new NativeReplayer(targetDevice);
			ScreenAnalyzer screenAnalyzer = new ScreenAnalyzer(targetDevice);
			PacketsAnalyzer packetsAnalyzer=new PacketsAnalyzer(targetDevice);
			MemoryAnalyzer memoryAnalyzer=new MemoryAnalyzer(targetDevice);
			CpuAnalyzer cpuAnalyzer=new CpuAnalyzer(targetDevice);
			if(replayInfo.isMemoryInfoAnalysis()){
				memoryAnalyzer.start(1*1000,1*1000, replayInfo.getMemPkgName());
			}
			if(replayInfo.isCpuInfoAnalysis()){
				cpuAnalyzer.start(1*1000,1*1000, replayInfo.getMemPkgName());
			}
			//do replaying
			MessageConsoleStream  printer =ConsoleFactory.console.newMessageStream();
			ConsoleFactory.showConsole();
			for (int i = 0; i < replayTimes;) {
				//print on console
				i++;
				printer.println(Messages.INFO_REPLAY_PROGRESS+i+"/"+replayTimes);
				
				niorReplayer.replay(recordFile);
				if(replayInfo.isScreenAnalysis()){
					screenAnalyzer.pullVideo();
				}
				if(replayInfo.isPacketsAnalysis()){
					packetsAnalyzer.pullPacketFile();
				}
			}
			//after replayed
			if(replayInfo.isScreenAnalysis()){
				screenAnalyzer.analysis();
			}
			if(replayInfo.isMemoryInfoAnalysis()){
				memoryAnalyzer.stop();
			}
			if(replayInfo.isCpuInfoAnalysis()){
				cpuAnalyzer.stop();
			}
			
			printer.println(Messages.INFO_REPLAY_FINISHED);
		}
		
	}


	@Override
	public void selectionChanged(IAction action, ISelection selection) {

	}

	public Shell getShell() {
		return shell;
	}

	public void setShell(Shell shell) {
		this.shell = shell;
	}

	public IWorkbenchPart getTargetPart() {
		return targetPart;
	}

	public void setTargetPart(IWorkbenchPart targetPart) {
		this.targetPart = targetPart;
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
		this.targetPart = targetPart;
	}

}
