package org.bitbucket.reliant.util;

import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.util.concurrent.Rate;

public final class ClientCmdRate extends Rate<String> {
	public boolean unrestricted;

	public ClientCmdRate(int execMax, long resetDelay) {
		super(execMax, resetDelay);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onExecute(String object) {
		// TODO Auto-generated method stub
		if (unrestricted) {
			Reliant.instance.getProcessStream().clientCmdUnrestricted(object, (byte) 0);
			unrestricted = false;
		} else {
			Reliant.instance.getProcessStream().clientCmd(object);
		}
	}

}
