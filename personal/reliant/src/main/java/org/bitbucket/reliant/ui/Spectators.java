package org.bitbucket.reliant.ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Main;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.FloatOption;
import org.bitbucket.reliant.cfg.IntOption;
import org.bitbucket.reliant.cfg.TextureOption;
import org.bitbucket.reliant.handler.PlayerHandler;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.render.Renderer;
import org.bitbucket.reliant.routine.BaseRoutine;
import org.bitbucket.reliant.routine.impl.NameProtectRoutine;
import org.bitbucket.reliant.util.SdkUtils;

public final class Spectators extends BaseRoutine implements PlayerHandler {
	private final class SpectateData {
		private final String text;
		private final int color;

		private SpectateData(final String text, final int color) {
			this.text = text;
			this.color = color;
		}
	}
	private static final Comparator<SpectateData> dataCmp = new Comparator<SpectateData>() {

		@Override
		public int compare(SpectateData o1, SpectateData o2) {
			// TODO Auto-generated method stub
			return StringUtils.alphabetCmp.compare(o1.text, o2.text);
		}

	};
	private final Set<SpectateData> spectatorSet = Collections.synchronizedSortedSet(new TreeSet<SpectateData>(dataCmp));
	private boolean updated;

	public Spectators() {
		super("Spectators", "Renders the spectators GUI onto the overlay.", true, true, true, 3000, new BoolOption("Include GOTV", "Specifies whether or not to include the GOTV spectator.", false), new BoolOption("Trim", "Specifies whether or not to draw the outline.", true), new FloatOption("Line Width", "Specifies the width of a line.", new ClampedNumber<Float>(1.0F, 0.1F, 10.0F), 0.1F), new TextureOption("Texture", "Specifies the image file to use as a texture.", ""), new BoolOption("Only Local", "Specifies whether or not to only display the local player's spectators.", false), new BoolOption("Colors", "Specifies whether or not to color code the entries.", true), new IntOption("X-coordinate", "Specifies the x-coordinate on your screen.", new ClampedNumber<Integer>(1920 - (243 + 4 + 4 + 2), 0, 10000), 100), new IntOption("Y-coordinate", "Specifies the y-coordinate on your screen.", new ClampedNumber<Integer>(330 + 2 + 15, 0, 10000), 100));
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean handle(final int player, final int entityIdx) {
		// TODO Auto-generated method stub
		final boolean onlyLocal = getBoolean("Only Local");
		if (!updated || player == GameCache.getLocalPlayer() || onlyLocal && !SdkUtils.entityAlive(GameCache.getLocalPlayer())) {
			return false;
		}
		final int observedEntity = SdkUtils.entityByHandle(Reliant.instance.getProcessStream().readInt(player + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_hObserverTarget")));
		if (!SdkUtils.entityAlive(observedEntity) || onlyLocal && observedEntity != GameCache.getLocalPlayer()) {
			return false;
		}
		final int observedIdx = SdkUtils.playerIdx(observedEntity);
		if (observedIdx == SdkUtils.INVALID_PLAYER_IDX) {
			return false;
		}
		final String name = SdkUtils.readRadarName(entityIdx), observedName = SdkUtils.readRadarName(observedIdx);
		if (StringUtils.empty(name) || StringUtils.empty(observedName) || !getBoolean("Include GOTV") && name.equals("GOTV")) {
			return false;
		}
		final NameProtectRoutine nameProtect = (NameProtectRoutine) Reliant.instance.getRoutineRegistry().get("Name Protect");
		final boolean friendsEnabled = nameProtect.isEnabled() && nameProtect.getBoolean("Allies"), protectedObserver = friendsEnabled && nameProtect.containsAlias(name);
		synchronized (spectatorSet) {
			spectatorSet.add(new SpectateData(onlyLocal ? name : name + " -> " + observedName, observedEntity == GameCache.getLocalPlayer() && !protectedObserver || friendsEnabled && nameProtect.containsAlias(observedName) ? 0xFFFF0000 : protectedObserver ? 0xFF0080FF : 0xFFFFFFFF));
		}
		return true;
	}
	@Override
	public void reset(final boolean shutdown) {
		super.reset(shutdown);
		updated = false;
		synchronized (spectatorSet) {
			spectatorSet.clear();
		}
	}

	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		if (post) {
			synchronized (spectatorSet) {
				if (spectatorSet.isEmpty()) {
					spectatorSet.add(new SpectateData(getBoolean("Only Local") ? "No players are spectating you." : "No players are being spectated.", 0xFFFFFFFF));
				}
			}
			int txtWidth = 0, txtHeight = 0;
			final String font = Reliant.instance.getGuiFont();
			final int FONT_SZ = 24;
			final boolean SHADOW = true;
			final Renderer renderer = Reliant.instance.getRenderer();
			synchronized (spectatorSet) {
				for (final SpectateData data : spectatorSet) {
					final int[] textSz = renderer.textSize(data.text, font, FONT_SZ, SHADOW);
					if (textSz != null) {
						if (textSz[0] > txtWidth) {
							txtWidth = textSz[0];
						}
						if (textSz[1] > txtHeight) {
							txtHeight = textSz[1];
						}
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
				synchronized (spectatorSet) {
					renderer.drawQuad(left, top, left + txtWidth + 4.0F + 4.0F, top + txtHeight * spectatorSet.size(), 0xFFFFFFFF, texture);
				}
			}
			final int x1 = left, y1 = top;
			final boolean colors = getBoolean("Colors");
			synchronized (spectatorSet) {
				for (final SpectateData data : spectatorSet) {
					renderer.drawQuad(left, top, left + txtWidth + 4.0F + 4.0F, top + txtHeight, 0x80000000, MemoryStream.NULL);
					renderer.drawText(data.text, left + 4, top, colors ? data.color : 0xFFFFFFFF, font, FONT_SZ, SHADOW);
					top += txtHeight;
				}
			}
			if (getBoolean("Trim")) {
				renderer.drawOutline(x1, y1, txtWidth + 4.0F + 4.0F, top - y1, 0xFF000000, getFloat("Line Width"));
			}
			synchronized (spectatorSet) {
				spectatorSet.clear();
			}
		} else {
			updated = true;
		}
	}
}
