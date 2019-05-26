package org.bitbucket.lanius.gui;

import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cfg.ConfigContainer;
import org.bitbucket.lanius.cfg.Configurable;
import org.bitbucket.lanius.routine.Routine;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class ClickGui extends GuiScreen implements Configurable {
	private static final File guiFile = new File(Lanius.dataDir, "click_gui.dat");
	public static final ClickGui instance = new ClickGui();
	static final int MAX_CHILDREN = 10;
	private FrameComponent dragFrame;
	private Point dragPoint;
	public final List<FrameComponent> frames = new ArrayList<FrameComponent>(), orderedFrames;
	private boolean movedFrame, loadedGui;

	public ClickGui() {
		int tabWidth = 0;
		for (final Tab guiTab : Tab.values()) {
			if (1 + Lanius.mc.fontRenderer.getStringWidth(guiTab.name) + 1 > tabWidth) {
				tabWidth = 1 + Lanius.mc.fontRenderer.getStringWidth(guiTab.name) + 1;
			}
			for (final Routine routine : guiTab.routines()) {
				if (1 + Lanius.mc.fontRenderer.getStringWidth(routine.name()) + 4 > tabWidth) {
					tabWidth = 1 + Lanius.mc.fontRenderer.getStringWidth(routine.name()) + 4;
				}
			}
		}
		for (final ConfigContainer cfgContainer : Lanius.getInstance().getCfgContainerRegistry().objects()) {
			if (cfgContainer.boolNames().isEmpty()) {
				continue;
			}
			if (1 + Lanius.mc.fontRenderer.getStringWidth(cfgContainer.category()) + 1 > tabWidth) {
				tabWidth = 1 + Lanius.mc.fontRenderer.getStringWidth(cfgContainer.category()) + 1;
			}
			for (final String cfgValue : cfgContainer.boolNames()) {
				if (1 + Lanius.mc.fontRenderer.getStringWidth(cfgValue) + 1 > tabWidth) {
					tabWidth = 1 + Lanius.mc.fontRenderer.getStringWidth(cfgValue) + 1;
				}
			}
			for (final String cfgValue : cfgContainer.floatNames()) {
				if (1 + Lanius.mc.fontRenderer.getStringWidth(cfgValue) + 1 > tabWidth) {
					tabWidth = 1 + Lanius.mc.fontRenderer.getStringWidth(cfgValue) + 1;
				}
			}
			for (final String cfgValue : cfgContainer.intNames()) {
				if (1 + Lanius.mc.fontRenderer.getStringWidth(cfgValue) + 1 > tabWidth) {
					tabWidth = 1 + Lanius.mc.fontRenderer.getStringWidth(cfgValue) + 1;
				}
			}
			for (final String cfgValue : cfgContainer.strNames()) {
				if (1 + Lanius.mc.fontRenderer.getStringWidth(cfgValue) + 1 > tabWidth) {
					tabWidth = 1 + Lanius.mc.fontRenderer.getStringWidth(cfgValue) + 1;
				}
			}
		}
		tabWidth += 1 + Math.max(Lanius.mc.fontRenderer.getStringWidth("+"), Lanius.mc.fontRenderer.getStringWidth("-"))
				+ 1 + Math.max(Lanius.mc.fontRenderer.getStringWidth("x"), Lanius.mc.fontRenderer.getStringWidth("o"));
		// Eric: Because width is used before it is set
		final ScaledResolution resolution = new ScaledResolution(Lanius.mc);
		setGuiSize(resolution.getScaledWidth(), resolution.getScaledHeight());
		int initX = 1, initY = resolution.getScaledHeight() - 28;
		for (final Tab guiTab : Tab.values()) {
			final FrameComponent frame = new FrameComponent(guiTab.name, initX, initY, 2 + tabWidth + 1 + 1 + 2,
					2 + Lanius.mc.fontRenderer.FONT_HEIGHT + 1);
			frame.setVisible(true);
			final int frameWidth = frame.getWidth((byte) 0), frameHeight = frame.getHeight();
			int buttonY = frame.getY() + frameHeight;
			int childCount = 0;
			for (final Routine routine : guiTab.routines()) {
				if (!(routine instanceof TabbedRoutine)) {
					continue;
				}
				final ButtonComponent routineBtn = new RoutineButton((TabbedRoutine) routine, frame.getX() + 1, buttonY,
						tabWidth + 2, 1 + Lanius.mc.fontRenderer.FONT_HEIGHT + 2, frame);
				frame.addChild(routineBtn);
				++childCount;
				if (childCount <= MAX_CHILDREN) {
					frame.setOpenBounds(frameWidth, buttonY + frameHeight - frame.getY() + 1);
				}
				buttonY += routineBtn.getHeight() + 1;
			}
			frame.setBtnBounds();
			frames.add(frame);
		}
		for (final ConfigContainer cfgContainer : Lanius.getInstance().getCfgContainerRegistry().objects()) {
			if (cfgContainer.boolNames().isEmpty() && cfgContainer.floatNames().isEmpty()
					&& cfgContainer.intNames().isEmpty() && cfgContainer.strNames().isEmpty()) {
				continue;
			}
			final FrameComponent frame = new FrameComponent(cfgContainer.category(), initX, initY,
					2 + tabWidth + 1 + 1 + 2, 2 + Lanius.mc.fontRenderer.FONT_HEIGHT + 1);
			boolean hasRoutineBtn = false;
			for (FrameComponent registeredFrame : frames) {
				for (Component registeredChild : registeredFrame.children) {
					if (registeredChild.text().equals(cfgContainer.category())
							&& registeredChild instanceof ButtonComponent) {
						((ButtonComponent) registeredChild).setChildFrame(frame);
						hasRoutineBtn = true;
						break;
					}
				}
			}
			frame.setVisible(!hasRoutineBtn);
			final int frameWidth = frame.getWidth((byte) 0), frameHeight = frame.getHeight();
			int buttonY = frame.getY() + frameHeight;
			int childCount = 0;
			for (final String cfgValue : cfgContainer.boolNames()) {
				final ButtonComponent configBtn = new ConfigButton(cfgValue, frame.getX() + 1, buttonY, tabWidth + 2,
						1 + Lanius.mc.fontRenderer.FONT_HEIGHT + 2, frame, cfgContainer);
				frame.addChild(configBtn);
				++childCount;
				if (childCount <= MAX_CHILDREN) {
					frame.setOpenBounds(frameWidth, buttonY + frameHeight - frame.getY() + 1);
				}
				buttonY += configBtn.getHeight() + 1;
			}
			for (final String cfgValue : cfgContainer.floatNames()) {
				final SliderComponent cfgSlider = new SliderComponent<Float>(cfgValue, frame.getX() + 1, buttonY,
						tabWidth + 2, 1 + Lanius.mc.fontRenderer.FONT_HEIGHT + 2, frame,
						cfgContainer.getFloat(cfgValue)) {

					@Override
					public void drag(int mouseX, final int mouseY) {
						// TODO Auto-generated method stub
						if ((mouseX < getX() || mouseX > getX() + getWidth((byte) 0) || mouseY < getY()
								|| mouseY > getY() + getHeight()) && changingSlider != null && changingSlider != this
								|| changingSlider != null && changingSlider != this) {
							return;
						}
						changingSlider = this;
						mouseX -= getX();
						clampedVal.setValue((float) (clampedVal.getMin() + ((double) mouseX / getWidth((byte) 0)
								* (clampedVal.getMax() - clampedVal.getMin()))));
						clampedVal.setValue((float) (clampedVal.getValue()
								- ((clampedVal.getValue() < 0.0D ? Math.ceil((clampedVal.getValue() % 1) / 1)
										: Math.floor((clampedVal.getValue() % 1) / 1)) * 1)));
						clampedVal.setValue(Float.parseFloat(valueTxt().replace(",", ""))); // Eric:
						// ensures
						// that
						// the
						// value
						// equals
						// the
						// displayed
						// value
					}

					@Override
					protected String valueTxt() {
						// TODO Auto-generated method stub
						return String.format("%,.1f", clampedVal.getValue());
					}

				};
				frame.addChild(cfgSlider);
				++childCount;
				if (childCount <= MAX_CHILDREN) {
					frame.setOpenBounds(frameWidth, buttonY + frameHeight - frame.getY() + 1);
				}
				buttonY += cfgSlider.getHeight() + 1;
			}
			for (final String cfgValue : cfgContainer.intNames()) {
				final SliderComponent cfgSlider = new SliderComponent<Integer>(cfgValue, frame.getX() + 1, buttonY,
						tabWidth + 2, 1 + Lanius.mc.fontRenderer.FONT_HEIGHT + 2, frame,
						cfgContainer.getInt(cfgValue)) {

					@Override
					public void drag(int mouseX, final int mouseY) {
						// TODO Auto-generated method stub
						if ((mouseX < getX() || mouseX > getX() + getWidth((byte) 0) || mouseY < getY()
								|| mouseY > getY() + getHeight()) && changingSlider != null && changingSlider != this
								|| changingSlider != null && changingSlider != this) {
							return;
						}
						changingSlider = this;
						mouseX -= getX();
						clampedVal.setValue((int) (clampedVal.getMin() + ((double) mouseX / getWidth((byte) 0)
								* (clampedVal.getMax() - clampedVal.getMin()))));
						clampedVal.setValue((int) (clampedVal.getValue()
								- ((clampedVal.getValue() < 0.0D ? Math.ceil((clampedVal.getValue() % 1) / 1)
										: Math.floor((clampedVal.getValue() % 1) / 1)) * 1)));
					}

					@Override
					protected String valueTxt() {
						// TODO Auto-generated method stub
						return String.format("%,d", Long.valueOf(Math.round(clampedVal.getValue())));
					}

				};
				frame.addChild(cfgSlider);
				++childCount;
				if (childCount <= MAX_CHILDREN) {
					frame.setOpenBounds(frameWidth, buttonY + frameHeight - frame.getY() + 1);
				}
				buttonY += cfgSlider.getHeight() + 1;
			}
			int componentId = 0;
			for (final String cfgValue : cfgContainer.strNames()) {
				final TextFieldComponent configTxtField = new TextFieldComponent(componentId, Lanius.mc.fontRenderer,
						frame.getX() + 1, buttonY, tabWidth + 2, 1 + Lanius.mc.fontRenderer.FONT_HEIGHT + 2, frame,
						cfgValue, cfgContainer);
				configTxtField.setMaxStringLength(128);
				frame.addChild(configTxtField);
				++childCount;
				if (childCount <= MAX_CHILDREN) {
					frame.setOpenBounds(frameWidth, buttonY + frameHeight - frame.getY() + 1);
				}
				buttonY += configTxtField.getHeight() + 1;
				++componentId;
			}
			frame.setBtnBounds();
			frames.add(frame);
		}
		orderedFrames = new ArrayList<FrameComponent>(frames);
		initPositions();
	}

	void bringToFront(final FrameComponent frame) {
		if (frame == frames.get(frames.size() - 1)) {
			return;
		}
		if (frames.remove(frame)) {
			frames.add(frame);
		}
	}

	public void drawPinned(float partialTicks) {
		if (Lanius.mc.currentScreen != null) {
			return;
		}
		ensureInit();
		for (FrameComponent frame : frames) {
			if (frame.isPinned() && !frame.isDisableOpen()) {
				frame.render(partialTicks);
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		final ScaledResolution resolution = new ScaledResolution(mc);
		final int width = resolution.getScaledWidth(), height = resolution.getScaledHeight(), y1 = height - 29;
		Gui.drawRect(0, y1, width, y1 + height, 0x80404040);
		for (final FrameComponent frame : frames) {
			frame.render(mouseX, mouseY, partialTicks);
		}
		for (int frameIdx = frames.size() - 1; frameIdx >= 0; frameIdx--) {
			if (frames.get(frameIdx).renderTooltips(mouseX, mouseY, partialTicks)) {
				break;
			}
		}
	}

	private void ensureInit() {
		if (Lanius.mc.currentScreen != null) {
			return;
		}
		// Eric: Sets up the GUI for rendering.
		ScaledResolution resolution = new ScaledResolution(Lanius.mc);
		boolean repeatEvents = Keyboard.areRepeatEventsEnabled();
		setWorldAndResolution(Lanius.mc, resolution.getScaledWidth(), resolution.getScaledHeight());
		Keyboard.enableRepeatEvents(repeatEvents); // Eric: This GUI modifies repeat events in ClickGui#initGui.
	}

	FrameComponent frontmostFrame(final int mouseX, final int mouseY) {
		for (int frameIdx = frames.size() - 1; frameIdx >= 0; frameIdx--) {
			final FrameComponent frame = frames.get(frameIdx);
			final int frameX = frame.getX(), frameY = frame.getY();
			if (mouseX >= frameX && mouseX <= frameX + frame.getWidth((byte) 0) && mouseY >= frameY
					&& mouseY <= frameY + frame.getHeight() && frame.isVisible()) {
				return frame;
			}
		}
		return null;
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		final FrameComponent scrollFrame = frontmostFrame(Mouse.getEventX() * width / mc.displayWidth,
				height - Mouse.getEventY() * height / mc.displayHeight - 1);
		if (scrollFrame != null) {
			scrollFrame.scroll();
		}
	}

	@Override
	public void initGui() {
		if (!guiFile.exists()) {
			initPositions();
		} else if (!loadedGui) {
			load();
		}
		Keyboard.enableRepeatEvents(true);
		final boolean prevPlaySound = FrameComponent.playSound;
		FrameComponent.playSound = false;
		// Eric: check for boundaries
		for (final FrameComponent frame : frames) {
			frame.moveUnchecked(0, 0);
		}
		FrameComponent.playSound = prevPlaySound;
	}

	private void initPositions() {
		if (movedFrame) {
			return;
		}
		int maxHeight = 0;
		for (final FrameComponent guiFrame : orderedFrames) {
			if (!guiFrame.isVisible()) {
				continue;
			}
			if (guiFrame.getHeight() > maxHeight) {
				maxHeight = guiFrame.getHeight();
			}
		}
		int initX = 1, initY = new ScaledResolution(Lanius.mc).getScaledHeight() - 28;
		for (final FrameComponent frame : orderedFrames) {
			if (!frame.isVisible()) {
				continue;
			}
			frame.moveUnchecked(initX - frame.getX(), initY - frame.getY());
			if (initX + frame.getWidth((byte) 0) + 1 <= width) {
				initX += frame.getWidth((byte) 0) + 1;
			} else if (initY + maxHeight + 1 <= height - maxHeight) {
				initX = 1;
				initY += maxHeight + 1;
			}
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		for (int frameIdx = frames.size() - 1; frameIdx >= 0; frameIdx--) {
			frames.get(frameIdx).keyTyped(typedChar, keyCode);
		}
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public void load() {
		if (guiFile.exists()) {
			BufferedInputStream in = null;
			try {
				in = new BufferedInputStream(new FileInputStream(guiFile));
				for (final FrameComponent frame : orderedFrames) {
					try {
						byte[] xBytes = new byte[Integer.SIZE / Byte.SIZE];
						if (in.read(xBytes) == -1) {
							break;
						}
						byte[] yBytes = new byte[Integer.SIZE / Byte.SIZE];
						in.read(yBytes);
						frame.move(ByteBuffer.wrap(xBytes).getInt() - frame.getX(),
								ByteBuffer.wrap(yBytes).getInt() - frame.getY());
						final boolean open = in.read() == 1;
						if (open && !frame.isOpen() || !open && frame.isOpen()) {
							frame.toggleOpen();
						}
						frame.setVisible(in.read() == 1);
						frame.setPinned(in.read() == 1);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		FrameComponent.playSound = true;
		loadedGui = true;
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		FrameComponent clickedFrame = frontmostFrame(mouseX, mouseY);
		if (clickedFrame != null) {
			bringToFront(clickedFrame);
			clickedFrame.click(mouseX, mouseY, mouseButton);
		}
		for (int frameIdx = frames.size() - 1; frameIdx >= 0; frameIdx--) {
			// Eric: handle text fields separately to fix focus issues
			frames.get(frameIdx).clickTextFields(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		if (dragFrame == null) {
			dragFrame = frontmostFrame(mouseX, mouseY);
			if (dragFrame != null) {
				bringToFront(dragFrame);
			}
		}
		if (dragPoint == null && dragFrame != null && mouseX >= dragFrame.getX()
				&& mouseX <= dragFrame.getX() + dragFrame.getClosedBounds().width && mouseY >= dragFrame.getY()
				&& mouseY <= dragFrame.getY() + dragFrame.getClosedBounds().height) {
			dragPoint = new Point(mouseX - dragFrame.getX(), mouseY - dragFrame.getY());
		}
		if (clickedMouseButton == 0) {
			if (dragFrame != null) {
				if (dragPoint != null && dragFrame.isVisible()) {
					dragFrame.move(mouseX - dragFrame.getX() - dragPoint.x, mouseY - dragFrame.getY() - dragPoint.y);
					movedFrame = true;
				} else if (dragFrame.isVisible()) {
					dragFrame.drag(mouseX, mouseY);
				}
			}
		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		if (state == 0) {
			dragFrame = null;
			dragPoint = null;
			SliderComponent.changingSlider = null;
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
		TooltipButton.setTooltipBtn();
		for (final FrameComponent frame : frames) {
			frame.updateTooltips();
		}
	}

	@SubscribeEvent
	public void onInitGuiPost(final GuiScreenEvent.InitGuiEvent.Post initGuiPostEv) {
		if ((initGuiPostEv.getGui() instanceof GuiMainMenu || initGuiPostEv.getGui() instanceof GuiConnecting)
				&& !loadedGui) {
			load();
		}
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		guiFile.delete();
		BufferedOutputStream out = null;
		try {
			out = new BufferedOutputStream((new FileOutputStream(guiFile)));
			for (final FrameComponent frame : orderedFrames) {
				out.write(ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt(frame.getX()).array());
				out.write(ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt(frame.getY()).array());
				out.write(frame.isOpen() ? 1 : 0);
				out.write(frame.isVisible() ? 1 : 0);
				out.write(frame.isPinned() ? 1 : 0);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void updateScreen() {
		ensureInit();
		for (final FrameComponent frame : frames) {
			frame.update();
		}
		for (final FrameComponent frame : frames) {
			frame.updateTooltips();
		}
	}
}
