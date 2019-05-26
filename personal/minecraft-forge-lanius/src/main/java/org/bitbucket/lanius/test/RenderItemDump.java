package org.bitbucket.lanius.test;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class RenderItemDump implements Opcodes {

	public static byte[] dump() throws Exception {

		ClassWriter cw = new ClassWriter(0);
		FieldVisitor fv;
		MethodVisitor mv;
		AnnotationVisitor av0;

		cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, "net/minecraft/client/renderer/RenderItem", null, "java/lang/Object",
				new String[] { "net/minecraft/client/resources/IResourceManagerReloadListener" });

		{
			av0 = cw.visitAnnotation("Lnet/minecraftforge/fml/relauncher/SideOnly;", true);
			av0.visitEnum("value", "Lnet/minecraftforge/fml/relauncher/Side;", "CLIENT");
			av0.visitEnd();
		}
		cw.visitInnerClass("net/minecraft/client/renderer/RenderItem$9", null, null, 0);

		cw.visitInnerClass("net/minecraft/client/renderer/RenderItem$8", null, null, 0);

		cw.visitInnerClass("net/minecraft/client/renderer/RenderItem$7", null, null, 0);

		cw.visitInnerClass("net/minecraft/client/renderer/RenderItem$6", null, null, 0);

		cw.visitInnerClass("net/minecraft/client/renderer/RenderItem$5", null, null, 0);

		cw.visitInnerClass("net/minecraft/client/renderer/RenderItem$4", null, null, 0);

		cw.visitInnerClass("net/minecraft/client/renderer/RenderItem$3", null, null, 0);

		cw.visitInnerClass("net/minecraft/client/renderer/RenderItem$2", null, null, 0);

		cw.visitInnerClass("net/minecraft/client/renderer/RenderItem$1", null, null, 0);

		cw.visitInnerClass("net/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType",
				"net/minecraft/client/renderer/block/model/ItemCameraTransforms", "TransformType",
				ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		cw.visitInnerClass("net/minecraft/client/renderer/GlStateManager$SourceFactor",
				"net/minecraft/client/renderer/GlStateManager", "SourceFactor",
				ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		cw.visitInnerClass("net/minecraft/client/renderer/GlStateManager$DestFactor",
				"net/minecraft/client/renderer/GlStateManager", "DestFactor",
				ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		cw.visitInnerClass("net/minecraft/client/renderer/GlStateManager$CullFace",
				"net/minecraft/client/renderer/GlStateManager", "CullFace",
				ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		cw.visitInnerClass("net/minecraft/block/BlockWall$EnumType", "net/minecraft/block/BlockWall", "EnumType",
				ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		cw.visitInnerClass("net/minecraft/block/BlockDirt$DirtType", "net/minecraft/block/BlockDirt", "DirtType",
				ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		cw.visitInnerClass("net/minecraft/block/BlockDoublePlant$EnumPlantType", "net/minecraft/block/BlockDoublePlant",
				"EnumPlantType", ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		cw.visitInnerClass("net/minecraft/block/BlockPlanks$EnumType", "net/minecraft/block/BlockPlanks", "EnumType",
				ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		cw.visitInnerClass("net/minecraft/block/BlockSilverfish$EnumType", "net/minecraft/block/BlockSilverfish",
				"EnumType", ACC_PUBLIC + ACC_STATIC + ACC_ENUM + ACC_ABSTRACT);

		cw.visitInnerClass("net/minecraft/block/BlockPrismarine$EnumType", "net/minecraft/block/BlockPrismarine",
				"EnumType", ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		cw.visitInnerClass("net/minecraft/block/BlockQuartz$EnumType", "net/minecraft/block/BlockQuartz", "EnumType",
				ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		cw.visitInnerClass("net/minecraft/block/BlockFlower$EnumFlowerType", "net/minecraft/block/BlockFlower",
				"EnumFlowerType", ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		cw.visitInnerClass("net/minecraft/block/BlockSand$EnumType", "net/minecraft/block/BlockSand", "EnumType",
				ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		cw.visitInnerClass("net/minecraft/block/BlockSandStone$EnumType", "net/minecraft/block/BlockSandStone",
				"EnumType", ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		cw.visitInnerClass("net/minecraft/block/BlockRedSandstone$EnumType", "net/minecraft/block/BlockRedSandstone",
				"EnumType", ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		cw.visitInnerClass("net/minecraft/block/BlockStone$EnumType", "net/minecraft/block/BlockStone", "EnumType",
				ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		cw.visitInnerClass("net/minecraft/block/BlockStoneBrick$EnumType", "net/minecraft/block/BlockStoneBrick",
				"EnumType", ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		cw.visitInnerClass("net/minecraft/block/BlockStoneSlab$EnumType", "net/minecraft/block/BlockStoneSlab",
				"EnumType", ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		cw.visitInnerClass("net/minecraft/block/BlockStoneSlabNew$EnumType", "net/minecraft/block/BlockStoneSlabNew",
				"EnumType", ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		cw.visitInnerClass("net/minecraft/block/BlockTallGrass$EnumType", "net/minecraft/block/BlockTallGrass",
				"EnumType", ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		cw.visitInnerClass("net/minecraft/item/ItemFishFood$FishType", "net/minecraft/item/ItemFishFood", "FishType",
				ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		cw.visitInnerClass("net/minecraft/block/BlockHugeMushroom$EnumType", "net/minecraft/block/BlockHugeMushroom",
				"EnumType", ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		cw.visitInnerClass("net/minecraft/tileentity/TileEntityStructure$Mode",
				"net/minecraft/tileentity/TileEntityStructure", "Mode", ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM);

		{
			fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "RES_ITEM_GLINT",
					"Lnet/minecraft/util/ResourceLocation;", null, null);
			fv.visitEnd();
		}
		{
			fv = cw.visitField(ACC_PRIVATE, "notRenderingEffectsInGUI", "Z", null, null);
			fv.visitEnd();
		}
		{
			fv = cw.visitField(ACC_PUBLIC, "zLevel", "F", null, null);
			fv.visitEnd();
		}
		{
			fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "itemModelMesher",
					"Lnet/minecraft/client/renderer/ItemModelMesher;", null, null);
			fv.visitEnd();
		}
		{
			fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "textureManager",
					"Lnet/minecraft/client/renderer/texture/TextureManager;", null, null);
			fv.visitEnd();
		}
		{
			fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "itemColors",
					"Lnet/minecraft/client/renderer/color/ItemColors;", null, null);
			fv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>",
					"(Lnet/minecraft/client/renderer/texture/TextureManager;Lnet/minecraft/client/renderer/block/model/ModelManager;Lnet/minecraft/client/renderer/color/ItemColors;)V",
					null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ICONST_1);
			mv.visitFieldInsn(PUTFIELD, "net/minecraft/client/renderer/RenderItem", "notRenderingEffectsInGUI", "Z");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(PUTFIELD, "net/minecraft/client/renderer/RenderItem", "textureManager",
					"Lnet/minecraft/client/renderer/texture/TextureManager;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitTypeInsn(NEW, "net/minecraftforge/client/ItemModelMesherForge");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraftforge/client/ItemModelMesherForge", "<init>",
					"(Lnet/minecraft/client/renderer/block/model/ModelManager;)V", false);
			mv.visitFieldInsn(PUTFIELD, "net/minecraft/client/renderer/RenderItem", "itemModelMesher",
					"Lnet/minecraft/client/renderer/ItemModelMesher;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItems", "()V",
					false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitFieldInsn(PUTFIELD, "net/minecraft/client/renderer/RenderItem", "itemColors",
					"Lnet/minecraft/client/renderer/color/ItemColors;");
			mv.visitInsn(RETURN);
			mv.visitMaxs(4, 4);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "isNotRenderingEffectsInGUI", "(Z)V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitFieldInsn(PUTFIELD, "net/minecraft/client/renderer/RenderItem", "notRenderingEffectsInGUI", "Z");
			mv.visitInsn(RETURN);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "getItemModelMesher", "()Lnet/minecraft/client/renderer/ItemModelMesher;",
					null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "itemModelMesher",
					"Lnet/minecraft/client/renderer/ItemModelMesher;");
			mv.visitInsn(ARETURN);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PROTECTED, "registerItem", "(Lnet/minecraft/item/Item;ILjava/lang/String;)V", null,
					null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "itemModelMesher",
					"Lnet/minecraft/client/renderer/ItemModelMesher;");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitTypeInsn(NEW, "net/minecraft/client/renderer/block/model/ModelResourceLocation");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitLdcInsn("inventory");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/block/model/ModelResourceLocation",
					"<init>", "(Ljava/lang/String;Ljava/lang/String;)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/ItemModelMesher", "register",
					"(Lnet/minecraft/item/Item;ILnet/minecraft/client/renderer/block/model/ModelResourceLocation;)V",
					false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(7, 4);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PROTECTED, "registerBlock", "(Lnet/minecraft/block/Block;ILjava/lang/String;)V",
					null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/item/Item", "getItemFromBlock",
					"(Lnet/minecraft/block/Block;)Lnet/minecraft/item/Item;", false);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(4, 4);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PRIVATE, "registerBlock", "(Lnet/minecraft/block/Block;Ljava/lang/String;)V", null,
					null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(4, 3);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PRIVATE, "registerItem", "(Lnet/minecraft/item/Item;Ljava/lang/String;)V", null,
					null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(4, 3);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PRIVATE, "renderModel",
					"(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/item/ItemStack;)V", null,
					null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitInsn(ICONST_M1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "renderModel",
					"(Lnet/minecraft/client/renderer/block/model/IBakedModel;ILnet/minecraft/item/ItemStack;)V", false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(4, 3);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PRIVATE, "renderModel",
					"(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitInsn(ACONST_NULL);
			mv.visitTypeInsn(CHECKCAST, "net/minecraft/item/ItemStack");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "renderModel",
					"(Lnet/minecraft/client/renderer/block/model/IBakedModel;ILnet/minecraft/item/ItemStack;)V", false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(4, 3);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PRIVATE, "renderModel",
					"(Lnet/minecraft/client/renderer/block/model/IBakedModel;ILnet/minecraft/item/ItemStack;)V", null,
					null);
			{
				av0 = mv.visitParameterAnnotation(2, "Ljavax/annotation/Nullable;", true);
				av0.visitEnd();
			}
			mv.visitCode();
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/Tessellator", "getInstance",
					"()Lnet/minecraft/client/renderer/Tessellator;", false);
			mv.visitVarInsn(ASTORE, 4);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/Tessellator", "getBuffer",
					"()Lnet/minecraft/client/renderer/BufferBuilder;", false);
			mv.visitVarInsn(ASTORE, 5);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitIntInsn(BIPUSH, 7);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/vertex/DefaultVertexFormats", "ITEM",
					"Lnet/minecraft/client/renderer/vertex/VertexFormat;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/BufferBuilder", "begin",
					"(ILnet/minecraft/client/renderer/vertex/VertexFormat;)V", false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/util/EnumFacing", "values",
					"()[Lnet/minecraft/util/EnumFacing;", false);
			mv.visitVarInsn(ASTORE, 6);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitInsn(ARRAYLENGTH);
			mv.visitVarInsn(ISTORE, 7);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ISTORE, 8);
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitFrame(Opcodes.F_FULL, 9,
					new Object[] { "net/minecraft/client/renderer/RenderItem",
							"net/minecraft/client/renderer/block/model/IBakedModel", Opcodes.INTEGER,
							"net/minecraft/item/ItemStack", "net/minecraft/client/renderer/Tessellator",
							"net/minecraft/client/renderer/BufferBuilder", "[Lnet/minecraft/util/EnumFacing;",
							Opcodes.INTEGER, Opcodes.INTEGER },
					0, new Object[] {});
			mv.visitVarInsn(ILOAD, 8);
			mv.visitVarInsn(ILOAD, 7);
			Label l1 = new Label();
			mv.visitJumpInsn(IF_ICMPGE, l1);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitVarInsn(ILOAD, 8);
			mv.visitInsn(AALOAD);
			mv.visitVarInsn(ASTORE, 9);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitInsn(ACONST_NULL);
			mv.visitTypeInsn(CHECKCAST, "net/minecraft/block/state/IBlockState");
			mv.visitVarInsn(ALOAD, 9);
			mv.visitInsn(LCONST_0);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/client/renderer/block/model/IBakedModel", "getQuads",
					"(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/EnumFacing;J)Ljava/util/List;", true);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "renderQuads",
					"(Lnet/minecraft/client/renderer/BufferBuilder;Ljava/util/List;ILnet/minecraft/item/ItemStack;)V",
					false);
			mv.visitIincInsn(8, 1);
			mv.visitJumpInsn(GOTO, l0);
			mv.visitLabel(l1);
			mv.visitFrame(Opcodes.F_CHOP, 3, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitInsn(ACONST_NULL);
			mv.visitTypeInsn(CHECKCAST, "net/minecraft/block/state/IBlockState");
			mv.visitInsn(ACONST_NULL);
			mv.visitTypeInsn(CHECKCAST, "net/minecraft/util/EnumFacing");
			mv.visitInsn(LCONST_0);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/client/renderer/block/model/IBakedModel", "getQuads",
					"(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/EnumFacing;J)Ljava/util/List;", true);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "renderQuads",
					"(Lnet/minecraft/client/renderer/BufferBuilder;Ljava/util/List;ILnet/minecraft/item/ItemStack;)V",
					false);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/Tessellator", "draw", "()V", false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(7, 10);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "renderItem",
					"(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", null,
					null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 1);
			Label l0 = new Label();
			mv.visitJumpInsn(IFNULL, l0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "pushMatrix", "()V",
					false);
			mv.visitLdcInsn(new Float("-0.5"));
			mv.visitLdcInsn(new Float("-0.5"));
			mv.visitLdcInsn(new Float("-0.5"));
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "translate", "(FFF)V",
					false);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/client/renderer/block/model/IBakedModel",
					"isBuiltInRenderer", "()Z", true);
			Label l1 = new Label();
			mv.visitJumpInsn(IFEQ, l1);
			mv.visitInsn(FCONST_1);
			mv.visitInsn(FCONST_1);
			mv.visitInsn(FCONST_1);
			mv.visitInsn(FCONST_1);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "color", "(FFFF)V", false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "enableRescaleNormal",
					"()V", false);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/tileentity/TileEntityItemStackRenderer",
					"instance", "Lnet/minecraft/client/renderer/tileentity/TileEntityItemStackRenderer;");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/tileentity/TileEntityItemStackRenderer",
					"renderByItem", "(Lnet/minecraft/item/ItemStack;)V", false);
			Label l2 = new Label();
			mv.visitJumpInsn(GOTO, l2);
			mv.visitLabel(l1);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "renderModel",
					"(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/item/ItemStack;)V", false);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "hasEffect", "()Z", false);
			mv.visitJumpInsn(IFEQ, l2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "renderEffect",
					"(Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", false);
			mv.visitLabel(l2);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "popMatrix", "()V", false);
			mv.visitLabel(l0);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			mv.visitMaxs(4, 3);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PRIVATE, "renderEffect",
					"(Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", null, null);
			mv.visitCode();
			mv.visitInsn(ICONST_0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "depthMask", "(Z)V",
					false);
			mv.visitIntInsn(SIPUSH, 514);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "depthFunc", "(I)V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "disableLighting", "()V",
					false);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/GlStateManager$SourceFactor", "SRC_COLOR",
					"Lnet/minecraft/client/renderer/GlStateManager$SourceFactor;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/GlStateManager$DestFactor", "ONE",
					"Lnet/minecraft/client/renderer/GlStateManager$DestFactor;");
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "blendFunc",
					"(Lnet/minecraft/client/renderer/GlStateManager$SourceFactor;Lnet/minecraft/client/renderer/GlStateManager$DestFactor;)V",
					false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "textureManager",
					"Lnet/minecraft/client/renderer/texture/TextureManager;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/RenderItem", "RES_ITEM_GLINT",
					"Lnet/minecraft/util/ResourceLocation;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/texture/TextureManager", "bindTexture",
					"(Lnet/minecraft/util/ResourceLocation;)V", false);
			mv.visitIntInsn(SIPUSH, 5890);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "matrixMode", "(I)V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "pushMatrix", "()V",
					false);
			mv.visitLdcInsn(new Float("8.0"));
			mv.visitLdcInsn(new Float("8.0"));
			mv.visitLdcInsn(new Float("8.0"));
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "scale", "(FFF)V", false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/Minecraft", "getSystemTime", "()J", false);
			mv.visitLdcInsn(new Long(3000L));
			mv.visitInsn(LREM);
			mv.visitInsn(L2F);
			mv.visitLdcInsn(new Float("3000.0"));
			mv.visitInsn(FDIV);
			mv.visitLdcInsn(new Float("8.0"));
			mv.visitInsn(FDIV);
			mv.visitVarInsn(FSTORE, 2);
			mv.visitVarInsn(FLOAD, 2);
			mv.visitInsn(FCONST_0);
			mv.visitInsn(FCONST_0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "translate", "(FFF)V",
					false);
			mv.visitLdcInsn(new Float("-50.0"));
			mv.visitInsn(FCONST_0);
			mv.visitInsn(FCONST_0);
			mv.visitInsn(FCONST_1);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "rotate", "(FFFF)V",
					false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitLdcInsn(new Integer(-8372020));
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "renderModel",
					"(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V", false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "popMatrix", "()V", false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "pushMatrix", "()V",
					false);
			mv.visitLdcInsn(new Float("8.0"));
			mv.visitLdcInsn(new Float("8.0"));
			mv.visitLdcInsn(new Float("8.0"));
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "scale", "(FFF)V", false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/Minecraft", "getSystemTime", "()J", false);
			mv.visitLdcInsn(new Long(4873L));
			mv.visitInsn(LREM);
			mv.visitInsn(L2F);
			mv.visitLdcInsn(new Float("4873.0"));
			mv.visitInsn(FDIV);
			mv.visitLdcInsn(new Float("8.0"));
			mv.visitInsn(FDIV);
			mv.visitVarInsn(FSTORE, 3);
			mv.visitVarInsn(FLOAD, 3);
			mv.visitInsn(FNEG);
			mv.visitInsn(FCONST_0);
			mv.visitInsn(FCONST_0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "translate", "(FFF)V",
					false);
			mv.visitLdcInsn(new Float("10.0"));
			mv.visitInsn(FCONST_0);
			mv.visitInsn(FCONST_0);
			mv.visitInsn(FCONST_1);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "rotate", "(FFFF)V",
					false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitLdcInsn(new Integer(-8372020));
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "renderModel",
					"(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V", false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "popMatrix", "()V", false);
			mv.visitIntInsn(SIPUSH, 5888);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "matrixMode", "(I)V",
					false);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/GlStateManager$SourceFactor", "SRC_ALPHA",
					"Lnet/minecraft/client/renderer/GlStateManager$SourceFactor;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/GlStateManager$DestFactor",
					"ONE_MINUS_SRC_ALPHA", "Lnet/minecraft/client/renderer/GlStateManager$DestFactor;");
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "blendFunc",
					"(Lnet/minecraft/client/renderer/GlStateManager$SourceFactor;Lnet/minecraft/client/renderer/GlStateManager$DestFactor;)V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "enableLighting", "()V",
					false);
			mv.visitIntInsn(SIPUSH, 515);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "depthFunc", "(I)V",
					false);
			mv.visitInsn(ICONST_1);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "depthMask", "(Z)V",
					false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "textureManager",
					"Lnet/minecraft/client/renderer/texture/TextureManager;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/texture/TextureMap", "LOCATION_BLOCKS_TEXTURE",
					"Lnet/minecraft/util/ResourceLocation;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/texture/TextureManager", "bindTexture",
					"(Lnet/minecraft/util/ResourceLocation;)V", false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(4, 4);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PRIVATE, "putQuadNormal",
					"(Lnet/minecraft/client/renderer/BufferBuilder;Lnet/minecraft/client/renderer/block/model/BakedQuad;)V",
					null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/block/model/BakedQuad", "getFace",
					"()Lnet/minecraft/util/EnumFacing;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/EnumFacing", "getDirectionVec",
					"()Lnet/minecraft/util/math/Vec3i;", false);
			mv.visitVarInsn(ASTORE, 3);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/math/Vec3i", "getX", "()I", false);
			mv.visitInsn(I2F);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/math/Vec3i", "getY", "()I", false);
			mv.visitInsn(I2F);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/math/Vec3i", "getZ", "()I", false);
			mv.visitInsn(I2F);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/BufferBuilder", "putNormal", "(FFF)V",
					false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(4, 4);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PRIVATE, "renderQuad",
					"(Lnet/minecraft/client/renderer/BufferBuilder;Lnet/minecraft/client/renderer/block/model/BakedQuad;I)V",
					null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/block/model/BakedQuad", "getVertexData",
					"()[I", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/BufferBuilder", "addVertexData", "([I)V",
					false);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/BufferBuilder", "putColor4", "(I)V",
					false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "putQuadNormal",
					"(Lnet/minecraft/client/renderer/BufferBuilder;Lnet/minecraft/client/renderer/block/model/BakedQuad;)V",
					false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(3, 4);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PRIVATE, "renderQuads",
					"(Lnet/minecraft/client/renderer/BufferBuilder;Ljava/util/List;ILnet/minecraft/item/ItemStack;)V",
					"(Lnet/minecraft/client/renderer/BufferBuilder;Ljava/util/List<Lnet/minecraft/client/renderer/block/model/BakedQuad;>;ILnet/minecraft/item/ItemStack;)V",
					null);
			{
				av0 = mv.visitParameterAnnotation(3, "Ljavax/annotation/Nullable;", true);
				av0.visitEnd();
			}
			mv.visitCode();
			mv.visitVarInsn(ILOAD, 3);
			mv.visitInsn(ICONST_M1);
			Label l0 = new Label();
			mv.visitJumpInsn(IF_ICMPNE, l0);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitJumpInsn(IFNULL, l0);
			mv.visitInsn(ICONST_1);
			Label l1 = new Label();
			mv.visitJumpInsn(GOTO, l1);
			mv.visitLabel(l0);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(ICONST_0);
			mv.visitLabel(l1);
			mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] { Opcodes.INTEGER });
			mv.visitVarInsn(ISTORE, 5);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ISTORE, 6);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "size", "()I", true);
			mv.visitVarInsn(ISTORE, 7);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitFrame(Opcodes.F_APPEND, 3, new Object[] { Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER }, 0,
					null);
			mv.visitVarInsn(ILOAD, 6);
			mv.visitVarInsn(ILOAD, 7);
			Label l3 = new Label();
			mv.visitJumpInsn(IF_ICMPGE, l3);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ILOAD, 6);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "get", "(I)Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "net/minecraft/client/renderer/block/model/BakedQuad");
			mv.visitVarInsn(ASTORE, 8);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ISTORE, 9);
			mv.visitVarInsn(ILOAD, 5);
			Label l4 = new Label();
			mv.visitJumpInsn(IFEQ, l4);
			mv.visitVarInsn(ALOAD, 8);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/block/model/BakedQuad", "hasTintIndex",
					"()Z", false);
			mv.visitJumpInsn(IFEQ, l4);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "itemColors",
					"Lnet/minecraft/client/renderer/color/ItemColors;");
			mv.visitVarInsn(ALOAD, 4);
			mv.visitVarInsn(ALOAD, 8);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/block/model/BakedQuad", "getTintIndex",
					"()I", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/color/ItemColors", "getColorFromItemstack",
					"(Lnet/minecraft/item/ItemStack;I)I", false);
			mv.visitVarInsn(ISTORE, 9);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/EntityRenderer", "anaglyphEnable", "Z");
			Label l5 = new Label();
			mv.visitJumpInsn(IFEQ, l5);
			mv.visitVarInsn(ILOAD, 9);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/texture/TextureUtil", "anaglyphColor",
					"(I)I", false);
			mv.visitVarInsn(ISTORE, 9);
			mv.visitLabel(l5);
			mv.visitFrame(Opcodes.F_APPEND, 2,
					new Object[] { "net/minecraft/client/renderer/block/model/BakedQuad", Opcodes.INTEGER }, 0, null);
			mv.visitVarInsn(ILOAD, 9);
			mv.visitLdcInsn(new Integer(-16777216));
			mv.visitInsn(IOR);
			mv.visitVarInsn(ISTORE, 9);
			mv.visitLabel(l4);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 8);
			mv.visitVarInsn(ILOAD, 9);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/client/model/pipeline/LightUtil", "renderQuadColor",
					"(Lnet/minecraft/client/renderer/BufferBuilder;Lnet/minecraft/client/renderer/block/model/BakedQuad;I)V",
					false);
			mv.visitIincInsn(6, 1);
			mv.visitJumpInsn(GOTO, l2);
			mv.visitLabel(l3);
			mv.visitFrame(Opcodes.F_CHOP, 3, null, 0, null);
			mv.visitInsn(RETURN);
			mv.visitMaxs(3, 10);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "shouldRenderItemIn3D", "(Lnet/minecraft/item/ItemStack;)Z", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "itemModelMesher",
					"Lnet/minecraft/client/renderer/ItemModelMesher;");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/ItemModelMesher", "getItemModel",
					"(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/renderer/block/model/IBakedModel;", false);
			mv.visitVarInsn(ASTORE, 2);
			mv.visitVarInsn(ALOAD, 2);
			Label l0 = new Label();
			mv.visitJumpInsn(IFNONNULL, l0);
			mv.visitInsn(ICONST_0);
			Label l1 = new Label();
			mv.visitJumpInsn(GOTO, l1);
			mv.visitLabel(l0);
			mv.visitFrame(Opcodes.F_APPEND, 1, new Object[] { "net/minecraft/client/renderer/block/model/IBakedModel" },
					0, null);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/client/renderer/block/model/IBakedModel", "isGui3d",
					"()Z", true);
			mv.visitLabel(l1);
			mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] { Opcodes.INTEGER });
			mv.visitInsn(IRETURN);
			mv.visitMaxs(2, 3);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "renderItem",
					"(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V",
					null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 1);
			Label l0 = new Label();
			mv.visitJumpInsn(IFNULL, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitInsn(ACONST_NULL);
			mv.visitTypeInsn(CHECKCAST, "net/minecraft/world/World");
			mv.visitInsn(ACONST_NULL);
			mv.visitTypeInsn(CHECKCAST, "net/minecraft/entity/EntityLivingBase");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "getItemModelWithOverrides",
					"(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;)Lnet/minecraft/client/renderer/block/model/IBakedModel;",
					false);
			mv.visitVarInsn(ASTORE, 3);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitInsn(ICONST_0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "renderItemModel",
					"(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V",
					false);
			mv.visitLabel(l0);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			mv.visitMaxs(5, 4);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "getItemModelWithOverrides",
					"(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;)Lnet/minecraft/client/renderer/block/model/IBakedModel;",
					null, null);
			{
				av0 = mv.visitParameterAnnotation(1, "Ljavax/annotation/Nullable;", true);
				av0.visitEnd();
			}
			{
				av0 = mv.visitParameterAnnotation(2, "Ljavax/annotation/Nullable;", true);
				av0.visitEnd();
			}
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "itemModelMesher",
					"Lnet/minecraft/client/renderer/ItemModelMesher;");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/ItemModelMesher", "getItemModel",
					"(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/renderer/block/model/IBakedModel;", false);
			mv.visitVarInsn(ASTORE, 4);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/client/renderer/block/model/IBakedModel", "getOverrides",
					"()Lnet/minecraft/client/renderer/block/model/ItemOverrideList;", true);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/block/model/ItemOverrideList",
					"handleItemState",
					"(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;)Lnet/minecraft/client/renderer/block/model/IBakedModel;",
					false);
			mv.visitInsn(ARETURN);
			mv.visitMaxs(5, 5);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "renderItem",
					"(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V",
					null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 1);
			Label l0 = new Label();
			mv.visitJumpInsn(IFNULL, l0);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitJumpInsn(IFNULL, l0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getItem", "()Lnet/minecraft/item/Item;",
					false);
			mv.visitJumpInsn(IFNULL, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/entity/EntityLivingBase", "world",
					"Lnet/minecraft/world/World;");
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "getItemModelWithOverrides",
					"(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;)Lnet/minecraft/client/renderer/block/model/IBakedModel;",
					false);
			mv.visitVarInsn(ASTORE, 5);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "renderItemModel",
					"(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V",
					false);
			mv.visitLabel(l0);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			mv.visitMaxs(5, 6);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PROTECTED, "renderItemModel",
					"(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V",
					null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getItem", "()Lnet/minecraft/item/Item;",
					false);
			Label l0 = new Label();
			mv.visitJumpInsn(IFNULL, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "textureManager",
					"Lnet/minecraft/client/renderer/texture/TextureManager;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/texture/TextureMap", "LOCATION_BLOCKS_TEXTURE",
					"Lnet/minecraft/util/ResourceLocation;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/texture/TextureManager", "bindTexture",
					"(Lnet/minecraft/util/ResourceLocation;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "textureManager",
					"Lnet/minecraft/client/renderer/texture/TextureManager;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/texture/TextureMap", "LOCATION_BLOCKS_TEXTURE",
					"Lnet/minecraft/util/ResourceLocation;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/texture/TextureManager", "getTexture",
					"(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/renderer/texture/ITextureObject;",
					false);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(ICONST_0);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/client/renderer/texture/ITextureObject", "setBlurMipmap",
					"(ZZ)V", true);
			mv.visitInsn(FCONST_1);
			mv.visitInsn(FCONST_1);
			mv.visitInsn(FCONST_1);
			mv.visitInsn(FCONST_1);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "color", "(FFFF)V", false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "enableRescaleNormal",
					"()V", false);
			mv.visitIntInsn(SIPUSH, 516);
			mv.visitLdcInsn(new Float("0.1"));
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "alphaFunc", "(IF)V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "enableBlend", "()V",
					false);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/GlStateManager$SourceFactor", "SRC_ALPHA",
					"Lnet/minecraft/client/renderer/GlStateManager$SourceFactor;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/GlStateManager$DestFactor",
					"ONE_MINUS_SRC_ALPHA", "Lnet/minecraft/client/renderer/GlStateManager$DestFactor;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/GlStateManager$SourceFactor", "ONE",
					"Lnet/minecraft/client/renderer/GlStateManager$SourceFactor;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/GlStateManager$DestFactor", "ZERO",
					"Lnet/minecraft/client/renderer/GlStateManager$DestFactor;");
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "tryBlendFuncSeparate",
					"(Lnet/minecraft/client/renderer/GlStateManager$SourceFactor;Lnet/minecraft/client/renderer/GlStateManager$DestFactor;Lnet/minecraft/client/renderer/GlStateManager$SourceFactor;Lnet/minecraft/client/renderer/GlStateManager$DestFactor;)V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "pushMatrix", "()V",
					false);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/client/ForgeHooksClient", "handleCameraTransforms",
					"(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)Lnet/minecraft/client/renderer/block/model/IBakedModel;",
					false);
			mv.visitVarInsn(ASTORE, 2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "renderItem",
					"(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", false);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/GlStateManager$CullFace", "BACK",
					"Lnet/minecraft/client/renderer/GlStateManager$CullFace;");
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "cullFace",
					"(Lnet/minecraft/client/renderer/GlStateManager$CullFace;)V", false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "popMatrix", "()V", false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "disableRescaleNormal",
					"()V", false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "disableBlend", "()V",
					false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "textureManager",
					"Lnet/minecraft/client/renderer/texture/TextureManager;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/texture/TextureMap", "LOCATION_BLOCKS_TEXTURE",
					"Lnet/minecraft/util/ResourceLocation;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/texture/TextureManager", "bindTexture",
					"(Lnet/minecraft/util/ResourceLocation;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "textureManager",
					"Lnet/minecraft/client/renderer/texture/TextureManager;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/texture/TextureMap", "LOCATION_BLOCKS_TEXTURE",
					"Lnet/minecraft/util/ResourceLocation;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/texture/TextureManager", "getTexture",
					"(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/renderer/texture/ITextureObject;",
					false);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/client/renderer/texture/ITextureObject",
					"restoreLastBlurMipmap", "()V", true);
			mv.visitLabel(l0);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			mv.visitMaxs(4, 5);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PRIVATE, "isThereOneNegativeScale",
					"(Lnet/minecraft/client/renderer/block/model/ItemTransformVec3f;)Z", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/block/model/ItemTransformVec3f", "scale",
					"Lorg/lwjgl/util/vector/Vector3f;");
			mv.visitFieldInsn(GETFIELD, "org/lwjgl/util/vector/Vector3f", "x", "F");
			mv.visitInsn(FCONST_0);
			mv.visitInsn(FCMPG);
			Label l0 = new Label();
			mv.visitJumpInsn(IFGE, l0);
			mv.visitInsn(ICONST_1);
			Label l1 = new Label();
			mv.visitJumpInsn(GOTO, l1);
			mv.visitLabel(l0);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(ICONST_0);
			mv.visitLabel(l1);
			mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] { Opcodes.INTEGER });
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/block/model/ItemTransformVec3f", "scale",
					"Lorg/lwjgl/util/vector/Vector3f;");
			mv.visitFieldInsn(GETFIELD, "org/lwjgl/util/vector/Vector3f", "y", "F");
			mv.visitInsn(FCONST_0);
			mv.visitInsn(FCMPG);
			Label l2 = new Label();
			mv.visitJumpInsn(IFGE, l2);
			mv.visitInsn(ICONST_1);
			Label l3 = new Label();
			mv.visitJumpInsn(GOTO, l3);
			mv.visitLabel(l2);
			mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] { Opcodes.INTEGER });
			mv.visitInsn(ICONST_0);
			mv.visitLabel(l3);
			mv.visitFrame(Opcodes.F_FULL, 2,
					new Object[] { "net/minecraft/client/renderer/RenderItem",
							"net/minecraft/client/renderer/block/model/ItemTransformVec3f" },
					2, new Object[] { Opcodes.INTEGER, Opcodes.INTEGER });
			mv.visitInsn(IXOR);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/block/model/ItemTransformVec3f", "scale",
					"Lorg/lwjgl/util/vector/Vector3f;");
			mv.visitFieldInsn(GETFIELD, "org/lwjgl/util/vector/Vector3f", "z", "F");
			mv.visitInsn(FCONST_0);
			mv.visitInsn(FCMPG);
			Label l4 = new Label();
			mv.visitJumpInsn(IFGE, l4);
			mv.visitInsn(ICONST_1);
			Label l5 = new Label();
			mv.visitJumpInsn(GOTO, l5);
			mv.visitLabel(l4);
			mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] { Opcodes.INTEGER });
			mv.visitInsn(ICONST_0);
			mv.visitLabel(l5);
			mv.visitFrame(Opcodes.F_FULL, 2,
					new Object[] { "net/minecraft/client/renderer/RenderItem",
							"net/minecraft/client/renderer/block/model/ItemTransformVec3f" },
					2, new Object[] { Opcodes.INTEGER, Opcodes.INTEGER });
			mv.visitInsn(IXOR);
			mv.visitInsn(IRETURN);
			mv.visitMaxs(3, 2);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "renderItemIntoGUI", "(Lnet/minecraft/item/ItemStack;II)V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitInsn(ACONST_NULL);
			mv.visitTypeInsn(CHECKCAST, "net/minecraft/world/World");
			mv.visitInsn(ACONST_NULL);
			mv.visitTypeInsn(CHECKCAST, "net/minecraft/entity/EntityLivingBase");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "getItemModelWithOverrides",
					"(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;)Lnet/minecraft/client/renderer/block/model/IBakedModel;",
					false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "renderItemModelIntoGUI",
					"(Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/renderer/block/model/IBakedModel;)V",
					false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(8, 4);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PROTECTED, "renderItemModelIntoGUI",
					"(Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/renderer/block/model/IBakedModel;)V", null,
					null);
			mv.visitCode();
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "pushMatrix", "()V",
					false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "textureManager",
					"Lnet/minecraft/client/renderer/texture/TextureManager;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/texture/TextureMap", "LOCATION_BLOCKS_TEXTURE",
					"Lnet/minecraft/util/ResourceLocation;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/texture/TextureManager", "bindTexture",
					"(Lnet/minecraft/util/ResourceLocation;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "textureManager",
					"Lnet/minecraft/client/renderer/texture/TextureManager;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/texture/TextureMap", "LOCATION_BLOCKS_TEXTURE",
					"Lnet/minecraft/util/ResourceLocation;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/texture/TextureManager", "getTexture",
					"(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/renderer/texture/ITextureObject;",
					false);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(ICONST_0);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/client/renderer/texture/ITextureObject", "setBlurMipmap",
					"(ZZ)V", true);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "enableRescaleNormal",
					"()V", false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "enableAlpha", "()V",
					false);
			mv.visitIntInsn(SIPUSH, 516);
			mv.visitLdcInsn(new Float("0.1"));
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "alphaFunc", "(IF)V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "enableBlend", "()V",
					false);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/GlStateManager$SourceFactor", "SRC_ALPHA",
					"Lnet/minecraft/client/renderer/GlStateManager$SourceFactor;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/GlStateManager$DestFactor",
					"ONE_MINUS_SRC_ALPHA", "Lnet/minecraft/client/renderer/GlStateManager$DestFactor;");
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "blendFunc",
					"(Lnet/minecraft/client/renderer/GlStateManager$SourceFactor;Lnet/minecraft/client/renderer/GlStateManager$DestFactor;)V",
					false);
			mv.visitInsn(FCONST_1);
			mv.visitInsn(FCONST_1);
			mv.visitInsn(FCONST_1);
			mv.visitInsn(FCONST_1);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "color", "(FFFF)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/client/renderer/block/model/IBakedModel", "isGui3d",
					"()Z", true);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "setupGuiTransform", "(IIZ)V",
					false);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType",
					"GUI", "Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;");
			mv.visitInsn(ICONST_0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/client/ForgeHooksClient", "handleCameraTransforms",
					"(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)Lnet/minecraft/client/renderer/block/model/IBakedModel;",
					false);
			mv.visitVarInsn(ASTORE, 4);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "renderItem",
					"(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "disableAlpha", "()V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "disableRescaleNormal",
					"()V", false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "disableLighting", "()V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "popMatrix", "()V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "textureManager",
					"Lnet/minecraft/client/renderer/texture/TextureManager;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/texture/TextureMap", "LOCATION_BLOCKS_TEXTURE",
					"Lnet/minecraft/util/ResourceLocation;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/texture/TextureManager", "bindTexture",
					"(Lnet/minecraft/util/ResourceLocation;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "textureManager",
					"Lnet/minecraft/client/renderer/texture/TextureManager;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/texture/TextureMap", "LOCATION_BLOCKS_TEXTURE",
					"Lnet/minecraft/util/ResourceLocation;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/texture/TextureManager", "getTexture",
					"(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/renderer/texture/ITextureObject;",
					false);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/client/renderer/texture/ITextureObject",
					"restoreLastBlurMipmap", "()V", true);
			mv.visitInsn(RETURN);
			mv.visitMaxs(4, 5);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PRIVATE, "setupGuiTransform", "(IIZ)V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(I2F);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitInsn(I2F);
			mv.visitLdcInsn(new Float("100.0"));
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "zLevel", "F");
			mv.visitInsn(FADD);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "translate", "(FFF)V",
					false);
			mv.visitLdcInsn(new Float("8.0"));
			mv.visitLdcInsn(new Float("8.0"));
			mv.visitInsn(FCONST_0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "translate", "(FFF)V",
					false);
			mv.visitInsn(FCONST_1);
			mv.visitLdcInsn(new Float("-1.0"));
			mv.visitInsn(FCONST_1);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "scale", "(FFF)V", false);
			mv.visitLdcInsn(new Float("16.0"));
			mv.visitLdcInsn(new Float("16.0"));
			mv.visitLdcInsn(new Float("16.0"));
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "scale", "(FFF)V", false);
			mv.visitVarInsn(ILOAD, 3);
			Label l0 = new Label();
			mv.visitJumpInsn(IFEQ, l0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "enableLighting", "()V",
					false);
			Label l1 = new Label();
			mv.visitJumpInsn(GOTO, l1);
			mv.visitLabel(l0);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "disableLighting", "()V",
					false);
			mv.visitLabel(l1);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			mv.visitMaxs(4, 4);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "renderItemAndEffectIntoGUI", "(Lnet/minecraft/item/ItemStack;II)V", null,
					null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/Minecraft", "getMinecraft",
					"()Lnet/minecraft/client/Minecraft;", false);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", "player",
					"Lnet/minecraft/client/entity/EntityPlayerSP;");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "renderItemAndEffectIntoGUI",
					"(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;II)V", false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(5, 4);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "renderItemAndEffectIntoGUI",
					"(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;II)V", null, null);
			{
				av0 = mv.visitParameterAnnotation(0, "Ljavax/annotation/Nullable;", true);
				av0.visitEnd();
			}
			mv.visitCode();
			Label l0 = new Label();
			Label l1 = new Label();
			Label l2 = new Label();
			mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Throwable");
			mv.visitVarInsn(ALOAD, 2);
			Label l3 = new Label();
			mv.visitJumpInsn(IFNULL, l3);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getItem", "()Lnet/minecraft/item/Item;",
					false);
			mv.visitJumpInsn(IFNULL, l3);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(DUP);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "zLevel", "F");
			mv.visitLdcInsn(new Float("50.0"));
			mv.visitInsn(FADD);
			mv.visitFieldInsn(PUTFIELD, "net/minecraft/client/renderer/RenderItem", "zLevel", "F");
			mv.visitLabel(l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitInsn(ACONST_NULL);
			mv.visitTypeInsn(CHECKCAST, "net/minecraft/world/World");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "getItemModelWithOverrides",
					"(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;)Lnet/minecraft/client/renderer/block/model/IBakedModel;",
					false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "renderItemModelIntoGUI",
					"(Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/renderer/block/model/IBakedModel;)V",
					false);
			mv.visitLabel(l1);
			Label l4 = new Label();
			mv.visitJumpInsn(GOTO, l4);
			mv.visitLabel(l2);
			mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] { "java/lang/Throwable" });
			mv.visitVarInsn(ASTORE, 5);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitLdcInsn("Rendering item");
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/crash/CrashReport", "makeCrashReport",
					"(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/crash/CrashReport;", false);
			mv.visitVarInsn(ASTORE, 6);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitLdcInsn("Item being rendered");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/crash/CrashReport", "makeCategory",
					"(Ljava/lang/String;)Lnet/minecraft/crash/CrashReportCategory;", false);
			mv.visitVarInsn(ASTORE, 7);
			mv.visitVarInsn(ALOAD, 7);
			mv.visitLdcInsn("Item Type");
			mv.visitTypeInsn(NEW, "net/minecraft/client/renderer/RenderItem$1");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem$1", "<init>",
					"(Lnet/minecraft/client/renderer/RenderItem;Lnet/minecraft/item/ItemStack;)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/crash/CrashReportCategory", "setDetail",
					"(Ljava/lang/String;Lnet/minecraft/crash/ICrashReportDetail;)V", false);
			mv.visitVarInsn(ALOAD, 7);
			mv.visitLdcInsn("Item Aux");
			mv.visitTypeInsn(NEW, "net/minecraft/client/renderer/RenderItem$2");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem$2", "<init>",
					"(Lnet/minecraft/client/renderer/RenderItem;Lnet/minecraft/item/ItemStack;)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/crash/CrashReportCategory", "setDetail",
					"(Ljava/lang/String;Lnet/minecraft/crash/ICrashReportDetail;)V", false);
			mv.visitVarInsn(ALOAD, 7);
			mv.visitLdcInsn("Item NBT");
			mv.visitTypeInsn(NEW, "net/minecraft/client/renderer/RenderItem$3");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem$3", "<init>",
					"(Lnet/minecraft/client/renderer/RenderItem;Lnet/minecraft/item/ItemStack;)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/crash/CrashReportCategory", "setDetail",
					"(Ljava/lang/String;Lnet/minecraft/crash/ICrashReportDetail;)V", false);
			mv.visitVarInsn(ALOAD, 7);
			mv.visitLdcInsn("Item Foil");
			mv.visitTypeInsn(NEW, "net/minecraft/client/renderer/RenderItem$4");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem$4", "<init>",
					"(Lnet/minecraft/client/renderer/RenderItem;Lnet/minecraft/item/ItemStack;)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/crash/CrashReportCategory", "setDetail",
					"(Ljava/lang/String;Lnet/minecraft/crash/ICrashReportDetail;)V", false);
			mv.visitTypeInsn(NEW, "net/minecraft/util/ReportedException");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/util/ReportedException", "<init>",
					"(Lnet/minecraft/crash/CrashReport;)V", false);
			mv.visitInsn(ATHROW);
			mv.visitLabel(l4);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(DUP);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "zLevel", "F");
			mv.visitLdcInsn(new Float("50.0"));
			mv.visitInsn(FSUB);
			mv.visitFieldInsn(PUTFIELD, "net/minecraft/client/renderer/RenderItem", "zLevel", "F");
			mv.visitLabel(l3);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			mv.visitMaxs(8, 8);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "renderItemOverlays",
					"(Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/item/ItemStack;II)V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitInsn(ACONST_NULL);
			mv.visitTypeInsn(CHECKCAST, "java/lang/String");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "renderItemOverlayIntoGUI",
					"(Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
					false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(6, 5);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "renderItemOverlayIntoGUI",
					"(Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
					null, null);
			{
				av0 = mv.visitParameterAnnotation(4, "Ljavax/annotation/Nullable;", true);
				av0.visitEnd();
			}
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 2);
			Label l0 = new Label();
			mv.visitJumpInsn(IFNULL, l0);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/item/ItemStack", "stackSize", "I");
			mv.visitInsn(ICONST_1);
			Label l1 = new Label();
			mv.visitJumpInsn(IF_ICMPNE, l1);
			mv.visitVarInsn(ALOAD, 5);
			Label l2 = new Label();
			mv.visitJumpInsn(IFNULL, l2);
			mv.visitLabel(l1);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 5);
			Label l3 = new Label();
			mv.visitJumpInsn(IFNONNULL, l3);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/item/ItemStack", "stackSize", "I");
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/String", "valueOf", "(I)Ljava/lang/String;", false);
			Label l4 = new Label();
			mv.visitJumpInsn(GOTO, l4);
			mv.visitLabel(l3);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitLabel(l4);
			mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] { "java/lang/String" });
			mv.visitVarInsn(ASTORE, 6);
			mv.visitVarInsn(ALOAD, 5);
			Label l5 = new Label();
			mv.visitJumpInsn(IFNONNULL, l5);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/item/ItemStack", "stackSize", "I");
			mv.visitInsn(ICONST_1);
			mv.visitJumpInsn(IF_ICMPGE, l5);
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/util/text/TextFormatting", "RED",
					"Lnet/minecraft/util/text/TextFormatting;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
					"(Ljava/lang/Object;)Ljava/lang/StringBuilder;", false);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/item/ItemStack", "stackSize", "I");
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/String", "valueOf", "(I)Ljava/lang/String;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
					"(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
			mv.visitVarInsn(ASTORE, 6);
			mv.visitLabel(l5);
			mv.visitFrame(Opcodes.F_APPEND, 1, new Object[] { "java/lang/String" }, 0, null);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "disableLighting", "()V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "disableDepth", "()V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "disableBlend", "()V",
					false);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitIntInsn(BIPUSH, 19);
			mv.visitInsn(IADD);
			mv.visitInsn(ICONST_2);
			mv.visitInsn(ISUB);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/gui/FontRenderer", "getStringWidth",
					"(Ljava/lang/String;)I", false);
			mv.visitInsn(ISUB);
			mv.visitInsn(I2F);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitIntInsn(BIPUSH, 6);
			mv.visitInsn(IADD);
			mv.visitInsn(ICONST_3);
			mv.visitInsn(IADD);
			mv.visitInsn(I2F);
			mv.visitLdcInsn(new Integer(16777215));
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/gui/FontRenderer", "drawStringWithShadow",
					"(Ljava/lang/String;FFI)I", false);
			mv.visitInsn(POP);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "enableLighting", "()V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "enableDepth", "()V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "enableBlend", "()V",
					false);
			mv.visitLabel(l2);
			mv.visitFrame(Opcodes.F_CHOP, 1, null, 0, null);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getItem", "()Lnet/minecraft/item/Item;",
					false);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/Item", "showDurabilityBar",
					"(Lnet/minecraft/item/ItemStack;)Z", false);
			Label l6 = new Label();
			mv.visitJumpInsn(IFEQ, l6);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getItem", "()Lnet/minecraft/item/Item;",
					false);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/Item", "getDurabilityForDisplay",
					"(Lnet/minecraft/item/ItemStack;)D", false);
			mv.visitVarInsn(DSTORE, 6);
			mv.visitLdcInsn(new Double("13.0"));
			mv.visitVarInsn(DLOAD, 6);
			mv.visitLdcInsn(new Double("13.0"));
			mv.visitInsn(DMUL);
			mv.visitInsn(DSUB);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "round", "(D)J", false);
			mv.visitInsn(L2I);
			mv.visitVarInsn(ISTORE, 8);
			mv.visitLdcInsn(new Double("255.0"));
			mv.visitVarInsn(DLOAD, 6);
			mv.visitLdcInsn(new Double("255.0"));
			mv.visitInsn(DMUL);
			mv.visitInsn(DSUB);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "round", "(D)J", false);
			mv.visitInsn(L2I);
			mv.visitVarInsn(ISTORE, 9);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "disableLighting", "()V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "disableDepth", "()V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "disableTexture2D", "()V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "disableAlpha", "()V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "disableBlend", "()V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/Tessellator", "getInstance",
					"()Lnet/minecraft/client/renderer/Tessellator;", false);
			mv.visitVarInsn(ASTORE, 10);
			mv.visitVarInsn(ALOAD, 10);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/Tessellator", "getBuffer",
					"()Lnet/minecraft/client/renderer/BufferBuilder;", false);
			mv.visitVarInsn(ASTORE, 11);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 11);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitInsn(ICONST_2);
			mv.visitInsn(IADD);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitIntInsn(BIPUSH, 13);
			mv.visitInsn(IADD);
			mv.visitIntInsn(BIPUSH, 13);
			mv.visitInsn(ICONST_2);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(ICONST_0);
			mv.visitIntInsn(SIPUSH, 255);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "draw",
					"(Lnet/minecraft/client/renderer/BufferBuilder;IIIIIIII)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 11);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitInsn(ICONST_2);
			mv.visitInsn(IADD);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitIntInsn(BIPUSH, 13);
			mv.visitInsn(IADD);
			mv.visitIntInsn(BIPUSH, 12);
			mv.visitInsn(ICONST_1);
			mv.visitIntInsn(SIPUSH, 255);
			mv.visitVarInsn(ILOAD, 9);
			mv.visitInsn(ISUB);
			mv.visitInsn(ICONST_4);
			mv.visitInsn(IDIV);
			mv.visitIntInsn(BIPUSH, 64);
			mv.visitInsn(ICONST_0);
			mv.visitIntInsn(SIPUSH, 255);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "draw",
					"(Lnet/minecraft/client/renderer/BufferBuilder;IIIIIIII)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 11);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitInsn(ICONST_2);
			mv.visitInsn(IADD);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitIntInsn(BIPUSH, 13);
			mv.visitInsn(IADD);
			mv.visitVarInsn(ILOAD, 8);
			mv.visitInsn(ICONST_1);
			mv.visitIntInsn(SIPUSH, 255);
			mv.visitVarInsn(ILOAD, 9);
			mv.visitInsn(ISUB);
			mv.visitVarInsn(ILOAD, 9);
			mv.visitInsn(ICONST_0);
			mv.visitIntInsn(SIPUSH, 255);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "draw",
					"(Lnet/minecraft/client/renderer/BufferBuilder;IIIIIIII)V", false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "enableBlend", "()V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "enableAlpha", "()V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "enableTexture2D", "()V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "enableLighting", "()V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "enableDepth", "()V",
					false);
			mv.visitLabel(l6);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/Minecraft", "getMinecraft",
					"()Lnet/minecraft/client/Minecraft;", false);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", "player",
					"Lnet/minecraft/client/entity/EntityPlayerSP;");
			mv.visitVarInsn(ASTORE, 6);
			mv.visitVarInsn(ALOAD, 6);
			Label l7 = new Label();
			mv.visitJumpInsn(IFNONNULL, l7);
			mv.visitInsn(FCONST_0);
			Label l8 = new Label();
			mv.visitJumpInsn(GOTO, l8);
			mv.visitLabel(l7);
			mv.visitFrame(Opcodes.F_APPEND, 1, new Object[] { "net/minecraft/client/entity/EntityPlayerSP" }, 0, null);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/entity/EntityPlayerSP", "getCooldownTracker",
					"()Lnet/minecraft/util/CooldownTracker;", false);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getItem", "()Lnet/minecraft/item/Item;",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/Minecraft", "getMinecraft",
					"()Lnet/minecraft/client/Minecraft;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "getRenderPartialTicks", "()F", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/CooldownTracker", "getCooldown",
					"(Lnet/minecraft/item/Item;F)F", false);
			mv.visitLabel(l8);
			mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] { Opcodes.FLOAT });
			mv.visitVarInsn(FSTORE, 7);
			mv.visitVarInsn(FLOAD, 7);
			mv.visitInsn(FCONST_0);
			mv.visitInsn(FCMPL);
			mv.visitJumpInsn(IFLE, l0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "disableLighting", "()V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "disableDepth", "()V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "disableTexture2D", "()V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/Tessellator", "getInstance",
					"()Lnet/minecraft/client/renderer/Tessellator;", false);
			mv.visitVarInsn(ASTORE, 8);
			mv.visitVarInsn(ALOAD, 8);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/Tessellator", "getBuffer",
					"()Lnet/minecraft/client/renderer/BufferBuilder;", false);
			mv.visitVarInsn(ASTORE, 9);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 9);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitLdcInsn(new Float("16.0"));
			mv.visitInsn(FCONST_1);
			mv.visitVarInsn(FLOAD, 7);
			mv.visitInsn(FSUB);
			mv.visitInsn(FMUL);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/util/math/MathHelper", "floor", "(F)I", false);
			mv.visitInsn(IADD);
			mv.visitIntInsn(BIPUSH, 16);
			mv.visitLdcInsn(new Float("16.0"));
			mv.visitVarInsn(FLOAD, 7);
			mv.visitInsn(FMUL);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/util/math/MathHelper", "ceil", "(F)I", false);
			mv.visitIntInsn(SIPUSH, 255);
			mv.visitIntInsn(SIPUSH, 255);
			mv.visitIntInsn(SIPUSH, 255);
			mv.visitIntInsn(BIPUSH, 127);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "draw",
					"(Lnet/minecraft/client/renderer/BufferBuilder;IIIIIIII)V", false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "enableTexture2D", "()V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "enableLighting", "()V",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/GlStateManager", "enableDepth", "()V",
					false);
			mv.visitLabel(l0);
			mv.visitFrame(Opcodes.F_CHOP, 1, null, 0, null);
			mv.visitInsn(RETURN);
			mv.visitMaxs(10, 12);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PRIVATE, "draw", "(Lnet/minecraft/client/renderer/BufferBuilder;IIIIIIII)V", null,
					null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 1);
			mv.visitIntInsn(BIPUSH, 7);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/renderer/vertex/DefaultVertexFormats", "POSITION_COLOR",
					"Lnet/minecraft/client/renderer/vertex/VertexFormat;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/BufferBuilder", "begin",
					"(ILnet/minecraft/client/renderer/vertex/VertexFormat;)V", false);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IADD);
			mv.visitInsn(I2D);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IADD);
			mv.visitInsn(I2D);
			mv.visitInsn(DCONST_0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/BufferBuilder", "pos",
					"(DDD)Lnet/minecraft/client/renderer/BufferBuilder;", false);
			mv.visitVarInsn(ILOAD, 6);
			mv.visitVarInsn(ILOAD, 7);
			mv.visitVarInsn(ILOAD, 8);
			mv.visitVarInsn(ILOAD, 9);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/BufferBuilder", "color",
					"(IIII)Lnet/minecraft/client/renderer/BufferBuilder;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/BufferBuilder", "endVertex", "()V", false);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IADD);
			mv.visitInsn(I2D);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ILOAD, 5);
			mv.visitInsn(IADD);
			mv.visitInsn(I2D);
			mv.visitInsn(DCONST_0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/BufferBuilder", "pos",
					"(DDD)Lnet/minecraft/client/renderer/BufferBuilder;", false);
			mv.visitVarInsn(ILOAD, 6);
			mv.visitVarInsn(ILOAD, 7);
			mv.visitVarInsn(ILOAD, 8);
			mv.visitVarInsn(ILOAD, 9);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/BufferBuilder", "color",
					"(IIII)Lnet/minecraft/client/renderer/BufferBuilder;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/BufferBuilder", "endVertex", "()V", false);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitInsn(IADD);
			mv.visitInsn(I2D);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ILOAD, 5);
			mv.visitInsn(IADD);
			mv.visitInsn(I2D);
			mv.visitInsn(DCONST_0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/BufferBuilder", "pos",
					"(DDD)Lnet/minecraft/client/renderer/BufferBuilder;", false);
			mv.visitVarInsn(ILOAD, 6);
			mv.visitVarInsn(ILOAD, 7);
			mv.visitVarInsn(ILOAD, 8);
			mv.visitVarInsn(ILOAD, 9);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/BufferBuilder", "color",
					"(IIII)Lnet/minecraft/client/renderer/BufferBuilder;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/BufferBuilder", "endVertex", "()V", false);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitInsn(IADD);
			mv.visitInsn(I2D);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IADD);
			mv.visitInsn(I2D);
			mv.visitInsn(DCONST_0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/BufferBuilder", "pos",
					"(DDD)Lnet/minecraft/client/renderer/BufferBuilder;", false);
			mv.visitVarInsn(ILOAD, 6);
			mv.visitVarInsn(ILOAD, 7);
			mv.visitVarInsn(ILOAD, 8);
			mv.visitVarInsn(ILOAD, 9);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/BufferBuilder", "color",
					"(IIII)Lnet/minecraft/client/renderer/BufferBuilder;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/BufferBuilder", "endVertex", "()V", false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/renderer/Tessellator", "getInstance",
					"()Lnet/minecraft/client/renderer/Tessellator;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/Tessellator", "draw", "()V", false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(7, 10);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PRIVATE, "registerItems", "()V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "ANVIL", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("anvil_intact");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "ANVIL", "Lnet/minecraft/block/Block;");
			mv.visitInsn(ICONST_1);
			mv.visitLdcInsn("anvil_slightly_damaged");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "ANVIL", "Lnet/minecraft/block/Block;");
			mv.visitInsn(ICONST_2);
			mv.visitLdcInsn("anvil_very_damaged");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "CARPET", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "BLACK",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("black_carpet");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "CARPET", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "BLUE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("blue_carpet");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "CARPET", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "BROWN",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("brown_carpet");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "CARPET", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "CYAN",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("cyan_carpet");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "CARPET", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "GRAY",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("gray_carpet");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "CARPET", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "GREEN",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("green_carpet");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "CARPET", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "LIGHT_BLUE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("light_blue_carpet");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "CARPET", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "LIME",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("lime_carpet");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "CARPET", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "MAGENTA",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("magenta_carpet");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "CARPET", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "ORANGE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("orange_carpet");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "CARPET", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "PINK",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("pink_carpet");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "CARPET", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "PURPLE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("purple_carpet");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "CARPET", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "RED", "Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("red_carpet");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "CARPET", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "SILVER",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("silver_carpet");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "CARPET", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "WHITE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("white_carpet");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "CARPET", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "YELLOW",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("yellow_carpet");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "COBBLESTONE_WALL",
					"Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockWall$EnumType", "MOSSY",
					"Lnet/minecraft/block/BlockWall$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockWall$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("mossy_cobblestone_wall");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "COBBLESTONE_WALL",
					"Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockWall$EnumType", "NORMAL",
					"Lnet/minecraft/block/BlockWall$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockWall$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("cobblestone_wall");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "DIRT", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockDirt$DirtType", "COARSE_DIRT",
					"Lnet/minecraft/block/BlockDirt$DirtType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockDirt$DirtType", "getMetadata", "()I", false);
			mv.visitLdcInsn("coarse_dirt");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "DIRT", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockDirt$DirtType", "DIRT",
					"Lnet/minecraft/block/BlockDirt$DirtType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockDirt$DirtType", "getMetadata", "()I", false);
			mv.visitLdcInsn("dirt");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "DIRT", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockDirt$DirtType", "PODZOL",
					"Lnet/minecraft/block/BlockDirt$DirtType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockDirt$DirtType", "getMetadata", "()I", false);
			mv.visitLdcInsn("podzol");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "DOUBLE_PLANT",
					"Lnet/minecraft/block/BlockDoublePlant;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockDoublePlant$EnumPlantType", "FERN",
					"Lnet/minecraft/block/BlockDoublePlant$EnumPlantType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockDoublePlant$EnumPlantType", "getMeta", "()I",
					false);
			mv.visitLdcInsn("double_fern");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "DOUBLE_PLANT",
					"Lnet/minecraft/block/BlockDoublePlant;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockDoublePlant$EnumPlantType", "GRASS",
					"Lnet/minecraft/block/BlockDoublePlant$EnumPlantType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockDoublePlant$EnumPlantType", "getMeta", "()I",
					false);
			mv.visitLdcInsn("double_grass");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "DOUBLE_PLANT",
					"Lnet/minecraft/block/BlockDoublePlant;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockDoublePlant$EnumPlantType", "PAEONIA",
					"Lnet/minecraft/block/BlockDoublePlant$EnumPlantType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockDoublePlant$EnumPlantType", "getMeta", "()I",
					false);
			mv.visitLdcInsn("paeonia");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "DOUBLE_PLANT",
					"Lnet/minecraft/block/BlockDoublePlant;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockDoublePlant$EnumPlantType", "ROSE",
					"Lnet/minecraft/block/BlockDoublePlant$EnumPlantType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockDoublePlant$EnumPlantType", "getMeta", "()I",
					false);
			mv.visitLdcInsn("double_rose");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "DOUBLE_PLANT",
					"Lnet/minecraft/block/BlockDoublePlant;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockDoublePlant$EnumPlantType", "SUNFLOWER",
					"Lnet/minecraft/block/BlockDoublePlant$EnumPlantType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockDoublePlant$EnumPlantType", "getMeta", "()I",
					false);
			mv.visitLdcInsn("sunflower");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "DOUBLE_PLANT",
					"Lnet/minecraft/block/BlockDoublePlant;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockDoublePlant$EnumPlantType", "SYRINGA",
					"Lnet/minecraft/block/BlockDoublePlant$EnumPlantType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockDoublePlant$EnumPlantType", "getMeta", "()I",
					false);
			mv.visitLdcInsn("syringa");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "LEAVES", "Lnet/minecraft/block/BlockLeaves;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "BIRCH",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("birch_leaves");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "LEAVES", "Lnet/minecraft/block/BlockLeaves;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "JUNGLE",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("jungle_leaves");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "LEAVES", "Lnet/minecraft/block/BlockLeaves;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "OAK",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("oak_leaves");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "LEAVES", "Lnet/minecraft/block/BlockLeaves;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "SPRUCE",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("spruce_leaves");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "LEAVES2", "Lnet/minecraft/block/BlockLeaves;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "ACACIA",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitInsn(ICONST_4);
			mv.visitInsn(ISUB);
			mv.visitLdcInsn("acacia_leaves");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "LEAVES2", "Lnet/minecraft/block/BlockLeaves;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "DARK_OAK",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitInsn(ICONST_4);
			mv.visitInsn(ISUB);
			mv.visitLdcInsn("dark_oak_leaves");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "LOG", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "BIRCH",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("birch_log");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "LOG", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "JUNGLE",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("jungle_log");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "LOG", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "OAK",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("oak_log");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "LOG", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "SPRUCE",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("spruce_log");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "LOG2", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "ACACIA",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitInsn(ICONST_4);
			mv.visitInsn(ISUB);
			mv.visitLdcInsn("acacia_log");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "LOG2", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "DARK_OAK",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitInsn(ICONST_4);
			mv.visitInsn(ISUB);
			mv.visitLdcInsn("dark_oak_log");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "MONSTER_EGG", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockSilverfish$EnumType", "CHISELED_STONEBRICK",
					"Lnet/minecraft/block/BlockSilverfish$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockSilverfish$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("chiseled_brick_monster_egg");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "MONSTER_EGG", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockSilverfish$EnumType", "COBBLESTONE",
					"Lnet/minecraft/block/BlockSilverfish$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockSilverfish$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("cobblestone_monster_egg");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "MONSTER_EGG", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockSilverfish$EnumType", "CRACKED_STONEBRICK",
					"Lnet/minecraft/block/BlockSilverfish$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockSilverfish$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("cracked_brick_monster_egg");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "MONSTER_EGG", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockSilverfish$EnumType", "MOSSY_STONEBRICK",
					"Lnet/minecraft/block/BlockSilverfish$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockSilverfish$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("mossy_brick_monster_egg");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "MONSTER_EGG", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockSilverfish$EnumType", "STONE",
					"Lnet/minecraft/block/BlockSilverfish$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockSilverfish$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("stone_monster_egg");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "MONSTER_EGG", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockSilverfish$EnumType", "STONEBRICK",
					"Lnet/minecraft/block/BlockSilverfish$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockSilverfish$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("stone_brick_monster_egg");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "PLANKS", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "ACACIA",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("acacia_planks");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "PLANKS", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "BIRCH",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("birch_planks");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "PLANKS", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "DARK_OAK",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("dark_oak_planks");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "PLANKS", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "JUNGLE",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("jungle_planks");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "PLANKS", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "OAK",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("oak_planks");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "PLANKS", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "SPRUCE",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("spruce_planks");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "PRISMARINE", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPrismarine$EnumType", "BRICKS",
					"Lnet/minecraft/block/BlockPrismarine$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPrismarine$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("prismarine_bricks");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "PRISMARINE", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPrismarine$EnumType", "DARK",
					"Lnet/minecraft/block/BlockPrismarine$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPrismarine$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("dark_prismarine");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "PRISMARINE", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPrismarine$EnumType", "ROUGH",
					"Lnet/minecraft/block/BlockPrismarine$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPrismarine$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("prismarine");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "QUARTZ_BLOCK", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockQuartz$EnumType", "CHISELED",
					"Lnet/minecraft/block/BlockQuartz$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockQuartz$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("chiseled_quartz_block");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "QUARTZ_BLOCK", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockQuartz$EnumType", "DEFAULT",
					"Lnet/minecraft/block/BlockQuartz$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockQuartz$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("quartz_block");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "QUARTZ_BLOCK", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockQuartz$EnumType", "LINES_Y",
					"Lnet/minecraft/block/BlockQuartz$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockQuartz$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("quartz_column");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "RED_FLOWER",
					"Lnet/minecraft/block/BlockFlower;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockFlower$EnumFlowerType", "ALLIUM",
					"Lnet/minecraft/block/BlockFlower$EnumFlowerType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockFlower$EnumFlowerType", "getMeta", "()I",
					false);
			mv.visitLdcInsn("allium");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "RED_FLOWER",
					"Lnet/minecraft/block/BlockFlower;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockFlower$EnumFlowerType", "BLUE_ORCHID",
					"Lnet/minecraft/block/BlockFlower$EnumFlowerType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockFlower$EnumFlowerType", "getMeta", "()I",
					false);
			mv.visitLdcInsn("blue_orchid");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "RED_FLOWER",
					"Lnet/minecraft/block/BlockFlower;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockFlower$EnumFlowerType", "HOUSTONIA",
					"Lnet/minecraft/block/BlockFlower$EnumFlowerType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockFlower$EnumFlowerType", "getMeta", "()I",
					false);
			mv.visitLdcInsn("houstonia");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "RED_FLOWER",
					"Lnet/minecraft/block/BlockFlower;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockFlower$EnumFlowerType", "ORANGE_TULIP",
					"Lnet/minecraft/block/BlockFlower$EnumFlowerType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockFlower$EnumFlowerType", "getMeta", "()I",
					false);
			mv.visitLdcInsn("orange_tulip");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "RED_FLOWER",
					"Lnet/minecraft/block/BlockFlower;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockFlower$EnumFlowerType", "OXEYE_DAISY",
					"Lnet/minecraft/block/BlockFlower$EnumFlowerType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockFlower$EnumFlowerType", "getMeta", "()I",
					false);
			mv.visitLdcInsn("oxeye_daisy");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "RED_FLOWER",
					"Lnet/minecraft/block/BlockFlower;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockFlower$EnumFlowerType", "PINK_TULIP",
					"Lnet/minecraft/block/BlockFlower$EnumFlowerType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockFlower$EnumFlowerType", "getMeta", "()I",
					false);
			mv.visitLdcInsn("pink_tulip");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "RED_FLOWER",
					"Lnet/minecraft/block/BlockFlower;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockFlower$EnumFlowerType", "POPPY",
					"Lnet/minecraft/block/BlockFlower$EnumFlowerType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockFlower$EnumFlowerType", "getMeta", "()I",
					false);
			mv.visitLdcInsn("poppy");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "RED_FLOWER",
					"Lnet/minecraft/block/BlockFlower;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockFlower$EnumFlowerType", "RED_TULIP",
					"Lnet/minecraft/block/BlockFlower$EnumFlowerType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockFlower$EnumFlowerType", "getMeta", "()I",
					false);
			mv.visitLdcInsn("red_tulip");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "RED_FLOWER",
					"Lnet/minecraft/block/BlockFlower;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockFlower$EnumFlowerType", "WHITE_TULIP",
					"Lnet/minecraft/block/BlockFlower$EnumFlowerType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockFlower$EnumFlowerType", "getMeta", "()I",
					false);
			mv.visitLdcInsn("white_tulip");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "SAND", "Lnet/minecraft/block/BlockSand;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockSand$EnumType", "RED_SAND",
					"Lnet/minecraft/block/BlockSand$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockSand$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("red_sand");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "SAND", "Lnet/minecraft/block/BlockSand;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockSand$EnumType", "SAND",
					"Lnet/minecraft/block/BlockSand$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockSand$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("sand");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "SANDSTONE", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockSandStone$EnumType", "CHISELED",
					"Lnet/minecraft/block/BlockSandStone$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockSandStone$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("chiseled_sandstone");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "SANDSTONE", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockSandStone$EnumType", "DEFAULT",
					"Lnet/minecraft/block/BlockSandStone$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockSandStone$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("sandstone");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "SANDSTONE", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockSandStone$EnumType", "SMOOTH",
					"Lnet/minecraft/block/BlockSandStone$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockSandStone$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("smooth_sandstone");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "RED_SANDSTONE", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockRedSandstone$EnumType", "CHISELED",
					"Lnet/minecraft/block/BlockRedSandstone$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockRedSandstone$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("chiseled_red_sandstone");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "RED_SANDSTONE", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockRedSandstone$EnumType", "DEFAULT",
					"Lnet/minecraft/block/BlockRedSandstone$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockRedSandstone$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("red_sandstone");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "RED_SANDSTONE", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockRedSandstone$EnumType", "SMOOTH",
					"Lnet/minecraft/block/BlockRedSandstone$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockRedSandstone$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("smooth_red_sandstone");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "SAPLING", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "ACACIA",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("acacia_sapling");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "SAPLING", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "BIRCH",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("birch_sapling");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "SAPLING", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "DARK_OAK",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("dark_oak_sapling");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "SAPLING", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "JUNGLE",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("jungle_sapling");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "SAPLING", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "OAK",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("oak_sapling");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "SAPLING", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "SPRUCE",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("spruce_sapling");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "SPONGE", "Lnet/minecraft/block/Block;");
			mv.visitInsn(ICONST_0);
			mv.visitLdcInsn("sponge");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "SPONGE", "Lnet/minecraft/block/Block;");
			mv.visitInsn(ICONST_1);
			mv.visitLdcInsn("sponge_wet");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS",
					"Lnet/minecraft/block/BlockStainedGlass;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "BLACK",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("black_stained_glass");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS",
					"Lnet/minecraft/block/BlockStainedGlass;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "BLUE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("blue_stained_glass");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS",
					"Lnet/minecraft/block/BlockStainedGlass;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "BROWN",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("brown_stained_glass");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS",
					"Lnet/minecraft/block/BlockStainedGlass;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "CYAN",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("cyan_stained_glass");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS",
					"Lnet/minecraft/block/BlockStainedGlass;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "GRAY",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("gray_stained_glass");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS",
					"Lnet/minecraft/block/BlockStainedGlass;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "GREEN",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("green_stained_glass");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS",
					"Lnet/minecraft/block/BlockStainedGlass;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "LIGHT_BLUE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("light_blue_stained_glass");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS",
					"Lnet/minecraft/block/BlockStainedGlass;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "LIME",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("lime_stained_glass");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS",
					"Lnet/minecraft/block/BlockStainedGlass;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "MAGENTA",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("magenta_stained_glass");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS",
					"Lnet/minecraft/block/BlockStainedGlass;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "ORANGE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("orange_stained_glass");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS",
					"Lnet/minecraft/block/BlockStainedGlass;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "PINK",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("pink_stained_glass");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS",
					"Lnet/minecraft/block/BlockStainedGlass;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "PURPLE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("purple_stained_glass");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS",
					"Lnet/minecraft/block/BlockStainedGlass;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "RED", "Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("red_stained_glass");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS",
					"Lnet/minecraft/block/BlockStainedGlass;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "SILVER",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("silver_stained_glass");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS",
					"Lnet/minecraft/block/BlockStainedGlass;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "WHITE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("white_stained_glass");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS",
					"Lnet/minecraft/block/BlockStainedGlass;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "YELLOW",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("yellow_stained_glass");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS_PANE",
					"Lnet/minecraft/block/BlockStainedGlassPane;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "BLACK",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("black_stained_glass_pane");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS_PANE",
					"Lnet/minecraft/block/BlockStainedGlassPane;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "BLUE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("blue_stained_glass_pane");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS_PANE",
					"Lnet/minecraft/block/BlockStainedGlassPane;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "BROWN",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("brown_stained_glass_pane");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS_PANE",
					"Lnet/minecraft/block/BlockStainedGlassPane;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "CYAN",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("cyan_stained_glass_pane");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS_PANE",
					"Lnet/minecraft/block/BlockStainedGlassPane;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "GRAY",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("gray_stained_glass_pane");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS_PANE",
					"Lnet/minecraft/block/BlockStainedGlassPane;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "GREEN",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("green_stained_glass_pane");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS_PANE",
					"Lnet/minecraft/block/BlockStainedGlassPane;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "LIGHT_BLUE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("light_blue_stained_glass_pane");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS_PANE",
					"Lnet/minecraft/block/BlockStainedGlassPane;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "LIME",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("lime_stained_glass_pane");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS_PANE",
					"Lnet/minecraft/block/BlockStainedGlassPane;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "MAGENTA",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("magenta_stained_glass_pane");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS_PANE",
					"Lnet/minecraft/block/BlockStainedGlassPane;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "ORANGE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("orange_stained_glass_pane");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS_PANE",
					"Lnet/minecraft/block/BlockStainedGlassPane;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "PINK",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("pink_stained_glass_pane");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS_PANE",
					"Lnet/minecraft/block/BlockStainedGlassPane;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "PURPLE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("purple_stained_glass_pane");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS_PANE",
					"Lnet/minecraft/block/BlockStainedGlassPane;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "RED", "Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("red_stained_glass_pane");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS_PANE",
					"Lnet/minecraft/block/BlockStainedGlassPane;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "SILVER",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("silver_stained_glass_pane");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS_PANE",
					"Lnet/minecraft/block/BlockStainedGlassPane;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "WHITE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("white_stained_glass_pane");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_GLASS_PANE",
					"Lnet/minecraft/block/BlockStainedGlassPane;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "YELLOW",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("yellow_stained_glass_pane");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_HARDENED_CLAY",
					"Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "BLACK",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("black_stained_hardened_clay");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_HARDENED_CLAY",
					"Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "BLUE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("blue_stained_hardened_clay");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_HARDENED_CLAY",
					"Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "BROWN",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("brown_stained_hardened_clay");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_HARDENED_CLAY",
					"Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "CYAN",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("cyan_stained_hardened_clay");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_HARDENED_CLAY",
					"Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "GRAY",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("gray_stained_hardened_clay");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_HARDENED_CLAY",
					"Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "GREEN",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("green_stained_hardened_clay");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_HARDENED_CLAY",
					"Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "LIGHT_BLUE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("light_blue_stained_hardened_clay");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_HARDENED_CLAY",
					"Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "LIME",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("lime_stained_hardened_clay");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_HARDENED_CLAY",
					"Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "MAGENTA",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("magenta_stained_hardened_clay");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_HARDENED_CLAY",
					"Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "ORANGE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("orange_stained_hardened_clay");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_HARDENED_CLAY",
					"Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "PINK",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("pink_stained_hardened_clay");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_HARDENED_CLAY",
					"Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "PURPLE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("purple_stained_hardened_clay");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_HARDENED_CLAY",
					"Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "RED", "Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("red_stained_hardened_clay");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_HARDENED_CLAY",
					"Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "SILVER",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("silver_stained_hardened_clay");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_HARDENED_CLAY",
					"Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "WHITE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("white_stained_hardened_clay");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STAINED_HARDENED_CLAY",
					"Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "YELLOW",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("yellow_stained_hardened_clay");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STONE", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockStone$EnumType", "ANDESITE",
					"Lnet/minecraft/block/BlockStone$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockStone$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("andesite");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STONE", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockStone$EnumType", "ANDESITE_SMOOTH",
					"Lnet/minecraft/block/BlockStone$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockStone$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("andesite_smooth");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STONE", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockStone$EnumType", "DIORITE",
					"Lnet/minecraft/block/BlockStone$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockStone$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("diorite");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STONE", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockStone$EnumType", "DIORITE_SMOOTH",
					"Lnet/minecraft/block/BlockStone$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockStone$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("diorite_smooth");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STONE", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockStone$EnumType", "GRANITE",
					"Lnet/minecraft/block/BlockStone$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockStone$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("granite");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STONE", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockStone$EnumType", "GRANITE_SMOOTH",
					"Lnet/minecraft/block/BlockStone$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockStone$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("granite_smooth");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STONE", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockStone$EnumType", "STONE",
					"Lnet/minecraft/block/BlockStone$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockStone$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("stone");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STONEBRICK", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockStoneBrick$EnumType", "CRACKED",
					"Lnet/minecraft/block/BlockStoneBrick$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockStoneBrick$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("cracked_stonebrick");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STONEBRICK", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockStoneBrick$EnumType", "DEFAULT",
					"Lnet/minecraft/block/BlockStoneBrick$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockStoneBrick$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("stonebrick");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STONEBRICK", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockStoneBrick$EnumType", "CHISELED",
					"Lnet/minecraft/block/BlockStoneBrick$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockStoneBrick$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("chiseled_stonebrick");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STONEBRICK", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockStoneBrick$EnumType", "MOSSY",
					"Lnet/minecraft/block/BlockStoneBrick$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockStoneBrick$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("mossy_stonebrick");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STONE_SLAB", "Lnet/minecraft/block/BlockSlab;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockStoneSlab$EnumType", "BRICK",
					"Lnet/minecraft/block/BlockStoneSlab$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockStoneSlab$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("brick_slab");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STONE_SLAB", "Lnet/minecraft/block/BlockSlab;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockStoneSlab$EnumType", "COBBLESTONE",
					"Lnet/minecraft/block/BlockStoneSlab$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockStoneSlab$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("cobblestone_slab");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STONE_SLAB", "Lnet/minecraft/block/BlockSlab;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockStoneSlab$EnumType", "WOOD",
					"Lnet/minecraft/block/BlockStoneSlab$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockStoneSlab$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("old_wood_slab");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STONE_SLAB", "Lnet/minecraft/block/BlockSlab;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockStoneSlab$EnumType", "NETHERBRICK",
					"Lnet/minecraft/block/BlockStoneSlab$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockStoneSlab$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("nether_brick_slab");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STONE_SLAB", "Lnet/minecraft/block/BlockSlab;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockStoneSlab$EnumType", "QUARTZ",
					"Lnet/minecraft/block/BlockStoneSlab$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockStoneSlab$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("quartz_slab");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STONE_SLAB", "Lnet/minecraft/block/BlockSlab;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockStoneSlab$EnumType", "SAND",
					"Lnet/minecraft/block/BlockStoneSlab$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockStoneSlab$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("sandstone_slab");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STONE_SLAB", "Lnet/minecraft/block/BlockSlab;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockStoneSlab$EnumType", "SMOOTHBRICK",
					"Lnet/minecraft/block/BlockStoneSlab$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockStoneSlab$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("stone_brick_slab");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STONE_SLAB", "Lnet/minecraft/block/BlockSlab;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockStoneSlab$EnumType", "STONE",
					"Lnet/minecraft/block/BlockStoneSlab$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockStoneSlab$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("stone_slab");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STONE_SLAB2", "Lnet/minecraft/block/BlockSlab;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockStoneSlabNew$EnumType", "RED_SANDSTONE",
					"Lnet/minecraft/block/BlockStoneSlabNew$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockStoneSlabNew$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("red_sandstone_slab");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "TALLGRASS",
					"Lnet/minecraft/block/BlockTallGrass;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockTallGrass$EnumType", "DEAD_BUSH",
					"Lnet/minecraft/block/BlockTallGrass$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockTallGrass$EnumType", "getMeta", "()I", false);
			mv.visitLdcInsn("dead_bush");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "TALLGRASS",
					"Lnet/minecraft/block/BlockTallGrass;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockTallGrass$EnumType", "FERN",
					"Lnet/minecraft/block/BlockTallGrass$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockTallGrass$EnumType", "getMeta", "()I", false);
			mv.visitLdcInsn("fern");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "TALLGRASS",
					"Lnet/minecraft/block/BlockTallGrass;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockTallGrass$EnumType", "GRASS",
					"Lnet/minecraft/block/BlockTallGrass$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockTallGrass$EnumType", "getMeta", "()I", false);
			mv.visitLdcInsn("tall_grass");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WOODEN_SLAB", "Lnet/minecraft/block/BlockSlab;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "ACACIA",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("acacia_slab");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WOODEN_SLAB", "Lnet/minecraft/block/BlockSlab;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "BIRCH",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("birch_slab");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WOODEN_SLAB", "Lnet/minecraft/block/BlockSlab;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "DARK_OAK",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("dark_oak_slab");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WOODEN_SLAB", "Lnet/minecraft/block/BlockSlab;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "JUNGLE",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("jungle_slab");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WOODEN_SLAB", "Lnet/minecraft/block/BlockSlab;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "OAK",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("oak_slab");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WOODEN_SLAB", "Lnet/minecraft/block/BlockSlab;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockPlanks$EnumType", "SPRUCE",
					"Lnet/minecraft/block/BlockPlanks$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockPlanks$EnumType", "getMetadata", "()I", false);
			mv.visitLdcInsn("spruce_slab");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WOOL", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "BLACK",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("black_wool");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WOOL", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "BLUE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("blue_wool");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WOOL", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "BROWN",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("brown_wool");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WOOL", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "CYAN",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("cyan_wool");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WOOL", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "GRAY",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("gray_wool");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WOOL", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "GREEN",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("green_wool");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WOOL", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "LIGHT_BLUE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("light_blue_wool");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WOOL", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "LIME",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("lime_wool");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WOOL", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "MAGENTA",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("magenta_wool");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WOOL", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "ORANGE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("orange_wool");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WOOL", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "PINK",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("pink_wool");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WOOL", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "PURPLE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("purple_wool");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WOOL", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "RED", "Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("red_wool");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WOOL", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "SILVER",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("silver_wool");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WOOL", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "WHITE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("white_wool");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WOOL", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "YELLOW",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getMetadata", "()I", false);
			mv.visitLdcInsn("yellow_wool");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "FARMLAND", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("farmland");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "ACACIA_STAIRS", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("acacia_stairs");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "ACTIVATOR_RAIL", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("activator_rail");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "BEACON", "Lnet/minecraft/block/BlockBeacon;");
			mv.visitLdcInsn("beacon");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "BEDROCK", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("bedrock");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "BIRCH_STAIRS", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("birch_stairs");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "BOOKSHELF", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("bookshelf");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "BRICK_BLOCK", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("brick_block");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "BRICK_BLOCK", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("brick_block");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "BRICK_STAIRS", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("brick_stairs");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "BROWN_MUSHROOM",
					"Lnet/minecraft/block/BlockBush;");
			mv.visitLdcInsn("brown_mushroom");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "CACTUS", "Lnet/minecraft/block/BlockCactus;");
			mv.visitLdcInsn("cactus");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "CLAY", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("clay");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "COAL_BLOCK", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("coal_block");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "COAL_ORE", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("coal_ore");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "COBBLESTONE", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("cobblestone");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "CRAFTING_TABLE", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("crafting_table");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "DARK_OAK_STAIRS", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("dark_oak_stairs");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "DAYLIGHT_DETECTOR",
					"Lnet/minecraft/block/BlockDaylightDetector;");
			mv.visitLdcInsn("daylight_detector");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "DEADBUSH",
					"Lnet/minecraft/block/BlockDeadBush;");
			mv.visitLdcInsn("dead_bush");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "DETECTOR_RAIL", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("detector_rail");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "DIAMOND_BLOCK", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("diamond_block");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "DIAMOND_ORE", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("diamond_ore");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "DISPENSER", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("dispenser");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "DROPPER", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("dropper");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "EMERALD_BLOCK", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("emerald_block");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "EMERALD_ORE", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("emerald_ore");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "ENCHANTING_TABLE",
					"Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("enchanting_table");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "END_PORTAL_FRAME",
					"Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("end_portal_frame");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "END_STONE", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("end_stone");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "OAK_FENCE", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("oak_fence");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "SPRUCE_FENCE", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("spruce_fence");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "BIRCH_FENCE", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("birch_fence");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "JUNGLE_FENCE", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("jungle_fence");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "DARK_OAK_FENCE", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("dark_oak_fence");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "ACACIA_FENCE", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("acacia_fence");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "OAK_FENCE_GATE", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("oak_fence_gate");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "SPRUCE_FENCE_GATE",
					"Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("spruce_fence_gate");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "BIRCH_FENCE_GATE",
					"Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("birch_fence_gate");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "JUNGLE_FENCE_GATE",
					"Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("jungle_fence_gate");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "DARK_OAK_FENCE_GATE",
					"Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("dark_oak_fence_gate");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "ACACIA_FENCE_GATE",
					"Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("acacia_fence_gate");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "FURNACE", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("furnace");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "GLASS", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("glass");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "GLASS_PANE", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("glass_pane");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "GLOWSTONE", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("glowstone");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "GOLDEN_RAIL", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("golden_rail");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "GOLD_BLOCK", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("gold_block");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "GOLD_ORE", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("gold_ore");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "GRASS", "Lnet/minecraft/block/BlockGrass;");
			mv.visitLdcInsn("grass");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "GRASS_PATH", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("grass_path");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "GRAVEL", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("gravel");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "HARDENED_CLAY", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("hardened_clay");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "HAY_BLOCK", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("hay_block");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "HEAVY_WEIGHTED_PRESSURE_PLATE",
					"Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("heavy_weighted_pressure_plate");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "HOPPER", "Lnet/minecraft/block/BlockHopper;");
			mv.visitLdcInsn("hopper");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "ICE", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("ice");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "IRON_BARS", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("iron_bars");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "IRON_BLOCK", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("iron_block");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "IRON_ORE", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("iron_ore");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "IRON_TRAPDOOR", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("iron_trapdoor");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "JUKEBOX", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("jukebox");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "JUNGLE_STAIRS", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("jungle_stairs");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "LADDER", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("ladder");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "LAPIS_BLOCK", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("lapis_block");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "LAPIS_ORE", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("lapis_ore");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "LEVER", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("lever");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "LIGHT_WEIGHTED_PRESSURE_PLATE",
					"Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("light_weighted_pressure_plate");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "LIT_PUMPKIN", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("lit_pumpkin");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "MELON_BLOCK", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("melon_block");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "MOSSY_COBBLESTONE",
					"Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("mossy_cobblestone");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "MYCELIUM",
					"Lnet/minecraft/block/BlockMycelium;");
			mv.visitLdcInsn("mycelium");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "NETHERRACK", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("netherrack");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "NETHER_BRICK", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("nether_brick");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "NETHER_BRICK_FENCE",
					"Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("nether_brick_fence");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "NETHER_BRICK_STAIRS",
					"Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("nether_brick_stairs");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "NOTEBLOCK", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("noteblock");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "OAK_STAIRS", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("oak_stairs");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "OBSIDIAN", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("obsidian");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "PACKED_ICE", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("packed_ice");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "PISTON",
					"Lnet/minecraft/block/BlockPistonBase;");
			mv.visitLdcInsn("piston");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "PUMPKIN", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("pumpkin");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "QUARTZ_ORE", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("quartz_ore");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "QUARTZ_STAIRS", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("quartz_stairs");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "RAIL", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("rail");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "REDSTONE_BLOCK", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("redstone_block");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "REDSTONE_LAMP", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("redstone_lamp");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "REDSTONE_ORE", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("redstone_ore");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "REDSTONE_TORCH", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("redstone_torch");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "RED_MUSHROOM",
					"Lnet/minecraft/block/BlockBush;");
			mv.visitLdcInsn("red_mushroom");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "SANDSTONE_STAIRS",
					"Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("sandstone_stairs");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "RED_SANDSTONE_STAIRS",
					"Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("red_sandstone_stairs");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "SEA_LANTERN", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("sea_lantern");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "SLIME_BLOCK", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("slime");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "SNOW", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("snow");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "SNOW_LAYER", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("snow_layer");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "SOUL_SAND", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("soul_sand");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "SPRUCE_STAIRS", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("spruce_stairs");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STICKY_PISTON",
					"Lnet/minecraft/block/BlockPistonBase;");
			mv.visitLdcInsn("sticky_piston");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STONE_BRICK_STAIRS",
					"Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("stone_brick_stairs");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STONE_BUTTON", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("stone_button");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STONE_PRESSURE_PLATE",
					"Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("stone_pressure_plate");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STONE_STAIRS", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("stone_stairs");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "TNT", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("tnt");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "TORCH", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("torch");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "TRAPDOOR", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("trapdoor");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "TRIPWIRE_HOOK",
					"Lnet/minecraft/block/BlockTripWireHook;");
			mv.visitLdcInsn("tripwire_hook");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "VINE", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("vine");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WATERLILY", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("waterlily");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WEB", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("web");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WOODEN_BUTTON", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("wooden_button");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "WOODEN_PRESSURE_PLATE",
					"Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("wooden_pressure_plate");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "YELLOW_FLOWER",
					"Lnet/minecraft/block/BlockFlower;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockFlower$EnumFlowerType", "DANDELION",
					"Lnet/minecraft/block/BlockFlower$EnumFlowerType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockFlower$EnumFlowerType", "getMeta", "()I",
					false);
			mv.visitLdcInsn("dandelion");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "END_ROD", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("end_rod");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "CHORUS_PLANT", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("chorus_plant");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "CHORUS_FLOWER", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("chorus_flower");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "PURPUR_BLOCK", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("purpur_block");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "PURPUR_PILLAR", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("purpur_pillar");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "PURPUR_STAIRS", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("purpur_stairs");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "PURPUR_SLAB", "Lnet/minecraft/block/BlockSlab;");
			mv.visitLdcInsn("purpur_slab");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "PURPUR_DOUBLE_SLAB",
					"Lnet/minecraft/block/BlockSlab;");
			mv.visitLdcInsn("purpur_double_slab");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "END_BRICKS", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("end_bricks");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "field_189877_df", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("magma");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "field_189878_dg", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("nether_wart_block");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "field_189879_dh", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("red_nether_brick");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "field_189880_di", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("bone_block");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "field_189881_dj", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("structure_void");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "CHEST", "Lnet/minecraft/block/BlockChest;");
			mv.visitLdcInsn("chest");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "TRAPPED_CHEST", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("trapped_chest");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "ENDER_CHEST", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("ender_chest");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "IRON_SHOVEL", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("iron_shovel");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "IRON_PICKAXE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("iron_pickaxe");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "IRON_AXE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("iron_axe");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "FLINT_AND_STEEL", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("flint_and_steel");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "APPLE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("apple");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "BOW", "Lnet/minecraft/item/ItemBow;");
			mv.visitLdcInsn("bow");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "ARROW", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("arrow");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "SPECTRAL_ARROW", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("spectral_arrow");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "TIPPED_ARROW", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("tipped_arrow");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "COAL", "Lnet/minecraft/item/Item;");
			mv.visitInsn(ICONST_0);
			mv.visitLdcInsn("coal");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "COAL", "Lnet/minecraft/item/Item;");
			mv.visitInsn(ICONST_1);
			mv.visitLdcInsn("charcoal");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DIAMOND", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("diamond");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "IRON_INGOT", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("iron_ingot");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "GOLD_INGOT", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("gold_ingot");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "IRON_SWORD", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("iron_sword");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "WOODEN_SWORD", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("wooden_sword");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "WOODEN_SHOVEL", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("wooden_shovel");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "WOODEN_PICKAXE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("wooden_pickaxe");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "WOODEN_AXE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("wooden_axe");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "STONE_SWORD", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("stone_sword");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "STONE_SHOVEL", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("stone_shovel");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "STONE_PICKAXE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("stone_pickaxe");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "STONE_AXE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("stone_axe");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DIAMOND_SWORD", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("diamond_sword");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DIAMOND_SHOVEL", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("diamond_shovel");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DIAMOND_PICKAXE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("diamond_pickaxe");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DIAMOND_AXE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("diamond_axe");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "STICK", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("stick");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "BOWL", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("bowl");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "MUSHROOM_STEW", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("mushroom_stew");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "GOLDEN_SWORD", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("golden_sword");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "GOLDEN_SHOVEL", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("golden_shovel");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "GOLDEN_PICKAXE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("golden_pickaxe");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "GOLDEN_AXE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("golden_axe");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "STRING", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("string");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "FEATHER", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("feather");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "GUNPOWDER", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("gunpowder");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "WOODEN_HOE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("wooden_hoe");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "STONE_HOE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("stone_hoe");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "IRON_HOE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("iron_hoe");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DIAMOND_HOE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("diamond_hoe");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "GOLDEN_HOE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("golden_hoe");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "WHEAT_SEEDS", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("wheat_seeds");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "WHEAT", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("wheat");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "BREAD", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("bread");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "LEATHER_HELMET",
					"Lnet/minecraft/item/ItemArmor;");
			mv.visitLdcInsn("leather_helmet");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "LEATHER_CHESTPLATE",
					"Lnet/minecraft/item/ItemArmor;");
			mv.visitLdcInsn("leather_chestplate");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "LEATHER_LEGGINGS",
					"Lnet/minecraft/item/ItemArmor;");
			mv.visitLdcInsn("leather_leggings");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "LEATHER_BOOTS", "Lnet/minecraft/item/ItemArmor;");
			mv.visitLdcInsn("leather_boots");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "CHAINMAIL_HELMET",
					"Lnet/minecraft/item/ItemArmor;");
			mv.visitLdcInsn("chainmail_helmet");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "CHAINMAIL_CHESTPLATE",
					"Lnet/minecraft/item/ItemArmor;");
			mv.visitLdcInsn("chainmail_chestplate");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "CHAINMAIL_LEGGINGS",
					"Lnet/minecraft/item/ItemArmor;");
			mv.visitLdcInsn("chainmail_leggings");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "CHAINMAIL_BOOTS",
					"Lnet/minecraft/item/ItemArmor;");
			mv.visitLdcInsn("chainmail_boots");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "IRON_HELMET", "Lnet/minecraft/item/ItemArmor;");
			mv.visitLdcInsn("iron_helmet");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "IRON_CHESTPLATE",
					"Lnet/minecraft/item/ItemArmor;");
			mv.visitLdcInsn("iron_chestplate");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "IRON_LEGGINGS", "Lnet/minecraft/item/ItemArmor;");
			mv.visitLdcInsn("iron_leggings");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "IRON_BOOTS", "Lnet/minecraft/item/ItemArmor;");
			mv.visitLdcInsn("iron_boots");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DIAMOND_HELMET",
					"Lnet/minecraft/item/ItemArmor;");
			mv.visitLdcInsn("diamond_helmet");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DIAMOND_CHESTPLATE",
					"Lnet/minecraft/item/ItemArmor;");
			mv.visitLdcInsn("diamond_chestplate");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DIAMOND_LEGGINGS",
					"Lnet/minecraft/item/ItemArmor;");
			mv.visitLdcInsn("diamond_leggings");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DIAMOND_BOOTS", "Lnet/minecraft/item/ItemArmor;");
			mv.visitLdcInsn("diamond_boots");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "GOLDEN_HELMET", "Lnet/minecraft/item/ItemArmor;");
			mv.visitLdcInsn("golden_helmet");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "GOLDEN_CHESTPLATE",
					"Lnet/minecraft/item/ItemArmor;");
			mv.visitLdcInsn("golden_chestplate");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "GOLDEN_LEGGINGS",
					"Lnet/minecraft/item/ItemArmor;");
			mv.visitLdcInsn("golden_leggings");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "GOLDEN_BOOTS", "Lnet/minecraft/item/ItemArmor;");
			mv.visitLdcInsn("golden_boots");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "FLINT", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("flint");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "PORKCHOP", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("porkchop");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "COOKED_PORKCHOP", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("cooked_porkchop");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "PAINTING", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("painting");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "GOLDEN_APPLE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("golden_apple");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "GOLDEN_APPLE", "Lnet/minecraft/item/Item;");
			mv.visitInsn(ICONST_1);
			mv.visitLdcInsn("golden_apple");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "SIGN", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("sign");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "OAK_DOOR", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("oak_door");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "SPRUCE_DOOR", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("spruce_door");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "BIRCH_DOOR", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("birch_door");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "JUNGLE_DOOR", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("jungle_door");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "ACACIA_DOOR", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("acacia_door");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DARK_OAK_DOOR", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("dark_oak_door");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "BUCKET", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("bucket");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "WATER_BUCKET", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("water_bucket");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "LAVA_BUCKET", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("lava_bucket");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "MINECART", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("minecart");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "SADDLE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("saddle");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "IRON_DOOR", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("iron_door");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "REDSTONE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("redstone");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "SNOWBALL", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("snowball");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "BOAT", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("oak_boat");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "SPRUCE_BOAT", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("spruce_boat");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "BIRCH_BOAT", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("birch_boat");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "JUNGLE_BOAT", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("jungle_boat");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "ACACIA_BOAT", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("acacia_boat");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DARK_OAK_BOAT", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("dark_oak_boat");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "LEATHER", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("leather");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "MILK_BUCKET", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("milk_bucket");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "BRICK", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("brick");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "CLAY_BALL", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("clay_ball");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "REEDS", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("reeds");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "PAPER", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("paper");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "BOOK", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("book");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "SLIME_BALL", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("slime_ball");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "CHEST_MINECART", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("chest_minecart");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "FURNACE_MINECART", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("furnace_minecart");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "EGG", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("egg");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "COMPASS", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("compass");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "FISHING_ROD",
					"Lnet/minecraft/item/ItemFishingRod;");
			mv.visitLdcInsn("fishing_rod");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "CLOCK", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("clock");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "GLOWSTONE_DUST", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("glowstone_dust");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "FISH", "Lnet/minecraft/item/Item;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/ItemFishFood$FishType", "COD",
					"Lnet/minecraft/item/ItemFishFood$FishType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemFishFood$FishType", "getMetadata", "()I", false);
			mv.visitLdcInsn("cod");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "FISH", "Lnet/minecraft/item/Item;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/ItemFishFood$FishType", "SALMON",
					"Lnet/minecraft/item/ItemFishFood$FishType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemFishFood$FishType", "getMetadata", "()I", false);
			mv.visitLdcInsn("salmon");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "FISH", "Lnet/minecraft/item/Item;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/ItemFishFood$FishType", "CLOWNFISH",
					"Lnet/minecraft/item/ItemFishFood$FishType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemFishFood$FishType", "getMetadata", "()I", false);
			mv.visitLdcInsn("clownfish");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "FISH", "Lnet/minecraft/item/Item;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/ItemFishFood$FishType", "PUFFERFISH",
					"Lnet/minecraft/item/ItemFishFood$FishType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemFishFood$FishType", "getMetadata", "()I", false);
			mv.visitLdcInsn("pufferfish");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "COOKED_FISH", "Lnet/minecraft/item/Item;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/ItemFishFood$FishType", "COD",
					"Lnet/minecraft/item/ItemFishFood$FishType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemFishFood$FishType", "getMetadata", "()I", false);
			mv.visitLdcInsn("cooked_cod");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "COOKED_FISH", "Lnet/minecraft/item/Item;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/ItemFishFood$FishType", "SALMON",
					"Lnet/minecraft/item/ItemFishFood$FishType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemFishFood$FishType", "getMetadata", "()I", false);
			mv.visitLdcInsn("cooked_salmon");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DYE", "Lnet/minecraft/item/Item;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "BLACK",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getDyeDamage", "()I", false);
			mv.visitLdcInsn("dye_black");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DYE", "Lnet/minecraft/item/Item;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "RED", "Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getDyeDamage", "()I", false);
			mv.visitLdcInsn("dye_red");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DYE", "Lnet/minecraft/item/Item;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "GREEN",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getDyeDamage", "()I", false);
			mv.visitLdcInsn("dye_green");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DYE", "Lnet/minecraft/item/Item;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "BROWN",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getDyeDamage", "()I", false);
			mv.visitLdcInsn("dye_brown");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DYE", "Lnet/minecraft/item/Item;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "BLUE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getDyeDamage", "()I", false);
			mv.visitLdcInsn("dye_blue");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DYE", "Lnet/minecraft/item/Item;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "PURPLE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getDyeDamage", "()I", false);
			mv.visitLdcInsn("dye_purple");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DYE", "Lnet/minecraft/item/Item;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "CYAN",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getDyeDamage", "()I", false);
			mv.visitLdcInsn("dye_cyan");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DYE", "Lnet/minecraft/item/Item;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "SILVER",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getDyeDamage", "()I", false);
			mv.visitLdcInsn("dye_silver");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DYE", "Lnet/minecraft/item/Item;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "GRAY",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getDyeDamage", "()I", false);
			mv.visitLdcInsn("dye_gray");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DYE", "Lnet/minecraft/item/Item;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "PINK",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getDyeDamage", "()I", false);
			mv.visitLdcInsn("dye_pink");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DYE", "Lnet/minecraft/item/Item;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "LIME",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getDyeDamage", "()I", false);
			mv.visitLdcInsn("dye_lime");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DYE", "Lnet/minecraft/item/Item;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "YELLOW",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getDyeDamage", "()I", false);
			mv.visitLdcInsn("dye_yellow");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DYE", "Lnet/minecraft/item/Item;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "LIGHT_BLUE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getDyeDamage", "()I", false);
			mv.visitLdcInsn("dye_light_blue");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DYE", "Lnet/minecraft/item/Item;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "MAGENTA",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getDyeDamage", "()I", false);
			mv.visitLdcInsn("dye_magenta");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DYE", "Lnet/minecraft/item/Item;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "ORANGE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getDyeDamage", "()I", false);
			mv.visitLdcInsn("dye_orange");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DYE", "Lnet/minecraft/item/Item;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumDyeColor", "WHITE",
					"Lnet/minecraft/item/EnumDyeColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/EnumDyeColor", "getDyeDamage", "()I", false);
			mv.visitLdcInsn("dye_white");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "BONE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("bone");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "SUGAR", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("sugar");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "CAKE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("cake");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "BED", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("bed");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "REPEATER", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("repeater");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "COOKIE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("cookie");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "SHEARS", "Lnet/minecraft/item/ItemShears;");
			mv.visitLdcInsn("shears");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "MELON", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("melon");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "PUMPKIN_SEEDS", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("pumpkin_seeds");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "MELON_SEEDS", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("melon_seeds");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "BEEF", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("beef");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "COOKED_BEEF", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("cooked_beef");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "CHICKEN", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("chicken");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "COOKED_CHICKEN", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("cooked_chicken");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "RABBIT", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("rabbit");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "COOKED_RABBIT", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("cooked_rabbit");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "MUTTON", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("mutton");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "COOKED_MUTTON", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("cooked_mutton");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "RABBIT_FOOT", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("rabbit_foot");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "RABBIT_HIDE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("rabbit_hide");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "RABBIT_STEW", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("rabbit_stew");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "ROTTEN_FLESH", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("rotten_flesh");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "ENDER_PEARL", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("ender_pearl");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "BLAZE_ROD", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("blaze_rod");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "GHAST_TEAR", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("ghast_tear");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "GOLD_NUGGET", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("gold_nugget");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "NETHER_WART", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("nether_wart");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "BEETROOT", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("beetroot");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "BEETROOT_SEEDS", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("beetroot_seeds");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "BEETROOT_SOUP", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("beetroot_soup");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "POTIONITEM", "Lnet/minecraft/item/ItemPotion;");
			mv.visitLdcInsn("bottle_drinkable");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "SPLASH_POTION",
					"Lnet/minecraft/item/ItemPotion;");
			mv.visitLdcInsn("bottle_splash");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "LINGERING_POTION",
					"Lnet/minecraft/item/ItemPotion;");
			mv.visitLdcInsn("bottle_lingering");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "GLASS_BOTTLE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("glass_bottle");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DRAGON_BREATH", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("dragon_breath");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "SPIDER_EYE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("spider_eye");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "FERMENTED_SPIDER_EYE",
					"Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("fermented_spider_eye");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "BLAZE_POWDER", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("blaze_powder");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "MAGMA_CREAM", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("magma_cream");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "BREWING_STAND", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("brewing_stand");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "CAULDRON", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("cauldron");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "ENDER_EYE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("ender_eye");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "SPECKLED_MELON", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("speckled_melon");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "itemModelMesher",
					"Lnet/minecraft/client/renderer/ItemModelMesher;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "SPAWN_EGG", "Lnet/minecraft/item/Item;");
			mv.visitTypeInsn(NEW, "net/minecraft/client/renderer/RenderItem$5");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem$5", "<init>",
					"(Lnet/minecraft/client/renderer/RenderItem;)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/ItemModelMesher", "register",
					"(Lnet/minecraft/item/Item;Lnet/minecraft/client/renderer/ItemMeshDefinition;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "EXPERIENCE_BOTTLE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("experience_bottle");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "FIRE_CHARGE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("fire_charge");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "WRITABLE_BOOK", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("writable_book");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "EMERALD", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("emerald");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "ITEM_FRAME", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("item_frame");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "FLOWER_POT", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("flower_pot");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "CARROT", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("carrot");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "POTATO", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("potato");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "BAKED_POTATO", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("baked_potato");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "POISONOUS_POTATO", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("poisonous_potato");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "MAP", "Lnet/minecraft/item/ItemEmptyMap;");
			mv.visitLdcInsn("map");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "GOLDEN_CARROT", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("golden_carrot");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "SKULL", "Lnet/minecraft/item/Item;");
			mv.visitInsn(ICONST_0);
			mv.visitLdcInsn("skull_skeleton");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "SKULL", "Lnet/minecraft/item/Item;");
			mv.visitInsn(ICONST_1);
			mv.visitLdcInsn("skull_wither");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "SKULL", "Lnet/minecraft/item/Item;");
			mv.visitInsn(ICONST_2);
			mv.visitLdcInsn("skull_zombie");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "SKULL", "Lnet/minecraft/item/Item;");
			mv.visitInsn(ICONST_3);
			mv.visitLdcInsn("skull_char");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "SKULL", "Lnet/minecraft/item/Item;");
			mv.visitInsn(ICONST_4);
			mv.visitLdcInsn("skull_creeper");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "SKULL", "Lnet/minecraft/item/Item;");
			mv.visitInsn(ICONST_5);
			mv.visitLdcInsn("skull_dragon");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "CARROT_ON_A_STICK", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("carrot_on_a_stick");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "NETHER_STAR", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("nether_star");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "END_CRYSTAL", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("end_crystal");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "PUMPKIN_PIE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("pumpkin_pie");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "FIREWORK_CHARGE", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("firework_charge");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "COMPARATOR", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("comparator");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "NETHERBRICK", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("netherbrick");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "QUARTZ", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("quartz");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "TNT_MINECART", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("tnt_minecart");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "HOPPER_MINECART", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("hopper_minecart");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "ARMOR_STAND",
					"Lnet/minecraft/item/ItemArmorStand;");
			mv.visitLdcInsn("armor_stand");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "IRON_HORSE_ARMOR", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("iron_horse_armor");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "GOLDEN_HORSE_ARMOR", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("golden_horse_armor");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "DIAMOND_HORSE_ARMOR",
					"Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("diamond_horse_armor");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "LEAD", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("lead");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "NAME_TAG", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("name_tag");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "itemModelMesher",
					"Lnet/minecraft/client/renderer/ItemModelMesher;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "BANNER", "Lnet/minecraft/item/Item;");
			mv.visitTypeInsn(NEW, "net/minecraft/client/renderer/RenderItem$6");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem$6", "<init>",
					"(Lnet/minecraft/client/renderer/RenderItem;)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/ItemModelMesher", "register",
					"(Lnet/minecraft/item/Item;Lnet/minecraft/client/renderer/ItemMeshDefinition;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "itemModelMesher",
					"Lnet/minecraft/client/renderer/ItemModelMesher;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "SHIELD", "Lnet/minecraft/item/Item;");
			mv.visitTypeInsn(NEW, "net/minecraft/client/renderer/RenderItem$7");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem$7", "<init>",
					"(Lnet/minecraft/client/renderer/RenderItem;)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/ItemModelMesher", "register",
					"(Lnet/minecraft/item/Item;Lnet/minecraft/client/renderer/ItemMeshDefinition;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "ELYTRA", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("elytra");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "CHORUS_FRUIT", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("chorus_fruit");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "CHORUS_FRUIT_POPPED",
					"Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("chorus_fruit_popped");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "RECORD_13", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("record_13");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "RECORD_CAT", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("record_cat");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "RECORD_BLOCKS", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("record_blocks");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "RECORD_CHIRP", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("record_chirp");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "RECORD_FAR", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("record_far");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "RECORD_MALL", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("record_mall");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "RECORD_MELLOHI", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("record_mellohi");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "RECORD_STAL", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("record_stal");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "RECORD_STRAD", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("record_strad");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "RECORD_WARD", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("record_ward");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "RECORD_11", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("record_11");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "RECORD_WAIT", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("record_wait");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "PRISMARINE_SHARD", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("prismarine_shard");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "PRISMARINE_CRYSTALS",
					"Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("prismarine_crystals");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "itemModelMesher",
					"Lnet/minecraft/client/renderer/ItemModelMesher;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "ENCHANTED_BOOK",
					"Lnet/minecraft/item/ItemEnchantedBook;");
			mv.visitTypeInsn(NEW, "net/minecraft/client/renderer/RenderItem$8");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem$8", "<init>",
					"(Lnet/minecraft/client/renderer/RenderItem;)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/ItemModelMesher", "register",
					"(Lnet/minecraft/item/Item;Lnet/minecraft/client/renderer/ItemMeshDefinition;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "itemModelMesher",
					"Lnet/minecraft/client/renderer/ItemModelMesher;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "FILLED_MAP", "Lnet/minecraft/item/ItemMap;");
			mv.visitTypeInsn(NEW, "net/minecraft/client/renderer/RenderItem$9");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem$9", "<init>",
					"(Lnet/minecraft/client/renderer/RenderItem;)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/ItemModelMesher", "register",
					"(Lnet/minecraft/item/Item;Lnet/minecraft/client/renderer/ItemMeshDefinition;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "COMMAND_BLOCK", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("command_block");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "FIREWORKS", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("fireworks");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "COMMAND_BLOCK_MINECART",
					"Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("command_block_minecart");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "BARRIER", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("barrier");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "MOB_SPAWNER", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("mob_spawner");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "WRITTEN_BOOK", "Lnet/minecraft/item/Item;");
			mv.visitLdcInsn("written_book");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerItem",
					"(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "BROWN_MUSHROOM_BLOCK",
					"Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockHugeMushroom$EnumType", "ALL_INSIDE",
					"Lnet/minecraft/block/BlockHugeMushroom$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockHugeMushroom$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("brown_mushroom_block");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "RED_MUSHROOM_BLOCK",
					"Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockHugeMushroom$EnumType", "ALL_INSIDE",
					"Lnet/minecraft/block/BlockHugeMushroom$EnumType;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockHugeMushroom$EnumType", "getMetadata", "()I",
					false);
			mv.visitLdcInsn("red_mushroom_block");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "DRAGON_EGG", "Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("dragon_egg");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "REPEATING_COMMAND_BLOCK",
					"Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("repeating_command_block");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "CHAIN_COMMAND_BLOCK",
					"Lnet/minecraft/block/Block;");
			mv.visitLdcInsn("chain_command_block");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STRUCTURE_BLOCK", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/tileentity/TileEntityStructure$Mode", "SAVE",
					"Lnet/minecraft/tileentity/TileEntityStructure$Mode;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/tileentity/TileEntityStructure$Mode", "getModeId", "()I",
					false);
			mv.visitLdcInsn("structure_block");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STRUCTURE_BLOCK", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/tileentity/TileEntityStructure$Mode", "LOAD",
					"Lnet/minecraft/tileentity/TileEntityStructure$Mode;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/tileentity/TileEntityStructure$Mode", "getModeId", "()I",
					false);
			mv.visitLdcInsn("structure_block");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STRUCTURE_BLOCK", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/tileentity/TileEntityStructure$Mode", "CORNER",
					"Lnet/minecraft/tileentity/TileEntityStructure$Mode;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/tileentity/TileEntityStructure$Mode", "getModeId", "()I",
					false);
			mv.visitLdcInsn("structure_block");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "STRUCTURE_BLOCK", "Lnet/minecraft/block/Block;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/tileentity/TileEntityStructure$Mode", "DATA",
					"Lnet/minecraft/tileentity/TileEntityStructure$Mode;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/tileentity/TileEntityStructure$Mode", "getModeId", "()I",
					false);
			mv.visitLdcInsn("structure_block");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderItem", "registerBlock",
					"(Lnet/minecraft/block/Block;ILjava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "itemModelMesher",
					"Lnet/minecraft/client/renderer/ItemModelMesher;");
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/client/model/ModelLoader", "onRegisterItems",
					"(Lnet/minecraft/client/renderer/ItemModelMesher;)V", false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(5, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "onResourceManagerReload",
					"(Lnet/minecraft/client/resources/IResourceManager;)V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/RenderItem", "itemModelMesher",
					"Lnet/minecraft/client/renderer/ItemModelMesher;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/ItemModelMesher", "rebuildCache", "()V",
					false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(1, 2);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
			mv.visitCode();
			mv.visitTypeInsn(NEW, "net/minecraft/util/ResourceLocation");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("textures/misc/enchanted_item_glint.png");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/util/ResourceLocation", "<init>", "(Ljava/lang/String;)V",
					false);
			mv.visitFieldInsn(PUTSTATIC, "net/minecraft/client/renderer/RenderItem", "RES_ITEM_GLINT",
					"Lnet/minecraft/util/ResourceLocation;");
			mv.visitInsn(RETURN);
			mv.visitMaxs(3, 0);
			mv.visitEnd();
		}
		cw.visitEnd();

		return cw.toByteArray();
	}
}