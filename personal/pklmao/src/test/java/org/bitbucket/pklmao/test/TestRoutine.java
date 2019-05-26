package org.bitbucket.pklmao.test;

import org.bitbucket.pklmao.cfg.BasicConfiguration;
import org.bitbucket.pklmao.cfg.BoolOption;
import org.bitbucket.pklmao.event.SubscribeEvent;
import org.bitbucket.pklmao.event.TickEvent;
import org.bitbucket.pklmao.routine.BaseRoutine;

public class TestRoutine extends BaseRoutine {

	public TestRoutine() {
		super("test", "test.routine.test_routine.name", "test.routine.test_routine.desc");
		BasicConfiguration cfg = (BasicConfiguration) config();
		cfg.addOption(new BoolOption("test", "cfg.impl.test_routine.opt.name.test_opt", "cfg.impl.test_routine.opt.desc.test_opt", true, false));
	}

	@SubscribeEvent
	public void onTickPost(TickEvent event) {
		if (event.getPhase().equals(TickEvent.Phase.END)) {
			System.out.println(((BasicConfiguration) config()).getOptById("test"));
		}
	}
}
