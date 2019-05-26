package org.bitbucket.lanius.core;

import static org.objectweb.asm.Opcodes.ASM5;

import org.bitbucket.lanius.hook.impl.LoginNetHandlerSub;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import net.minecraft.client.network.NetHandlerLoginClient;

public final class MinecraftTransformer extends BasicTransformer {

	private final class MinecraftVisitor extends ClassVisitor {

		public MinecraftVisitor(ClassVisitor cv) {
			super(ASM5, cv);
			// TODO Auto-generated constructor stub
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			final MethodVisitor delegateVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			return name.equals(LaniusLoadingPlugin.nameRegistry.get("launchIntegratedServer"))
					&& desc.equals("(Ljava/lang/String;Ljava/lang/String;Lnet/minecraft/world/WorldSettings;)V")
							? new MethodVisitor(ASM5, delegateVisitor) {
								private final String loginHandlerName = Type
										.getInternalName(NetHandlerLoginClient.class),
										subName = Type.getInternalName(LoginNetHandlerSub.class);

								@Override
								public void visitMethodInsn(int opcode, String owner, String name, String desc,
										boolean itf) {
									if (opcode == Opcodes.INVOKESPECIAL && owner.equals(loginHandlerName)
											&& name.equals("<init>") && desc.equals(
													"(Lnet/minecraft/network/NetworkManager;Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/GuiScreen;)V")) {
										owner = subName;
									}
									super.visitMethodInsn(opcode, owner, name, desc, itf);
								}

								@Override
								public void visitTypeInsn(int opcode, String type) {
									if (opcode == Opcodes.NEW && type.equals(loginHandlerName)) {
										type = subName;
									}
									super.visitTypeInsn(opcode, type);
								}
							}
							: delegateVisitor;
		}

	}

	public MinecraftTransformer() {
		super("net/minecraft/client/Minecraft");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ClassVisitor classVisitor(final ClassWriter classWriter) {
		// TODO Auto-generated method stub
		return new MinecraftVisitor(classWriter);
	}

}