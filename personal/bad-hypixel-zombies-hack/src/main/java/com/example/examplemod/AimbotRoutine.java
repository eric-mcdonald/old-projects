package com.example.examplemod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.MathHelper;

public class AimbotRoutine extends Routine {
	private static final Comparator<EntityLivingBase> distanceCmp = new Comparator<EntityLivingBase>() {

		@Override
		public int compare(EntityLivingBase arg0, EntityLivingBase arg1) {
			// TODO Auto-generated method stub
			return Double.compare(Minecraft.getMinecraft().thePlayer.getDistanceSqToEntity(arg0), Minecraft.getMinecraft().thePlayer.getDistanceSqToEntity(arg1));
		}

	};
	private final List<EntityLivingBase> enemies = new ArrayList<EntityLivingBase>();
	private EntityLivingBase target = null;
	
	AimbotRoutine() {
		super("Aimbot", Keyboard.KEY_K, false);
		// TODO Auto-generated constructor stub
	}
	
	public void faceEntity(Entity entityIn, float p_70625_2_, float p_70625_3_)
	{
		double localX = Minecraft.getMinecraft().thePlayer.posX, localY = Minecraft.getMinecraft().thePlayer.posY, localZ = Minecraft.getMinecraft().thePlayer.posZ;
		if (Minecraft.getMinecraft().thePlayer.worldObj.getCollidingBoundingBoxes(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().addCoord(Minecraft.getMinecraft().thePlayer.motionX, Minecraft.getMinecraft().thePlayer.motionY, Minecraft.getMinecraft().thePlayer.motionZ)).isEmpty()) {
			localX += Minecraft.getMinecraft().thePlayer.motionX;
			localY += Minecraft.getMinecraft().thePlayer.motionY;
			localZ += Minecraft.getMinecraft().thePlayer.motionZ;
		}
		double entityX = entityIn.posX, entityY = entityIn.posY, entityZ = entityIn.posZ;
		if (entityIn.worldObj.getCollidingBoundingBoxes(entityIn, entityIn.getEntityBoundingBox().addCoord(entityIn.motionX, entityIn.motionY, entityIn.motionZ)).isEmpty()) {
			entityX += entityIn.motionX;
			entityY += entityIn.motionY;
			entityZ += entityIn.motionZ;
		}
		double d0 = entityX - localX;
		double d2 = entityZ - localZ;
		double d1;

		if (entityIn instanceof EntityLivingBase)
		{
			EntityLivingBase entitylivingbase = (EntityLivingBase)entityIn;
			d1 = entityY + (double)entitylivingbase.getEyeHeight() - (localY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight());
		}
		else
		{
			d1 = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0D + entityIn.motionY - (localY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight());
		}

		double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2);
		float f = (float)(MathHelper.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
		float f1 = (float)(-(MathHelper.atan2(d1, d3) * 180.0D / Math.PI));
		Minecraft.getMinecraft().thePlayer.rotationPitch = updateRotation(Minecraft.getMinecraft().thePlayer.rotationPitch, f1, p_70625_3_);
		Minecraft.getMinecraft().thePlayer.rotationYaw = updateRotation(Minecraft.getMinecraft().thePlayer.rotationYaw, f, p_70625_2_);
	}
	private float updateRotation(float p_70663_1_, float p_70663_2_, float p_70663_3_)
	{
		float f = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);

		if (f > p_70663_3_)
		{
			f = p_70663_3_;
		}

		if (f < -p_70663_3_)
		{
			f = -p_70663_3_;
		}

		return p_70663_1_ + f;
	}
	void cleanup() {
		enemies.clear();
		target = null;
	}
	void preUpdate() {
		for (final Entity e : Minecraft.getMinecraft().theWorld.loadedEntityList) {
			if (!(e instanceof EntityLivingBase)) {
				continue;
			}
			final EntityLivingBase enemy = (EntityLivingBase) e;
			if (enemy.isEntityAlive() && enemy.getHealth() > 0.0F && (e instanceof EntityMob || e instanceof EntityWolf)) {
				enemies.add(enemy);
			}
		}
		Collections.sort(enemies, distanceCmp);
		if (!enemies.isEmpty() && target == null && (Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null && (Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem() instanceof ItemHoe || Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem() instanceof ItemTool) && Minecraft.getMinecraft().thePlayer.canEntityBeSeen(enemies.get(0)) || Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null && Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword && Minecraft.getMinecraft().thePlayer.getDistanceToEntity(enemies.get(0)) < 4.0F)) {
			target = enemies.get(0);
			faceEntity(target, 360.0F, 360.0F);
		}
	}
	void postUpdate() {
		if (target != null) {
			if (Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null && (Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem() instanceof ItemHoe || Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem() instanceof ItemTool) && !Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().isItemDamaged() && Minecraft.getMinecraft().thePlayer.experience >= 0.99F && Minecraft.getMinecraft().thePlayer.canEntityBeSeen(target)) {
				Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem());
			} else if (Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null && Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword && Minecraft.getMinecraft().thePlayer.getDistanceToEntity(target) < 4.0F) {
				Minecraft.getMinecraft().thePlayer.swingItem();
				Minecraft.getMinecraft().playerController.attackEntity(Minecraft.getMinecraft().thePlayer, target);
			}
			target = null;
		}
		enemies.clear();
	}
}
