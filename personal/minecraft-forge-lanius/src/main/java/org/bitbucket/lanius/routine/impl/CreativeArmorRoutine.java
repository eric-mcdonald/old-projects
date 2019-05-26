package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.impl.ItemValidData;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.InventoryUtils;
import org.bitbucket.lanius.util.Phase;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

public class CreativeArmorRoutine extends TabbedRoutine implements Hook<ItemValidData> {

	public CreativeArmorRoutine() {
		super(Keyboard.KEY_NONE, true, Tab.PLAYER);
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
		return "Allows you to equip any item as armor.";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Creative Armor";
	}

	@Override
	public void onExecute(ItemValidData data, Phase phase) {
		// TODO Auto-generated method stub
		if (phase.equals(Phase.START) && isEnabled() && Lanius.mc.player.capabilities.isCreativeMode
				&& InventoryUtils.isStackValid(data.getStack())) {
			data.retVal = true;
		}
	}

}
