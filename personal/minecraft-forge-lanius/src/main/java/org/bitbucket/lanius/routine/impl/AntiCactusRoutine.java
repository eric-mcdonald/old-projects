package org.bitbucket.lanius.routine.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.HookManager;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.CollisionUtils;
import org.bitbucket.lanius.util.ReflectHelper;
import org.bitbucket.lanius.util.RoutineUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public final class AntiCactusRoutine extends TabbedRoutine {

	private static final class CactusSub extends BlockCactus {
		@Override
		public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
			return HookManager.executeCollision(this, blockState, worldIn, pos);
		}

		@Override
		public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
			// Eric: prevent damage on the server side
			if (RoutineUtils.enabled("Anti-cactus") && entityIn instanceof EntityPlayerMP) {
				return;
			}
			super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
		}
	}

	static final CactusSub cactus = (CactusSub) new CactusSub().setHardness(0.4F).setUnlocalizedName("cactus");

	static {
		try {
			ReflectionHelper.findMethod(Block.class, "setSoundType", "func_149672_a", SoundType.class).invoke(cactus,
					SoundType.CLOTH);
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
	}

	public AntiCactusRoutine() {
		super(Keyboard.KEY_NONE, true, Tab.WORLD);
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
		return "Prevents the player from being damaged by cacti.";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Anti-cactus";
	}

	@SubscribeEvent
	public void onLivingUpdate(final LivingEvent.LivingUpdateEvent livingUpdateEv) {
		final EntityLivingBase livingEntity = livingUpdateEv.getEntityLiving();
		if (!livingEntity.equals(Lanius.mc.player) || livingEntity instanceof EntityPlayerMP) {
			return;
		}
		// Eric: 10 blocks is about the fastest the player will ever move
		for (final IBlockState collidingState : CollisionUtils.collidingStates(livingEntity,
				new Class<?>[] { Blocks.CACTUS.getClass() }, 10.0D, true)) {
			final Block collidingBlock = collidingState.getBlock();
			if (collidingBlock instanceof CactusSub || collidingBlock != Blocks.CACTUS) {
				continue;
			}
			ReflectHelper.setValue(BlockStateContainer.StateImplementation.class,
					(BlockStateContainer.StateImplementation) collidingState, cactus, "field_177239_a", "block");
		}
	}

}
