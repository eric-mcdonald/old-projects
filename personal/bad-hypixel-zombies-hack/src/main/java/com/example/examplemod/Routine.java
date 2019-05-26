package com.example.examplemod;

public abstract class Routine {
	boolean enabled;
	final String name;
	final int key;
	
	Routine(final String name, final int key, final boolean enabled) {
		this.name = name;
		this.key = key;
		this.enabled = enabled;
	}
	
	void preUpdate() {}
	void postUpdate() {}
	void cleanup() {}
	void render3d(final float partialTicks) {}
}
