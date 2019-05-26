package org.bitbucket.lanius.core;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.FALOAD;
import static org.objectweb.asm.Opcodes.FASTORE;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.ICONST_2;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.NEWARRAY;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.T_FLOAT;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import net.minecraft.util.MovementInput;

public final class EntPlayerTransformer extends BasicTransformer {

	private final class EntPlayerVisitor extends ClassVisitor {

		public EntPlayerVisitor(ClassVisitor cv) {
			super(ASM5, cv);
			// TODO Auto-generated constructor stub
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			final MethodVisitor delegateVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			return name.equals(LaniusLoadingPlugin.nameRegistry.get("onLivingUpdate")) && desc.equals("()V")
					? new MethodVisitor(ASM5, delegateVisitor) {
						private boolean visitedUpdateMove;

						@Override
						public void visitFieldInsn(int opcode, String owner, String name, String desc) {
							super.visitFieldInsn(opcode, owner, name, desc);
							if (visitedUpdateMove && opcode == PUTFIELD
									&& owner.equals("net/minecraft/client/entity/EntityPlayerSP")
									&& name.equals(LaniusLoadingPlugin.nameRegistry.get("sprintToggleTimer"))
									&& desc.equals("I")) {
								// final float[] moveVecs =
								// HookManager.slowdownManager.execute(new
								// SlowdownData(Lanius.mc.player, new float[]
								// {Lanius.mc.player.movementInput.moveStrafe,
								// Lanius.mc.player.movementInput.moveForward}),
								// Phase.END);
								// Lanius.mc.player.movementInput.moveStrafe
								// = moveVecs[0];
								// Lanius.mc.player.movementInput.moveForward
								// = moveVecs[1];
								mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/hook/HookManager", "slowdownManager",
										"Lorg/bitbucket/lanius/hook/HookManager;");
								mv.visitTypeInsn(NEW, "org/bitbucket/lanius/hook/impl/SlowdownData");
								mv.visitInsn(DUP);
								mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/Lanius", "mc",
										"Lnet/minecraft/client/Minecraft;");
								final String playerName = LaniusLoadingPlugin.nameRegistry.get("player");
								mv.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", playerName,
										"Lnet/minecraft/client/entity/EntityPlayerSP;");
								mv.visitInsn(ICONST_2);
								mv.visitIntInsn(NEWARRAY, T_FLOAT);
								mv.visitInsn(DUP);
								mv.visitInsn(ICONST_0);
								mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/Lanius", "mc",
										"Lnet/minecraft/client/Minecraft;");
								mv.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", playerName,
										"Lnet/minecraft/client/entity/EntityPlayerSP;");
								final String moveInName = LaniusLoadingPlugin.nameRegistry.get("movementInput");
								mv.visitFieldInsn(GETFIELD, "net/minecraft/client/entity/EntityPlayerSP", moveInName,
										"Lnet/minecraft/util/MovementInput;");
								final String strafeName = LaniusLoadingPlugin.nameRegistry.get("moveStrafe");
								mv.visitFieldInsn(GETFIELD, "net/minecraft/util/MovementInput", strafeName, "F");
								mv.visitInsn(FASTORE);
								mv.visitInsn(DUP);
								mv.visitInsn(ICONST_1);
								mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/Lanius", "mc",
										"Lnet/minecraft/client/Minecraft;");
								mv.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", playerName,
										"Lnet/minecraft/client/entity/EntityPlayerSP;");
								mv.visitFieldInsn(GETFIELD, "net/minecraft/client/entity/EntityPlayerSP", moveInName,
										"Lnet/minecraft/util/MovementInput;");
								final String forwardName = LaniusLoadingPlugin.nameRegistry.get("moveForward");
								mv.visitFieldInsn(GETFIELD, "net/minecraft/util/MovementInput", forwardName, "F");
								mv.visitInsn(FASTORE);
								mv.visitMethodInsn(INVOKESPECIAL, "org/bitbucket/lanius/hook/impl/SlowdownData",
										"<init>", "(Lnet/minecraft/client/entity/EntityPlayerSP;[F)V", false);
								mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/util/Phase", "END",
										"Lorg/bitbucket/lanius/util/Phase;");
								mv.visitMethodInsn(INVOKEVIRTUAL, "org/bitbucket/lanius/hook/HookManager", "execute",
										"(Lorg/bitbucket/lanius/hook/HookData;Lorg/bitbucket/lanius/util/Phase;)Ljava/lang/Object;",
										false);
								mv.visitTypeInsn(CHECKCAST, "[F");
								mv.visitVarInsn(ASTORE, 9);
								mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/Lanius", "mc",
										"Lnet/minecraft/client/Minecraft;");
								mv.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", playerName,
										"Lnet/minecraft/client/entity/EntityPlayerSP;");
								mv.visitFieldInsn(GETFIELD, "net/minecraft/client/entity/EntityPlayerSP", moveInName,
										"Lnet/minecraft/util/MovementInput;");
								mv.visitVarInsn(ALOAD, 9);
								mv.visitInsn(ICONST_0);
								mv.visitInsn(FALOAD);
								mv.visitFieldInsn(PUTFIELD, "net/minecraft/util/MovementInput", strafeName, "F");
								mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/Lanius", "mc",
										"Lnet/minecraft/client/Minecraft;");
								mv.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", playerName,
										"Lnet/minecraft/client/entity/EntityPlayerSP;");
								mv.visitFieldInsn(GETFIELD, "net/minecraft/client/entity/EntityPlayerSP", moveInName,
										"Lnet/minecraft/util/MovementInput;");
								mv.visitVarInsn(ALOAD, 9);
								mv.visitInsn(ICONST_1);
								mv.visitInsn(FALOAD);
								mv.visitFieldInsn(PUTFIELD, "net/minecraft/util/MovementInput", forwardName, "F");
								visitedUpdateMove = false;
							}
						}

						@Override
						public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
							super.visitMethodInsn(opcode, owner, name, desc, itf);
							if (opcode == INVOKEVIRTUAL && owner.equals(Type.getInternalName(MovementInput.class))
									&& name.equals(LaniusLoadingPlugin.nameRegistry.get("updatePlayerMoveState"))
									&& desc.equals("()V")) {
								visitedUpdateMove = true;
							}
						}
					}
					: delegateVisitor;
		}

	}

	public EntPlayerTransformer() {
		super("net.minecraft.client.entity.EntityPlayerSP");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ClassVisitor classVisitor(final ClassWriter classWriter) {
		// TODO Auto-generated method stub
		return new EntPlayerVisitor(classWriter);
	}

}
