package org.bitbucket.reliant.util;

public final class Player {
	public final int entity, entityIdx;
	public final float[] vecOrigin;
	public final short health;
	
	public Player(final int entity, final int entityIdx, final float[] vecOrigin, final short health) {
		this.entity = entity;
		this.entityIdx = entityIdx;
		this.vecOrigin = vecOrigin;
		this.health = health;
	}
}