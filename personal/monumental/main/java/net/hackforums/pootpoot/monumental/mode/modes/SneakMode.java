package net.hackforums.pootpoot.monumental.mode.modes;

import net.hackforums.pootpoot.monumental.Monumental;
import net.hackforums.pootpoot.monumental.init.Modes;
import net.hackforums.pootpoot.monumental.mode.BaseMode;
import net.hackforums.pootpoot.monumental.mode.DisplayText;
import net.hackforums.pootpoot.monumental.mode.Displayable;
import net.hackforums.pootpoot.monumental.mode.EventHandler;
import net.hackforums.pootpoot.monumental.util.MessageType;
import net.hackforums.pootpoot.monumental.util.MessageUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import org.lwjgl.input.Keyboard;

public class SneakMode extends BaseMode implements Displayable, EventHandler {

	private DisplayText displayText = new DisplayText(getName(), 0x00FF00);
	private boolean sneaking;
	private double prevPosX, prevPosY, prevPosZ;
	public SneakMode() {
		super("Sneak", "Hides the player's name from other players", new KeyBinding("Sneak", Keyboard.KEY_Z, Monumental.NAME));
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean bypassesNoCheatPlus() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public DisplayText getDisplayText() {
		// TODO Auto-generated method stub
		return displayText;
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
		if (thePlayer != null && !thePlayer.isSneaking()) {
			setSneaking(false);
		}
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		super.onEnable();
		EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
		if (thePlayer != null && shouldSneak(thePlayer) && !thePlayer.isSneaking()) {
			setSneaking(true);
		}
	}

	@Override
	public void onEvent(Event event) {
		// TODO Auto-generated method stub
		if (!isEnabled()) {
			return;
		}
		if (event instanceof InputEvent) {
			GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
			if (gameSettings.keyBindSneak.isKeyDown()) {
				sneaking = false;
			}
			EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
			if (Modes.NOCHEATPLUS_MODE.isEnabled() && gameSettings.keyBindUseItem.isKeyDown() && thePlayer != null) {
				ItemStack currentItem = thePlayer.inventory.getCurrentItem();
				if (currentItem != null) {
					Item item = currentItem.getItem();
					if ((item == Items.bow && (thePlayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, currentItem) > 0 || thePlayer.inventory.hasItem(Items.arrow))) || item == Items.ender_pearl || item == Items.ender_eye || item == Items.egg || item == Items.snowball || item == Items.experience_bottle || (item == Items.potionitem && ItemPotion.isSplash(currentItem.getItemDamage()))) {
						ObfuscationReflectionHelper.setPrivateValue(KeyBinding.class, gameSettings.keyBindUseItem, 0, "i", "pressTime");
						KeyBinding.setKeyBindState(gameSettings.keyBindUseItem.getKeyCode(), false);
						MessageUtils.addMessage(getName().appendText(": cannot use the player's current item because mode ").appendSibling(Modes.NOCHEATPLUS_MODE.getName()).appendText(" is enabled"), MessageType.ERROR);
					}
				}
			}
		}
		else if (event instanceof PlayerEvent.PlayerChangedDimensionEvent || event instanceof PlayerEvent.PlayerLoggedOutEvent || event instanceof PlayerEvent.PlayerRespawnEvent) {
			sneaking = false;
			prevPosX = 0.0D;
			prevPosY = 0.0D;
			prevPosZ = 0.0D;
		}
		else if (event instanceof TickEvent) {
			EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
			if (event instanceof TickEvent.PlayerTickEvent) {
				TickEvent.PlayerTickEvent playerTickEvent = (PlayerTickEvent) event;
				EntityPlayer player = playerTickEvent.player;
				if (player.equals(thePlayer)) {
					double deltaX = player.posX - prevPosX, deltaY = player.posY - prevPosY, deltaZ = player.posZ - prevPosZ;
					if (playerTickEvent.phase.equals(Phase.START) && deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ > 9.0E-4D) {
						if (Modes.NOCHEATPLUS_MODE.isEnabled() && !player.capabilities.isCreativeMode && !player.isSneaking()) {
							setSneaking(false);
						}
						prevPosX = player.posX;
						prevPosY = player.posY;
						prevPosZ = player.posZ;
					}
				}
			}
			else if (event instanceof TickEvent.ClientTickEvent && ((TickEvent) event).phase.equals(Phase.END) && thePlayer != null && shouldSneak(thePlayer) && !thePlayer.isSneaking()) {
				setSneaking(true);
			}
		}
	}

	private void setSneaking(boolean sneaking) {
		EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
		if (thePlayer != null) {
			if (!this.sneaking && sneaking) {
				thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
			}
			else if (this.sneaking && !sneaking) {
				thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
			}
			this.sneaking = sneaking;
		}
	}

	private boolean shouldSneak(EntityPlayer player) {
		if (Minecraft.getMinecraft().theWorld != null) {
			for (Object o : Minecraft.getMinecraft().theWorld.playerEntities) {
				if (o instanceof EntityOtherPlayerMP && ((EntityOtherPlayerMP) o).getDistanceToEntity(player) < 64.0F) {
					return true;
				}
			}
		}
		return false;
	}

}
