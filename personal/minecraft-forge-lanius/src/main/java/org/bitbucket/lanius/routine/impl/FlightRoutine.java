package org.bitbucket.lanius.routine.impl;

import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.hook.impl.NetHandlerHook;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.RoutineUtils;
import org.bitbucket.lanius.util.math.MathHelper;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class FlightRoutine extends TabbedRoutine implements Hook<NetHandlerData> {

	private static final double INIT_POS = -999.0D;

	private boolean executing, movePlayer, keysReset = true;

	// Eric: the flight bypass was patched in 1.9...
	private double oldX, oldY, oldZ;

	public FlightRoutine() {
		super(Keyboard.KEY_G, false, Tab.MOVEMENT);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.NOCHEATPLUS, Compatibility.VIAVERSION, Compatibility.NO_VIAVERSION);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Makes the player to walk through the air.";
	}

	private double fly(final EntityLivingBase entityLiving, final float speed) {
		Lanius.mc.player.capabilities.isFlying = false; // Eric: override
		// vanilla flight
		entityLiving.motionX = entityLiving.motionZ = 0.0D;
		final MovementInput movementIn = Lanius.mc.player.movementInput;
		movementIn.updatePlayerMoveState();
		if (movementIn.sneak) {
			movementIn.moveStrafe /= 0.3D;
			movementIn.moveForward /= 0.3D;
		}
		entityLiving.moveRelative(movementIn.moveStrafe, 0.0F, movementIn.moveForward, speed);
		if (getBoolean("3D")) {
			final Vec3d lookVec = entityLiving.getLookVec();
			return movementIn.moveForward > 0.0D ? lookVec.y * speed
					: movementIn.moveForward < 0.0D ? -lookVec.y * speed : 0.0D;
		} else {
			return movementIn.jump ? speed : movementIn.sneak ? -speed : 0.0D;
		}
	}

	@Override
	public void init() {
		oldX = oldY = oldZ = INIT_POS;
		executing = false;
		movePlayer = true;
		resetKeys();
	}

	public boolean isExecuting() {
		return executing;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Flight";
	}

	@Override
	public void onExecute(final NetHandlerData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (!phase.equals(Phase.START) || !isEnabled()) {
			return;
		}
		if (data.retVal instanceof CPacketPlayer) {
			final CPacketPlayer playerPacket = (CPacketPlayer) data.retVal;
			final double posX = playerPacket.getX(0.0D), posY = playerPacket.getY(0.0D), posZ = playerPacket.getZ(0.0D),
					EXPAND_VEC = 0.0625D;
			if (RoutineUtils.viaVersionEnabled() && getBoolean("Levitation")
					&& (!((Boolean) ObfuscationReflectionHelper.getPrivateValue(CPacketPlayer.class, playerPacket,
							"field_149480_h", "moving"))
							|| (oldX != INIT_POS || oldY != INIT_POS || oldZ != INIT_POS)
									&& MathHelper.distance(oldX, oldY, oldZ, posX, posY, posZ) < 9.0D
									&& !Lanius.mc.player.capabilities.allowFlying
									&& !Lanius.mc.player.isPotionActive(MobEffects.LEVITATION)
									&& !Lanius.mc.player.isElytraFlying()
									&& !Lanius.mc.world.checkBlockCollision(Lanius.mc.player.getEntityBoundingBox()
											.grow(EXPAND_VEC, EXPAND_VEC, EXPAND_VEC).expand(0.0D, -0.55D, 0.0D)))
					&& !RoutineUtils.noclipEnabled() && !RoutineUtils.ncpEnabled()) {
				data.retVal = null;
			} else {
				oldX = posX;
				oldY = posY;
				oldZ = posZ;
			}
		} else if (data.retVal instanceof SPacketPlayerPosLook || data.retVal instanceof SPacketRespawn) {
			movePlayer = true;
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLivingUpdateLowest(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase entityLiving = livingUpdateEv.getEntityLiving();
		if (!entityLiving.equals(Lanius.mc.player) || entityLiving instanceof EntityPlayerMP
				|| Lanius.mc.player.movementInput == null || RoutineUtils.noclipEnabled()) {
			return;
		}
		if (entityLiving.isPlayerSleeping()) {
			((NoclipRoutine) Lanius.getInstance().getRoutineRegistry().get("Noclip")).wakeNoPacket();
			executing = true;
		}
		final float speed = getFloat("Speed").floatValue();
		if (RoutineUtils.ncpEnabled()) {
			if (Lanius.mc.player.capabilities.allowFlying) {
				Lanius.mc.player.capabilities.isFlying = true;
				Lanius.mc.player.sendPlayerAbilities();
				Lanius.mc.player.capabilities.isFlying = false;
			}
			if (speed > 0.6F || !Lanius.mc.player.capabilities.allowFlying) {
				if (executing) {
					final long worldTime = entityLiving.world.getWorldTime();
					if (worldTime < 13000L || worldTime >= 24000L) {
						executing = false;
						return;
					}
				} else {
					if (!RoutineUtils.viaVersionEnabled() && !(entityLiving.isInWater() || entityLiving.isInLava()
							|| (Boolean) ObfuscationReflectionHelper.getPrivateValue(Entity.class, entityLiving,
									"field_70134_J", "isInWeb"))) {
						final double prevX = entityLiving.posX, prevMinY = entityLiving.getEntityBoundingBox().minY,
								prevZ = entityLiving.posZ;
						final float incSpeed = movePlayer ? Math.min(0.0625F, speed) : 0.0F;
						entityLiving.motionY = 0.0D;
						entityLiving.motionY = fly(entityLiving, incSpeed);
						if (movePlayer) {
							resetKeys();
							fly(entityLiving, (float) Math.max(incSpeed - entityLiving.motionY, 0.0F));
							entityLiving.move(MoverType.SELF, entityLiving.motionX, entityLiving.motionY,
									entityLiving.motionZ);
							NetHandlerHook.sendPlayerPacket(new CPacketPlayer.Position(entityLiving.posX,
									entityLiving.getEntityBoundingBox().minY, entityLiving.posZ,
									entityLiving.onGround));
							NetHandlerHook.sendPlayerPacket(new CPacketPlayer.Position(entityLiving.posX,
									entityLiving.getEntityBoundingBox().minY - 999.0D, entityLiving.posZ, false));
						} else {
							KeyBinding.setKeyBindState(Lanius.mc.gameSettings.keyBindForward.getKeyCode(), false);
							KeyBinding.setKeyBindState(Lanius.mc.gameSettings.keyBindBack.getKeyCode(), false);
							KeyBinding.setKeyBindState(Lanius.mc.gameSettings.keyBindLeft.getKeyCode(), false);
							KeyBinding.setKeyBindState(Lanius.mc.gameSettings.keyBindRight.getKeyCode(), false);
							keysReset = false;
						}
						entityLiving.setSprinting(false);
						((SpeedRoutine) Lanius.getInstance().getRoutineRegistry().get("Speed")).setSprinting();
						movePlayer = false;
					}
					return;
				}
			}
		}
		entityLiving.motionY = 0.0D;
		entityLiving.motionY = fly(entityLiving, speed);
		if (entityLiving.isSprinting()) {
			final double SPRINT_MULT = 1.30000001192092896D;
			entityLiving.motionX *= SPRINT_MULT;
			entityLiving.motionZ *= SPRINT_MULT;
		}
	}

	@Override
	public void registerValues() {
		registerValue("Levitation", false, "Determines whether or not to execute the pre-1.9 flight bypass.");
		registerValue("3D", false, "Determines whether or not to move the player based on their rotations.");
		registerValue("Speed", 0.6F, 0.1F, 6.0F, "Specifies the speed the player will fly at.");
	}

	private void resetKeys() {
		if (keysReset) {
			return;
		}
		KeyBinding.setKeyBindState(Lanius.mc.gameSettings.keyBindForward.getKeyCode(),
				GameSettings.isKeyDown(Lanius.mc.gameSettings.keyBindForward));
		KeyBinding.setKeyBindState(Lanius.mc.gameSettings.keyBindBack.getKeyCode(),
				GameSettings.isKeyDown(Lanius.mc.gameSettings.keyBindBack));
		KeyBinding.setKeyBindState(Lanius.mc.gameSettings.keyBindLeft.getKeyCode(),
				GameSettings.isKeyDown(Lanius.mc.gameSettings.keyBindLeft));
		KeyBinding.setKeyBindState(Lanius.mc.gameSettings.keyBindRight.getKeyCode(),
				GameSettings.isKeyDown(Lanius.mc.gameSettings.keyBindRight));
		keysReset = true;
	}

}