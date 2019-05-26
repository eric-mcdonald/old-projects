package org.bitbucket.lanius.core;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public final class RenderEntityItemTransformer extends BasicTransformer {

	private final class RenderVisitor extends ClassVisitor {

		public RenderVisitor(ClassVisitor cv) {
			super(ASM5, cv);
			// TODO Auto-generated constructor stub
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			final MethodVisitor delegateVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			return name.equals(LaniusLoadingPlugin.nameRegistry.get("doRender"))
					&& desc.equals("(Lnet/minecraft/entity/item/EntityItem;DDDFF)V")
							? new MethodVisitor(ASM5, delegateVisitor) {
								@Override
								public void visitCode() {
									super.visitCode();
									mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/hook/HookManager",
											"doRenderManager", "Lorg/bitbucket/lanius/hook/HookManager;");
									mv.visitTypeInsn(NEW, "org/bitbucket/lanius/hook/impl/DoRenderData");
									mv.visitInsn(DUP);
									mv.visitVarInsn(ALOAD, 0);
									mv.visitMethodInsn(INVOKESPECIAL, "org/bitbucket/lanius/hook/impl/DoRenderData",
											"<init>", "(Lnet/minecraft/client/renderer/entity/Render;)V", false);
									mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/util/Phase", "START",
											"Lorg/bitbucket/lanius/util/Phase;");
									mv.visitMethodInsn(INVOKEVIRTUAL, "org/bitbucket/lanius/hook/HookManager",
											"execute",
											"(Lorg/bitbucket/lanius/hook/HookData;Lorg/bitbucket/lanius/util/Phase;)Ljava/lang/Object;",
											false);
									mv.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
									mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z",
											false);
									final Label retVal = new Label();
									mv.visitJumpInsn(IFEQ, retVal);
									mv.visitInsn(RETURN);
									mv.visitLabel(retVal);
								}
							}
							: delegateVisitor;
		}
	}

	public RenderEntityItemTransformer() {
		super("net/minecraft/client/renderer/entity/RenderEntityItem");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ClassVisitor classVisitor(final ClassWriter classWriter) {
		// TODO Auto-generated method stub
		return new RenderVisitor(classWriter);
	}

}
