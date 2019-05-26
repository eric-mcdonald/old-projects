package org.bitbucket.lanius.core;

import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PROTECTED;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ACC_SUPER;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DCONST_0;
import static org.objectweb.asm.Opcodes.DCONST_1;
import static org.objectweb.asm.Opcodes.DLOAD;
import static org.objectweb.asm.Opcodes.DMUL;
import static org.objectweb.asm.Opcodes.DSTORE;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.POP;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.PUTSTATIC;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_6;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public final class SoulSandTransformer extends ModTransformer {

	public SoulSandTransformer() {
		super("net/minecraft/block/BlockSoulSand");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected byte[] transformedClass(final byte[] basicClass) {
		// TODO Auto-generated method stub
		ClassWriter cw = new ClassWriter(0);
		FieldVisitor fv;
		MethodVisitor mv;
		AnnotationVisitor av0;

		cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, "net/minecraft/block/BlockSoulSand", null, "net/minecraft/block/Block",
				null);

		final String ssBoxName = LaniusLoadingPlugin.nameRegistry.get("SOUL_SAND_AABB");
		{
			fv = cw.visitField(ACC_PROTECTED + ACC_FINAL + ACC_STATIC, ssBoxName,
					"Lnet/minecraft/util/math/AxisAlignedBB;", null, null);
			fv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
			mv.visitCode();
			mv.visitTypeInsn(NEW, "net/minecraft/util/math/AxisAlignedBB");
			mv.visitInsn(DUP);
			mv.visitInsn(DCONST_0);
			mv.visitInsn(DCONST_0);
			mv.visitInsn(DCONST_0);
			mv.visitInsn(DCONST_1);
			mv.visitLdcInsn(new Double("0.875"));
			mv.visitInsn(DCONST_1);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/util/math/AxisAlignedBB", "<init>", "(DDDDDD)V", false);
			mv.visitFieldInsn(PUTSTATIC, "net/minecraft/block/BlockSoulSand", ssBoxName,
					"Lnet/minecraft/util/math/AxisAlignedBB;");
			mv.visitInsn(RETURN);
			mv.visitMaxs(14, 0);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/material/Material",
					LaniusLoadingPlugin.nameRegistry.get("SAND"), "Lnet/minecraft/block/material/Material;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/material/MapColor",
					LaniusLoadingPlugin.nameRegistry.get("BROWN"), "Lnet/minecraft/block/material/MapColor;");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/block/Block", "<init>",
					"(Lnet/minecraft/block/material/Material;Lnet/minecraft/block/material/MapColor;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/creativetab/CreativeTabs",
					LaniusLoadingPlugin.nameRegistry.get("BUILDING_BLOCKS"),
					"Lnet/minecraft/creativetab/CreativeTabs;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockSoulSand",
					LaniusLoadingPlugin.nameRegistry.get("setCreativeTab"),
					"(Lnet/minecraft/creativetab/CreativeTabs;)Lnet/minecraft/block/Block;", false);
			mv.visitInsn(POP);
			mv.visitInsn(RETURN);
			mv.visitMaxs(3, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, LaniusLoadingPlugin.nameRegistry.get("getCollisionBoundingBox"),
					"(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/AxisAlignedBB;",
					null, null);
			{
				av0 = mv.visitAnnotation("Ljavax/annotation/Nullable;", true);
				av0.visitEnd();
			}
			mv.visitCode();
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockSoulSand", ssBoxName,
					"Lnet/minecraft/util/math/AxisAlignedBB;");
			mv.visitInsn(ARETURN);
			mv.visitMaxs(1, 4);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, LaniusLoadingPlugin.nameRegistry.get("onEntityCollidedWithBlock"),
					"(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/Entity;)V",
					null, null);
			mv.visitCode();
			mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/hook/HookManager", "soulSandManager",
					"Lorg/bitbucket/lanius/hook/HookManager;");
			mv.visitTypeInsn(NEW, "org/bitbucket/lanius/hook/impl/SoulSandData");
			mv.visitInsn(DUP);
			// mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks",
			// "SOUL_SAND", "Lnet/minecraft/block/Block;");
			// mv.visitTypeInsn(CHECKCAST, "net/minecraft/block/BlockSoulSand");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitLdcInsn(new Double("0.4"));
			mv.visitMethodInsn(INVOKESPECIAL, "org/bitbucket/lanius/hook/impl/SoulSandData", "<init>",
					"(Lnet/minecraft/block/BlockSoulSand;Lnet/minecraft/entity/Entity;D)V", false);
			mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/util/Phase", "START",
					"Lorg/bitbucket/lanius/util/Phase;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bitbucket/lanius/hook/HookManager", "execute",
					"(Lorg/bitbucket/lanius/hook/HookData;Lorg/bitbucket/lanius/util/Phase;)Ljava/lang/Object;", false);
			mv.visitTypeInsn(CHECKCAST, "java/lang/Double");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false);
			mv.visitVarInsn(DSTORE, 5);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitInsn(DUP);
			final String motXName = LaniusLoadingPlugin.nameRegistry.get("motionX");
			mv.visitFieldInsn(GETFIELD, "net/minecraft/entity/Entity", motXName, "D");
			mv.visitVarInsn(DLOAD, 5);
			mv.visitInsn(DMUL);
			mv.visitFieldInsn(PUTFIELD, "net/minecraft/entity/Entity", motXName, "D");
			mv.visitVarInsn(ALOAD, 4);
			mv.visitInsn(DUP);
			final String motZName = LaniusLoadingPlugin.nameRegistry.get("motionZ");
			mv.visitFieldInsn(GETFIELD, "net/minecraft/entity/Entity", motZName, "D");
			mv.visitVarInsn(DLOAD, 5);
			mv.visitInsn(DMUL);
			mv.visitFieldInsn(PUTFIELD, "net/minecraft/entity/Entity", motZName, "D");
			mv.visitInsn(RETURN);
			mv.visitMaxs(7, 7);
			mv.visitEnd();
		}
		cw.visitEnd();

		return cw.toByteArray();
	}

}
