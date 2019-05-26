package net.hackforums.pootpoot.monumental.util;

import net.hackforums.pootpoot.monumental.Monumental;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class MessageUtils {

	public static void addMessage(IChatComponent message, MessageType messageType) {
		EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
		if (thePlayer != null) {
			thePlayer.addChatMessage(new ChatComponentText("[\2473" + Monumental.NAME + "\247r] [").appendSibling(messageType.getName()).appendText("] ").appendSibling(message.createCopy()));
		}
	}
}
