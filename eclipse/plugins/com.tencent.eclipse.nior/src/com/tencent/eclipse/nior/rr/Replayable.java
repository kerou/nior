package com.tencent.eclipse.nior.rr;

public interface Replayable {

	void replay(String fileName);

	void replay(String fileName, int times);

}
