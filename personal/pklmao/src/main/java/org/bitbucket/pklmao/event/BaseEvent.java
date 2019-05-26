package org.bitbucket.pklmao.event;

public abstract class BaseEvent implements Event {
	private Object source;
	
	public BaseEvent(Object source) {
		this.source = source;
	}

	@Override
	public Object source() {
		return source;
	}
}
