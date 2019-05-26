package org.bitbucket.lanius.routine.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.HookManager;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.ReflectHelper;
import org.bitbucket.lanius.util.RoutineUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public final class JesusRoutine extends TabbedRoutine implements Hook<NetHandlerData> {

	private static final class DynamicLiquidSub extends BlockDynamicLiquid {

		public DynamicLiquidSub(Material materialIn) {
			super(materialIn);
			// TODO Auto-generated constructor stub
		}

		@Override
		public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
			return HookManager.executeCollision(this, state, worldIn, pos);
		}

	}

	private static final class StaticLiquidSub extends BlockStaticLiquid {

		public StaticLiquidSub(Material materialIn) {
			super(materialIn);
			// TODO Auto-generated constructor stub
		}

		@Override
		public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
			return HookManager.executeCollision(this, state, worldIn, pos);
		}

	}

	public static final double BLOCK_OFF = 0.01D;

	static final DynamicLiquidSub flowingWater = (DynamicLiquidSub) disableStats(
			new DynamicLiquidSub(Material.WATER).setHardness(100.0F).setLightOpacity(3).setUnlocalizedName("water")),
			flowingLava = (DynamicLiquidSub) disableStats(new DynamicLiquidSub(Material.LAVA).setHardness(100.0F)
					.setLightLevel(1.0F).setUnlocalizedName("lava"));

	static final StaticLiquidSub water = (StaticLiquidSub) disableStats(
			new StaticLiquidSub(Material.WATER).setHardness(100.0F).setLightOpacity(3).setUnlocalizedName("water")),
			lava = (StaticLiquidSub) disableStats(new StaticLiquidSub(Material.LAVA).setHardness(100.0F)
					.setLightLevel(1.0F).setUnlocalizedName("lava"));

	private static Block disableStats(final Block block) {
		try {
			return (Block) ReflectionHelper.findMethod(Block.class, "disableStats", "func_149649_H").invoke(block);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return block;
	}

	private boolean movedDown;
	private boolean pressedJump;

	public JesusRoutine() {
		super(Keyboard.KEY_J, false, Tab.MOVEMENT);
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
		return "Allows the player to walk on water.";
	}

	@Override
	public void init() {
		movedDown = false;
		pressJump(GameSettings.isKeyDown(Lanius.mc.gameSettings.keyBindJump));
		pressedJump = false;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Jesus";
	}

	@Override
	public void onExecute(final NetHandlerData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (!phase.equals(Phase.START) || !isEnabled() || !RoutineUtils.ncpEnabled()
				|| !(data.retVal instanceof CPacketPlayer) || Lanius.mc.player.capabilities.allowFlying) {
			return;
		}
		final CPacketPlayer playerPacket = (CPacketPlayer) data.retVal;
		final Vec3d normVec = new Vec3d(Lanius.mc.player.motionX, 0.0D, Lanius.mc.player.motionZ).normalize(),
				hVec = new Vec3d(Lanius.mc.player.width * normVec.x, normVec.y, Lanius.mc.player.width * normVec.z);
		final Block prevBlock = Lanius.mc.player.world.getBlockState(new BlockPos(Lanius.mc.player.posX - hVec.x,
				Lanius.mc.player.posY + Lanius.mc.player.motionY, Lanius.mc.player.posZ - hVec.z)).getBlock(),
				predictBlock = Lanius.mc.player.world
						.getBlockState(new BlockPos(Lanius.mc.player.posX + hVec.x,
								Lanius.mc.player.posY + Lanius.mc.player.motionY, Lanius.mc.player.posZ + hVec.z))
						.getBlock(),
				posBlock = Lanius.mc.player.world.getBlockState(new BlockPos(Lanius.mc.player.posX,
						Lanius.mc.player.posY + Lanius.mc.player.motionY, Lanius.mc.player.posZ)).getBlock();
		final boolean moveFrom = prevBlock instanceof DynamicLiquidSub || prevBlock instanceof StaticLiquidSub,
				moveTo = predictBlock instanceof DynamicLiquidSub || predictBlock instanceof StaticLiquidSub;
		if (!movedDown
				&& !(moveFrom && !moveTo || moveTo && !moveFrom
						|| !(predictBlock instanceof DynamicLiquidSub || predictBlock instanceof StaticLiquidSub))
				&& Lanius.mc.player.onGround) {
			final double BYPASS_Y_OFFSET = -0.04D;
			if ((Boolean) ObfuscationReflectionHelper.getPrivateValue(CPacketPlayer.class, playerPacket,
					"field_149480_h", "moving")) {
				ReflectHelper.setValue(CPacketPlayer.class, playerPacket, playerPacket.getY(0.0D) + BYPASS_Y_OFFSET,
						"field_149477_b", "y");
			} else {
				data.retVal = new CPacketPlayer.PositionRotation(Lanius.mc.player.posX,
						Lanius.mc.player.getEntityBoundingBox().minY + BYPASS_Y_OFFSET, Lanius.mc.player.posZ,
						Lanius.mc.player.rotationYaw, Lanius.mc.player.rotationPitch, Lanius.mc.player.onGround);
			}
		}
		movedDown = !movedDown;
	}

	@SubscribeEvent
	public void onLivingUpdate(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase livingEntity = livingUpdateEv.getEntityLiving();
		if (!livingEntity.equals(Lanius.mc.player) || livingEntity instanceof EntityPlayerMP) {
			return;
		}
		IBlockState predictState = livingEntity.world
				.getBlockState(new BlockPos(livingEntity.posX + livingEntity.motionX,
						livingEntity.posY + livingEntity.motionY, livingEntity.posZ + livingEntity.motionZ));
		Block predictBlock = predictState.getBlock();
		Material predictMat = predictState.getMaterial();
		boolean changedJump = false;
		if (RoutineUtils.ncpEnabled() && !Lanius.mc.player.capabilities.allowFlying
				&& Lanius.mc.gameSettings.keyBindJump.isKeyDown() && !movedDown
				&& (predictMat == Material.WATER || predictMat == Material.LAVA)
				&& !(livingEntity.isInWater() || livingEntity.isInLava()) && livingEntity.onGround) {
			pressJump(false);
			changedJump = true;
		}
		if (pressedJump
				&& (movedDown || (Lanius.mc.player.capabilities.allowFlying
						|| !(livingEntity.isInWater() || livingEntity.isInLava())) && !changedJump)
				&& !(livingEntity.isInWater() || livingEntity.isInLava())) {
			pressJump(GameSettings.isKeyDown(Lanius.mc.gameSettings.keyBindJump));
			pressedJump = false;
		}
		IBlockState posState = livingEntity.world
				.getBlockState(new BlockPos(livingEntity.posX, livingEntity.posY, livingEntity.posZ));
		final Material posMat = posState.getMaterial();
		if ((posMat == Material.WATER || posMat == Material.LAVA)) {
			if (Math.abs(
					BlockLiquid.getLiquidHeightPercent(posState.getValue(BlockLiquid.LEVEL)) - 0.11111111F) < 0.001F
					|| !RoutineUtils.ncpEnabled()) {
				if (!Lanius.mc.gameSettings.keyBindJump.isKeyDown()) {
					final double SWIM_MOTION = 0.04D;
					livingEntity.motionY += livingEntity.isInWater() || livingEntity.isInLava() ? SWIM_MOTION
							: SWIM_MOTION * 2.0D;
				}
			} else if (livingEntity.isInWater() || livingEntity.isInLava()) {
				pressJump(true);
			}
		}
		predictState = livingEntity.world.getBlockState(new BlockPos(livingEntity.posX + livingEntity.motionX,
				livingEntity.posY + livingEntity.motionY, livingEntity.posZ + livingEntity.motionZ));
		predictBlock = predictState.getBlock();
		predictMat = predictState.getMaterial();
		if (predictBlock instanceof DynamicLiquidSub || predictBlock instanceof StaticLiquidSub
				|| predictMat != Material.WATER && predictMat != Material.LAVA) {
			return;
		}
		final boolean flowing = predictBlock instanceof BlockDynamicLiquid;
		ReflectHelper.setValue(BlockStateContainer.StateImplementation.class,
				(BlockStateContainer.StateImplementation) predictState,
				predictMat == Material.WATER ? flowing ? flowingWater : water : flowing ? flowingLava : lava,
				"field_177239_a", "block");
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLivingUpdateLowest(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase livingEntity = livingUpdateEv.getEntityLiving();
		if (!livingEntity.equals(Lanius.mc.player) || livingEntity instanceof EntityPlayerMP
				|| !RoutineUtils.ncpEnabled()) {
			return;
		}
		Material predictMat = livingEntity.world
				.getBlockState(new BlockPos(livingEntity.posX + livingEntity.motionX,
						livingEntity.posY + livingEntity.motionY, livingEntity.posZ + livingEntity.motionZ))
				.getMaterial();
		if (!Lanius.mc.player.capabilities.allowFlying && (predictMat == Material.WATER || predictMat == Material.LAVA)
				&& !(livingEntity.isInWater() || livingEntity.isInLava()) && livingEntity.onGround
				&& livingEntity.moveForward == 0.0F && livingEntity.moveStrafing == 0.0F) {
			HookManager.netHook.forcePlayerPacket();
		}
	}

	private void pressJump(boolean pressed) {
		KeyBinding.setKeyBindState(Lanius.mc.gameSettings.keyBindJump.getKeyCode(), pressed);
		pressedJump = true;
	}

}
