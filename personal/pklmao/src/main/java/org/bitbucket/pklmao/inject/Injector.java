package org.bitbucket.pklmao.inject;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARRAYLENGTH;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.BASTORE;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.F_APPEND;
import static org.objectweb.asm.Opcodes.F_SAME;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.I2B;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.IF_ICMPLT;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INTEGER;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.ISTORE;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.SIPUSH;

import org.bitbucket.pklmao.PkLmao;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class Injector {
	private PkLmao pkLmao;
	
	public Injector(PkLmao pkLmao) {
		this.pkLmao = pkLmao;
	}
	
	public void injectClasses(ClassLoader loader) {
		ClassTransformer clientTransformer = new ClassTransformer(pkLmao.getMappings().getObfName("class_Main")) {

			@Override
			protected ClassVisitor createVisitor(ClassWriter writer) {
				return new ClassVisitor(ASM5, writer) {
					@Override
					public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
						if (name.equals(pkLmao.getMappings().getObfName("method_createHwidStr")) && descriptor.equals("([B)Ljava/lang/String;") && (Boolean) pkLmao.getMainCfg().getOptById("randomize_mac_address").getValue()) {
							return new MethodVisitor(ASM5, super.visitMethod(access, name, descriptor, signature, exceptions)) {
								@Override
								public void visitCode() {
									super.visitCode();
									mv.visitTypeInsn(NEW, "java/util/Random");
									mv.visitInsn(DUP);
									mv.visitMethodInsn(INVOKESPECIAL, "java/util/Random", "<init>", "()V", false);
									mv.visitVarInsn(ASTORE, 1);
									mv.visitInsn(ICONST_0);
									mv.visitVarInsn(ISTORE, 2);
									Label l0 = new Label();
									mv.visitJumpInsn(GOTO, l0);
									Label l1 = new Label();
									mv.visitLabel(l1);
									mv.visitFrame(F_APPEND,2, new Object[] {"java/util/Random", INTEGER}, 0, null);
									mv.visitVarInsn(ALOAD, 0);
									mv.visitVarInsn(ILOAD, 2);
									mv.visitVarInsn(ALOAD, 1);
									mv.visitIntInsn(SIPUSH, 128);
									mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/Random", "nextInt", "(I)I", false);
									mv.visitInsn(I2B);
									mv.visitInsn(BASTORE);
									mv.visitIincInsn(2, 1);
									mv.visitLabel(l0);
									mv.visitFrame(F_SAME, 0, null, 0, null);
									mv.visitVarInsn(ILOAD, 2);
									mv.visitVarInsn(ALOAD, 0);
									mv.visitInsn(ARRAYLENGTH);
									mv.visitJumpInsn(IF_ICMPLT, l1);
								}
							};
						}
						return super.visitMethod(access, name, descriptor, signature, exceptions);
					}
				};
			}

		};
		clientTransformer.loadClass(loader);
		ClassTransformer gameLoopTransformer = new ClassTransformer(pkLmao.getMappings().getObfName("class_GameLoop")) {
			@Override
			protected ClassVisitor createVisitor(ClassWriter writer) {
				return new ClassVisitor(ASM5, writer) {
					@Override
					public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
						if (name.equals("run") && descriptor.equals("()V")) {
							return new MethodVisitor(ASM5, super.visitMethod(access, name, descriptor, signature, exceptions)) {
								@Override
								public void visitLdcInsn(Object value) {
									if (value instanceof Long && ((Long) value).longValue() == 50L) {
										mv.visitMethodInsn(INVOKESTATIC, "org/bitbucket/pklmao/event/EventManager", "getInstance", "()Lorg/bitbucket/pklmao/event/EventManager;", false);
										mv.visitTypeInsn(NEW, "org/bitbucket/pklmao/event/TickEvent");
										mv.visitInsn(DUP);
										mv.visitVarInsn(ALOAD, 0);
										mv.visitFieldInsn(GETSTATIC, "org/bitbucket/pklmao/event/TickEvent$Phase", "END", "Lorg/bitbucket/pklmao/event/TickEvent$Phase;");
										mv.visitMethodInsn(INVOKESPECIAL, "org/bitbucket/pklmao/event/TickEvent", "<init>", "(Ljava/lang/Object;Lorg/bitbucket/pklmao/event/TickEvent$Phase;)V", false);
										mv.visitMethodInsn(INVOKEVIRTUAL, "org/bitbucket/pklmao/event/EventManager", "fireEvent", "(Lorg/bitbucket/pklmao/event/Event;)V", false);
									}
									super.visitLdcInsn(value);
								}
							};
						}
						return super.visitMethod(access, name, descriptor, signature, exceptions);
					}
				};
			}
		};
		gameLoopTransformer.loadClass(loader);
	}
}
