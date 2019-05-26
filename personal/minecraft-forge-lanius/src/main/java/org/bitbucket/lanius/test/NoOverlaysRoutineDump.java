package org.bitbucket.lanius.test;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class NoOverlaysRoutineDump implements Opcodes {

	public static byte[] dump() throws Exception {

		ClassWriter cw = new ClassWriter(0);
		FieldVisitor fv;
		MethodVisitor mv;
		AnnotationVisitor av0;

		cw.visit(V1_6, ACC_PUBLIC + ACC_FINAL + ACC_SUPER, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", null,
				"org/bitbucket/lanius/routine/TabbedRoutine", null);

		cw.visitInnerClass("net/minecraftforge/client/event/EntityViewRenderEvent$FOVModifier",
				"net/minecraftforge/client/event/EntityViewRenderEvent", "FOVModifier", ACC_PUBLIC + ACC_STATIC);

		cw.visitInnerClass("net/minecraftforge/client/event/RenderBlockOverlayEvent$OverlayType",
				"net/minecraftforge/client/event/RenderBlockOverlayEvent", "OverlayType",
				ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		cw.visitInnerClass("net/minecraftforge/client/event/RenderGameOverlayEvent$ElementType",
				"net/minecraftforge/client/event/RenderGameOverlayEvent", "ElementType",
				ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		cw.visitInnerClass("net/minecraftforge/client/event/RenderGameOverlayEvent$Pre",
				"net/minecraftforge/client/event/RenderGameOverlayEvent", "Pre", ACC_PUBLIC + ACC_STATIC);

		cw.visitInnerClass("net/minecraftforge/client/event/RenderLivingEvent$Pre",
				"net/minecraftforge/client/event/RenderLivingEvent", "Pre", ACC_PUBLIC + ACC_STATIC);

		cw.visitInnerClass("net/minecraftforge/fml/common/gameevent/TickEvent$Phase",
				"net/minecraftforge/fml/common/gameevent/TickEvent", "Phase",
				ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		cw.visitInnerClass("net/minecraftforge/fml/common/gameevent/TickEvent$RenderTickEvent",
				"net/minecraftforge/fml/common/gameevent/TickEvent", "RenderTickEvent", ACC_PUBLIC + ACC_STATIC);

		{
			fv = cw.visitField(ACC_PRIVATE, "prevBlindness", "Lnet/minecraft/potion/PotionEffect;", null, null);
			fv.visitEnd();
		}
		{
			fv = cw.visitField(ACC_PRIVATE, "prevNausea", "Lnet/minecraft/potion/PotionEffect;", null, null);
			fv.visitEnd();
		}
		{
			fv = cw.visitField(ACC_PRIVATE, "prevHurtTime", "I", null, null);
			fv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ICONST_0);
			mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/gui/Tab", "RENDER", "Lorg/bitbucket/lanius/gui/Tab;");
			mv.visitMethodInsn(INVOKESPECIAL, "org/bitbucket/lanius/routine/TabbedRoutine", "<init>",
					"(ILorg/bitbucket/lanius/gui/Tab;)V", false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(3, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "bypassesNcp", "()Z", null, null);
			mv.visitCode();
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "color", "()I", null, null);
			mv.visitCode();
			mv.visitInsn(ICONST_M1);
			mv.visitInsn(IRETURN);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "description", "()Ljava/lang/String;", null, null);
			mv.visitCode();
			mv.visitLdcInsn(
					"Removes unwanted effects such as the hurt camera, blindness, nausea, water, fire, pumpkin blur, and block textures.");
			mv.visitInsn(ARETURN);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "name", "()Ljava/lang/String;", null, null);
			mv.visitCode();
			mv.visitLdcInsn("No Overlays");
			mv.visitInsn(ARETURN);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "onFovModifier",
					"(Lnet/minecraftforge/client/event/EntityViewRenderEvent$FOVModifier;)V", null, null);
			{
				av0 = mv.visitAnnotation("Lnet/minecraftforge/fml/common/eventhandler/SubscribeEvent;", true);
				av0.visitEnd();
			}
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/client/event/EntityViewRenderEvent$FOVModifier",
					"getEntity", "()Lnet/minecraft/entity/Entity;", false);
			mv.visitVarInsn(ASTORE, 2);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitTypeInsn(INSTANCEOF, "net/minecraft/entity/EntityLivingBase");
			Label l0 = new Label();
			mv.visitJumpInsn(IFNE, l0);
			mv.visitInsn(RETURN);
			mv.visitLabel(l0);
			mv.visitFrame(Opcodes.F_APPEND, 1, new Object[] { "net/minecraft/entity/Entity" }, 0, null);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitTypeInsn(CHECKCAST, "net/minecraft/entity/EntityLivingBase");
			mv.visitVarInsn(ASTORE, 3);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitLdcInsn("Nausea");
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "getBoolean",
					"(Ljava/lang/String;)Ljava/lang/Boolean;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
			Label l1 = new Label();
			mv.visitJumpInsn(IFEQ, l1);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/MobEffects", "NAUSEA", "Lnet/minecraft/potion/Potion;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "isPotionActive",
					"(Lnet/minecraft/potion/Potion;)Z", false);
			mv.visitJumpInsn(IFEQ, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/MobEffects", "NAUSEA", "Lnet/minecraft/potion/Potion;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "getActivePotionEffect",
					"(Lnet/minecraft/potion/Potion;)Lnet/minecraft/potion/PotionEffect;", false);
			mv.visitFieldInsn(PUTFIELD, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "prevNausea",
					"Lnet/minecraft/potion/PotionEffect;");
			mv.visitVarInsn(ALOAD, 3);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/MobEffects", "NAUSEA", "Lnet/minecraft/potion/Potion;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "removeActivePotionEffect",
					"(Lnet/minecraft/potion/Potion;)Lnet/minecraft/potion/PotionEffect;", false);
			mv.visitInsn(POP);
			mv.visitLabel(l1);
			mv.visitFrame(Opcodes.F_APPEND, 1, new Object[] { "net/minecraft/entity/EntityLivingBase" }, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitLdcInsn("Hurt Camera");
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "getBoolean",
					"(Ljava/lang/String;)Ljava/lang/Boolean;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
			Label l2 = new Label();
			mv.visitJumpInsn(IFEQ, l2);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/entity/EntityLivingBase", "hurtTime", "I");
			mv.visitJumpInsn(IFLE, l2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "prevHurtTime", "I");
			mv.visitJumpInsn(IFNE, l2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/entity/EntityLivingBase", "hurtTime", "I");
			mv.visitFieldInsn(PUTFIELD, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "prevHurtTime", "I");
			mv.visitVarInsn(ALOAD, 3);
			mv.visitInsn(ICONST_0);
			mv.visitFieldInsn(PUTFIELD, "net/minecraft/entity/EntityLivingBase", "hurtTime", "I");
			mv.visitLabel(l2);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			mv.visitMaxs(3, 4);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "onRenderBlockOverlay",
					"(Lnet/minecraftforge/client/event/RenderBlockOverlayEvent;)V", null, null);
			{
				av0 = mv.visitAnnotation("Lnet/minecraftforge/fml/common/eventhandler/SubscribeEvent;", true);
				av0.visitEnd();
			}
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/client/event/RenderBlockOverlayEvent",
					"getOverlayType", "()Lnet/minecraftforge/client/event/RenderBlockOverlayEvent$OverlayType;", false);
			mv.visitVarInsn(ASTORE, 2);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/client/event/RenderBlockOverlayEvent$OverlayType", "BLOCK",
					"Lnet/minecraftforge/client/event/RenderBlockOverlayEvent$OverlayType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/client/event/RenderBlockOverlayEvent$OverlayType",
					"equals", "(Ljava/lang/Object;)Z", false);
			Label l0 = new Label();
			mv.visitJumpInsn(IFEQ, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitLdcInsn("Block");
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "getBoolean",
					"(Ljava/lang/String;)Ljava/lang/Boolean;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
			Label l1 = new Label();
			mv.visitJumpInsn(IFNE, l1);
			mv.visitLabel(l0);
			mv.visitFrame(Opcodes.F_APPEND, 1,
					new Object[] { "net/minecraftforge/client/event/RenderBlockOverlayEvent$OverlayType" }, 0, null);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/client/event/RenderBlockOverlayEvent$OverlayType", "FIRE",
					"Lnet/minecraftforge/client/event/RenderBlockOverlayEvent$OverlayType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/client/event/RenderBlockOverlayEvent$OverlayType",
					"equals", "(Ljava/lang/Object;)Z", false);
			Label l2 = new Label();
			mv.visitJumpInsn(IFEQ, l2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitLdcInsn("Fire");
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "getBoolean",
					"(Ljava/lang/String;)Ljava/lang/Boolean;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
			mv.visitJumpInsn(IFNE, l1);
			mv.visitLabel(l2);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/client/event/RenderBlockOverlayEvent$OverlayType", "WATER",
					"Lnet/minecraftforge/client/event/RenderBlockOverlayEvent$OverlayType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/client/event/RenderBlockOverlayEvent$OverlayType",
					"equals", "(Ljava/lang/Object;)Z", false);
			Label l3 = new Label();
			mv.visitJumpInsn(IFEQ, l3);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitLdcInsn("Water");
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "getBoolean",
					"(Ljava/lang/String;)Ljava/lang/Boolean;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
			mv.visitJumpInsn(IFEQ, l3);
			mv.visitLabel(l1);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitInsn(ICONST_1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/client/event/RenderBlockOverlayEvent", "setCanceled",
					"(Z)V", false);
			mv.visitLabel(l3);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			mv.visitMaxs(2, 3);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "onRenderGameOverlayPre",
					"(Lnet/minecraftforge/client/event/RenderGameOverlayEvent$Pre;)V", null, null);
			{
				av0 = mv.visitAnnotation("Lnet/minecraftforge/fml/common/eventhandler/SubscribeEvent;", true);
				av0.visitEnd();
			}
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "prevBlindness",
					"Lnet/minecraft/potion/PotionEffect;");
			Label l0 = new Label();
			mv.visitJumpInsn(IFNULL, l0);
			mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/Lanius", "mc", "Lnet/minecraft/client/Minecraft;");
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", "player",
					"Lnet/minecraft/client/entity/EntityPlayerSP;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "prevBlindness",
					"Lnet/minecraft/potion/PotionEffect;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/entity/EntityPlayerSP", "addPotionEffect",
					"(Lnet/minecraft/potion/PotionEffect;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ACONST_NULL);
			mv.visitFieldInsn(PUTFIELD, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "prevBlindness",
					"Lnet/minecraft/potion/PotionEffect;");
			mv.visitLabel(l0);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "prevNausea",
					"Lnet/minecraft/potion/PotionEffect;");
			Label l1 = new Label();
			mv.visitJumpInsn(IFNULL, l1);
			mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/Lanius", "mc", "Lnet/minecraft/client/Minecraft;");
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", "player",
					"Lnet/minecraft/client/entity/EntityPlayerSP;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "prevNausea",
					"Lnet/minecraft/potion/PotionEffect;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/entity/EntityPlayerSP", "addPotionEffect",
					"(Lnet/minecraft/potion/PotionEffect;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ACONST_NULL);
			mv.visitFieldInsn(PUTFIELD, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "prevNausea",
					"Lnet/minecraft/potion/PotionEffect;");
			mv.visitLabel(l1);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitLdcInsn("Pumpkin Blur");
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "getBoolean",
					"(Ljava/lang/String;)Ljava/lang/Boolean;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
			Label l2 = new Label();
			mv.visitJumpInsn(IFEQ, l2);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/client/event/RenderGameOverlayEvent$Pre", "getType",
					"()Lnet/minecraftforge/client/event/RenderGameOverlayEvent$ElementType;", false);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/client/event/RenderGameOverlayEvent$ElementType", "HELMET",
					"Lnet/minecraftforge/client/event/RenderGameOverlayEvent$ElementType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/client/event/RenderGameOverlayEvent$ElementType",
					"equals", "(Ljava/lang/Object;)Z", false);
			mv.visitJumpInsn(IFEQ, l2);
			mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/Lanius", "mc", "Lnet/minecraft/client/Minecraft;");
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", "player",
					"Lnet/minecraft/client/entity/EntityPlayerSP;");
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/entity/EntityPlayerSP", "inventory",
					"Lnet/minecraft/entity/player/InventoryPlayer;");
			mv.visitInsn(ICONST_3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/player/InventoryPlayer", "armorItemInSlot",
					"(I)Lnet/minecraft/item/ItemStack;", false);
			mv.visitVarInsn(ASTORE, 2);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitJumpInsn(IFNULL, l2);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getItem", "()Lnet/minecraft/item/Item;",
					false);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "PUMPKIN", "Lnet/minecraft/block/Block;");
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/item/Item", "getItemFromBlock",
					"(Lnet/minecraft/block/Block;)Lnet/minecraft/item/Item;", false);
			mv.visitJumpInsn(IF_ACMPNE, l2);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitInsn(ICONST_1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/client/event/RenderGameOverlayEvent$Pre",
					"setCanceled", "(Z)V", false);
			mv.visitLabel(l2);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			mv.visitMaxs(2, 3);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "onRenderLivingPre",
					"(Lnet/minecraftforge/client/event/RenderLivingEvent$Pre;)V",
					"(Lnet/minecraftforge/client/event/RenderLivingEvent$Pre<Lnet/minecraft/client/entity/AbstractClientPlayer;>;)V",
					null);
			{
				av0 = mv.visitAnnotation("Lnet/minecraftforge/fml/common/eventhandler/SubscribeEvent;", true);
				av0.visitEnd();
			}
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/client/event/RenderLivingEvent$Pre", "getEntity",
					"()Lnet/minecraft/entity/EntityLivingBase;", false);
			mv.visitVarInsn(ASTORE, 2);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/Lanius", "mc", "Lnet/minecraft/client/Minecraft;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "getRenderViewEntity",
					"()Lnet/minecraft/entity/Entity;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "equals",
					"(Ljava/lang/Object;)Z", false);
			Label l0 = new Label();
			mv.visitJumpInsn(IFNE, l0);
			mv.visitInsn(RETURN);
			mv.visitLabel(l0);
			mv.visitFrame(Opcodes.F_APPEND, 1, new Object[] { "net/minecraft/entity/EntityLivingBase" }, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "prevHurtTime", "I");
			Label l1 = new Label();
			mv.visitJumpInsn(IFLE, l1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/entity/EntityLivingBase", "hurtTime", "I");
			mv.visitJumpInsn(IFNE, l1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "prevHurtTime", "I");
			mv.visitFieldInsn(PUTFIELD, "net/minecraft/entity/EntityLivingBase", "hurtTime", "I");
			mv.visitLabel(l1);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			mv.visitMaxs(2, 3);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "onRenderTick",
					"(Lnet/minecraftforge/fml/common/gameevent/TickEvent$RenderTickEvent;)V", null, null);
			{
				av0 = mv.visitAnnotation("Lnet/minecraftforge/fml/common/eventhandler/SubscribeEvent;", true);
				av0.visitEnd();
			}
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(GETFIELD, "net/minecraftforge/fml/common/gameevent/TickEvent$RenderTickEvent", "phase",
					"Lnet/minecraftforge/fml/common/gameevent/TickEvent$Phase;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/fml/common/gameevent/TickEvent$Phase", "START",
					"Lnet/minecraftforge/fml/common/gameevent/TickEvent$Phase;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/fml/common/gameevent/TickEvent$Phase", "equals",
					"(Ljava/lang/Object;)Z", false);
			Label l0 = new Label();
			mv.visitJumpInsn(IFEQ, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitLdcInsn("Blindness");
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "getBoolean",
					"(Ljava/lang/String;)Ljava/lang/Boolean;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
			mv.visitJumpInsn(IFEQ, l0);
			mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/Lanius", "mc", "Lnet/minecraft/client/Minecraft;");
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", "player",
					"Lnet/minecraft/client/entity/EntityPlayerSP;");
			mv.visitJumpInsn(IFNULL, l0);
			mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/Lanius", "mc", "Lnet/minecraft/client/Minecraft;");
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", "player",
					"Lnet/minecraft/client/entity/EntityPlayerSP;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/MobEffects", "BLINDNESS", "Lnet/minecraft/potion/Potion;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/entity/EntityPlayerSP", "isPotionActive",
					"(Lnet/minecraft/potion/Potion;)Z", false);
			mv.visitJumpInsn(IFEQ, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/Lanius", "mc", "Lnet/minecraft/client/Minecraft;");
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", "player",
					"Lnet/minecraft/client/entity/EntityPlayerSP;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/MobEffects", "BLINDNESS", "Lnet/minecraft/potion/Potion;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/entity/EntityPlayerSP", "getActivePotionEffect",
					"(Lnet/minecraft/potion/Potion;)Lnet/minecraft/potion/PotionEffect;", false);
			mv.visitFieldInsn(PUTFIELD, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "prevBlindness",
					"Lnet/minecraft/potion/PotionEffect;");
			mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/Lanius", "mc", "Lnet/minecraft/client/Minecraft;");
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", "player",
					"Lnet/minecraft/client/entity/EntityPlayerSP;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/MobEffects", "BLINDNESS", "Lnet/minecraft/potion/Potion;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/entity/EntityPlayerSP", "removeActivePotionEffect",
					"(Lnet/minecraft/potion/Potion;)Lnet/minecraft/potion/PotionEffect;", false);
			mv.visitInsn(POP);
			Label l1 = new Label();
			mv.visitJumpInsn(GOTO, l1);
			mv.visitLabel(l0);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(GETFIELD, "net/minecraftforge/fml/common/gameevent/TickEvent$RenderTickEvent", "phase",
					"Lnet/minecraftforge/fml/common/gameevent/TickEvent$Phase;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/fml/common/gameevent/TickEvent$Phase", "END",
					"Lnet/minecraftforge/fml/common/gameevent/TickEvent$Phase;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/fml/common/gameevent/TickEvent$Phase", "equals",
					"(Ljava/lang/Object;)Z", false);
			mv.visitJumpInsn(IFEQ, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ICONST_0);
			mv.visitFieldInsn(PUTFIELD, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "prevHurtTime", "I");
			mv.visitLabel(l1);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			mv.visitMaxs(3, 2);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "registerValues", "()V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitLdcInsn("Hurt Camera");
			mv.visitInsn(ICONST_1);
			mv.visitLdcInsn("Determines whether or not to prevent the camera from shaking when the player is damaged.");
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "registerValue",
					"(Ljava/lang/String;ZLjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitLdcInsn("Blindness");
			mv.visitInsn(ICONST_1);
			mv.visitLdcInsn("Determines whether or not to prevent blindness from rendering.");
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "registerValue",
					"(Ljava/lang/String;ZLjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitLdcInsn("Nausea");
			mv.visitInsn(ICONST_1);
			mv.visitLdcInsn("Determines whether or not to prevent nausea from rendering.");
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "registerValue",
					"(Ljava/lang/String;ZLjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitLdcInsn("Pumpkin Blur");
			mv.visitInsn(ICONST_1);
			mv.visitLdcInsn("Determines whether or not to prevent the blur effect when the player equips a pumpkin.");
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "registerValue",
					"(Ljava/lang/String;ZLjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitLdcInsn("Block");
			mv.visitInsn(ICONST_1);
			mv.visitLdcInsn(
					"Determines whether or not to prevent the block texture from rendering while the player is inside a block.");
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "registerValue",
					"(Ljava/lang/String;ZLjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitLdcInsn("Water");
			mv.visitInsn(ICONST_1);
			mv.visitLdcInsn("Determines whether or not to prevent the underwater overlay from rendering.");
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "registerValue",
					"(Ljava/lang/String;ZLjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitLdcInsn("Fire");
			mv.visitInsn(ICONST_1);
			mv.visitLdcInsn("Determines whether or not to prevent the fire overlay from rendering.");
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bitbucket/lanius/routine/impl/NoOverlaysRoutine", "registerValue",
					"(Ljava/lang/String;ZLjava/lang/String;)V", false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(4, 1);
			mv.visitEnd();
		}
		cw.visitEnd();

		return cw.toByteArray();
	}
}