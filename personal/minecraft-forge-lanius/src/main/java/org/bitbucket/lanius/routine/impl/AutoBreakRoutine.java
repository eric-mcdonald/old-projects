package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.RoutineUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class AutoBreakRoutine extends TabbedRoutine {

	public AutoBreakRoutine() {
		super(Keyboard.KEY_V, false, Tab.WORLD);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.NOCHEATPLUS, Compatibility.VIAVERSION, Compatibility.NO_VIAVERSION);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Automatically breaks blocks when you hover over them.";
	}

	@Override
	public void init() {
		syncAttackBind();
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Auto-break";
	}

	@SubscribeEvent
	public void onClientTick(final TickEvent.ClientTickEvent clientTickEv) {
		if (!clientTickEv.phase.equals(TickEvent.Phase.START) || Lanius.mc.player == null || Lanius.mc.isGamePaused()) {
			return;
		}
		if (Lanius.mc.currentScreen == null) {
			final BlockPos mouseOverPos;
			final GameType currentGameType;
			if (Lanius.mc.objectMouseOver != null && !Lanius.mc.player.isRowingBoat()
					&& Lanius.mc.objectMouseOver.typeOfHit.equals(RayTraceResult.Type.BLOCK)
					&& !Lanius.mc.world.isAirBlock(mouseOverPos = Lanius.mc.objectMouseOver.getBlockPos())
					&& !(currentGameType = Lanius.mc.playerController.getCurrentGameType()).equals(GameType.ADVENTURE)
					&& Lanius.mc.player.isAllowEdit() && Lanius.mc.world.getWorldBorder().contains(mouseOverPos)
					&& (currentGameType.isCreative() || Lanius.mc.world.getBlockState(mouseOverPos)
							.getBlockHardness(Lanius.mc.world, mouseOverPos) != -1.0F)
					&& !RoutineUtils.enabled("Nuker")) {
				KeyBinding.setKeyBindState(Lanius.mc.gameSettings.keyBindAttack.getKeyCode(), true);
			} else {
				syncAttackBind();
			}
		} else {
			syncAttackBind();
		}
	}

	private void syncAttackBind() {
		if (!GameSettings.isKeyDown(Lanius.mc.gameSettings.keyBindAttack)
				&& Lanius.mc.gameSettings.keyBindAttack.isKeyDown()) {
			KeyBinding.setKeyBindState(Lanius.mc.gameSettings.keyBindAttack.getKeyCode(), false);
		}
	}

}
