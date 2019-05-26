package org.bitbucket.lanius.core;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public final class RenderLivingTransformer extends BasicTransformer {

	private final class RenderLivingVisitor extends ClassVisitor {

		public RenderLivingVisitor(ClassVisitor cv) {
			super(ASM5, cv);
			// TODO Auto-generated constructor stub
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			final MethodVisitor delegateVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			return name.equals(LaniusLoadingPlugin.nameRegistry.get("doRender"))
					&& desc.equals("(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V") && signature.equals("(TT;DDDFF)V")
							? new MethodVisitor(ASM5, delegateVisitor) {
								@Override
								public void visitMethodInsn(int opcode, String owner, String name, String desc,
										boolean itf) {
									super.visitMethodInsn(opcode, owner, name, desc, itf);
									if (name.equals(LaniusLoadingPlugin.nameRegistry.get("enableOutlineMode"))
											&& desc.equals("(I)V")) {
										mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/hook/HookManager",
												"outlineManager", "Lorg/bitbucket/lanius/hook/HookManager;");
										mv.visitTypeInsn(NEW, "org/bitbucket/lanius/hook/impl/OutlineModeData");
										mv.visitInsn(DUP);
										mv.visitVarInsn(ALOAD, 0);
										mv.visitVarInsn(ALOAD, 0);
										mv.visitVarInsn(ALOAD, 1);
										mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/renderer/entity/Render",
												LaniusLoadingPlugin.nameRegistry.get("getTeamColor"),
												"(Lnet/minecraft/entity/Entity;)I", false);
										mv.visitVarInsn(ALOAD, 1);
										mv.visitMethodInsn(INVOKESPECIAL,
												"org/bitbucket/lanius/hook/impl/OutlineModeData", "<init>",
												"(Lnet/minecraft/client/renderer/entity/RenderLivingBase;ILnet/minecraft/entity/Entity;)V",
												false);
										mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/util/Phase", "START",
												"Lorg/bitbucket/lanius/util/Phase;");
										mv.visitMethodInsn(INVOKEVIRTUAL, "org/bitbucket/lanius/hook/HookManager",
												"execute",
												"(Lorg/bitbucket/lanius/hook/HookData;Lorg/bitbucket/lanius/util/Phase;)Ljava/lang/Object;",
												false);
										mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
										mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I",
												false);
										super.visitMethodInsn(opcode, owner, name, desc, itf);
									}
								}
							}
							: delegateVisitor;
		}
	}

	public RenderLivingTransformer() {
		super("net.minecraft.client.renderer.entity.RenderLivingBase");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ClassVisitor classVisitor(final ClassWriter classWriter) {
		// TODO Auto-generated method stub
		return new RenderLivingVisitor(classWriter);
	}

}
