package org.bitbucket.lanius.routine.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nullable;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cfg.ConfigContainer;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.HookManager;
import org.bitbucket.lanius.routine.ConfigRoutine;
import org.bitbucket.lanius.routine.Routine;
import org.bitbucket.lanius.util.CollisionUtils;
import org.bitbucket.lanius.util.ReflectHelper;
import org.bitbucket.lanius.util.RoutineUtils;
import org.bitbucket.lanius.util.registry.Registry;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public final class ViaVersionRoutine extends ConfigRoutine {

	private static final class FarmlandSub extends BlockFarmland {
		@Override
		public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
			return HookManager.executeCollision(this, state, worldIn, pos);
		}
	}

	private static final class LilyPadSub extends BlockLilyPad {
		@Override
		public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
				List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean actualState) {
			if (RoutineUtils.viaVersionEnabled()
					&& Lanius.getInstance().getRoutineRegistry().get("ViaVersion").getBoolean("Block Boxes")
					&& !(entityIn instanceof EntityBoat)) {
				float xOffset = 0.5F, yMax = 0.015625F;
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.5F - xOffset, 0.0F, 0.5F - xOffset, 0.5F + xOffset, yMax, 0.5F + xOffset));
			} else {
				super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, actualState);
			}
		}

		@Override
		public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
			float xOffset = 0.5F, yMax = 0.015625F;
			return RoutineUtils.viaVersionEnabled()
					&& Lanius.getInstance().getRoutineRegistry().get("ViaVersion").getBoolean("Block Boxes")
							? new AxisAlignedBB(0.5F - xOffset, 0.0F, 0.5F - xOffset, 0.5F + xOffset, yMax,
									0.5F + xOffset)
							: super.getBoundingBox(state, source, pos);
		}
	}

	static final FarmlandSub farmland = (FarmlandSub) new FarmlandSub().setHardness(0.6F)
			.setUnlocalizedName("farmland");
	static final LilyPadSub waterlily = (LilyPadSub) new LilyPadSub().setHardness(0.0F).setUnlocalizedName("waterlily");

	static {
		final Method setSoundMethod = ReflectionHelper.findMethod(Block.class, "setSoundType", "func_149672_a",
				SoundType.class);
		try {
			setSoundMethod.invoke(farmland, SoundType.GROUND);
			setSoundMethod.invoke(waterlily, SoundType.PLANT);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ViaVersionRoutine() {
		super(Keyboard.KEY_NONE, true, Tab.MISCELLANEOUS);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.NOCHEATPLUS, Compatibility.VIAVERSION, Compatibility.NO_VIAVERSION);
	}

	@Override
	protected void configureValues(final Registry<Routine> routineRegistry,
			final Registry<ConfigContainer> cfgContainerRegistry) {
		// TODO Auto-generated method stub
		for (final Routine routine : routineRegistry.objects()) {
			if (routine.isEnabled() && !routine.compatibleWith().contains(Compatibility.VIAVERSION)) {
				routine.setEnabled();
			}
		}
		String container = "Auto-use";
		String cfgVal = "Delay";
		if (cfgContainerRegistry.get(container).getInt(cfgVal).intValue() < 100) {
			putChangedInt(new String[] { container, cfgVal },
					cfgContainerRegistry.get(container).getInt(cfgVal).intValue(), 100);
		}
		container = "Speedy Gonzales";
		cfgVal = "Extra Movement";
		if (!cfgContainerRegistry.get(container).getBoolean(cfgVal)) {
			putChangedBool(new String[] { container, cfgVal }, cfgContainerRegistry.get(container).getBoolean(cfgVal),
					true);
		}
		cfgVal = "Shoot Delay";
		if (cfgContainerRegistry.get(container).getInt(cfgVal).intValue() > 16) {
			putChangedInt(new String[] { container, cfgVal },
					cfgContainerRegistry.get(container).getInt(cfgVal).intValue(), 16);
		}
		container = "Kill Aura";
		cfgVal = "Strength";
		if (cfgContainerRegistry.get(container).getFloat(cfgVal).floatValue() > 0.0F) {
			putChangedFloat(new String[] { container, cfgVal },
					cfgContainerRegistry.get(container).getFloat(cfgVal).floatValue(), 0.0F);
		}
		container = "Flight";
		cfgVal = "Levitation";
		if (!cfgContainerRegistry.get(container).getBoolean(cfgVal)) {
			putChangedBool(new String[] { container, cfgVal }, cfgContainerRegistry.get(container).getBoolean(cfgVal),
					true);
		}
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Puts " + Lanius.NAME + " into its ViaVersion compatibility mode.";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "ViaVersion";
	}

	@SubscribeEvent
	public void onLivingUpdate(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase livingEntity = livingUpdateEv.getEntityLiving();
		if (!livingEntity.equals(Lanius.mc.player) || livingEntity instanceof EntityPlayerMP) {
			return;
		}
		// Eric: 10 blocks is about the fastest the player will ever move
		final String[] blockMappings = new String[] { "field_177239_a", "block" };
		for (final IBlockState collidingState : CollisionUtils.collidingStates(livingEntity,
				new Class<?>[] { Blocks.FARMLAND.getClass(), Blocks.WATERLILY.getClass() }, 10.0D, true)) {
			final Block collidingBlock = collidingState.getBlock();
			final BlockStateContainer.StateImplementation collidingImpl = (BlockStateContainer.StateImplementation) collidingState;
			if (!(collidingBlock instanceof FarmlandSub) && collidingBlock == Blocks.FARMLAND) {
				ReflectHelper.setValue(BlockStateContainer.StateImplementation.class, collidingImpl, farmland,
						blockMappings);
			} else if (!(collidingBlock instanceof LilyPadSub) && collidingBlock == Blocks.WATERLILY) {
				ReflectHelper.setValue(BlockStateContainer.StateImplementation.class, collidingImpl, waterlily,
						blockMappings);
			}
		}
	}

	@Override
	public void registerValues() {
		super.registerValues();
		registerValue("Block Boxes", true, "Determines whether or not to use Minecraft 1.8's block bounding boxes.");
	}

}
