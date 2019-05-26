package org.bitbucket.lanius.test;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class NetHandlerLoginClientDump implements Opcodes {

	public static byte[] dump() throws Exception {

		ClassWriter cw = new ClassWriter(0);
		FieldVisitor fv;
		MethodVisitor mv;
		AnnotationVisitor av0;

		cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, "org/bitbucket/lanius/test/NetHandlerLoginClient", null,
				"java/lang/Object", new String[] { "net/minecraft/network/login/INetHandlerLoginClient" });

		{
			av0 = cw.visitAnnotation("Lnet/minecraftforge/fml/relauncher/SideOnly;", true);
			av0.visitEnum("value", "Lnet/minecraftforge/fml/relauncher/Side;", "CLIENT");
			av0.visitEnd();
		}
		cw.visitInnerClass("org/bitbucket/lanius/test/NetHandlerLoginClient$1", null, null, 0);

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
			mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
			mv.visitCode();
			mv.visitMethodInsn(INVOKESTATIC, "org/apache/logging/log4j/LogManager", "getLogger",
					"()Lorg/apache/logging/log4j/Logger;", false);
			mv.visitFieldInsn(PUTSTATIC, "org/bitbucket/lanius/test/NetHandlerLoginClient", "LOGGER",
					"Lorg/apache/logging/log4j/Logger;");
			mv.visitInsn(RETURN);
			mv.visitMaxs(1, 0);
			mv.visitEnd();
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
			mv.visitFieldInsn(PUTFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "networkManager",
					"Lnet/minecraft/network/NetworkManager;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitFieldInsn(PUTFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "mc",
					"Lnet/minecraft/client/Minecraft;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitFieldInsn(PUTFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "previousGuiScreen",
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
			mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Exception");
			Label l3 = new Label();
			Label l4 = new Label();
			mv.visitTryCatchBlock(l3, l4, l2, "java/lang/Exception");
			Label l5 = new Label();
			Label l6 = new Label();
			Label l7 = new Label();
			mv.visitTryCatchBlock(l5, l6, l7, "com/mojang/authlib/exceptions/AuthenticationException");
			Label l8 = new Label();
			Label l9 = new Label();
			Label l10 = new Label();
			mv.visitTryCatchBlock(l8, l9, l10, "com/mojang/authlib/exceptions/AuthenticationUnavailableException");
			Label l11 = new Label();
			mv.visitTryCatchBlock(l8, l9, l11, "com/mojang/authlib/exceptions/InvalidCredentialsException");
			Label l12 = new Label();
			mv.visitTryCatchBlock(l8, l9, l12, "com/mojang/authlib/exceptions/AuthenticationException");
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
			mv.visitMethodInsn(INVOKESTATIC, "org/bitbucket/lanius/gui/McLeaksGui", "isTokenActive", "()Z", false);
			Label l13 = new Label();
			mv.visitJumpInsn(IFEQ, l13);
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "networkManager",
					"Lnet/minecraft/network/NetworkManager;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/network/NetworkManager", "getRemoteAddress",
					"()Ljava/net/SocketAddress;", false);
			mv.visitTypeInsn(CHECKCAST, "java/net/InetSocketAddress");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/net/InetSocketAddress", "getHostName", "()Ljava/lang/String;",
					false);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/String", "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;",
					false);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
			mv.visitLdcInsn(":");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
					"(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "networkManager",
					"Lnet/minecraft/network/NetworkManager;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/network/NetworkManager", "getRemoteAddress",
					"()Ljava/net/SocketAddress;", false);
			mv.visitTypeInsn(CHECKCAST, "java/net/InetSocketAddress");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/net/InetSocketAddress", "getPort", "()I", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;",
					false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
			mv.visitVarInsn(ASTORE, 6);
			mv.visitLabel(l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "mc",
					"Lnet/minecraft/client/Minecraft;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "getSession",
					"()Lnet/minecraft/util/Session;", false);
			mv.visitVarInsn(ASTORE, 7);
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("{\"session\":\"");
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 7);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/Session", "getToken", "()Ljava/lang/String;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
					"(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
			mv.visitLdcInsn("\",\"mcname\":\"");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
					"(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
			mv.visitVarInsn(ALOAD, 7);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/Session", "getUsername", "()Ljava/lang/String;",
					false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
					"(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
			mv.visitLdcInsn("\",\"serverhash\":\"");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
					"(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
					"(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
			mv.visitLdcInsn("\",\"server\":\"");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
					"(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
					"(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
			mv.visitLdcInsn("\"}");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
					"(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
			mv.visitVarInsn(ASTORE, 8);
			mv.visitTypeInsn(NEW, "java/net/URL");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("http://auth.mcleaks.net/v1/joinserver");
			mv.visitMethodInsn(INVOKESPECIAL, "java/net/URL", "<init>", "(Ljava/lang/String;)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/net/URL", "openConnection", "()Ljava/net/URLConnection;", false);
			mv.visitTypeInsn(CHECKCAST, "java/net/HttpURLConnection");
			mv.visitVarInsn(ASTORE, 9);
			mv.visitVarInsn(ALOAD, 9);
			mv.visitIntInsn(SIPUSH, 10000);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/net/HttpURLConnection", "setConnectTimeout", "(I)V", false);
			mv.visitVarInsn(ALOAD, 9);
			mv.visitIntInsn(SIPUSH, 10000);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/net/HttpURLConnection", "setReadTimeout", "(I)V", false);
			mv.visitVarInsn(ALOAD, 9);
			mv.visitLdcInsn("POST");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/net/HttpURLConnection", "setRequestMethod", "(Ljava/lang/String;)V",
					false);
			mv.visitVarInsn(ALOAD, 9);
			mv.visitInsn(ICONST_1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/net/HttpURLConnection", "setDoOutput", "(Z)V", false);
			mv.visitTypeInsn(NEW, "java/io/DataOutputStream");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 9);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/net/HttpURLConnection", "getOutputStream",
					"()Ljava/io/OutputStream;", false);
			mv.visitMethodInsn(INVOKESPECIAL, "java/io/DataOutputStream", "<init>", "(Ljava/io/OutputStream;)V", false);
			mv.visitVarInsn(ASTORE, 10);
			mv.visitVarInsn(ALOAD, 10);
			mv.visitVarInsn(ALOAD, 8);
			mv.visitLdcInsn("UTF-8");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "getBytes", "(Ljava/lang/String;)[B", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/DataOutputStream", "write", "([B)V", false);
			mv.visitVarInsn(ALOAD, 10);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/DataOutputStream", "flush", "()V", false);
			mv.visitVarInsn(ALOAD, 10);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/DataOutputStream", "close", "()V", false);
			mv.visitTypeInsn(NEW, "java/io/BufferedReader");
			mv.visitInsn(DUP);
			mv.visitTypeInsn(NEW, "java/io/InputStreamReader");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 9);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/net/HttpURLConnection", "getInputStream", "()Ljava/io/InputStream;",
					false);
			mv.visitMethodInsn(INVOKESPECIAL, "java/io/InputStreamReader", "<init>", "(Ljava/io/InputStream;)V", false);
			mv.visitMethodInsn(INVOKESPECIAL, "java/io/BufferedReader", "<init>", "(Ljava/io/Reader;)V", false);
			mv.visitVarInsn(ASTORE, 11);
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
			mv.visitVarInsn(ASTORE, 12);
			Label l14 = new Label();
			mv.visitJumpInsn(GOTO, l14);
			Label l15 = new Label();
			mv.visitLabel(l15);
			mv.visitFrame(Opcodes.F_FULL, 14,
					new Object[] { "org/bitbucket/lanius/test/NetHandlerLoginClient",
							"net/minecraft/network/login/server/SPacketEncryptionRequest", "javax/crypto/SecretKey",
							"java/lang/String", "java/security/PublicKey", "java/lang/String", "java/lang/String",
							"net/minecraft/util/Session", "java/lang/String", "java/net/HttpURLConnection",
							"java/io/DataOutputStream", "java/io/BufferedReader", "java/lang/StringBuilder",
							"java/lang/String" },
					0, new Object[] {});
			mv.visitVarInsn(ALOAD, 12);
			mv.visitVarInsn(ALOAD, 13);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
					"(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
			mv.visitInsn(POP);
			mv.visitLabel(l14);
			mv.visitFrame(Opcodes.F_CHOP, 1, null, 0, null);
			mv.visitVarInsn(ALOAD, 11);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/BufferedReader", "readLine", "()Ljava/lang/String;", false);
			mv.visitInsn(DUP);
			mv.visitVarInsn(ASTORE, 13);
			mv.visitJumpInsn(IFNONNULL, l15);
			mv.visitVarInsn(ALOAD, 11);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/BufferedReader", "close", "()V", false);
			mv.visitTypeInsn(NEW, "com/google/gson/Gson");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "com/google/gson/Gson", "<init>", "()V", false);
			mv.visitVarInsn(ALOAD, 12);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
			mv.visitLdcInsn(Type.getType("Lcom/google/gson/JsonElement;"));
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/google/gson/Gson", "fromJson",
					"(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;", false);
			mv.visitTypeInsn(CHECKCAST, "com/google/gson/JsonElement");
			mv.visitVarInsn(ASTORE, 14);
			mv.visitVarInsn(ALOAD, 14);
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/google/gson/JsonElement", "isJsonObject", "()Z", false);
			Label l16 = new Label();
			mv.visitJumpInsn(IFEQ, l16);
			mv.visitVarInsn(ALOAD, 14);
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/google/gson/JsonElement", "getAsJsonObject",
					"()Lcom/google/gson/JsonObject;", false);
			mv.visitLdcInsn("success");
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/google/gson/JsonObject", "has", "(Ljava/lang/String;)Z", false);
			mv.visitJumpInsn(IFNE, l3);
			mv.visitLabel(l16);
			mv.visitFrame(Opcodes.F_APPEND, 2, new Object[] { "java/lang/String", "com/google/gson/JsonElement" }, 0,
					null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "networkManager",
					"Lnet/minecraft/network/NetworkManager;");
			mv.visitTypeInsn(NEW, "net/minecraft/util/text/TextComponentString");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("Invalid response from MCLeaks API");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/util/text/TextComponentString", "<init>",
					"(Ljava/lang/String;)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/network/NetworkManager", "closeChannel",
					"(Lnet/minecraft/util/text/ITextComponent;)V", false);
			mv.visitLabel(l1);
			mv.visitInsn(RETURN);
			mv.visitLabel(l3);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 14);
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/google/gson/JsonElement", "getAsJsonObject",
					"()Lcom/google/gson/JsonObject;", false);
			mv.visitLdcInsn("success");
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/google/gson/JsonObject", "get",
					"(Ljava/lang/String;)Lcom/google/gson/JsonElement;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/google/gson/JsonElement", "getAsBoolean", "()Z", false);
			Label l17 = new Label();
			mv.visitJumpInsn(IFNE, l17);
			mv.visitLdcInsn("Received a failing response from MCLeaks API");
			mv.visitVarInsn(ASTORE, 15);
			mv.visitVarInsn(ALOAD, 14);
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/google/gson/JsonElement", "getAsJsonObject",
					"()Lcom/google/gson/JsonObject;", false);
			mv.visitLdcInsn("errorMessage");
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/google/gson/JsonObject", "has", "(Ljava/lang/String;)Z", false);
			Label l18 = new Label();
			mv.visitJumpInsn(IFEQ, l18);
			mv.visitVarInsn(ALOAD, 14);
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/google/gson/JsonElement", "getAsJsonObject",
					"()Lcom/google/gson/JsonObject;", false);
			mv.visitLdcInsn("errorMessage");
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/google/gson/JsonObject", "get",
					"(Ljava/lang/String;)Lcom/google/gson/JsonElement;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/google/gson/JsonElement", "getAsString", "()Ljava/lang/String;",
					false);
			mv.visitVarInsn(ASTORE, 15);
			mv.visitLabel(l18);
			mv.visitFrame(Opcodes.F_APPEND, 1, new Object[] { "java/lang/String" }, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "networkManager",
					"Lnet/minecraft/network/NetworkManager;");
			mv.visitTypeInsn(NEW, "net/minecraft/util/text/TextComponentString");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 15);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/util/text/TextComponentString", "<init>",
					"(Ljava/lang/String;)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/network/NetworkManager", "closeChannel",
					"(Lnet/minecraft/util/text/ITextComponent;)V", false);
			mv.visitLabel(l4);
			mv.visitInsn(RETURN);
			mv.visitLabel(l2);
			mv.visitFrame(Opcodes.F_FULL, 7,
					new Object[] { "org/bitbucket/lanius/test/NetHandlerLoginClient",
							"net/minecraft/network/login/server/SPacketEncryptionRequest", "javax/crypto/SecretKey",
							"java/lang/String", "java/security/PublicKey", "java/lang/String", "java/lang/String" },
					1, new Object[] { "java/lang/Exception" });
			mv.visitVarInsn(ASTORE, 7);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "networkManager",
					"Lnet/minecraft/network/NetworkManager;");
			mv.visitTypeInsn(NEW, "net/minecraft/util/text/TextComponentString");
			mv.visitInsn(DUP);
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("Error whilst contacting MCLeaks API: ");
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 7);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "toString", "()Ljava/lang/String;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
					"(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/util/text/TextComponentString", "<init>",
					"(Ljava/lang/String;)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/network/NetworkManager", "closeChannel",
					"(Lnet/minecraft/util/text/ITextComponent;)V", false);
			mv.visitInsn(RETURN);
			mv.visitLabel(l13);
			mv.visitFrame(Opcodes.F_CHOP, 1, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "mc",
					"Lnet/minecraft/client/Minecraft;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "getCurrentServerData",
					"()Lnet/minecraft/client/multiplayer/ServerData;", false);
			mv.visitJumpInsn(IFNULL, l8);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "mc",
					"Lnet/minecraft/client/Minecraft;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "getCurrentServerData",
					"()Lnet/minecraft/client/multiplayer/ServerData;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/multiplayer/ServerData", "isOnLAN", "()Z", false);
			mv.visitJumpInsn(IFEQ, l8);
			mv.visitLabel(l5);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "org/bitbucket/lanius/test/NetHandlerLoginClient", "getSessionService",
					"()Lcom/mojang/authlib/minecraft/MinecraftSessionService;", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "mc",
					"Lnet/minecraft/client/Minecraft;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "getSession",
					"()Lnet/minecraft/util/Session;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/Session", "getProfile",
					"()Lcom/mojang/authlib/GameProfile;", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "mc",
					"Lnet/minecraft/client/Minecraft;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "getSession",
					"()Lnet/minecraft/util/Session;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/Session", "getToken", "()Ljava/lang/String;", false);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitMethodInsn(INVOKEINTERFACE, "com/mojang/authlib/minecraft/MinecraftSessionService", "joinServer",
					"(Lcom/mojang/authlib/GameProfile;Ljava/lang/String;Ljava/lang/String;)V", true);
			mv.visitLabel(l6);
			mv.visitJumpInsn(GOTO, l17);
			mv.visitLabel(l7);
			mv.visitFrame(Opcodes.F_SAME1, 0, null, 1,
					new Object[] { "com/mojang/authlib/exceptions/AuthenticationException" });
			mv.visitVarInsn(ASTORE, 6);
			mv.visitFieldInsn(GETSTATIC, "org/bitbucket/lanius/test/NetHandlerLoginClient", "LOGGER",
					"Lorg/apache/logging/log4j/Logger;");
			mv.visitLdcInsn("Couldn't connect to auth servers but will continue to join LAN");
			mv.visitMethodInsn(INVOKEINTERFACE, "org/apache/logging/log4j/Logger", "warn", "(Ljava/lang/String;)V",
					true);
			mv.visitJumpInsn(GOTO, l17);
			mv.visitLabel(l8);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "org/bitbucket/lanius/test/NetHandlerLoginClient", "getSessionService",
					"()Lcom/mojang/authlib/minecraft/MinecraftSessionService;", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "mc",
					"Lnet/minecraft/client/Minecraft;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "getSession",
					"()Lnet/minecraft/util/Session;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/Session", "getProfile",
					"()Lcom/mojang/authlib/GameProfile;", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "mc",
					"Lnet/minecraft/client/Minecraft;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "getSession",
					"()Lnet/minecraft/util/Session;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/Session", "getToken", "()Ljava/lang/String;", false);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitMethodInsn(INVOKEINTERFACE, "com/mojang/authlib/minecraft/MinecraftSessionService", "joinServer",
					"(Lcom/mojang/authlib/GameProfile;Ljava/lang/String;Ljava/lang/String;)V", true);
			mv.visitLabel(l9);
			mv.visitJumpInsn(GOTO, l17);
			mv.visitLabel(l10);
			mv.visitFrame(Opcodes.F_SAME1, 0, null, 1,
					new Object[] { "com/mojang/authlib/exceptions/AuthenticationUnavailableException" });
			mv.visitVarInsn(ASTORE, 6);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "networkManager",
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
			mv.visitLabel(l11);
			mv.visitFrame(Opcodes.F_SAME1, 0, null, 1,
					new Object[] { "com/mojang/authlib/exceptions/InvalidCredentialsException" });
			mv.visitVarInsn(ASTORE, 6);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "networkManager",
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
			mv.visitLabel(l12);
			mv.visitFrame(Opcodes.F_SAME1, 0, null, 1,
					new Object[] { "com/mojang/authlib/exceptions/AuthenticationException" });
			mv.visitVarInsn(ASTORE, 6);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "networkManager",
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
			mv.visitLabel(l17);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "networkManager",
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
			mv.visitTypeInsn(NEW, "org/bitbucket/lanius/test/NetHandlerLoginClient$1");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKESPECIAL, "org/bitbucket/lanius/test/NetHandlerLoginClient$1", "<init>",
					"(Lorg/bitbucket/lanius/test/NetHandlerLoginClient;Ljavax/crypto/SecretKey;)V", false);
			mv.visitInsn(ICONST_0);
			mv.visitTypeInsn(ANEWARRAY, "io/netty/util/concurrent/GenericFutureListener");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/network/NetworkManager", "sendPacket",
					"(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;[Lio/netty/util/concurrent/GenericFutureListener;)V",
					false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(11, 16);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PRIVATE, "getSessionService",
					"()Lcom/mojang/authlib/minecraft/MinecraftSessionService;", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "mc",
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
			mv.visitFieldInsn(PUTFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "gameProfile",
					"Lcom/mojang/authlib/GameProfile;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "networkManager",
					"Lnet/minecraft/network/NetworkManager;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/network/EnumConnectionState", "PLAY",
					"Lnet/minecraft/network/EnumConnectionState;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/network/NetworkManager", "setConnectionState",
					"(Lnet/minecraft/network/EnumConnectionState;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "networkManager",
					"Lnet/minecraft/network/NetworkManager;");
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/fml/common/network/internal/FMLNetworkHandler",
					"fmlClientHandshake", "(Lnet/minecraft/network/NetworkManager;)V", false);
			mv.visitTypeInsn(NEW, "net/minecraft/client/network/NetHandlerPlayClient");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "mc",
					"Lnet/minecraft/client/Minecraft;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "previousGuiScreen",
					"Lnet/minecraft/client/gui/GuiScreen;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "networkManager",
					"Lnet/minecraft/network/NetworkManager;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "gameProfile",
					"Lcom/mojang/authlib/GameProfile;");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/network/NetHandlerPlayClient", "<init>",
					"(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/GuiScreen;Lnet/minecraft/network/NetworkManager;Lcom/mojang/authlib/GameProfile;)V",
					false);
			mv.visitVarInsn(ASTORE, 2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "networkManager",
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
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "previousGuiScreen",
					"Lnet/minecraft/client/gui/GuiScreen;");
			Label l0 = new Label();
			mv.visitJumpInsn(IFNULL, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "previousGuiScreen",
					"Lnet/minecraft/client/gui/GuiScreen;");
			mv.visitTypeInsn(INSTANCEOF, "net/minecraft/client/gui/GuiScreenRealmsProxy");
			mv.visitJumpInsn(IFEQ, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "mc",
					"Lnet/minecraft/client/Minecraft;");
			mv.visitTypeInsn(NEW, "net/minecraft/realms/DisconnectedRealmsScreen");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "previousGuiScreen",
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
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "mc",
					"Lnet/minecraft/client/Minecraft;");
			mv.visitTypeInsn(NEW, "net/minecraft/client/gui/GuiDisconnected");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "previousGuiScreen",
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
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "networkManager",
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
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "networkManager",
					"Lnet/minecraft/network/NetworkManager;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/network/NetworkManager", "isLocalChannel", "()Z", false);
			Label l0 = new Label();
			mv.visitJumpInsn(IFNE, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "networkManager",
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
			mv = cw.visitMethod(ACC_STATIC + ACC_SYNTHETIC, "access$0",
					"(Lorg/bitbucket/lanius/test/NetHandlerLoginClient;)Lnet/minecraft/network/NetworkManager;", null,
					null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/bitbucket/lanius/test/NetHandlerLoginClient", "networkManager",
					"Lnet/minecraft/network/NetworkManager;");
			mv.visitInsn(ARETURN);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		cw.visitEnd();

		return cw.toByteArray();
	}
}