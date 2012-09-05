package com.tencent.eclipse.nior.rr;

import java.util.ArrayList;
import java.util.List;

import com.android.ddmlib.MultiLineReceiver;
import com.tencent.eclipse.nior.Activator;

public class MultiLineShellReceiver extends MultiLineReceiver {
	private List<String> output=new ArrayList<String>();
	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public void processNewLines(String[] lines) {
		for (String line : lines) {
				output.add(line);
				Activator.logInfo(line);
		}
	}

	public List<String> getOutput() {
		return output;
	}

	public void setOutput(List<String> output) {
		this.output = output;
	}
	
	

}
