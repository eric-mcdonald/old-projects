package org.bitbucket.reliant.test;

import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.routine.BaseRoutine;
import org.bitbucket.reliant.util.SdkUtils;

public final class TestFovRoutine extends BaseRoutine {

	public TestFovRoutine() {
		super("test_write_fov", "Writes to the local player's FOV.", true, 4001);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		if (post || !SdkUtils.entityAlive(GameCache.getLocalPlayer())) {
			return;
		}
		Reliant.instance.getProcessStream().write(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iFOVStart") - 0x4, 130);
	}

}
