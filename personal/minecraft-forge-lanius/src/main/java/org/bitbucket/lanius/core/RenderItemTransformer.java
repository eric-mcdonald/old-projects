package org.bitbucket.lanius.core;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public final class RenderItemTransformer extends BasicTransformer {

	private final class RenderItemVisitor extends ClassVisitor {

		private boolean visitedInvoke;

		public RenderItemVisitor(ClassVisitor cv) {
			super(ASM5, cv);
			// TODO Auto-generated constructor stub
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			final MethodVisitor delegateVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			return name.equals(LaniusLoadingPlugin.nameRegistry.get("renderItem")) && desc
					.equals("(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V")
							? new MethodVisitor(ASM5, delegateVisitor) {
								@Override
								public void visitJumpInsn(int opcode, Label label) {
									super.visitJumpInsn(opcode, label);
									if (opcode == IFEQ && visitedInvoke) {
										mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/hook/HookManager",
												"renderEffManager", "Lorg/bitbucket/lanius/hook/HookManager;");
										mv.visitTypeInsn(NEW, "org/bitbucket/lanius/hook/impl/RenderEffectData");
										mv.visitInsn(DUP);
										mv.visitVarInsn(ALOAD, 0);
										mv.visitMethodInsn(INVOKESPECIAL,
												"org/bitbucket/lanius/hook/impl/RenderEffectData", "<init>",
												"(Lnet/minecraft/client/renderer/RenderItem;)V", false);
										mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/util/Phase", "START",
												"Lorg/bitbucket/lanius/util/Phase;");
										mv.visitMethodInsn(INVOKEVIRTUAL, "org/bitbucket/lanius/hook/HookManager",
												"execute",
												"(Lorg/bitbucket/lanius/hook/HookData;Lorg/bitbucket/lanius/util/Phase;)Ljava/lang/Object;",
												false);
										mv.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
										mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z",
												false);
										mv.visitJumpInsn(IFNE, label);
										visitedInvoke = false;
									}
								}

								@Override
								public void visitMethodInsn(int opcode, String owner, String name, String desc,
										boolean itf) {
									super.visitMethodInsn(opcode, owner, name, desc, itf);
									if (opcode == INVOKESPECIAL
											&& name.equals(LaniusLoadingPlugin.nameRegistry.get("renderModel"))
											&& desc.equals(
													"(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/item/ItemStack;)V")) {
										visitedInvoke = true;
									}
								}
							}
							: delegateVisitor;
		}

	}

	public RenderItemTransformer() {
		super("net/minecraft/client/renderer/RenderItem");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ClassVisitor classVisitor(final ClassWriter classWriter) {
		// TODO Auto-generated method stub
		return new RenderItemVisitor(classWriter);
	}

}
