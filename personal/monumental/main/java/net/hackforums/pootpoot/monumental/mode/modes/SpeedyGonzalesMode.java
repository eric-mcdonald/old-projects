package net.hackforums.pootpoot.monumental.mode.modes;

import net.hackforums.pootpoot.monumental.Monumental;
import net.hackforums.pootpoot.monumental.init.Commands;
import net.hackforums.pootpoot.monumental.init.Modes;
import net.hackforums.pootpoot.monumental.mode.BaseMode;
import net.hackforums.pootpoot.monumental.mode.DisplayText;
import net.hackforums.pootpoot.monumental.mode.Displayable;
import net.hackforums.pootpoot.monumental.mode.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

import org.lwjgl.input.Keyboard;

public class SpeedyGonzalesMode extends BaseMode implements Displayable, EventHandler {

	private DisplayText displayText = new DisplayText(getName(), 0xFF8000);
	private int prevItem = -1;
	private boolean usedItemFaster;
	public SpeedyGonzalesMode() {
		super("Speedy Gonzales", "Allows the player to use items faster, mine blocks faster, and place blocks faster", new KeyBinding("Speedy Gonzales", Keyboard.KEY_G, Monumental.NAME));
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
		prevItem = -1;
		usedItemFaster = false;
	}

	@Override
	public void onEvent(Event event) {
		// TODO Auto-generated method stub
		if (!isEnabled()) {
			return;
		}
		if (event instanceof PlayerEvent.PlayerChangedDimensionEvent || event instanceof PlayerEvent.PlayerLoggedOutEvent || event instanceof PlayerEvent.PlayerRespawnEvent) {
			prevItem = -1;
			usedItemFaster = false;
		}
		else if (event instanceof TickEvent.ClientTickEvent) {
			TickEvent.ClientTickEvent clientTickEvent = (ClientTickEvent) event;
			Minecraft minecraft = Minecraft.getMinecraft();
			if (clientTickEvent.phase.equals(Phase.START) && (Integer) ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, minecraft, "al", "rightClickDelayTimer") == 3) {
				ObfuscationReflectionHelper.setPrivateValue(Minecraft.class, minecraft, Commands.SPEEDY_GONZALES_COMMAND.getPlaceSpeed(), "al", "rightClickDelayTimer");
			}
			else if (clientTickEvent.phase.equals(Phase.END) && Minecraft.getMinecraft().thePlayer != null) {
				EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
				PlayerControllerMP playerController = Minecraft.getMinecraft().playerController;
				boolean isHittingBlock = !thePlayer.capabilities.isCreativeMode && playerController != null && (Boolean) ObfuscationReflectionHelper.getPrivateValue(PlayerControllerMP.class, playerController, "h", "isHittingBlock");
				if (isHittingBlock) {
					int bestSlotId = -1;
					float bestStrength = 1.0F;
					Block currentBlock = Minecraft.getMinecraft().theWorld.getBlockState((BlockPos) ObfuscationReflectionHelper.getPrivateValue(PlayerControllerMP.class, playerController, "c", "field_178895_c")).getBlock();
					for (int slotId = 9; slotId < 45; slotId++) {
						ItemStack stack = thePlayer.inventoryContainer.getSlot(slotId).getStack();
						if (stack != null) {
							float strength = stack.getStrVsBlock(currentBlock);
							if (thePlayer.inventoryContainer.getSlot(slotId).getHasStack() && strength > bestStrength) {
								bestSlotId = slotId;
								bestStrength = strength;
							}
						}
					}
					if (bestSlotId != -1) {
						if (thePlayer.openContainer != thePlayer.inventoryContainer) {
							thePlayer.closeScreen();
						}
						if (prevItem == -1) {
							prevItem = thePlayer.inventory.currentItem;
						}
						if (bestSlotId < 36) {
							int currentItem = -1;
							for (int newSlotId = 36; newSlotId < 45; newSlotId++) {
								if (!thePlayer.inventoryContainer.getSlot(newSlotId).getHasStack()) {
									currentItem = newSlotId - 36;
									break;
								}
							}
							playerController.windowClick(thePlayer.openContainer.windowId, bestSlotId, 0, 1, thePlayer);
							if (currentItem != -1) {
								setCurrentItem(currentItem);
							}
						}
						else {
							setCurrentItem(bestSlotId - 36);
						}
					}
				}
				if ((Integer) ObfuscationReflectionHelper.getPrivateValue(PlayerControllerMP.class, playerController, "g", "blockHitDelay") == 5) {
					ObfuscationReflectionHelper.setPrivateValue(PlayerControllerMP.class, playerController, Modes.NOCHEATPLUS_MODE.isEnabled() ? thePlayer.capabilities.isCreativeMode ? 2 : 1 : 0, "g", "blockHitDelay");
				}
				if (isHittingBlock && (Float) ObfuscationReflectionHelper.getPrivateValue(PlayerControllerMP.class, playerController, "e", "curBlockDamageMP") >= Commands.SPEEDY_GONZALES_COMMAND.getBreakThreshold()) {
					ObfuscationReflectionHelper.setPrivateValue(PlayerControllerMP.class, playerController, 1.0F, "e", "curBlockDamageMP");
				}
				else if (!isHittingBlock && prevItem != -1) {
					if (!Modes.NOCHEATPLUS_MODE.isEnabled()) {
						setCurrentItem(prevItem);
					}
					prevItem = -1;
				}
				ItemStack currentItem = thePlayer.inventory.getCurrentItem();
				if (thePlayer.isUsingItem() && (thePlayer.capabilities.isCreativeMode || thePlayer.worldObj.checkBlockCollision(thePlayer.getEntityBoundingBox().expand(0.0625D, 0.0625D, 0.0625D).addCoord(0.0D, -0.55D, 0.0D))) && currentItem != null && (currentItem.getItem() instanceof ItemFood || currentItem.getItem() instanceof ItemPotion) && (!usedItemFaster || !Modes.NOCHEATPLUS_MODE.isEnabled())) {
					int packets = Modes.NOCHEATPLUS_MODE.isEnabled() ? currentItem.getMaxItemUseDuration() / 4 : currentItem.getMaxItemUseDuration();
					for (int packetCount = 0; packetCount < packets; packetCount++) {
						thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(thePlayer.onGround));
					}
					usedItemFaster = true;
				}
				else if (!thePlayer.isUsingItem()) {
					usedItemFaster = false;
				}
			}
		}
	}

	private void setCurrentItem(int currentItem) {
		EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
		if (currentItem != thePlayer.inventory.currentItem) {
			thePlayer.inventory.currentItem = currentItem;
			thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(currentItem));
		}
	}
}
