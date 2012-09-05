package com.tencent.eclipse.nior.rr;

/**
 * 
 * abstract recorder for record user operations.<p>
 * maybe we have a lot kinds of recorders. like monkeyrunner recorder, linux event recorder, adb shell getevent recorder,etc
 * @author rainliwang
 *
 */
public abstract class NiorRecorder implements Recordable, Stoppable {

	@Override
	public Record record() {
		if (!isReady()) {
			init();
		}
		return doRecord();
	}

	@Override
	public boolean stop() {
		//TODO temporarily comment the codes,currently we don't check if recorder is work,just kill it
//		if (!isRecording()) {
//			throw new RRException("recorder is already stopped");
//		}
		return doStop();
	}

	protected abstract boolean doStop();

	protected abstract boolean isRecording();

	protected abstract Record doRecord();

	protected abstract boolean isReady();

	/**
	 * initialization the recorder
	 */
	protected abstract void init();

}
