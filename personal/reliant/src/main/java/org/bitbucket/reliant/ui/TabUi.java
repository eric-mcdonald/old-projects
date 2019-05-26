package org.bitbucket.reliant.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.reliant.Main;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.FloatOption;
import org.bitbucket.reliant.cfg.IntOption;
import org.bitbucket.reliant.cfg.KeyOption;
import org.bitbucket.reliant.cfg.Option;
import org.bitbucket.reliant.cfg.SoundOption;
import org.bitbucket.reliant.cfg.TextureOption;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.render.Renderer;
import org.bitbucket.reliant.routine.BaseRoutine;

public final class TabUi extends BaseRoutine {
	private final class Tab implements ChangeListener, ItemListener {
		private int selectedComponent;
		private final String name;
		private final Component[] components;
		private int scrollIdx, scrollEnd;

		private Tab(final String name, final Component[] components) {
			this.name = name;
			this.components = components;
			setSelectedComponent(0);
			for (final Component component : components) {
				if (component instanceof JLabel) {
					((JSlider) ((JLabel) component).getLabelFor()).addChangeListener(this);
				} else if (component instanceof JCheckBox) {
					((JCheckBox) component).addItemListener(this);
				}
			}
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			refreshSelected = true;
		}

		private void setSelectedComponent(final int selectedComponent) {
			this.selectedComponent = scrollIdx = selectedComponent;
			final int maxComponents = Math.min(getInt("Maximum Components"), components.length);
			if (components.length - scrollIdx < maxComponents) {
				scrollIdx -= maxComponents - (components.length - scrollIdx);
			}
			scrollEnd = scrollIdx + maxComponents;
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			// TODO Auto-generated method stub
			refreshSelected = true;
		}
	}
	private static boolean refreshSelected;
	private final List<Tab> tabs = new ArrayList<Tab>();
	private int selectedTab;
	private boolean tabOpen;
	private int scrollIdx, scrollEnd;

	private boolean clearTabs; // Eric: Prevents concurrency issues between this routine's resetting and updating

	public TabUi() {
		super("Tab UI", "Renders a tab UI onto the overlay.", false, true, true, 3002, new SoundOption("Press Sound", "Specifies the sound to play when you press a component.", "/audio/ui/press.wav"), new SoundOption("Select Sound", "Specifies the sound to play when you select a component.", "/audio/ui/select.wav"), new BoolOption("Trim", "Specifies whether or not to draw the outline.", true), new IntOption("Maximum Components", "Specifies the maximum amount of components to display.", new ClampedNumber<Integer>(10, 1, 100), 10), new IntOption("Maximum Tabs", "Specifies the maximum amount of tabs to display.", new ClampedNumber<Integer>(10, 1, 100), 10), new FloatOption("Line Width", "Specifies the width of a line.", new ClampedNumber<Float>(1.0F, 0.1F, 10.0F), 0.1F), new FloatOption("Volume", "Specifies the volume of the UI sounds.", new ClampedNumber<Float>(100.0F, 0.0F, 100.0F), 10.0F), new TextureOption("Texture", "Specifies the image file to use as a texture.", ""), new IntOption("X-coordinate", "Specifies the x-coordinate on your screen.", new ClampedNumber<Integer>(2, 0, 10000), 100), new IntOption("Y-coordinate", "Specifies the y-coordinate on your screen.", new ClampedNumber<Integer>(330 + 2 + 15, 0, 10000), 100), new KeyOption("Increment Key", "Specifies the key used to increment a slider's value.", 0xBB), new KeyOption("Decrement Key", "Specifies the key used to decrement a slider's value.", 0xBD), new KeyOption("Up Key", "Specifies the key used to navigate up.", 0x26), new KeyOption("Down Key", "Specifies the key used to navigate down.", 0x28), new KeyOption("Left Key", "Specifies the key used to navigate left.", 0x25), new KeyOption("Right Key", "Specifies the key used to navigate right.", 0x27));
		// TODO Auto-generated constructor stub
	}
	@Override
	public void reset(final boolean shutdown) {
		super.reset(shutdown);
		setSelectedTab();
		clearTabs = true;
		tabOpen = false;
	}
	private void setScrollIdx(final int scrollIdx) {
		this.scrollIdx = scrollIdx;
		final int maxTabs = Math.min(getInt("Maximum Tabs"), tabs.size());
		if (tabs.size() - this.scrollIdx < maxTabs) {
			this.scrollIdx -= maxTabs - (tabs.size() - this.scrollIdx);
		}
		scrollEnd = this.scrollIdx + maxTabs;
	}
	private void setSelectedTab() {
		selectedTab = 0;
		setScrollIdx(selectedTab);
	}
	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		if (!post) {
			return;
		}
		final ConfigPanel configGui = Reliant.instance.getConfigGui();
		if (configGui == null) {
			return;
		}
		if (clearTabs) {
			tabs.clear();
			clearTabs = false;
		}
		for (final Tab tab : tabs) {
			if (refreshSelected) {
				tab.setSelectedComponent(tab.selectedComponent);
				setScrollIdx(selectedTab);
			}
		}
		refreshSelected = false;
		if (tabs.isEmpty()) {
			for (final Map.Entry<String, Component> tabEntry : configGui.tabMap.entrySet()) {
				final Component component = tabEntry.getValue();
				if (!(component instanceof Container)) {
					continue;
				}
				final List<Component> validComponents = new ArrayList<Component>(); // TODO(Eric) All components should be valid
				synchronized (component.getTreeLock()) {
					for (final Component child : ((Container) component).getComponents()) {
						if (!(child instanceof AbstractButton) && (!(child instanceof JLabel) || !(((JLabel) child).getLabelFor() instanceof JSlider))) {
							continue;
						}
						validComponents.add(child);
					}
					if (validComponents.isEmpty()) {
						continue;
					}
					tabs.add(new Tab(tabEntry.getKey(), validComponents.toArray(new Component[0])));
				}
			}
			if (tabs.isEmpty()) {
				return;
			}
			Collections.sort(tabs, new Comparator<Tab>() {

				@Override
				public int compare(Tab o1, Tab o2) {
					// TODO Auto-generated method stub
					return StringUtils.alphabetCmp.compare(o1.name, o2.name);
				}

			});
			setSelectedTab();
		}
		if (tabOpen) {
			if (getKeyOption("Down Key").keyPressed(false)) {
				tabs.get(selectedTab).setSelectedComponent(tabs.get(selectedTab).selectedComponent + 1);
				if (tabs.get(selectedTab).selectedComponent >= tabs.get(selectedTab).components.length) {
					tabs.get(selectedTab).setSelectedComponent(0);
				}
				getSoundOpt("Select Sound").play(getFloat("Volume"));
			}
			if (getKeyOption("Up Key").keyPressed(false)) {
				tabs.get(selectedTab).setSelectedComponent(tabs.get(selectedTab).selectedComponent - 1);
				if (tabs.get(selectedTab).selectedComponent < 0) {
					tabs.get(selectedTab).setSelectedComponent(tabs.get(selectedTab).components.length - 1);
				}
				getSoundOpt("Select Sound").play(getFloat("Volume"));
			}
			if (getKeyOption("Right Key").keyPressed(false)) {
				// TODO(Eric) Handle other component types
				if (tabs.get(selectedTab).components[tabs.get(selectedTab).selectedComponent] instanceof AbstractButton) {
					final AbstractButton button = (AbstractButton) tabs.get(selectedTab).components[tabs.get(selectedTab).selectedComponent];
					if (button.isEnabled()) {
						button.doClick();
						getSoundOpt("Press Sound").play(getFloat("Volume"));
					}
				}
			}
			if (tabs.get(selectedTab).components[tabs.get(selectedTab).selectedComponent] instanceof JLabel) {
				final JSlider slider = (JSlider) ((JLabel) tabs.get(selectedTab).components[tabs.get(selectedTab).selectedComponent]).getLabelFor();
				final Option<?> sliderOpt = Reliant.instance.getConfigGui().getOptionByComponent(slider);
				final float DECIMAL_INC = 0.1F;
				final int INTEGER_INC = 1;
				if (getKeyOption("Increment Key").keyPressed(false)) {
					// Eric: Floating-point numbers need special handling to avoid precision errors
					boolean changed;
					if (sliderOpt instanceof FloatOption) {
						final FloatOption decimalOpt = (FloatOption) sliderOpt;
						final float prevValue = decimalOpt.getValue().getValue();
						decimalOpt.setValue(decimalOpt.createValue(decimalOpt.getValue().getValue() + DECIMAL_INC));
						changed = prevValue != decimalOpt.getValue().getValue();
					} else {
						final int prevValue = slider.getValue();
						slider.setValue(slider.getValue() + INTEGER_INC);
						changed = prevValue != slider.getValue();
					}
					if (changed) {
						getSoundOpt("Press Sound").play(getFloat("Volume"));
					}
				}
				if (getKeyOption("Decrement Key").keyPressed(false)) {
					boolean changed;
					if (sliderOpt instanceof FloatOption) {
						final FloatOption decimalOpt = (FloatOption) sliderOpt;
						final float prevValue = decimalOpt.getValue().getValue();
						decimalOpt.setValue(decimalOpt.createValue(decimalOpt.getValue().getValue() - DECIMAL_INC));
						changed = prevValue != decimalOpt.getValue().getValue();
					} else {
						final int prevValue = slider.getValue();
						slider.setValue(slider.getValue() - INTEGER_INC);
						changed = prevValue != slider.getValue();
					}
					if (changed) {
						getSoundOpt("Press Sound").play(getFloat("Volume"));
					}
				}
			}
			if (getKeyOption("Left Key").keyPressed(false)) {
				tabOpen = false;
				getSoundOpt("Select Sound").play(getFloat("Volume"));
			}
		} else {
			if (getKeyOption("Down Key").keyPressed(false)) {
				if (++selectedTab >= tabs.size()) {
					selectedTab = 0;
				}
				setScrollIdx(selectedTab);
				getSoundOpt("Select Sound").play(getFloat("Volume"));
			}
			if (getKeyOption("Up Key").keyPressed(false)) {
				if (--selectedTab < 0) {
					selectedTab = tabs.size() - 1;
				}
				setScrollIdx(selectedTab);
				getSoundOpt("Select Sound").play(getFloat("Volume"));
			}
			if (getKeyOption("Right Key").keyPressed(false)) {
				tabOpen = true;
				getSoundOpt("Select Sound").play(getFloat("Volume"));
			}
		}
		int boxWidth = 0, boxHeight = 0;
		final String font = Reliant.instance.getGuiFont();
		final int FONT_SZ = 24;
		final boolean SHADOW = true;
		final Renderer renderer = Reliant.instance.getRenderer();
		for (int i = 0; i < tabs.size(); i++) {
			final int[] textSz = renderer.textSize("> " + tabs.get(i).name, font, FONT_SZ, SHADOW);
			if (textSz != null) {
				if (textSz[0] > boxWidth) {
					boxWidth = textSz[0];
				}
				if (textSz[1] > boxHeight) {
					boxHeight = textSz[1];
				}
			}
		}
		int left = getInt("X-coordinate"), top = getInt("Y-coordinate");
		if (renderer.hasBorder(Main.TARGET_WINDOW_CLASS)) {
			left += Renderer.BORDER_WIDTH;
			top += Renderer.BORDER_HEIGHT;
		}
		final int SPACING = 4;
		final float lineWidth = getFloat("Line Width");
		final int maxTabs = getInt("Maximum Tabs");
		final float bottom = top + boxHeight * Math.min(maxTabs, tabs.size()), right = left + boxWidth + SPACING + SPACING;
		final boolean trim = getBoolean("Trim");
		if (maxTabs < tabs.size() && scrollIdx > 0) {
			renderer.drawQuad(left, top - boxHeight / 2.0F - SPACING, right, top - SPACING, 0x80000000, MemoryStream.NULL);
			if (trim) {
				renderer.drawOutline(left, top - boxHeight / 2.0F - SPACING, right - left, boxHeight / 2.0F, 0xFF000000, lineWidth);
			}
		}
		if (maxTabs < tabs.size() && scrollEnd < tabs.size()) {
			renderer.drawQuad(left, bottom + SPACING, right, bottom + SPACING + boxHeight / 2.0F, 0x80000000, MemoryStream.NULL);
			if (trim) {
				renderer.drawOutline(left, bottom + SPACING, right - left, boxHeight / 2.0F, 0xFF000000, lineWidth);
			}
		}
		final long texture = getTexture("Texture");
		if (texture != MemoryStream.NULL) {
			renderer.drawQuad(left, top, right, bottom, 0xFFFFFFFF, texture);
		}
		int boxY1 = top;
		for (int i = scrollIdx; i < scrollEnd; i++) {
			final Tab tab = tabs.get(i);
			final boolean equalsSelected = tabs.get(selectedTab).equals(tab);
			renderer.drawQuad(left, boxY1, left + boxWidth + SPACING + SPACING, boxY1 + boxHeight, equalsSelected ? 0x80808080 : 0x80000000, MemoryStream.NULL);
			if (equalsSelected && trim) {
				renderer.drawOutline(left, boxY1, boxWidth + SPACING + SPACING, boxHeight, 0xFF000000, lineWidth);
			}
			renderer.drawText(tabOpen && equalsSelected ? "> " + tab.name : tab.name, left + SPACING, boxY1, 0xFFFFFFFF, font, FONT_SZ, SHADOW);
			boxY1 += boxHeight;
			if (tabOpen && equalsSelected) {
				int childWidth = 0, childHeight = 0;
				for (int j = 0; j < tab.components.length; j++) {
					final Component component = tab.components[j];
					final int[] textSz = component instanceof AbstractButton ? renderer.textSize("> " + ((AbstractButton) component).getText(), font, FONT_SZ, SHADOW) : renderer.textSize(((JLabel) component).getText(), font, FONT_SZ, SHADOW);
					if (textSz != null) {
						if (textSz[0] > childWidth) {
							childWidth = textSz[0];
						}
						if (textSz[1] > childHeight) {
							childHeight = textSz[1];
						}
					}
				}
				final int absMaxComponents = getInt("Maximum Components");
				if (absMaxComponents < tab.components.length && tab.scrollIdx > 0) {
					renderer.drawQuad(left + boxWidth + SPACING + SPACING + SPACING, top - childHeight / 2.0F - SPACING, (left + boxWidth + SPACING + SPACING + SPACING) + childWidth + SPACING + SPACING, top - SPACING, 0x80000000, MemoryStream.NULL);
					if (trim) {
						renderer.drawOutline(left + boxWidth + SPACING + SPACING + SPACING, top - childHeight / 2.0F - SPACING, childWidth + SPACING + SPACING, childHeight / 2.0F, 0xFF000000, lineWidth);
					}
				}
				final int maxComponents = Math.min(absMaxComponents, tab.components.length);
				if (absMaxComponents < tab.components.length && tab.scrollEnd < tab.components.length) {
					renderer.drawQuad(left + boxWidth + SPACING + SPACING + SPACING, top + childHeight * maxComponents + SPACING, (left + boxWidth + SPACING + SPACING + SPACING) + childWidth + SPACING + SPACING, top + childHeight * maxComponents + SPACING + childHeight / 2.0F, 0x80000000, MemoryStream.NULL);
					if (trim) {
						renderer.drawOutline(left + boxWidth + SPACING + SPACING + SPACING, top + childHeight * maxComponents + SPACING, childWidth + SPACING + SPACING, childHeight / 2.0F, 0xFF000000, lineWidth);
					}
				}
				if (texture != MemoryStream.NULL) {
					renderer.drawQuad(left + boxWidth + SPACING + SPACING + SPACING, top, (left + boxWidth + SPACING + SPACING + SPACING) + childWidth + SPACING + SPACING, top + childHeight * maxComponents, 0xFFFFFFFF, texture);
				}
				int childY1 = top;
				for (int j = tab.scrollIdx; j < tab.scrollEnd; j++) {
					final Component component = tab.components[j];
					renderer.drawQuad(left + boxWidth + SPACING + SPACING + SPACING, childY1, (left + boxWidth + SPACING + SPACING + SPACING) + childWidth + SPACING + SPACING, childY1 + childHeight, tab.components[tab.selectedComponent].equals(component) ? 0x80808080 : 0x80000000, MemoryStream.NULL);
					boolean drawnOutline = false;
					if (component instanceof AbstractButton) {
						final AbstractButton btnComponent = (AbstractButton) component;
						final String text = btnComponent.getText();
						renderer.drawText(btnComponent.isSelected() ? "> " + text : text, (left + boxWidth + SPACING + SPACING + SPACING) + SPACING, childY1, 0xFFFFFFFF, font, FONT_SZ, SHADOW);
						if (!btnComponent.isEnabled()) {
							renderer.drawQuad(left + boxWidth + SPACING + SPACING + SPACING, childY1, (left + boxWidth + SPACING + SPACING + SPACING) + childWidth + SPACING + SPACING, childY1 + childHeight, 0x80404040, MemoryStream.NULL);
						}
						if (tab.components[tab.selectedComponent].equals(component) || !btnComponent.isEnabled()) {
							if (trim) {
								renderer.drawOutline(left + boxWidth + SPACING + SPACING + SPACING, childY1, childWidth + SPACING + SPACING, childHeight, 0xFF000000, lineWidth);
							}
							drawnOutline = true;
						}
					} else if (component instanceof JLabel) {
						renderer.drawText(((JLabel) component).getText(), (left + boxWidth + SPACING + SPACING + SPACING) + SPACING, childY1, 0xFFFFFFFF, font, FONT_SZ, SHADOW);
					}
					if (tab.components[tab.selectedComponent].equals(component) && !drawnOutline && trim) {
						renderer.drawOutline(left + boxWidth + SPACING + SPACING + SPACING, childY1, childWidth + SPACING + SPACING, childHeight, 0xFF000000, lineWidth);
					}
					childY1 += childHeight;
				}
				if (trim) {
					renderer.drawOutline(left + boxWidth + SPACING + SPACING + SPACING, top, childWidth + SPACING + SPACING, childY1 - top, 0xFF000000, lineWidth);
				}
			}
		}
		if (trim) {
			renderer.drawOutline(left, top, boxWidth + SPACING + SPACING, boxY1 - top, 0xFF000000, lineWidth);
		}
	}
}
