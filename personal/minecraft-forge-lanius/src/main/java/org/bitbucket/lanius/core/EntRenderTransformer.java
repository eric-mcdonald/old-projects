package org.bitbucket.lanius.core;

import org.bitbucket.lanius.hook.HookManager;
import org.bitbucket.lanius.hook.impl.FrustumData;
import org.bitbucket.lanius.util.Phase;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.Frustum;

public final class EntRenderTransformer extends BasicTransformer {

	private final class EntRenderVisitor extends ClassVisitor {

		public EntRenderVisitor(ClassVisitor cv) {
			super(Opcodes.ASM5, cv);
			// TODO Auto-generated constructor stub
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			final MethodVisitor delegateVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			return name.equals(LaniusLoadingPlugin.nameRegistry.get("renderWorldPass")) && desc.equals("(IFJ)V")
					? new MethodVisitor(Opcodes.ASM5, delegateVisitor) {
						private final String frustumName = Type.getInternalName(Frustum.class),
								subName = Type.getInternalName(FrustumSub.class);

						@Override
						public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
							if (owner.equals(frustumName) && (opcode == Opcodes.INVOKESPECIAL && name.equals("<init>")
									|| opcode == Opcodes.INVOKEVIRTUAL)) {
								owner = subName;
							}
							super.visitMethodInsn(opcode, owner, name, desc, itf);
						}

						@Override
						public void visitTypeInsn(int opcode, String type) {
							if (opcode == Opcodes.NEW && type.equals(frustumName)) {
								type = subName;
							}
							super.visitTypeInsn(opcode, type);
						}
					}
					: delegateVisitor;
		}

	}

	public static final class FrustumSub extends Frustum {
		public FrustumSub() {
			super();
		}

		public FrustumSub(ClippingHelper clipHelper) {
			super(clipHelper);
		}

		@Override
		public boolean isBoxInFrustum(double p_78548_1_, double p_78548_3_, double p_78548_5_, double p_78548_7_,
				double p_78548_9_, double p_78548_11_) {
			return HookManager.frustumManager.execute(new FrustumData(this,
					super.isBoxInFrustum(p_78548_1_, p_78548_3_, p_78548_5_, p_78548_7_, p_78548_9_, p_78548_11_)),
					Phase.START);
		}
	}

	public EntRenderTransformer() {
		super("net.minecraft.client.renderer.EntityRenderer");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ClassVisitor classVisitor(final ClassWriter classWriter) {
		// TODO Auto-generated method stub
		return new EntRenderVisitor(classWriter);
	}

}
