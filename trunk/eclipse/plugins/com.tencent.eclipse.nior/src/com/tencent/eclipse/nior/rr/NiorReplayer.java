package com.tencent.eclipse.nior.rr;

import com.tencent.eclipse.nior.RRException;


/**
 * 
 * abstract replayer for replay user operations.<p>
 * maybe we have a lot kinds of replayers. like monkeyrunner replayer, linux event replayer, adb shell sendevent replayer,etc
 * @author rainliwang
 *
 */
public abstract class NiorReplayer implements Replayable, Stoppable {

	@Override
	public void replay(String fileName) {
		replay(fileName, 1);
	}
	
	@Override
	public void replay(String fileName,int times) {
		if (!isReady()) {
			init();
		}
		doReplay(fileName, times);
	}
	

	@Override
	public boolean stop() {
		if (isReplaying()) {
			throw new RRException("recorder is already stopped");
		}
		return doStop();
	}

	protected abstract boolean doStop();

	protected abstract boolean isReplaying();

	protected abstract void doReplay(String fileName,int times);

	protected abstract boolean isReady();

	/**
	 * initialization the replayer
	 */
	protected abstract void init();

}
