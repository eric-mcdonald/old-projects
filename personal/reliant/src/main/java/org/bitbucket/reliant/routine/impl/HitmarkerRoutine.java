package org.bitbucket.reliant.routine.impl;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.bitbucket.eric_generic.lang.time.Timer;
import org.bitbucket.eric_generic.nio.NioUtils;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Main;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.FloatOption;
import org.bitbucket.reliant.cfg.IntOption;
import org.bitbucket.reliant.cfg.SoundOption;
import org.bitbucket.reliant.cfg.TextureOption;
import org.bitbucket.reliant.handler.CrosshairHandler;
import org.bitbucket.reliant.memory.InvalidDataException;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.render.Renderer;
import org.bitbucket.reliant.routine.PlayerRoutine;
import org.bitbucket.reliant.util.Item;
import org.bitbucket.reliant.util.SdkUtils;

public final class HitmarkerRoutine extends PlayerRoutine implements CrosshairHandler {
	private static class Life {
		private int state;
		private short hitPoints;
		
		private Life(final int player) {
			state = Reliant.instance.getProcessStream().readByte(player + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_lifeState"));
			hitPoints = (short) (Reliant.instance.getProcessStream().readShort(player + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_iHealth")) + Reliant.instance.getProcessStream().readShort(player + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_ArmorValue")));
		}
	}
	private int prevTotalHits = -1;
	private final Map<Integer, Life> prevHealthMap = new HashMap<Integer, Life>();
	private final Timer displayTimer = new Timer() {

		@Override
		protected long delay() {
			// TODO Auto-generated method stub
			return getInt("Display Time");
		}
		
	};
	private int crosshairEntity = MemoryStream.NULL;

	public HitmarkerRoutine() {
		super("Hitmarker", "Displays a hitmarker when you attack another player.", true, 1006, new FloatOption("Width", "Specifies the hitmarker's width.", new ClampedNumber<Float>(25.0F, 1.0F, 100.0F), 10.0F), new FloatOption("Height", "Specifies the hitmarker's height.", new ClampedNumber<Float>(25.0F, 1.0F, 100.0F), 10.0F), new IntOption("Display Time", "Specifies how long to display the hitmarker for.", new ClampedNumber<Integer>(1000, 1, 10000), 1000), new FloatOption("Volume", "Specifies the volume of the sound.", new ClampedNumber<Float>(100.0F, 0.0F, 100.0F), 0.0F), new SoundOption("Sound", "Specifies the audio file to use.", "/audio/ui/hitmarker.wav"), new TextureOption("Texture", "Specifies the image file to use as a texture.", "/textures/ui/hitmarker.png"));
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int color() {
		// TODO Auto-generated method stub
		return 0xFF80FF00;
	}
	@Override
	public int getCrosshairEntity() {
		// TODO Auto-generated method stub
		return crosshairEntity == MemoryStream.NULL ? GameCache.getCrosshairEntity() : crosshairEntity;
	}

	@Override
	public boolean handle(final int player, final int entityIdx) {
		if (!super.handle(player, entityIdx) || !SdkUtils.entityAlive(GameCache.getLocalPlayer()) || !SdkUtils.entityValid(player) || player == GameCache.getLocalPlayer()) {
			return false;
		}
		final Life playerLife = new Life(player);
		if (!prevHealthMap.containsKey(player) || playerLife.hitPoints > prevHealthMap.get(player).hitPoints/* || playerLife.state != prevHealthMap.get(player).state && playerLife.state == SdkUtils.LIFE_ALIVE*/) {
			prevHealthMap.put(player, playerLife);
		}
		final int weaponEntity = SdkUtils.entityByHandle(Reliant.instance.getProcessStream().readInt(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_hActiveWeapon")));
		if (weaponEntity == MemoryStream.NULL) {
			throw new InvalidDataException("weapon_entity");
		}
		final long modAddr = Reliant.instance.isLegacyToolchain() ? Reliant.instance.getProcessStream().moduleAddress("client.dll") : Reliant.instance.getProcessStream().moduleAddress("engine.dll");
		if (modAddr == MemoryStream.NULL) {
			throw new InvalidDataException("mod_addr");
		}
		final long globalVars = modAddr + Reliant.instance.getOffsetsRegistry().get("haze").offset("dwGlobalVars");
		if (globalVars == MemoryStream.NULL) {
			throw new InvalidDataException("global_vars");
		}
		final int weaponIdx = Reliant.instance.getProcessStream().readInt(weaponEntity + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_iItemDefinitionIndex"));
		// Eric: m_iTotalHits does not update when the player attacks with a knife; so, we need to guess if they did.
		// TODO(Eric) m_iHealth or m_ArmorValue isn't always updated per hit?
		final Life prevLife = prevHealthMap.get(player);
		final float[] playerOrigin = new float[3];
		((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * playerOrigin.length).put(Reliant.instance.getProcessStream().read(player + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_vecOrigin"), MemoryStream.FLOAT_SZ * playerOrigin.length)).rewind()).asFloatBuffer().get(playerOrigin);
		SdkUtils.setLocalOrigin();
		final float[] localOrigin = SdkUtils.getLocalOrigin();
		if ((playerLife.hitPoints < prevLife.hitPoints && getCrosshairEntity() == player || playerLife.state != prevLife.state && playerLife.state == SdkUtils.LIFE_DYING) && org.bitbucket.eric_generic.math.MathHelper.distance(localOrigin[0], localOrigin[1], localOrigin[2], playerOrigin[0], playerOrigin[1], playerOrigin[2]) <= 100.0F && Item.melee(weaponIdx) && weaponIdx != Item.ZEUS_X27.index && Reliant.instance.getProcessStream().readFloat(weaponEntity + Reliant.instance.getOffsetsRegistry().get("y3").offset("m_flNextPrimaryAttack")) - Reliant.instance.getProcessStream().readFloat(globalVars + MemoryStream.FLOAT_SZ + MemoryStream.INT_SZ + MemoryStream.FLOAT_SZ * 2) > 0.0F) {
			prevHealthMap.remove(player);
			getSoundOpt("Sound").play(getFloat("Volume"));
			displayTimer.setStartTime();
		}
		return true;
	}
	@Override
	public void reset(final boolean shutdown) {
		super.reset(shutdown);
		crosshairEntity = MemoryStream.NULL;
		prevTotalHits = -1;
		displayTimer.reset();
		prevHealthMap.clear();
	}

	@Override
	public void setCrosshairEntity(final int crosshairEntity) {
		// TODO Auto-generated method stub
		this.crosshairEntity = crosshairEntity;
	}

	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		super.update(post);
		if (!post || !SdkUtils.entityAlive(GameCache.getLocalPlayer())) {
			return;
		}
		final int totalHits = Reliant.instance.getProcessStream().readInt(GameCache.getLocalPlayer() + Reliant.instance.getCustomOffsetsRegistry().get("m_iTotalHits"));
		if (totalHits > prevTotalHits && prevTotalHits != -1) {
			getSoundOpt("Sound").play(getFloat("Volume"));
			prevTotalHits = totalHits;
			displayTimer.setStartTime();
		}
		if (!displayTimer.delayPassed()) {
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
			Reliant.instance.getRenderer().drawQuad(centerX - width / 2.0F, centerY - height / 2.0F, centerX + width / 2.0F, centerY + height / 2.0F, 0xFFFFFFFF, getTexture("Texture"));
		}
		// Eric: m_iTotalHits resets on each round.
		if (prevTotalHits == -1 || totalHits < prevTotalHits) {
			prevTotalHits = totalHits;
		}
		setCrosshairEntity(MemoryStream.NULL);
	}
}
