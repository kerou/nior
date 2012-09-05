package com.tencent.eclipse.nior.rr.nativelib;

import com.tencent.eclipse.nior.rr.MultiLineShellReceiver;

public class OneLineReceiver extends MultiLineShellReceiver{
	private String info;
	
	@Override
	public void processNewLines(String[] lines) {
		super.processNewLines(lines);
		info=lines[0];
	}

	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
}
