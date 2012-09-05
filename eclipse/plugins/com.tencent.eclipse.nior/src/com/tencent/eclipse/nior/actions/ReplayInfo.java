package com.tencent.eclipse.nior.actions;

public class ReplayInfo {
	private String recordFile;
	private int replayTimes;
	private boolean screenAnalysis;
	private boolean packetsAnalysis;
	private boolean cpuInfoAnalysis;
	private boolean memoryInfoAnalysis;
	private String memPkgName;
	
	
	public String getMemPkgName() {
		return memPkgName;
	}
	public void setMemPkgName(String memPkgName) {
		this.memPkgName = memPkgName;
	}
	public int getReplayTimes() {
		return replayTimes;
	}
	public void setReplayTimes(int replayTimes) {
		this.replayTimes = replayTimes;
	}
	public boolean isScreenAnalysis() {
		return screenAnalysis;
	}
	public void setScreenAnalysis(boolean screenAnalysis) {
		this.screenAnalysis = screenAnalysis;
	}
	public String getRecordFile() {
		return recordFile;
	}
	public void setRecordFile(String recordFile) {
		this.recordFile = recordFile;
	}
	public boolean isPacketsAnalysis() {
		return packetsAnalysis;
	}
	public void setPacketsAnalysis(boolean packetsAnalysis) {
		this.packetsAnalysis = packetsAnalysis;
	}
	public boolean isCpuInfoAnalysis() {
		return cpuInfoAnalysis;
	}
	public void setCpuInfoAnalysis(boolean cpuInfoAnalysis) {
		this.cpuInfoAnalysis = cpuInfoAnalysis;
	}
	public boolean isMemoryInfoAnalysis() {
		return memoryInfoAnalysis;
	}
	public void setMemoryInfoAnalysis(boolean memoryInfoAnalysis) {
		this.memoryInfoAnalysis = memoryInfoAnalysis;
	}
	
	
	
}
