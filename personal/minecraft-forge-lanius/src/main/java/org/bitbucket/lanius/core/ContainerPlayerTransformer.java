package org.bitbucket.lanius.core;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.IF_ACMPNE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.POP;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class ContainerPlayerTransformer extends BasicTransformer {
	private class Visitor extends ClassVisitor {

		public Visitor(ClassVisitor cv) {
			super(ASM5, cv);
			// TODO Auto-generated constructor stub
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor delegateVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			if (name.equals(LaniusLoadingPlugin.nameRegistry.get("isItemValid"))
					&& desc.equals("(Lnet/minecraft/item/ItemStack;)Z")) {
				return new MethodVisitor(ASM5, delegateVisitor) {
					@Override
					public void visitCode() {
						super.visitCode();
						mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/hook/HookManager", "itemValidManager",
								"Lorg/bitbucket/lanius/hook/HookManager;");
						mv.visitTypeInsn(NEW, "org/bitbucket/lanius/hook/impl/ItemValidData");
						mv.visitInsn(DUP);
						mv.visitVarInsn(ALOAD, 0);
						mv.visitVarInsn(ALOAD, 1);
						mv.visitInsn(DUP);
						mv.visitVarInsn(ALOAD, 0);
						mv.visitFieldInsn(GETFIELD, "net/minecraft/inventory/ContainerPlayer$1",
								LaniusLoadingPlugin.nameRegistry.get("val$entityequipmentslot"),
								"Lnet/minecraft/inventory/EntityEquipmentSlot;");
						mv.visitVarInsn(ALOAD, 1);
						mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/entity/EntityLiving",
								LaniusLoadingPlugin.nameRegistry.get("getSlotForItemStack"),
								"(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/inventory/EntityEquipmentSlot;", false);
						Label cmpneLabel = new Label();
						mv.visitJumpInsn(IF_ACMPNE, cmpneLabel);
						mv.visitInsn(POP);
						mv.visitInsn(ICONST_1);
						Label gotoLabel = new Label();
						mv.visitJumpInsn(GOTO, gotoLabel);
						mv.visitLabel(cmpneLabel);
						mv.visitInsn(POP);
						mv.visitInsn(ICONST_0);
						mv.visitLabel(gotoLabel);
						mv.visitMethodInsn(INVOKESPECIAL, "org/bitbucket/lanius/hook/impl/ItemValidData", "<init>",
								"(Lnet/minecraft/inventory/Slot;Lnet/minecraft/item/ItemStack;Z)V", false);
						mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/util/Phase", "START",
								"Lorg/bitbucket/lanius/util/Phase;");
						mv.visitMethodInsn(INVOKEVIRTUAL, "org/bitbucket/lanius/hook/HookManager", "execute",
								"(Lorg/bitbucket/lanius/hook/HookData;Lorg/bitbucket/lanius/util/Phase;)Ljava/lang/Object;",
								false);
						mv.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
						mv.visitInsn(IRETURN);
					}
				};
			}
			return delegateVisitor;
		}

	}

	public ContainerPlayerTransformer() {
		super("net.minecraft.inventory.ContainerPlayer$1");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ClassVisitor classVisitor(ClassWriter classWriter) {
		// TODO Auto-generated method stub
		return new Visitor(classWriter);
	}

}
