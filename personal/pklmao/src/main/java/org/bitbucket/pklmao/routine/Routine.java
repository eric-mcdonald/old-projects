package org.bitbucket.pklmao.routine;

import javax.swing.AbstractButton;

import org.bitbucket.pklmao.cfg.Configuration;

public interface Routine {
	String id();
	String name();
	String desc();
	void setEnabled(boolean enabled);
	boolean isEnabled();
	Configuration config();
	AbstractButton toggleBtn();
}
