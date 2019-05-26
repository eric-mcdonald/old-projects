package org.bitbucket.lanius.cfg;

public final class ValueAlreadyRegisteredException extends RuntimeException {
	public ValueAlreadyRegisteredException(final String name, final String category) {
		super("A value map for category \"" + category + "\" already contains value \"" + name + "\"");
	}
}
