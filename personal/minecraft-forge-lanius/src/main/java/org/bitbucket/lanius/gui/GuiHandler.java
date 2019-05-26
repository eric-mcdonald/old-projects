package org.bitbucket.lanius.gui;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cfg.ConfigContainer;
import org.bitbucket.lanius.cmd.impl.TimersCommand;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.impl.RenderEffectData;
import org.bitbucket.lanius.routine.Routine;
import org.bitbucket.lanius.util.InventoryUtils;
import org.bitbucket.lanius.util.NetworkUtils;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.concurrent.Timer;
import org.lwjgl.input.Keyboard;

import net.minecraft.block.material.Material;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class GuiHandler extends ConfigContainer implements Hook<RenderEffectData> {
	private boolean cancelEffect;
	private int selectedTab;

	@Override
	public String category() {
		// TODO Auto-generated method stub
		return "HUD";
	}

	@SubscribeEvent
	public void onClientTick(final TickEvent.ClientTickEvent clientTickEv) {
		if (!clientTickEv.phase.equals(TickEvent.Phase.START) || Lanius.mc.player == null
				|| Lanius.mc.currentScreen != null) {
			return;
		}
		ClickGui.instance.updateScreen();
	}

	@Override
	public void onExecute(final RenderEffectData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (!phase.equals(Phase.START) || !cancelEffect) {
			return;
		}
		data.retVal = true;
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onGuiOpenHighest(final GuiOpenEvent guiOpenEv) {
		if (guiOpenEv.isCanceled()) {
			return;
		}
		if (guiOpenEv.getGui() instanceof GuiMainMenu) {
			guiOpenEv.setGui(new GuiMainMenu() {
				@Override
				protected void actionPerformed(GuiButton button) throws IOException {
					super.actionPerformed(button);
					if (button.id == 15) {
						mc.displayGuiScreen(new LoginGui(this));
					}
				}

				@Override
				public void drawScreen(int mouseX, int mouseY, float partialTicks) {
					super.drawScreen(mouseX, mouseY, partialTicks);
					final String loginStr = "Logged in as " + Lanius.mc.getSession().getUsername();
					drawString(fontRenderer, loginStr, width - fontRenderer.getStringWidth(loginStr) - 2, 1, -1);
				}

				@Override
				public void initGui() {
					super.initGui();
					final GuiButton multiplayerBtn = buttonList.get(1);
					final int newBtnWidth = multiplayerBtn.width / 2;
					multiplayerBtn.x += newBtnWidth;
					multiplayerBtn.setWidth(newBtnWidth);
					buttonList
							.add(new GuiButton(15, multiplayerBtn.x - newBtnWidth, multiplayerBtn.y, 98, 20, "Login"));
					final int X_OFF = 2;
					multiplayerBtn.x += X_OFF;
					multiplayerBtn.setWidth(multiplayerBtn.width - X_OFF);
				}
			});
		}
	}

	@SubscribeEvent
	public void onKeyInput(final InputEvent.KeyInputEvent keyInEv) {
		final int evKey = Keyboard.getEventKey() == Keyboard.KEY_NONE
				? Keyboard.getEventCharacter() + Keyboard.KEYBOARD_SIZE
				: Keyboard.getEventKey();
		if (Keyboard.getEventKeyState() && evKey == Keyboard.getKeyIndex(getString("Click GUI Key"))) {
			Lanius.mc.displayGuiScreen(ClickGui.instance);
		}
		if (Keyboard.getEventKeyState() && getBoolean("Tab GUI") && !Lanius.mc.gameSettings.showDebugInfo
				&& Lanius.mc.currentScreen == null) {
			final Tab[] tabs = Tab.values();
			final int tabsLen = tabs.length;
			if (evKey == Keyboard.KEY_DOWN) {
				if (tabs[selectedTab].open) {
					++tabs[selectedTab].routineIdx;
					if (tabs[selectedTab].routineIdx >= tabs[selectedTab].routines().length) {
						tabs[selectedTab].routineIdx = 0;
					}
				} else {
					++selectedTab;
					if (selectedTab >= tabsLen) {
						selectedTab = 0;
					}
				}
			} else if (evKey == Keyboard.KEY_UP) {
				if (tabs[selectedTab].open) {
					--tabs[selectedTab].routineIdx;
					if (tabs[selectedTab].routineIdx < 0) {
						tabs[selectedTab].routineIdx = tabs[selectedTab].routines().length - 1;
					}
				} else {
					--selectedTab;
					if (selectedTab < 0) {
						selectedTab = tabsLen - 1;
					}
				}
			} else if (evKey == Keyboard.KEY_RIGHT || evKey == Keyboard.KEY_RETURN) {
				if (tabs[selectedTab].open) {
					tabs[selectedTab].routines()[tabs[selectedTab].routineIdx].setEnabled();
				} else {
					tabs[selectedTab].open = true;
				}
			} else if (evKey == Keyboard.KEY_LEFT) {
				tabs[selectedTab].open = false;
			}
		}
	}

	@SubscribeEvent
	public void onRenderGameChat(final RenderGameOverlayEvent.Chat renderGameChatEv) {
		if (Lanius.mc.playerController.shouldDrawHUD() && Lanius.mc.getRenderViewEntity() instanceof EntityPlayer
				&& getBoolean("Items")) {
			final int maxHealth = MathHelper
					.ceil((float) ((Lanius.mc.player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH)
							.getAttributeValue() + Lanius.mc.player.getAbsorptionAmount()) / 2.0F / 10.0F));
			final ScaledResolution resolution = renderGameChatEv.getResolution();
			int y = resolution.getScaledHeight() - 39 - (maxHealth - 1) * Math.max(10 - (maxHealth - 2), 3) - 16;
			if (Lanius.mc.player.isInsideOfMaterial(Material.WATER)) {
				y -= 10;
			}
			final ItemStack mainItem = Lanius.mc.player.getHeldItemMainhand(),
					offItem = Lanius.mc.player.getHeldItemOffhand();
			final Iterator<ItemStack> equipmentIt = Lanius.mc.player.getEquipmentAndArmor().iterator();
			int equipCount = 0;
			final int spaceWidth = Lanius.mc.fontRenderer.getCharWidth(' ');
			while (equipmentIt.hasNext()) {
				final ItemStack stack = equipmentIt.next();
				if (InventoryUtils.isStackValid(stack)) {
					if (stack == mainItem || stack == offItem) {
						continue;
					}
					renderStackGui(stack,
							resolution.getScaledWidth() / 2 + 91 - equipCount * (21 + spaceWidth) + 2 - 17, y, true);
					++equipCount;
				}
			}
			y = resolution.getScaledHeight() - 16 - 3;
			int compassX = resolution.getScaledWidth() / 2 - 90 + -1 * 20 + 2,
					clockX = resolution.getScaledWidth() / 2 - 90 + 9 * 20 + 2;
			if (InventoryUtils.isStackValid(offItem)) {
				final int OFF_BOX_WIDTH = 29;
				if (Lanius.mc.player.getPrimaryHand().opposite() == EnumHandSide.LEFT) {
					compassX -= OFF_BOX_WIDTH;
				} else {
					clockX += OFF_BOX_WIDTH;
				}
			}
			renderStackGui(new ItemStack(Items.COMPASS), compassX, y, false);
			renderStackGui(new ItemStack(Items.CLOCK), clockX, y, false);
			GlStateManager.enableDepth();
			GlStateManager.depthMask(true);
		}
	}

	@SubscribeEvent
	public void onRenderGameText(final RenderGameOverlayEvent.Pre.Text renderGameTxtEv) {
		if (!renderGameTxtEv.getLeft().isEmpty() || !renderGameTxtEv.getRight().isEmpty()) {
			return;
		}
		final int TXT_SPACING = 2;
		int tabY = TXT_SPACING;
		if (getBoolean("Watermark")) {
			Lanius.mc.fontRenderer.drawStringWithShadow("\247c" + Lanius.NAME + " \2477" + Lanius.VERSION, TXT_SPACING,
					TXT_SPACING, 0);
			tabY += 1 + Lanius.mc.fontRenderer.FONT_HEIGHT;
		}
		if (getBoolean("Ping") && !Lanius.mc.isSingleplayer()) {
			int ping = NetworkUtils.lagTime(Lanius.mc.player.connection.getPlayerInfo(Lanius.mc.player.getName()));
			float pingPercent = ping / 1000.0F;
			if (pingPercent < 0.0F) {
				pingPercent = 0.0F;
			}
			if (pingPercent > 1.0F) {
				pingPercent = 1.0F;
			}
			Color pingCol = new Color(pingPercent, 1.0F - pingPercent, 0.0F);
			Lanius.mc.fontRenderer.drawStringWithShadow(String.valueOf(ping) + " \247fms", TXT_SPACING,
					renderGameTxtEv.getResolution().getScaledHeight() - Lanius.mc.fontRenderer.FONT_HEIGHT - 1,
					(pingCol.getRed() << 16) | (pingCol.getGreen() << 8));
		}
		if (getBoolean("Tab GUI")) {
			int tabMaxX = 0;
			for (final Tab guiTab : Tab.values()) {
				final int right = TXT_SPACING + 1 + Lanius.mc.fontRenderer.getStringWidth("> " + guiTab.name);
				if (right > tabMaxX) {
					tabMaxX = right;
				}
			}
			int selectedMaxX = 0;
			for (final Tab guiTab : Tab.values()) {
				if (guiTab.open) {
					for (final Routine routine : guiTab.routines()) {
						final int right = TXT_SPACING + tabMaxX + TXT_SPACING
								+ Lanius.mc.fontRenderer.getStringWidth("> " + routine.name());
						if (right > selectedMaxX) {
							selectedMaxX = right;
						}
					}
				}
			}
			int tabIdx = 0, selectedY = tabY;
			for (final Tab guiTab : Tab.values()) {
				final int bottom = tabY + 1 + Lanius.mc.fontRenderer.FONT_HEIGHT;
				final Color color = new Color(guiTab.color),
						tabCol = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0x80);
				Gui.drawRect(TXT_SPACING, tabY, TXT_SPACING + tabMaxX, bottom,
						tabIdx == selectedTab ? tabCol.getRGB() : 0x80202020);
				Lanius.mc.fontRenderer.drawStringWithShadow(guiTab.open ? "> " + guiTab.name : guiTab.name,
						TXT_SPACING + TXT_SPACING, 1 + tabY, 16777215);
				tabY += bottom - tabY;
				if (guiTab.open) {
					int routineIdx = 0;
					for (final Routine routine : guiTab.routines()) {
						final int selectBottom = selectedY + 1 + Lanius.mc.fontRenderer.FONT_HEIGHT;
						final Color color1 = new Color(guiTab.color),
								selectCol = new Color(color1.getRed(), color1.getGreen(), color1.getBlue(), 0x80);
						Gui.drawRect(TXT_SPACING + tabMaxX + 1, selectedY, TXT_SPACING + selectedMaxX, selectBottom,
								routineIdx == guiTab.routineIdx ? selectCol.getRGB() : 0x80202020);
						Lanius.mc.fontRenderer.drawStringWithShadow(
								routine.isEnabled() ? "> " + routine.name() : routine.name(),
								TXT_SPACING + tabMaxX + 1 + TXT_SPACING, 1 + selectedY, 16777215);
						selectedY += selectBottom - selectedY;
						++routineIdx;
					}
				}
				++tabIdx;
			}
		}
		final ScaledResolution resolution = renderGameTxtEv.getResolution();
		final int betweenSpacing = TXT_SPACING / 2, heightSpacing = Lanius.mc.fontRenderer.FONT_HEIGHT + betweenSpacing;
		int height = resolution.getScaledHeight() - betweenSpacing;
		final List<Timer> timers = new ArrayList<Timer>(
				((TimersCommand) Lanius.getInstance().getCmdRegistry().get("timers")).timers);
		final List<Routine> routines = Lanius.getInstance().getRoutineRegistry().objects();
		final boolean enabledRoutines = getBoolean("Enabled Routines");
		if (enabledRoutines) {
			for (final Routine routine : routines) {
				final int routineCol = routine.color();
				if (!routine.isEnabled() || routineCol == Routine.NO_COLOR || routine.isHidden()) {
					continue;
				}
				height -= heightSpacing;
			}
		}
		for (final Timer timer : timers) {
			height -= heightSpacing;
		}
		if (enabledRoutines && !routines.isEmpty()) {
			Collections.sort(routines, new Comparator<Routine>() {

				@Override
				public int compare(Routine arg0, Routine arg1) {
					// TODO Auto-generated method stub
					final int width1 = Lanius.mc.fontRenderer
							.getStringWidth(StringUtils.isNullOrEmpty(arg0.displayData()) ? arg0.name()
									: arg0.name() + " [" + arg0.displayData() + "]"),
							width2 = Lanius.mc.fontRenderer
									.getStringWidth(StringUtils.isNullOrEmpty(arg1.displayData()) ? arg1.name()
											: arg1.name() + " [" + arg1.displayData() + "]");
					return width1 < width2 ? -1 : width1 == width2 ? 0 : 1;
				}

			});
			for (final Routine routine : routines) {
				final int routineCol = routine.color();
				if (!routine.isEnabled() || routineCol == Routine.NO_COLOR || routine.isHidden()) {
					continue;
				}
				final String routineData = routine.displayData();
				String routineName = routine.name();
				if (!StringUtils.isNullOrEmpty(routineData)) {
					routineName += " [" + routineData + "]";
				}
				Lanius.mc.fontRenderer.drawStringWithShadow(routineName,
						resolution.getScaledWidth() - Lanius.mc.fontRenderer.getStringWidth(routineName) - TXT_SPACING,
						height, routineCol);
				height += heightSpacing;
			}
		}
		if (!timers.isEmpty()) {
			Collections.sort(timers, new Comparator<Timer>() {

				@Override
				public int compare(Timer o1, Timer o2) {
					// TODO Auto-generated method stub
					final int width1 = Lanius.mc.fontRenderer.getStringWidth(o1.name),
							width2 = Lanius.mc.fontRenderer.getStringWidth(o2.name);
					return width1 < width2 ? -1 : width1 == width2 ? 0 : 1;
				}

			});
			for (final Timer timer : timers) {
				final int hours = timer.getHours(), minutes = timer.getMinutes(), seconds = timer.getSeconds();
				final String text = timer.name + " [" + (hours < 10 ? "0" + hours : hours) + ":"
						+ (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds)
						+ "]";
				Lanius.mc.fontRenderer.drawStringWithShadow(text,
						resolution.getScaledWidth() - Lanius.mc.fontRenderer.getStringWidth(text) - TXT_SPACING, height,
						16777215);
				height += heightSpacing;
			}
		}
		// TODO(Eric): This is rendering over the Items HUD. Consider moving it to Items
		// HUD rendering.
		ClickGui.instance.drawPinned(renderGameTxtEv.getPartialTicks());
	}

	@Override
	public void registerValues() {
		registerValue("Watermark", false, "Determines whether or not to display the watermark in the HUD.");
		registerValue("Enabled Routines", true,
				"Determines whether or not to display the enabled routines in the HUD.");
		registerValue("Tab GUI", true, "Determines whether or not to display the tab GUI in the HUD.");
		registerValue("Click GUI Key", Keyboard.getKeyName(Keyboard.KEY_GRAVE),
				"Specifies the key that should be used to open the click GUI.");
		registerValue("Items", true, "Determines whether or not to render items into the HUD.");
		registerValue("Ping", true, "Determines whether or not to display your ping in the HUD.");
	}

	public boolean renderStack(final ItemStack stack, final int x, final int y, final boolean drawRarity) {
		if (!InventoryUtils.isStackValid(stack)) {
			return false;
		}
		GlStateManager.enableRescaleNormal();
		final RenderItem itemRenderer = ObfuscationReflectionHelper.getPrivateValue(GuiIngame.class,
				Lanius.mc.ingameGUI, "field_73841_b", "itemRenderer");
		final float prevZ = itemRenderer.zLevel;
		itemRenderer.zLevel = -150.0F;
		RenderHelper.enableGUIStandardItemLighting();
		cancelEffect = true;
		itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
		cancelEffect = false;
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GlStateManager.depthMask(false);
		GlStateManager.enableAlpha();
		GlStateManager.disableLighting();
		itemRenderer.renderItemOverlays(Lanius.mc.fontRenderer, stack, x, y);
		GlStateManager.disableDepth();
		GlStateManager.disableLighting();
		if (drawRarity) {
			glPushMatrix();
			final EnumRarity rarity = stack.getRarity();
			final String rarityName = " " + rarity.rarityColor + rarity.rarityName.toLowerCase().substring(0, 3);
			final float xTrans = x + Lanius.mc.fontRenderer.getStringWidth(rarityName);
			glTranslatef(xTrans, y, 0.0F);
			glScalef(0.5F, 0.5F, 0.5F);
			glTranslatef(-xTrans, -y, 0.0F);
			Lanius.mc.fontRenderer.drawStringWithShadow(rarityName, x, y, 16777215);
			glPopMatrix();
		}
		if (stack.hasTagCompound()) {
			final NBTTagList enchTags = stack.getEnchantmentTagList();
			if (enchTags != null) {
				final Map<Enchantment, Short> enchMap = new HashMap<Enchantment, Short>();
				for (int tagIdx = 0, enchCount = 0; tagIdx < enchTags.tagCount() && enchCount < 2; ++tagIdx) {
					Enchantment enchant = Enchantment
							.getEnchantmentByID(enchTags.getCompoundTagAt(tagIdx).getShort("id"));
					if (enchant != null) {
						enchMap.put(enchant, enchTags.getCompoundTagAt(tagIdx).getShort("lvl"));
						++enchCount;
					}
				}
				if (!enchMap.isEmpty()) {
					final List<Enchantment> enchants = new ArrayList<Enchantment>(enchMap.keySet());
					Collections.sort(enchants, new Comparator<Enchantment>() {

						@Override
						public int compare(Enchantment o1, Enchantment o2) {
							// TODO Auto-generated method stub
							final int enchId1 = Enchantment.getEnchantmentID(o1),
									enchId2 = Enchantment.getEnchantmentID(o2);
							return enchId1 < enchId2 ? -1 : enchId1 == enchId2 ? 0 : 1;
						}

					});
					int enchY = y;
					for (final Enchantment enchant : enchants) {
						glPushMatrix();
						glTranslatef(x, enchY, 0.0F);
						glScalef(0.5F, 0.5F, 0.5F);
						glTranslatef(-x, -enchY, 0.0F);
						Lanius.mc.fontRenderer.drawStringWithShadow(
								I18n.translateToLocal(enchant.getName()).toLowerCase().substring(0, 2)
										+ enchMap.get(enchant),
								x, enchY, 16777215);
						glPopMatrix();
						enchY += Lanius.mc.fontRenderer.FONT_HEIGHT / 2 + 1;
					}
				}
			}
		}
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		RenderHelper.disableStandardItemLighting();
		Lanius.mc.entityRenderer.disableLightmap();
		itemRenderer.zLevel = prevZ;
		GlStateManager.disableRescaleNormal();
		return true;
	}

	private boolean renderStackGui(final ItemStack stack, final int x, final int y, final boolean drawRarity) {
		GlStateManager.disableDepth();
		return renderStack(stack, x, y, drawRarity);
	}
}
