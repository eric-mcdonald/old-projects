package org.bitbucket.lanius.routine;

import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cfg.ConfigContainer;
import org.bitbucket.lanius.util.CommandUtils;
import org.bitbucket.lanius.util.RoutineUtils;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public abstract class Routine extends ConfigContainer {
	public enum Compatibility {
		NO_VIAVERSION("No ViaVersion"), NOCHEATPLUS("NoCheatPlus"), VIAVERSION("ViaVersion");
		private final String name;

		private Compatibility(final String name) {
			this.name = name;
		}
	}

	public static final String CFG_CATEGORY = "Routine";
	@Deprecated
	public static final int NO_COLOR = -1;

	protected static final int TEST_COLOR = 0x808080;

	private boolean enabled;
	private boolean hidden;

	public final KeyBinding keyBind;

	public Routine(final int defaultKey, boolean hidden) {
		final String name = name();
		this.hidden = hidden;
		keyBind = new KeyBinding(name, defaultKey, "Routines");
	}

	@Override
	public final String category() {
		return name();
	}

	public final void changeKey(final int newKey) {
		keyBind.setKeyCode(newKey);
	}

	public abstract int color();

	public abstract HashSet<Compatibility> compatibleWith();

	public abstract String description();

	public String displayData() {
		return null;
	}

	@Override
	public final boolean equals(Object obj) {
		if (!(obj instanceof Routine)) {
			return false;
		}
		Routine cmpRoutine = (Routine) obj;
		return cmpRoutine.keyBind.equals(keyBind) && cmpRoutine.enabled == enabled && cmpRoutine.name().equals(name())
				&& cmpRoutine.description().equals(description()) && cmpRoutine.toString().equals(toString())
				&& cmpRoutine.isEnabled() == isEnabled() && cmpRoutine.isHidden() == isHidden();
	}

	public void init() {
	}

	public final boolean isEnabled() {
		return enabled;
	}

	public boolean isHidden() {
		return hidden;
	}

	public abstract String name();

	@SubscribeEvent
	public void onUnload(final WorldEvent.Unload unloadEv) {
		init();
	}

	@Override
	public void registerValues() {
		// Eric: empty because a routine does not need to have any configuration
		// values
	}

	/**
	 * Toggles this routine on/off. Note that you must invoke super#setEnabled if
	 * you want to override this method.
	 */
	public void setEnabled() {
		enabled = !enabled;
		if (enabled) {
			MinecraftForge.EVENT_BUS.register(this);
		} else {
			MinecraftForge.EVENT_BUS.unregister(this);
			init();
		}
		if (enabled) {
			if (!compatibleWith().contains(Compatibility.NOCHEATPLUS) && RoutineUtils.ncpEnabled()) {
				CommandUtils.addText(Lanius.mc.player,
						name() + " does not bypass " + Compatibility.NOCHEATPLUS.name + ".");
			}
			if (!compatibleWith().contains(Compatibility.VIAVERSION) && RoutineUtils.viaVersionEnabled()) {
				CommandUtils.addText(Lanius.mc.player,
						name() + " does not work with " + Compatibility.VIAVERSION.name + ".");
			} else if (!compatibleWith().contains(Compatibility.NO_VIAVERSION) && !RoutineUtils.viaVersionEnabled()) {
				CommandUtils.addText(Lanius.mc.player, name() + " does not work without ViaVersion.");
			}
		}
		if (Lanius.getInstance().getModCfg().getBoolean("Toggle Messages", CFG_CATEGORY, false,
				"Determines whether or not to display a toggle message when a routine is toggled.")) {
			CommandUtils.addEnabledMsg(Lanius.mc.player, this);
		}
	}

	public void setHidden() {
		hidden = !hidden;
	}

	@Override
	public final String toString() {
		return name();
	}
}
