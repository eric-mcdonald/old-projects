package org.bitbucket.reliant.ui;

import java.util.ArrayList;
import java.util.List;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.reliant.Main;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.Configuration;
import org.bitbucket.reliant.cfg.FloatOption;
import org.bitbucket.reliant.cfg.IntOption;
import org.bitbucket.reliant.cfg.KeyOption;
import org.bitbucket.reliant.cfg.TextureOption;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.render.Renderer;
import org.bitbucket.reliant.routine.BaseRoutine;
import org.bitbucket.reliant.routine.Displayable;

public final class EnabledList extends BaseRoutine {
	public EnabledList() {
		super("Enabled List", "Renders the enabled list GUI onto the overlay.", false, true, true, 3001, new BoolOption("Trim", "Specifies whether or not to draw the outline.", true), new FloatOption("Line Width", "Specifies the width of a line.", new ClampedNumber<Float>(1.0F, 0.1F, 10.0F), 0.1F), new TextureOption("Texture", "Specifies the image file to use as a texture.", ""), new BoolOption("Information", "Specifies whether or not to display an entry's information text.", true), new BoolOption("State", "Specifies whether or not to display an entry's active state text.", true), new BoolOption("Colors", "Specifies whether or not to color code the entries.", true), new IntOption("X-coordinate", "Specifies the x-coordinate on your screen.", new ClampedNumber<Integer>(1920 - (243 + 4 + 4 + 2) - (208 + 4 + 4 + 2), 0, 10000), 100), new IntOption("Y-coordinate", "Specifies the y-coordinate on your screen.", new ClampedNumber<Integer>(330 + 2 + 15, 0, 10000), 100));
		// TODO Auto-generated constructor stub
	}

	private String displayTxt(final Displayable displayRoutine) {
		final KeyOption key = displayRoutine instanceof Configuration ? (KeyOption) ((Configuration) displayRoutine).getOptionByName("Key") : null;
		final String info = displayRoutine.info();
		String text = displayRoutine.baseTxt();
		final boolean displayInfo = getBoolean("Information") && !StringUtils.empty(info);
		if (getBoolean("State") && key != null && key.getValue() > 0x0) {
			text += " [" + (key.keyDown(true) ? "active" : "inactive");
			text += displayInfo ? ", " + info + "]" : "]";
		} else if (displayInfo) {
			text += " [" + info + "]";
		}
		return text;
	}
	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		if (!post) {
			return;
		}
		int txtWidth = 0, txtHeight = 0;
		final String font = Reliant.instance.getGuiFont();
		final int FONT_SZ = 24;
		final boolean SHADOW = true;
		final Renderer renderer = Reliant.instance.getRenderer();
		final List<Displayable> displayRoutines = new ArrayList<Displayable>();
		for (final Displayable displayRoutine : Reliant.instance.getDisplayRoutineRegistry().objects()) {
			if (!displayRoutine.isEnabled()) {
				continue;
			}
			displayRoutines.add(displayRoutine);
			final int[] textSz = renderer.textSize(displayTxt(displayRoutine), font, FONT_SZ, SHADOW);
			if (textSz != null) {
				if (textSz[0] > txtWidth) {
					txtWidth = textSz[0];
				}
				if (textSz[1] > txtHeight) {
					txtHeight = textSz[1];
				}
			}
		}
		int left = getInt("X-coordinate"), top = getInt("Y-coordinate");
		if (renderer.hasBorder(Main.TARGET_WINDOW_CLASS)) {
			left += Renderer.BORDER_WIDTH;
			top += Renderer.BORDER_HEIGHT;
		}
		final long texture = getTexture("Texture");
		if (texture != MemoryStream.NULL) {
			renderer.drawQuad(left, top, left + txtWidth + 4.0F + 4.0F, top + txtHeight * displayRoutines.size(), 0xFFFFFFFF, texture);
		}
		final int x1 = left, y1 = top;
		final boolean colors = getBoolean("Colors");
		for (final Displayable displayRoutine : displayRoutines) {
			renderer.drawQuad(left, top, left + txtWidth + 4.0F + 4.0F, top + txtHeight, 0x80000000, MemoryStream.NULL);
			renderer.drawText(displayTxt(displayRoutine), left + 4, top, colors ? displayRoutine.color() : 0xFFFFFFFF, font, FONT_SZ, SHADOW);
			top += txtHeight;
		}
		if (getBoolean("Trim")) {
			renderer.drawOutline(x1, y1, txtWidth + 4.0F + 4.0F, top - y1, 0xFF000000, getFloat("Line Width"));
		}
	}
}
