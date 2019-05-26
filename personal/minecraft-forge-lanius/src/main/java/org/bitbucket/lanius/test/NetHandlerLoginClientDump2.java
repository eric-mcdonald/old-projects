package org.bitbucket.lanius.test;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class NetHandlerLoginClientDump2 implements Opcodes {

	public static byte[] dump() throws Exception {

		ClassWriter cw = new ClassWriter(0);
		FieldVisitor fv;
		MethodVisitor mv;
		AnnotationVisitor av0;

		cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, "net/minecraft/client/network/NetHandlerLoginClient", null,
				"java/lang/Object", new String[] { "net/minecraft/network/login/INetHandlerLoginClient" });

		{
			av0 = cw.visitAnnotation("Lnet/minecraftforge/fml/relauncher/SideOnly;", true);
			av0.visitEnum("value", "Lnet/minecraftforge/fml/relauncher/Side;", "CLIENT");
			av0.visitEnd();
		}
		cw.visitInnerClass("net/minecraft/client/network/NetHandlerLoginClient$1", null, null, 0);

		{
			fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "LOGGER", "Lorg/apache/logging/log4j/Logger;",
					null, null);
			fv.visitEnd();
		}
		{
			fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "mc", "Lnet/minecraft/client/Minecraft;", null, null);
			fv.visitEnd();
		}
		{
			fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "previousGuiScreen", "Lnet/minecraft/client/gui/GuiScreen;",
					null, null);
			fv.visitEnd();
		}
		{
			fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "networkManager", "Lnet/minecraft/network/NetworkManager;",
					null, null);
			fv.visitEnd();
		}
		{
			fv = cw.visitField(ACC_PRIVATE, "gameProfile", "Lcom/mojang/authlib/GameProfile;", null, null);
			fv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>",
					"(Lnet/minecraft/network/NetworkManager;Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/GuiScreen;)V",
					null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(PUTFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "networkManager",
					"Lnet/minecraft/network/NetworkManager;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitFieldInsn(PUTFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "mc",
					"Lnet/minecraft/client/Minecraft;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitFieldInsn(PUTFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "previousGuiScreen",
					"Lnet/minecraft/client/gui/GuiScreen;");
			mv.visitInsn(RETURN);
			mv.visitMaxs(2, 4);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "handleEncryptionRequest",
					"(Lnet/minecraft/network/login/server/SPacketEncryptionRequest;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			Label l1 = new Label();
			Label l2 = new Label();
			mv.visitTryCatchBlock(l0, l1, l2, "com/mojang/authlib/exceptions/AuthenticationException");
			Label l3 = new Label();
			Label l4 = new Label();
			Label l5 = new Label();
			mv.visitTryCatchBlock(l3, l4, l5, "com/mojang/authlib/exceptions/AuthenticationUnavailableException");
			Label l6 = new Label();
			mv.visitTryCatchBlock(l3, l4, l6, "com/mojang/authlib/exceptions/InvalidCredentialsException");
			Label l7 = new Label();
			mv.visitTryCatchBlock(l3, l4, l7, "com/mojang/authlib/exceptions/AuthenticationException");
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/util/CryptManager", "createNewSharedKey",
					"()Ljavax/crypto/SecretKey;", false);
			mv.visitVarInsn(ASTORE, 2);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/network/login/server/SPacketEncryptionRequest",
					"getServerId", "()Ljava/lang/String;", false);
			mv.visitVarInsn(ASTORE, 3);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/network/login/server/SPacketEncryptionRequest",
					"getPublicKey", "()Ljava/security/PublicKey;", false);
			mv.visitVarInsn(ASTORE, 4);
			mv.visitTypeInsn(NEW, "java/math/BigInteger");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/util/CryptManager", "getServerIdHash",
					"(Ljava/lang/String;Ljava/security/PublicKey;Ljavax/crypto/SecretKey;)[B", false);
			mv.visitMethodInsn(INVOKESPECIAL, "java/math/BigInteger", "<init>", "([B)V", false);
			mv.visitIntInsn(BIPUSH, 16);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/math/BigInteger", "toString", "(I)Ljava/lang/String;", false);
			mv.visitVarInsn(ASTORE, 5);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "mc",
					"Lnet/minecraft/client/Minecraft;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "getCurrentServerData",
					"()Lnet/minecraft/client/multiplayer/ServerData;", false);
			mv.visitJumpInsn(IFNULL, l3);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "mc",
					"Lnet/minecraft/client/Minecraft;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "getCurrentServerData",
					"()Lnet/minecraft/client/multiplayer/ServerData;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/multiplayer/ServerData", "isOnLAN", "()Z", false);
			mv.visitJumpInsn(IFEQ, l3);
			mv.visitLabel(l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/network/NetHandlerLoginClient", "getSessionService",
					"()Lcom/mojang/authlib/minecraft/MinecraftSessionService;", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "mc",
					"Lnet/minecraft/client/Minecraft;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "getSession",
					"()Lnet/minecraft/util/Session;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/Session", "getProfile",
					"()Lcom/mojang/authlib/GameProfile;", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "mc",
					"Lnet/minecraft/client/Minecraft;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "getSession",
					"()Lnet/minecraft/util/Session;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/Session", "getToken", "()Ljava/lang/String;", false);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitMethodInsn(INVOKEINTERFACE, "com/mojang/authlib/minecraft/MinecraftSessionService", "joinServer",
					"(Lcom/mojang/authlib/GameProfile;Ljava/lang/String;Ljava/lang/String;)V", true);
			mv.visitLabel(l1);
			Label l8 = new Label();
			mv.visitJumpInsn(GOTO, l8);
			mv.visitLabel(l2);
			mv.visitFrame(Opcodes.F_FULL, 6,
					new Object[] { "net/minecraft/client/network/NetHandlerLoginClient",
							"net/minecraft/network/login/server/SPacketEncryptionRequest", "javax/crypto/SecretKey",
							"java/lang/String", "java/security/PublicKey", "java/lang/String" },
					1, new Object[] { "com/mojang/authlib/exceptions/AuthenticationException" });
			mv.visitVarInsn(ASTORE, 6);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/client/network/NetHandlerLoginClient", "LOGGER",
					"Lorg/apache/logging/log4j/Logger;");
			mv.visitLdcInsn("Couldn't connect to auth servers but will continue to join LAN");
			mv.visitMethodInsn(INVOKEINTERFACE, "org/apache/logging/log4j/Logger", "warn", "(Ljava/lang/String;)V",
					true);
			mv.visitJumpInsn(GOTO, l8);
			mv.visitLabel(l3);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/network/NetHandlerLoginClient", "getSessionService",
					"()Lcom/mojang/authlib/minecraft/MinecraftSessionService;", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "mc",
					"Lnet/minecraft/client/Minecraft;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "getSession",
					"()Lnet/minecraft/util/Session;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/Session", "getProfile",
					"()Lcom/mojang/authlib/GameProfile;", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "mc",
					"Lnet/minecraft/client/Minecraft;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "getSession",
					"()Lnet/minecraft/util/Session;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/Session", "getToken", "()Ljava/lang/String;", false);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitMethodInsn(INVOKEINTERFACE, "com/mojang/authlib/minecraft/MinecraftSessionService", "joinServer",
					"(Lcom/mojang/authlib/GameProfile;Ljava/lang/String;Ljava/lang/String;)V", true);
			mv.visitLabel(l4);
			mv.visitJumpInsn(GOTO, l8);
			mv.visitLabel(l5);
			mv.visitFrame(Opcodes.F_SAME1, 0, null, 1,
					new Object[] { "com/mojang/authlib/exceptions/AuthenticationUnavailableException" });
			mv.visitVarInsn(ASTORE, 6);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "networkManager",
					"Lnet/minecraft/network/NetworkManager;");
			mv.visitTypeInsn(NEW, "net/minecraft/util/text/TextComponentTranslation");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("disconnect.loginFailedInfo");
			mv.visitInsn(ICONST_1);
			mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_0);
			mv.visitTypeInsn(NEW, "net/minecraft/util/text/TextComponentTranslation");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("disconnect.loginFailedInfo.serversUnavailable");
			mv.visitInsn(ICONST_0);
			mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/util/text/TextComponentTranslation", "<init>",
					"(Ljava/lang/String;[Ljava/lang/Object;)V", false);
			mv.visitInsn(AASTORE);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/util/text/TextComponentTranslation", "<init>",
					"(Ljava/lang/String;[Ljava/lang/Object;)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/network/NetworkManager", "closeChannel",
					"(Lnet/minecraft/util/text/ITextComponent;)V", false);
			mv.visitInsn(RETURN);
			mv.visitLabel(l6);
			mv.visitFrame(Opcodes.F_SAME1, 0, null, 1,
					new Object[] { "com/mojang/authlib/exceptions/InvalidCredentialsException" });
			mv.visitVarInsn(ASTORE, 6);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "networkManager",
					"Lnet/minecraft/network/NetworkManager;");
			mv.visitTypeInsn(NEW, "net/minecraft/util/text/TextComponentTranslation");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("disconnect.loginFailedInfo");
			mv.visitInsn(ICONST_1);
			mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_0);
			mv.visitTypeInsn(NEW, "net/minecraft/util/text/TextComponentTranslation");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("disconnect.loginFailedInfo.invalidSession");
			mv.visitInsn(ICONST_0);
			mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/util/text/TextComponentTranslation", "<init>",
					"(Ljava/lang/String;[Ljava/lang/Object;)V", false);
			mv.visitInsn(AASTORE);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/util/text/TextComponentTranslation", "<init>",
					"(Ljava/lang/String;[Ljava/lang/Object;)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/network/NetworkManager", "closeChannel",
					"(Lnet/minecraft/util/text/ITextComponent;)V", false);
			mv.visitInsn(RETURN);
			mv.visitLabel(l7);
			mv.visitFrame(Opcodes.F_SAME1, 0, null, 1,
					new Object[] { "com/mojang/authlib/exceptions/AuthenticationException" });
			mv.visitVarInsn(ASTORE, 6);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "networkManager",
					"Lnet/minecraft/network/NetworkManager;");
			mv.visitTypeInsn(NEW, "net/minecraft/util/text/TextComponentTranslation");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("disconnect.loginFailedInfo");
			mv.visitInsn(ICONST_1);
			mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/mojang/authlib/exceptions/AuthenticationException", "getMessage",
					"()Ljava/lang/String;", false);
			mv.visitInsn(AASTORE);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/util/text/TextComponentTranslation", "<init>",
					"(Ljava/lang/String;[Ljava/lang/Object;)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/network/NetworkManager", "closeChannel",
					"(Lnet/minecraft/util/text/ITextComponent;)V", false);
			mv.visitInsn(RETURN);
			mv.visitLabel(l8);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "networkManager",
					"Lnet/minecraft/network/NetworkManager;");
			mv.visitTypeInsn(NEW, "net/minecraft/network/login/client/CPacketEncryptionResponse");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/network/login/server/SPacketEncryptionRequest",
					"getVerifyToken", "()[B", false);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/network/login/client/CPacketEncryptionResponse", "<init>",
					"(Ljavax/crypto/SecretKey;Ljava/security/PublicKey;[B)V", false);
			mv.visitTypeInsn(NEW, "net/minecraft/client/network/NetHandlerLoginClient$1");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/network/NetHandlerLoginClient$1", "<init>",
					"(Lnet/minecraft/client/network/NetHandlerLoginClient;Ljavax/crypto/SecretKey;)V", false);
			mv.visitInsn(ICONST_0);
			mv.visitTypeInsn(ANEWARRAY, "io/netty/util/concurrent/GenericFutureListener");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/network/NetworkManager", "sendPacket",
					"(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;[Lio/netty/util/concurrent/GenericFutureListener;)V",
					false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(11, 7);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PRIVATE, "getSessionService",
					"()Lcom/mojang/authlib/minecraft/MinecraftSessionService;", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "mc",
					"Lnet/minecraft/client/Minecraft;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "getSessionService",
					"()Lcom/mojang/authlib/minecraft/MinecraftSessionService;", false);
			mv.visitInsn(ARETURN);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "handleLoginSuccess",
					"(Lnet/minecraft/network/login/server/SPacketLoginSuccess;)V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/network/login/server/SPacketLoginSuccess", "getProfile",
					"()Lcom/mojang/authlib/GameProfile;", false);
			mv.visitFieldInsn(PUTFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "gameProfile",
					"Lcom/mojang/authlib/GameProfile;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "networkManager",
					"Lnet/minecraft/network/NetworkManager;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/network/EnumConnectionState", "PLAY",
					"Lnet/minecraft/network/EnumConnectionState;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/network/NetworkManager", "setConnectionState",
					"(Lnet/minecraft/network/EnumConnectionState;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "networkManager",
					"Lnet/minecraft/network/NetworkManager;");
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/fml/common/network/internal/FMLNetworkHandler",
					"fmlClientHandshake", "(Lnet/minecraft/network/NetworkManager;)V", false);
			mv.visitTypeInsn(NEW, "net/minecraft/client/network/NetHandlerPlayClient");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "mc",
					"Lnet/minecraft/client/Minecraft;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "previousGuiScreen",
					"Lnet/minecraft/client/gui/GuiScreen;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "networkManager",
					"Lnet/minecraft/network/NetworkManager;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "gameProfile",
					"Lcom/mojang/authlib/GameProfile;");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/network/NetHandlerPlayClient", "<init>",
					"(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/GuiScreen;Lnet/minecraft/network/NetworkManager;Lcom/mojang/authlib/GameProfile;)V",
					false);
			mv.visitVarInsn(ASTORE, 2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "networkManager",
					"Lnet/minecraft/network/NetworkManager;");
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/network/NetworkManager", "setNetHandler",
					"(Lnet/minecraft/network/INetHandler;)V", false);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/fml/client/FMLClientHandler", "instance",
					"()Lnet/minecraftforge/fml/client/FMLClientHandler;", false);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/fml/client/FMLClientHandler", "setPlayClient",
					"(Lnet/minecraft/client/network/NetHandlerPlayClient;)V", false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(6, 3);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "onDisconnect", "(Lnet/minecraft/util/text/ITextComponent;)V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "previousGuiScreen",
					"Lnet/minecraft/client/gui/GuiScreen;");
			Label l0 = new Label();
			mv.visitJumpInsn(IFNULL, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "previousGuiScreen",
					"Lnet/minecraft/client/gui/GuiScreen;");
			mv.visitTypeInsn(INSTANCEOF, "net/minecraft/client/gui/GuiScreenRealmsProxy");
			mv.visitJumpInsn(IFEQ, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "mc",
					"Lnet/minecraft/client/Minecraft;");
			mv.visitTypeInsn(NEW, "net/minecraft/realms/DisconnectedRealmsScreen");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "previousGuiScreen",
					"Lnet/minecraft/client/gui/GuiScreen;");
			mv.visitTypeInsn(CHECKCAST, "net/minecraft/client/gui/GuiScreenRealmsProxy");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/gui/GuiScreenRealmsProxy", "getProxy",
					"()Lnet/minecraft/realms/RealmsScreen;", false);
			mv.visitLdcInsn("connect.failed");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/realms/DisconnectedRealmsScreen", "<init>",
					"(Lnet/minecraft/realms/RealmsScreen;Ljava/lang/String;Lnet/minecraft/util/text/ITextComponent;)V",
					false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/realms/DisconnectedRealmsScreen", "getProxy",
					"()Lnet/minecraft/client/gui/GuiScreenRealmsProxy;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "displayGuiScreen",
					"(Lnet/minecraft/client/gui/GuiScreen;)V", false);
			Label l1 = new Label();
			mv.visitJumpInsn(GOTO, l1);
			mv.visitLabel(l0);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "mc",
					"Lnet/minecraft/client/Minecraft;");
			mv.visitTypeInsn(NEW, "net/minecraft/client/gui/GuiDisconnected");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "previousGuiScreen",
					"Lnet/minecraft/client/gui/GuiScreen;");
			mv.visitLdcInsn("connect.failed");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/gui/GuiDisconnected", "<init>",
					"(Lnet/minecraft/client/gui/GuiScreen;Ljava/lang/String;Lnet/minecraft/util/text/ITextComponent;)V",
					false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "displayGuiScreen",
					"(Lnet/minecraft/client/gui/GuiScreen;)V", false);
			mv.visitLabel(l1);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			mv.visitMaxs(6, 2);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "handleDisconnect",
					"(Lnet/minecraft/network/login/server/SPacketDisconnect;)V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "networkManager",
					"Lnet/minecraft/network/NetworkManager;");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/network/login/server/SPacketDisconnect", "getReason",
					"()Lnet/minecraft/util/text/ITextComponent;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/network/NetworkManager", "closeChannel",
					"(Lnet/minecraft/util/text/ITextComponent;)V", false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "handleEnableCompression",
					"(Lnet/minecraft/network/login/server/SPacketEnableCompression;)V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "networkManager",
					"Lnet/minecraft/network/NetworkManager;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/network/NetworkManager", "isLocalChannel", "()Z", false);
			Label l0 = new Label();
			mv.visitJumpInsn(IFNE, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "networkManager",
					"Lnet/minecraft/network/NetworkManager;");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/network/login/server/SPacketEnableCompression",
					"getCompressionThreshold", "()I", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/network/NetworkManager", "setCompressionThreshold", "(I)V",
					false);
			mv.visitLabel(l0);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_STATIC + ACC_SYNTHETIC, "access$000",
					"(Lnet/minecraft/client/network/NetHandlerLoginClient;)Lnet/minecraft/network/NetworkManager;",
					null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/network/NetHandlerLoginClient", "networkManager",
					"Lnet/minecraft/network/NetworkManager;");
			mv.visitInsn(ARETURN);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
			mv.visitCode();
			mv.visitMethodInsn(INVOKESTATIC, "org/apache/logging/log4j/LogManager", "getLogger",
					"()Lorg/apache/logging/log4j/Logger;", false);
			mv.visitFieldInsn(PUTSTATIC, "net/minecraft/client/network/NetHandlerLoginClient", "LOGGER",
					"Lorg/apache/logging/log4j/Logger;");
			mv.visitInsn(RETURN);
			mv.visitMaxs(1, 0);
			mv.visitEnd();
		}
		cw.visitEnd();

		return cw.toByteArray();
	}
}