package org.bitbucket.reliant.test;

import org.bitbucket.eric_generic.lang.time.BasicTimer;
import org.bitbucket.eric_generic.lang.time.Timer;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.routine.BaseRoutine;

public final class TestClientCmdRoutine extends BaseRoutine {
	private final Timer delayTimer = new BasicTimer(1000L);

	public TestClientCmdRoutine() {
		super("test_client_cmd", "Tests if ClientCMD is executed correctly.", true, 4000);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		if (delayTimer.delayPassed()) {
			Reliant.instance.getProcessStream().clientCmdUnrestricted("say test", (byte) 0);
			delayTimer.setStartTime();
		}
	}

}
