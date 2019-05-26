package org.bitbucket.reliant.routine;

import java.awt.CheckboxMenuItem;
import java.awt.ItemSelectable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;

import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BasicConfiguration;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.KeyOption;
import org.bitbucket.reliant.cfg.Option;
import org.bitbucket.reliant.ui.TrayListener;

public abstract class BaseRoutine extends BasicConfiguration implements Routine {
	private static List<Option<?>> getDefaultOptions(final boolean defaultEnabled, final int defaultKey, final Option<?>[] options) {
		final List<Option<?>> optionList = new ArrayList<Option<?>>();
		optionList.add(new BoolOption("Enable on Startup", "Specifies whether or not to enable this routine on startup.", defaultEnabled));
		optionList.add(new KeyOption("Key", "Specifies the key that should be held down to execute this routine. Set to 0 for none.", defaultKey));
		return optionList;
	}
	private final String description;
	private boolean enabled;
	private final boolean inGameOnly, ignoresMouse;
	private final int priority;

	private final JCheckBox guiComponent;

	public BaseRoutine(final String name, final String description, final boolean inGameOnly, final boolean ignoresMouse, final boolean defaultEnabled, final int defaultKey, final int priority, final Option<?>... options) {
		super(name, null);
		final List<Option<?>> defaultOpts = getDefaultOptions(defaultEnabled, defaultKey, options);
		if (options != null) {
			Arrays.sort(options, new Comparator<Option<?>>() {

				@Override
				public int compare(Option<?> o1, Option<?> o2) {
					// TODO Auto-generated method stub
					return String.CASE_INSENSITIVE_ORDER.compare(o1.name(), o2.name());
				}
			});
			for (final Option<?> option : options) {
				defaultOpts.add(option);
			}
		}
		getOptions().addAll(defaultOpts);
		this.description = description;
		guiComponent = new JCheckBox("Toggle");
		guiComponent.setToolTipText("Toggles this routine's enabled state.");
		this.inGameOnly = inGameOnly;
		this.ignoresMouse = ignoresMouse;
		this.priority = priority;
		if (defaultEnabled) {
			setEnabled();
		}
		// TODO Auto-generated constructor stub
	}

	public BaseRoutine(final String name, final String description, final boolean inGameOnly, final boolean ignoresMouse, final boolean defaultEnabled, final int priority, final Option<?>... options) {
		this(name, description, inGameOnly, ignoresMouse, defaultEnabled, 0x0, priority, options);
	}

	public BaseRoutine(final String name, final String description, final boolean inGameOnly, final int priority, final Option<?>... options) {
		this(name, description, inGameOnly, false, false, priority, options);
	}

	@Override
	public final String description() {
		// TODO Auto-generated method stub
		return description;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public final JCheckBox guiComponent() {
		return guiComponent;
	}

	@Override
	public final boolean ignoresMouse() {
		return ignoresMouse;
	}

	@Override
	public final boolean inGameOnly() {
		return inGameOnly;
	}

	@Override
	public final boolean isEnabled() {
		// TODO Auto-generated method stub
		return enabled;
	}

	protected void onEnabled() {
		// Eric: Empty implementation; use the reset method for checking if the routine was disabled.
	}
	@Override
	public void reset(final boolean shutdown) {
		// TODO Auto-generated method stub
		// Eric: Empty implementation for in case a routine does not have to be reset.
	}
	@Override
	public final void setEnabled() {
		// TODO Auto-generated method stub
		enabled = !enabled;
		// Eric: Update the GUI buttons
		guiComponent.setSelected(isEnabled());
		final TrayListener trayListener = Reliant.instance.getTrayListener();
		if (trayListener != null) {
			for (final Map.Entry<ItemSelectable, Routine> trayBoxEntry : trayListener.getTrayBoxEntries()) {
				final ItemSelectable trayBoxSelectable = trayBoxEntry.getKey();
				if (trayBoxEntry.getValue().equals(this) && trayBoxSelectable instanceof CheckboxMenuItem) {
					final CheckboxMenuItem trayBoxItem = (CheckboxMenuItem) trayBoxSelectable;
					trayBoxItem.setState(isEnabled());
					break;
				}
			}
		}
		if (enabled) {
			onEnabled();
		}
	}
	public void startupToggle() {
		final boolean startupEnabled = getBoolean("Enable on Startup");
		if (enabled && !startupEnabled || !enabled && startupEnabled) {
			setEnabled();
		}
	}
}
