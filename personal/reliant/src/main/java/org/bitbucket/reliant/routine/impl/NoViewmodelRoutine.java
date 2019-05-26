package org.bitbucket.reliant.routine.impl;

import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.memory.InvalidDataException;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.routine.DisplayRoutine;
import org.bitbucket.reliant.util.Item;
import org.bitbucket.reliant.util.SdkUtils;

public final class NoViewmodelRoutine extends DisplayRoutine {
	private int prevDrawViewmodel = -1;

	public NoViewmodelRoutine() {
		super("No Viewmodel", "Prevents the player's viewmodel from rendering.", true, 2000);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int color() {
		// TODO Auto-generated method stub
		return 0xFF00FF00;
	}

	@Override
	public void reset(final boolean shutdown) {
		super.reset(shutdown);
		if (prevDrawViewmodel != -1) {
			final long engineAddr = Reliant.instance.getProcessStream().moduleAddress("engine.dll");
			if (engineAddr != MemoryStream.NULL) {
				if (GameCache.getClientState() != MemoryStream.NULL && GameCache.getInGame() == SdkUtils.SIGNONSTATE_FULL) {
					if (GameCache.getLocalPlayer() != MemoryStream.NULL) {
						Reliant.instance.getProcessStream().write(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_bDrawViewmodel"), prevDrawViewmodel);
					}
				}
			}
		}
	}

	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		if (post) {
			return;
		}
		if (!SdkUtils.entityValid(GameCache.getLocalPlayer())) {
			return;
		}
		final int weaponEntity = SdkUtils.entityByHandle(Reliant.instance.getProcessStream().readInt(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_hActiveWeapon")));
		if (weaponEntity == MemoryStream.NULL) {
			throw new InvalidDataException("weapon_entity");
		}
		if (!Item.scoped(Reliant.instance.getProcessStream().readInt(weaponEntity + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_iItemDefinitionIndex"))) || Reliant.instance.getProcessStream().readShort(weaponEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_zoomLevel")) <= 0) {
			if (prevDrawViewmodel == -1) {
				prevDrawViewmodel = Reliant.instance.getProcessStream().readInt(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_bDrawViewmodel"));
			}
			Reliant.instance.getProcessStream().write(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_bDrawViewmodel"), MemoryStream.FALSE);
		} else {
			reset(false);
		}
	}
}
