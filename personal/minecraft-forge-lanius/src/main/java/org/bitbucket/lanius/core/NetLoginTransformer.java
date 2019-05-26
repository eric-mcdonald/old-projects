package org.bitbucket.lanius.core;

import org.bitbucket.lanius.hook.impl.PlayNetHandlerSub;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import net.minecraft.client.network.NetHandlerPlayClient;

public final class NetLoginTransformer extends BasicTransformer {

	private final class NetLoginVisitor extends ClassVisitor {

		public NetLoginVisitor(ClassVisitor cv) {
			super(Opcodes.ASM5, cv);
			// TODO Auto-generated constructor stub
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			final MethodVisitor delegateVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			return name.equals(LaniusLoadingPlugin.nameRegistry.get("handleLoginSuccess"))
					&& desc.equals("(Lnet/minecraft/network/login/server/SPacketLoginSuccess;)V")
							? new MethodVisitor(Opcodes.ASM5, delegateVisitor) {
								private final String originalName = Type.getInternalName(NetHandlerPlayClient.class),
										subName = Type.getInternalName(PlayNetHandlerSub.class);

								@Override
								public void visitMethodInsn(int opcode, String owner, String name, String desc,
										boolean itf) {
									if (opcode == Opcodes.INVOKESPECIAL && owner.equals(originalName)
											&& name.equals("<init>") && desc.equals(
													"(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/GuiScreen;Lnet/minecraft/network/NetworkManager;Lcom/mojang/authlib/GameProfile;)V")) {
										owner = subName;
									}
									super.visitMethodInsn(opcode, owner, name, desc, itf);
								}

								@Override
								public void visitTypeInsn(int opcode, String type) {
									if (opcode == Opcodes.NEW && type.equals(originalName)) {
										type = subName;
									}
									super.visitTypeInsn(opcode, type);
								}
							}
							: delegateVisitor;
		}

	}

	public NetLoginTransformer() {
		super("net.minecraft.client.network.NetHandlerLoginClient");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ClassVisitor classVisitor(final ClassWriter classWriter) {
		// TODO Auto-generated method stub
		return new NetLoginVisitor(classWriter);
	}

}
