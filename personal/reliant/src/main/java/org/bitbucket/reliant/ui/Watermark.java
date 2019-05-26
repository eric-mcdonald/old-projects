package org.bitbucket.reliant.ui;

import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Main;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.FloatOption;
import org.bitbucket.reliant.cfg.TextureOption;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.render.Renderer;
import org.bitbucket.reliant.routine.BaseRoutine;
import org.bitbucket.reliant.util.SdkUtils;

public final class Watermark extends BaseRoutine {

	public Watermark() {
		super("Watermark", "Draws the watermark onto the screen.", false, true, true, 3003, new BoolOption("Build", "Specifies whether or not to display the build number.", true), new BoolOption("Trim", "Specifies whether or not to draw the outline.", true), new TextureOption("Texture", "Specifies the image file to use as a texture.", ""), new FloatOption("Line Width", "Specifies the width of a line.", new ClampedNumber<Float>(1.0F, 0.1F, 10.0F), 0.1F));
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		if (!post) {
			return;
		}
		String watermark = Reliant.NAME;
		if (getBoolean("Build")) {
			watermark += " " + Reliant.instance.buildText();
		}
		final String font = Reliant.instance.getGuiFont();
		final int FONT_SZ = 24;
		final boolean SHADOW = true;
		final Renderer renderer = Reliant.instance.getRenderer();
		final int[] textSz = renderer.textSize(watermark, font, FONT_SZ, SHADOW);
		if (textSz != null) {
			int x1, y1;
			if (GameCache.getInGame() == SdkUtils.SIGNONSTATE_FULL) {
				x1 = 269 + 2;
				y1 = 2;
			} else {
				x1 = 2;
				y1 = 58;
			}
			if (renderer.hasBorder(Main.TARGET_WINDOW_CLASS)) {
				x1 += Renderer.BORDER_WIDTH;
				y1 += Renderer.BORDER_HEIGHT;
			}
			final long texture = getTexture("Texture");
			if (texture != MemoryStream.NULL) {
				renderer.drawQuad(x1, y1, x1 + textSz[0] + 4.0F + 4.0F, y1 + textSz[1], 0xFFFFFFFF, texture);
			}
			renderer.drawQuad(x1, y1, x1 + textSz[0] + 4.0F + 4.0F, y1 + textSz[1], 0x80000000, MemoryStream.NULL);
			renderer.drawText(watermark, x1 + 4, y1, 0xFFFFFFFF, font, FONT_SZ, SHADOW);
			if (getBoolean("Trim")) {
				renderer.drawOutline(x1, y1, x1 + textSz[0] + 4.0F + 4.0F - x1, y1 + textSz[1] - y1, 0xFF000000, getFloat("Line Width"));
			}
		}
	}

}
