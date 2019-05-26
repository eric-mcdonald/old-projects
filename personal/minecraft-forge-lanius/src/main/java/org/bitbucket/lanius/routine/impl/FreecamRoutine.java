package org.bitbucket.lanius.routine.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.impl.FrustumData;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.NetworkUtils;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.ReflectHelper;
import org.bitbucket.lanius.util.math.MathHelper;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class FreecamRoutine extends TabbedRoutine implements Hook<FrustumData> {

	private Map<KeyBinding, Boolean> keyMap;

	private final MovementInputFromOptions movementIn = new MovementInputFromOptions(Lanius.mc.gameSettings);

	public final Hook<NetHandlerData> netHook = new Hook<NetHandlerData>() {

		@Override
		public void onExecute(final NetHandlerData data, final Phase phase) {
			// TODO Auto-generated method stub
			if (!phase.equals(Phase.START) || !isEnabled() || !NetworkUtils.motionPacket(data.retVal)) {
				return;
			}
			data.retVal = null;
		}

	};

	private double posX, posY, posZ, prevX, prevY, prevZ;

	private EntityOtherPlayerMP renderEntity;

	public FreecamRoutine() {
		super(Keyboard.KEY_M, true, Tab.RENDER);
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
		return "Releases the camera from the player.";
	}

	public double getPosX() {
		return posX;
	}

	public double getPosY() {
		return posY;
	}

	public double getPosZ() {
		return posZ;
	}

	public EntityOtherPlayerMP getRenderEntity() {
		return renderEntity;
	}

	@Override
	public void init() {
		posX = posY = posZ = prevX = prevY = prevZ = 0.0D;
		ReflectHelper.setValue(EntityRenderer.class, Lanius.mc.entityRenderer, true, "field_175074_C", "renderHand");
		setRenderEntity();
		if (keyMap == null) {
			keyMap = new HashMap<KeyBinding, Boolean>();
		}
		resetKeys();
	}

	double interpolatedX(final float partialTicks) {
		return MathHelper.interpolate(posX, prevX, partialTicks);
	}

	double interpolatedY(final float partialTicks) {
		return MathHelper.interpolate(posY, prevY, partialTicks);
	}

	double interpolatedZ(final float partialTicks) {
		return MathHelper.interpolate(posZ, prevZ, partialTicks);
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Freecam";
	}

	@SubscribeEvent
	public void onCameraSetup(final EntityViewRenderEvent.CameraSetup cameraSetupEv) {
		final float roll = cameraSetupEv.getRoll(), pitch = cameraSetupEv.getPitch(), yaw = cameraSetupEv.getYaw(),
				partialTicks = (float) cameraSetupEv.getRenderPartialTicks();
		GlStateManager.rotate(roll, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(-interpolatedX(partialTicks), -interpolatedY(partialTicks),
				-interpolatedZ(partialTicks));
		GlStateManager.rotate(-yaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-pitch, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(-roll, 0.0F, 0.0F, 1.0F);
	}

	@SubscribeEvent
	public void onClientTick(final TickEvent.ClientTickEvent clientTickEv) {
		if (!clientTickEv.phase.equals(TickEvent.Phase.END) || Lanius.mc.player == null || Lanius.mc.isGamePaused()) {
			return;
		}
		resetKeys();
	}

	@Override
	public void onExecute(final FrustumData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (!phase.equals(Phase.START) || !isEnabled()) {
			return;
		}
		data.retVal = true;
	}

	@SubscribeEvent
	public void onLivingJump(final LivingJumpEvent livingJumpEv) {
		final EntityLivingBase entityLiving = livingJumpEv.getEntityLiving();
		if (!entityLiving.equals(Lanius.mc.player) || entityLiving instanceof EntityPlayerMP) {
			return;
		}
		entityLiving.motionY = 0.0D;
	}

	@SubscribeEvent
	public void onLivingUpdate(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase entityLiving = livingUpdateEv.getEntityLiving();
		if (!entityLiving.equals(Lanius.mc.player) || entityLiving instanceof EntityPlayerMP) {
			return;
		}
		// livingUpdateEv.setCanceled(true);
		movementIn.updatePlayerMoveState();
		final float speed = getFloat("Speed").floatValue();
		prevX = posX;
		prevY = posY;
		prevZ = posZ;
		if (getBoolean("3D")) {
			final Vec3d lookVec = entityLiving.getLookVec();
			if (movementIn.moveForward > 0.0D) {
				posY += lookVec.y * speed;
			} else if (movementIn.moveForward < 0.0D) {
				posY -= lookVec.y * speed;
			}
		} else {
			posY += movementIn.jump ? speed : movementIn.sneak ? -speed : 0.0D;
		}
		entityLiving.motionX = entityLiving.motionY = entityLiving.motionZ = 0.0D;
		entityLiving.moveRelative(movementIn.moveStrafe, 0.0F, movementIn.moveForward, speed);
		posX += entityLiving.motionX;
		posZ += entityLiving.motionZ;
		entityLiving.motionX = entityLiving.motionZ = 0.0D;
		unpressKey(Lanius.mc.gameSettings.keyBindForward);
		unpressKey(Lanius.mc.gameSettings.keyBindBack);
		unpressKey(Lanius.mc.gameSettings.keyBindLeft);
		unpressKey(Lanius.mc.gameSettings.keyBindRight);
		if (renderEntity == null && Lanius.mc.gameSettings.thirdPersonView == 0) {
			renderEntity = new EntityOtherPlayerMP(Lanius.mc.player.world, Lanius.mc.player.getGameProfile());
			renderEntity.setPositionAndRotation(Lanius.mc.player.posX, Lanius.mc.player.posY, Lanius.mc.player.posZ,
					Lanius.mc.player.rotationYaw, Lanius.mc.player.rotationPitch);
			renderEntity.rotationYawHead = Lanius.mc.player.rotationYawHead;
			Lanius.mc.world.addEntityToWorld(renderEntity.getEntityId(), renderEntity);
		} else if (Lanius.mc.gameSettings.thirdPersonView > 0) {
			setRenderEntity();
		}
		ReflectHelper.setValue(EntityRenderer.class, Lanius.mc.entityRenderer, false, "field_175074_C", "renderHand");
	}

	@SubscribeEvent
	public void onRenderSpecialsPre(final RenderLivingEvent.Specials.Pre renderSpecialsPre) {
		if (renderSpecialsPre.getEntity().equals(renderEntity)) {
			renderSpecialsPre.setCanceled(true);
		}
	}

	private void pressKey(final KeyBinding keyBind, final boolean pressed) {
		KeyBinding.setKeyBindState(keyBind.getKeyCode(), pressed);
	}

	@Override
	public void registerValues() {
		registerValue("Speed", 0.6F, 0.1F, 6.0F, "Specifies the speed at which the camera will move.");
		registerValue("3D", false, "Determines whether or not to move the camera based on its rotations.");
	}

	private void resetKeys() {
		for (final Entry<KeyBinding, Boolean> keyEntry : keyMap.entrySet()) {
			pressKey(keyEntry.getKey(), keyEntry.getValue());
		}
		keyMap.clear();
	}

	private void setRenderEntity() {
		if (renderEntity == null) {
			return;
		}
		Lanius.mc.world.removeEntityFromWorld(renderEntity.getEntityId());
		renderEntity = null;
	}

	private void unpressKey(final KeyBinding keyBind) {
		keyMap.put(keyBind, keyBind.isKeyDown());
		pressKey(keyBind, false);
	}

}
