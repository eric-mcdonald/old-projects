package org.bitbucket.reliant.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.Configuration;
import org.bitbucket.reliant.cfg.NumberOption;
import org.bitbucket.reliant.cfg.Option;
import org.bitbucket.reliant.routine.Routine;

public final class ConfigPanel extends JTabbedPane implements ItemListener, ActionListener {

	/**
	 * The default serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	private final Map<JCheckBox, Routine> routineBoxMap = new HashMap<JCheckBox, Routine>();
	private final Map<String, Object[]> resetBtnMap = new HashMap<String, Object[]>();
	final Map<String, Component> tabMap = new HashMap<String, Component>();

	public ConfigPanel() {
		for (final Configuration config : Reliant.instance.getConfigRegistry().objects()) {
			final List<Option<?>> options = config.getOptions();
			if (options.isEmpty()) {
				continue;
			}
			final JPanel configPanel = new JPanel();
			configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.PAGE_AXIS));
			if (config instanceof Routine) {
				final Routine routine = (Routine) config;
				final JCheckBox routineBox = routine.guiComponent();
				routineBoxMap.put(routineBox, routine);
				routineBox.addItemListener(this);
				configPanel.add(routineBox, BorderLayout.CENTER);
			}
			for (final Option<?> option : options) {
				final Component guiComponent = option.guiComponent();
				if (!(option.getValue() instanceof Boolean)) {
					final JLabel componentLabel = new JLabel(option.name());
					if (option instanceof NumberOption<?>) {
						updateNumLabel(componentLabel, (NumberOption<?>) option);
					}
					componentLabel.setLabelFor(guiComponent);
					configPanel.add(componentLabel, BorderLayout.CENTER);
				}
				configPanel.add(guiComponent, BorderLayout.CENTER);
			}
			final JButton resetBtn = new JButton("Reset");
			resetBtn.setToolTipText("Resets " + config.name() + "'s options to their default values.");
			resetBtn.setActionCommand(StringUtils.configName("Reset " + config.name()));
			resetBtnMap.put(resetBtn.getActionCommand(), new Object[] {resetBtn, config});
			resetBtn.addActionListener(this);
			configPanel.add(resetBtn, BorderLayout.CENTER);
			putTab(config.name(), configPanel);
		}
		for (final Configuration config : Reliant.instance.getConfigRegistry().objects()) {
			final List<Option<?>> options = config.getOptions();
			if (options.isEmpty()) {
				continue;
			}
			boolean allDefault = true;
			for (final Option<?> option : options) {
				if (!option.getValue().equals(option.getDefault())) {
					toggleResetBtn(config, true);
					allDefault = false;
					break;
				}
			}
			if (allDefault) {
				disableResetBtn(config);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		final Object[] configBtn = resetBtnMap.get(e.getActionCommand());
		((Configuration) configBtn[1]).loadDefaults();
		((JButton) configBtn[0]).setEnabled(false);
	}

	public void disableResetBtn(final Configuration config) {
		final List<Option<?>> options = config.getOptions();
		if (options.isEmpty()) {
			return;
		}
		boolean willDisable = true;
		for (final Option<?> option : options) {
			if (!option.getValue().equals(option.getDefault())) {
				willDisable = false;
				break;
			}
		}
		if (willDisable) {
			toggleResetBtn(config, false);
		}
	}

	public Option<?> getOptionByComponent(final Component component) {
		for (final Configuration config : Reliant.instance.getConfigRegistry().objects()) {
			final List<Option<?>> options = config.getOptions();
			if (options.isEmpty()) {
				continue;
			}
			for (final Option<?> option : options) {
				if (option.guiComponent().equals(component)) {
					return option;
				}
			}
		}
		return null;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		final ItemSelectable itemSelectable = e.getItemSelectable();
		if (routineBoxMap.containsKey(itemSelectable)) {
			final Routine routine = routineBoxMap.get(itemSelectable);
			final boolean selected = e.getStateChange() == ItemEvent.SELECTED;
			if (selected && !routine.isEnabled() || !selected && routine.isEnabled()) {
				routine.setEnabled();
			}
		}
	}

	private Component putTab(final String title, final Component component) {
		final JScrollPane scrollComponent = new JScrollPane(component);
		scrollComponent.setPreferredSize(new Dimension(512, 512));
		addTab(title, scrollComponent);
		return tabMap.put(title, component);
	}

	public void toggleResetBtn(final Configuration config, final boolean enable) {
		for (final Object[] resetBtn : resetBtnMap.values()) {
			if (resetBtn[1].equals(config)) {
				((AbstractButton) resetBtn[0]).setEnabled(enable);
				break;
			}
		}
	}

	private void updateNumLabel(final JLabel cfgLabel, final NumberOption<?> option) {
		cfgLabel.setText(option.name() + ": " + StringUtils.formatNum(((ClampedNumber<?>) option.getValue()).getValue().doubleValue(), "%,.1f"));
	}
	public void updateNumLabel(final NumberOption<?> option) {
		final Component optionComponent = option.guiComponent();
		outer_loop:
			for (final Map.Entry<String, Component> tabEntry : tabMap.entrySet()) {
				final Component tabComponent = tabEntry.getValue();
				if (!(tabComponent instanceof Container)) {
					continue;
				}
				final Container tabContainer = (Container) tabComponent;
				synchronized (tabContainer.getTreeLock()) {
					for (final Component component : tabContainer.getComponents()) {
						if (!(component instanceof JLabel)) {
							continue;
						}
						final JLabel label = (JLabel) component;
						final Component labelFor = label.getLabelFor();
						if (labelFor != null && labelFor.equals(optionComponent)) {
							updateNumLabel(label, option);
							break outer_loop;
						}
					}
				}
			}
	}
}
