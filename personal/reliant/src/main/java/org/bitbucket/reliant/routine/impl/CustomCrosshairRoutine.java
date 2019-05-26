package org.bitbucket.reliant.routine.impl;

import java.awt.Color;
import java.nio.ByteBuffer;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.eric_generic.lang.time.BasicTimer;
import org.bitbucket.eric_generic.lang.time.Timer;
import org.bitbucket.eric_generic.math.Vector;
import org.bitbucket.eric_generic.nio.NioUtils;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Main;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.FloatOption;
import org.bitbucket.reliant.cfg.IntOption;
import org.bitbucket.reliant.memory.InvalidDataException;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.render.Renderer;
import org.bitbucket.reliant.routine.CrosshairRoutine;
import org.bitbucket.reliant.util.Item;
import org.bitbucket.reliant.util.SdkUtils;

public final class CustomCrosshairRoutine extends CrosshairRoutine {
	enum Mode {
		NORMAL_ONLY,
		RECOIL_ONLY,
		BOTH
	}
	private final Timer crosshairTimer = new BasicTimer(SdkUtils.TICK_TIME * 2);
	private short prevCrossTeam;
	private String prevCrosshairName;

	private boolean disabledDefault;

	public CustomCrosshairRoutine() {
		super("Custom Crosshair", "Draws a custom crosshair.", true, true, 1002, new BoolOption("Sniper", "Specifies whether or not to render a crosshair for snipers.", true), new IntOption("Mode", "Specifies the condition to execute on.", new ClampedNumber<Integer>(Mode.BOTH.ordinal(), 0, Mode.values().length - 1), 1), new BoolOption("Disable Default", "Specifies whether or not to disable the game's crosshair.", true), new FloatOption("Line Width", "Specifies the width of a line.", new ClampedNumber<Float>(1.0F, 0.1F, 10.0F), 0.1F), new BoolOption("Trim", "Specifies whether or not to draw the outline.", true), new BoolOption("All Teams", "Specifies whether or not to identify all teams as enemies.", false), new IntOption("Alpha", "Specifies the crosshair's alpha.", new ClampedNumber<Integer>(255, 1, 255), 10), new IntOption("Red", "Specifies the crosshair's red. Set to -1 for the crosshair to use color codes.", new ClampedNumber<Integer>(-1, -1, 255), 10), new IntOption("Green", "Specifies the crosshair's green. Set to -1 for the crosshair to use color codes.", new ClampedNumber<Integer>(-1, -1, 255), 10), new IntOption("Blue", "Specifies the crosshair's blue. Set to -1 for the crosshair to use color codes.", new ClampedNumber<Integer>(-1, -1, 255), 10), new FloatOption("Width", "Specifies each crosshair box's width.", new ClampedNumber<Float>(10.0F * 2, 1.0F, 100.0F), 10.0F), new FloatOption("Height", "Specifies each crosshair box's height.", new ClampedNumber<Float>(1.2F, 0.1F, 10.0F), 1.0F));
		// TODO Auto-generated constructor stub
	}

	@Override
	public int color() {
		// TODO Auto-generated method stub
		return 0xFF00FF00;
	}

	private void draw(final float centerX, final float centerY, final float width, final float height, final boolean trim, final float lineWidth, final int crossCol, final int outlineCol) {
		if (trim) {
			Reliant.instance.getRenderer().drawOutline(centerX - width / 2 - lineWidth, centerY - height / 2 - lineWidth, centerX + width / 2 + lineWidth - (centerX - width / 2 - lineWidth), centerY + height / 2 + lineWidth - (centerY - height / 2 - lineWidth), outlineCol, lineWidth);
			Reliant.instance.getRenderer().drawOutline(centerX - height / 2 - lineWidth, centerY - width / 2 - lineWidth, centerX + height / 2 + lineWidth - (centerX - height / 2 - lineWidth), centerY + width / 2 + lineWidth - (centerY - width / 2 - lineWidth), outlineCol, lineWidth);
		}
		Reliant.instance.getRenderer().drawQuad(centerX - width / 2, centerY - height / 2, centerX + width / 2, centerY + height / 2, crossCol, MemoryStream.NULL);
		Reliant.instance.getRenderer().drawQuad(centerX - height / 2, centerY - width / 2, centerX + height / 2, centerY + width / 2, crossCol, MemoryStream.NULL);
	}

	@Override
	public void reset(final boolean shutdown) {
		super.reset(shutdown);
		crosshairTimer.reset();
		prevCrossTeam = 0;
		prevCrosshairName = null;
		if (shutdown || !isEnabled()) {
			disabledDefault = false;
		}
	}

	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		if (!post) {
			return;
		}
		if (!disabledDefault && getBoolean("Disable Default")) {
			Reliant.instance.clientCmdRate.execute("crosshair 0", 0);
			disabledDefault = true;
		}
		if (!SdkUtils.entityAlive(GameCache.getLocalPlayer())) {
			return;
		}
		final int[] resolution = Reliant.instance.getRenderer().resolution();
		if (resolution == null) {
			throw new InvalidDataException("resolution");
		}
		final float borderX, borderY;
		if (Reliant.instance.getRenderer().hasBorder(Main.TARGET_WINDOW_CLASS)) {
			borderX = Renderer.BORDER_WIDTH;
			borderY = Renderer.BORDER_HEIGHT;
		} else {
			borderX = borderY = 0.0F;
		}
		final float centerX = resolution[0] / 2.0F + borderX / 2.0F, centerY = resolution[1] / 2.0F + borderY;
		final float width = getFloat("Width"), height = getFloat("Height");
		final int crosshairEntity = getCrosshairEntity();
		final int alpha = getInt("Alpha"), red = getInt("Red"), green = getInt("Green"), blue = getInt("Blue");
		final boolean crosshairAlive = SdkUtils.entityAlive(crosshairEntity);
		if (crosshairAlive) {
			crosshairTimer.setStartTime();
		}
		final NameProtectRoutine nameProtect = (NameProtectRoutine) Reliant.instance.getRoutineRegistry().get("Name Protect");
		final String playerName = crosshairAlive ? GameCache.getCrosshairName() : prevCrosshairName;
		final boolean emptyName = StringUtils.empty(playerName);
		if (crosshairAlive && !emptyName) {
			prevCrosshairName = playerName;
		}
		final short crossTeam = crosshairAlive ? Reliant.instance.getProcessStream().readShort(crosshairEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum")) : prevCrossTeam;
		final int crossCol = (red == -1 || green == -1 || blue == -1 ? crosshairAlive || !crosshairTimer.delayPassed() ? nameProtect.isEnabled() && nameProtect.getBoolean("Allies") && nameProtect.containsAlias(playerName) ? new Color(0x0, 0x80, 0xFF, alpha) : getBoolean("All Teams") || crossTeam != Reliant.instance.getProcessStream().readShort(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iTeamNum")) ? new Color(0xFF, 0x0, 0x0, alpha) : new Color(0x0, 0x0, 0xFF, alpha) : new Color(0x0, 0xFF, 0x0, alpha) : new Color(red, green, blue, alpha)).getRGB();
		final boolean trim = getBoolean("Trim");
		final int outlineCol = new Color(0x0, 0x0, 0x0, alpha).getRGB();
		final float lineWidth = getFloat("Line Width");
		final int mode = getInt("Mode");
		final int weaponEntity = SdkUtils.entityByHandle(Reliant.instance.getProcessStream().readInt(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_hActiveWeapon")));
		if (weaponEntity == MemoryStream.NULL) {
			throw new InvalidDataException("weapon_entity");
		}
		final int weaponIdx = Reliant.instance.getProcessStream().readInt(weaponEntity + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_iItemDefinitionIndex"));
		if (mode == Mode.BOTH.ordinal() || mode == Mode.NORMAL_ONLY.ordinal() || getBoolean("Sniper") && Item.sniper(weaponIdx) && Reliant.instance.getProcessStream().readShort(weaponEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_zoomLevel")) <= 0) {
			draw(centerX, centerY, width, height, trim, lineWidth, crossCol, outlineCol);
		}
		if (mode == Mode.BOTH.ordinal() || mode == Mode.RECOIL_ONLY.ordinal()) {
			if (GameCache.getClientState() == MemoryStream.NULL) {
				throw new InvalidDataException("client_state");
			}
			final float[] viewAngles = new float[2];
			((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * viewAngles.length).put(Reliant.instance.getProcessStream().read(GameCache.getClientState() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_dwViewAngles"), MemoryStream.FLOAT_SZ * viewAngles.length)).rewind()).asFloatBuffer().get(viewAngles);
			final float[] vecPunch = new float[2];
			((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * vecPunch.length).put(Reliant.instance.getProcessStream().read(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecPunch"), MemoryStream.FLOAT_SZ * vecPunch.length)).rewind()).asFloatBuffer().get(vecPunch);
			if (vecPunch[0] != 0.0F || vecPunch[1] != 0.0F) {
				final float RECOIL_SCALE = 2.0F;
				viewAngles[0] += vecPunch[0] * RECOIL_SCALE;
				viewAngles[1] += vecPunch[1] * RECOIL_SCALE;
				final Vector anglesVec = org.bitbucket.eric_generic.math.MathHelper.anglesVec(viewAngles);
				anglesVec.x *= SdkUtils.MAX_DIST;
				anglesVec.y *= SdkUtils.MAX_DIST;
				anglesVec.z *= SdkUtils.MAX_DIST;
				SdkUtils.setLocalOrigin();
				final Vector localOrigin = Vector.wrap(SdkUtils.getLocalOrigin());
				final float localViewOffset = Reliant.instance.getProcessStream().readFloat(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecViewOffset[0]") + MemoryStream.FLOAT_SZ * 2);
				localOrigin.y += localViewOffset;
				/*final Vector traceEndVec = anglesVec.copy();
				traceEndVec.add(localOrigin);
				final float viewTrace = SdkUtils.traceRay(localOrigin, traceEndVec, 1.0F, SdkUtils.MASK_SHOT_HULL, true, true);
				anglesVec.x *= viewTrace;
				anglesVec.y *= viewTrace;
				anglesVec.z *= viewTrace;*/
				anglesVec.add(localOrigin);
				final float[] screenPos = Reliant.instance.getRenderer().screenPos(anglesVec.data());
				if (screenPos == null || screenPos[3] < org.bitbucket.eric_generic.math.MathHelper.EQUALS_PRECISION) {
					return;
				}
				final long modAddr = Reliant.instance.getProcessStream().moduleAddress(Reliant.instance.isLegacyToolchain() ? "client.dll" : "engine.dll");
				if (modAddr == MemoryStream.NULL) {
					throw new InvalidDataException("mod_addr");
				}
				final long globalVars = Reliant.instance.isLegacyToolchain() ? Reliant.instance.getProcessStream().readInt(modAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwGlobalVars")) : modAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwGlobalVars");
				if (globalVars == MemoryStream.NULL) {
					throw new InvalidDataException("global_vars");
				}
				if (Reliant.instance.getProcessStream().readShort(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iShotsFired")) >= 0) {
					if (Reliant.instance.getRenderer().hasBorder(Main.TARGET_WINDOW_CLASS)) {
						screenPos[0] -= Renderer.BORDER_WIDTH / 2.0F;
					}
					draw(screenPos[0], screenPos[1], width / 2.0F, height, trim, lineWidth, new Color(0xFF, 0xFF, 0xFF, alpha).getRGB(), outlineCol);
				}
			}
		}
		prevCrossTeam = crossTeam;
		setCrosshairEntity(MemoryStream.NULL);
	}
}
