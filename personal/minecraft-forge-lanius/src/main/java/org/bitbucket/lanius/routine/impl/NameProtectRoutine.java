package org.bitbucket.lanius.routine.impl;

import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST_FUNC;
import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST_REF;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW_MATRIX;
import static org.lwjgl.opengl.GL11.GL_PROJECTION_MATRIX;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_VIEWPORT;
import static org.lwjgl.opengl.GL11.glGetFloat;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cfg.Configurable;
import org.bitbucket.lanius.gui.Tab;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.hook.impl.TabOverlayData;
import org.bitbucket.lanius.routine.Renderable;
import org.bitbucket.lanius.routine.RoutineHandler;
import org.bitbucket.lanius.routine.TabbedRoutine;
import org.bitbucket.lanius.util.InventoryUtils;
import org.bitbucket.lanius.util.NetworkUtils;
import org.bitbucket.lanius.util.Phase;
import org.bitbucket.lanius.util.ReflectHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.glu.GLU;

import com.google.common.collect.Sets;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public final class NameProtectRoutine extends TabbedRoutine implements Hook<TabOverlayData>, Configurable, Renderable {

	private static final String CLIENT_PLAYER = "me";

	private static final File protectFile = new File(Lanius.dataDir, "name_protect.cfg");

	private final FloatBuffer modelviewMatrix = GLAllocation.createDirectFloatBuffer(16),
			projectionMatrix = GLAllocation.createDirectFloatBuffer(16),
			windowPos = GLAllocation.createDirectFloatBuffer(3);

	private final Map<String, String> nameMap = new HashMap<String, String>();

	public final Hook<NetHandlerData> netHook = new Hook<NetHandlerData>() {

		@Override
		public void onExecute(final NetHandlerData data, final Phase phase) {
			// TODO Auto-generated method stub
			if (!phase.equals(Phase.START) || !isEnabled()) {
				return;
			}
			if (data.retVal instanceof SPacketCombatEvent) {
				final SPacketCombatEvent combatEvPacket = (SPacketCombatEvent) data.retVal;
				if (combatEvPacket.eventType != SPacketCombatEvent.Event.ENTITY_DIED
						|| Lanius.mc.world.getEntityByID(combatEvPacket.entityId) != Lanius.mc.player) {
					return;
				}
				String deathTxt = combatEvPacket.deathMessage.getUnformattedText();
				for (final Entry<String, String> nameEntry : nameEntries()) {
					deathTxt = deathTxt.replace(nameEntry.getKey(), nameEntry.getValue());
				}
				combatEvPacket.deathMessage = new TextComponentString(deathTxt)
						.setStyle(combatEvPacket.deathMessage.getStyle());
			} else if (data.retVal instanceof CPacketChatMessage) {
				final CPacketChatMessage chatMsgPacket = (CPacketChatMessage) data.retVal;
				for (final Entry<String, String> nameEntry : nameEntries()) {
					ObfuscationReflectionHelper.setPrivateValue(CPacketChatMessage.class, chatMsgPacket,
							chatMsgPacket.getMessage().replace("-" + nameEntry.getValue(), nameEntry.getKey()),
							"field_149440_a", "message");
				}
			}
		}

	};

	private final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);

	public NameProtectRoutine() {
		super(Keyboard.KEY_NONE, true, Tab.RENDER);
		// TODO Auto-generated constructor stub
	}

	public void clearTags() {
		nameMap.clear();
	}

	@Override
	public HashSet<Compatibility> compatibleWith() {
		// TODO Auto-generated method stub
		return Sets.newHashSet(Compatibility.NOCHEATPLUS, Compatibility.VIAVERSION, Compatibility.NO_VIAVERSION);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Hides the selected players' name with its corresponding alias.";
	}

	public String getAlias(final String name) {
		return nameMap.get(StringUtils.stripControlCodes(name));
	}

	@Override
	public void load() {
		nameMap.clear();
		if (!protectFile.exists()) {
			nameMap.put(Lanius.mc.getSession().getUsername(), "Chuck Knoblock");
			return;
		}
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(protectFile));
			String line;
			try {
				while ((line = in.readLine()) != null) {
					final int colonIdx = line.indexOf(":");
					final String name = line.substring(0, colonIdx);
					nameMap.put(name.equals(CLIENT_PLAYER) ? Lanius.mc.getSession().getUsername() : name,
							line.substring(colonIdx + 1));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Name Protect";
	}

	public Set<Entry<String, String>> nameEntries() {
		return nameMap.entrySet();
	}

	/**
	 * Ported from Lanius v1.0.
	 * 
	 * @param clientChatReceivedEv
	 */
	@SubscribeEvent
	public void onClientChatReceived(final ClientChatReceivedEvent clientChatReceivedEv) {
		final ITextComponent messageComponent = clientChatReceivedEv.getMessage();
		boolean replacedKey = false;
		if (messageComponent instanceof TextComponentTranslation) {
			final TextComponentTranslation translationMsg = (TextComponentTranslation) messageComponent;
			final Object[] oldFormatArgs = translationMsg.getFormatArgs(),
					newFormatArgs = new Object[oldFormatArgs.length];
			for (int argIdx = 0; argIdx < oldFormatArgs.length; argIdx++) {
				final Object oldFormatArg = oldFormatArgs[argIdx];
				String text = null;
				for (final Entry<String, String> nameEntry : nameEntries()) {
					String formattedMsg = text != null ? text
							: oldFormatArg instanceof ITextComponent
									? ((ITextComponent) oldFormatArg).getFormattedText()
									: "" + oldFormatArg;
					final String name = nameEntry.getKey(), alias = nameEntry.getValue();
					boolean replaced = false;
					for (int nameIdx = formattedMsg.indexOf(name); nameIdx >= 0; nameIdx--) {
						if (formattedMsg.substring(nameIdx, nameIdx + 1).equals("\247")) {
							text = formattedMsg.replace(name,
									"\2473" + alias + formattedMsg.substring(nameIdx, nameIdx + 2));
							replaced = true;
							replacedKey = true;
							break;
						}
					}
					if (!replaced && formattedMsg.contains(name)) {
						text = formattedMsg.replace(name, "\2473" + alias + "\247r");
						replacedKey = true;
					}
				}
				if (text != null) {
					if (oldFormatArg instanceof ITextComponent) {
						TextComponentString ct = new TextComponentString(text);
						ct.setStyle(((ITextComponent) oldFormatArg).getStyle().createShallowCopy());
						newFormatArgs[argIdx] = ct;
					} else {
						newFormatArgs[argIdx] = text;
					}
				}
				if (newFormatArgs[argIdx] == null) {
					newFormatArgs[argIdx] = oldFormatArg;
				}
			}
			if (replacedKey) {
				for (final Object newFormatArg : newFormatArgs) {
					if (newFormatArg != null) {
						final TextComponentTranslation newMessage = new TextComponentTranslation(
								translationMsg.getKey(), newFormatArgs);
						newMessage.getStyle().setColor(messageComponent.getStyle().getColor());
						clientChatReceivedEv.setMessage(newMessage);
						break;
					}
				}
			}
		} else if (messageComponent instanceof TextComponentString) {
			String message = null;
			for (final Entry<String, String> nameEntry : nameEntries()) {
				String formattedMsg = message != null ? message : messageComponent.getFormattedText();
				final String name = nameEntry.getKey(), alias = nameEntry.getValue();
				boolean replaced = false;
				for (int nameIdx = formattedMsg.indexOf(name); nameIdx >= 0; nameIdx--) {
					if (formattedMsg.substring(nameIdx, nameIdx + 1).equals("\247")) {
						message = formattedMsg.replace(name,
								"\2473" + alias + formattedMsg.substring(nameIdx, nameIdx + 2));
						replaced = true;
						replacedKey = true;
						break;
					}
				}
				if (!replaced && formattedMsg.contains(name)) {
					message = formattedMsg.replace(name, "\2473" + alias + "\247r");
					replacedKey = true;
				}
			}
			if (message != null && replacedKey) {
				clientChatReceivedEv.setMessage(new TextComponentString(message));
			}
		}
		final ClickEvent clickEv = messageComponent.getStyle().getClickEvent();
		final String[] valueMappings = new String[] { "field_150670_b", "value" };
		if (clickEv != null && clickEv.getAction().equals(ClickEvent.Action.SUGGEST_COMMAND)) {
			for (final Entry<String, String> nameEntry : nameEntries()) {
				ReflectHelper.setValue(ClickEvent.class, clickEv,
						clickEv.getValue().replace(nameEntry.getKey(), "-" + nameEntry.getValue()), valueMappings);
			}
		}
		if (messageComponent instanceof TextComponentTranslation) {
			for (final Object formattedArg : ((TextComponentTranslation) messageComponent).getFormatArgs()) {
				if (!(formattedArg instanceof ITextComponent)) {
					continue;
				}
				final ClickEvent argEv = ((ITextComponent) formattedArg).getStyle().getClickEvent();
				if (argEv != null && argEv.getAction().equals(ClickEvent.Action.SUGGEST_COMMAND)) {
					for (final Entry<String, String> nameEntry : nameEntries()) {
						ReflectHelper.setValue(ClickEvent.class, argEv,
								argEv.getValue().replace(nameEntry.getKey(), "-" + nameEntry.getValue()),
								valueMappings);
					}
				}
			}
		}
	}

	@Override
	public void onExecute(final TabOverlayData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (!phase.equals(Phase.START) || !isEnabled()) {
			return;
		}
		final ScorePlayerTeam playerTeam = data.playerInfo.getPlayerTeam();
		final String formattedName = ScorePlayerTeam.formatPlayerName(playerTeam,
				data.playerInfo.getGameProfile().getName()), alias = getAlias(formattedName);
		if (alias == null && (data.playerInfo.getDisplayName() == null
				|| getAlias(data.playerInfo.getDisplayName().getFormattedText()) == null)) {
			return;
		}
		final String formattedAlias = ScorePlayerTeam.formatPlayerName(playerTeam, alias);
		data.retVal = data.playerInfo.getDisplayName() != null
				? getAlias(data.playerInfo.getDisplayName().getFormattedText()) != null
						? new TextComponentString(getAlias(data.playerInfo.getDisplayName().getFormattedText()))
								.setStyle(data.playerInfo.getDisplayName().getStyle()).getFormattedText()
						: data.playerInfo.getDisplayName().getFormattedText()
				: formattedAlias != null && !StringUtils.stripControlCodes(formattedAlias).equals("null")
						? formattedAlias
						: formattedName;
	}

	@SubscribeEvent
	public void onRenderSpecialsPre(final RenderLivingEvent.Specials.Pre renderSpecialsPre) {
		if (!(renderSpecialsPre.getEntity() instanceof EntityOtherPlayerMP)) {
			return;
		}
		renderSpecialsPre.setCanceled(true);
	}

	private void project(final float objX, final float objY, final float objZ) {
		windowPos.rewind();
		GLU.gluProject(objX, objY, objZ, modelviewMatrix, projectionMatrix, viewport, windowPos);
	}

	public String putTag(final String name, final String alias) {
		return nameMap.put(StringUtils.stripControlCodes(name), alias);
	}

	@Override
	public void registerValues() {
		final String displayName = name();
		registerValue("Invisible Players", true,
				"Determines whether or not " + displayName + " should put name tags above invisible players.");
		registerValue("Entity Age", 0, 0, 200, "Specifies how long " + displayName
				+ " will wait before rendering a name tag on a recently spawned entity.");
		registerValue("Custom Tags", true, "Determines whether or not custom name tags should be used.");
		registerValue("Scale", 9.0F, 0.0F, 10.0F, "Specifies the value to scale the name tags by.");
		registerValue("Scale Focused", false,
				"Determines whether or not to only scale the name tags of entities that are over the cursor.");
		registerValue("Focus Width", 5.0F, 0.1F, 20.0F, "Determines the width of the focus.");
		registerValue("Focus Height", 5.0F, 0.1F, 20.0F, "Determines the height of the focus.");
		registerValue("Armor", false,
				"Determines whether or not to only render a name tag on a player that has armor equipped.");
		registerValue("Held Item", false,
				"Determines whether or not to only render a name tag on a player that is holding an item.");
	}

	public String removeAlias(final String name) {
		return nameMap.remove(StringUtils.stripControlCodes(name));
	}

	private void renderEquipment(final EntityPlayer player, byte yOffset) {
		yOffset += 5;
		final Iterable<ItemStack> equipment = player.getEquipmentAndArmor();
		int equipCount = 0;
		Iterator<ItemStack> equipmentIt = equipment.iterator();
		while (equipmentIt.hasNext()) {
			final ItemStack stack = equipmentIt.next();
			if (!InventoryUtils.isStackValid(stack)) {
				continue;
			}
			++equipCount;
		}
		final int spaceWidth = Lanius.mc.fontRenderer.getCharWidth(' ');
		int x = -equipCount * (20 + spaceWidth) / 2;
		final int y = -20 + yOffset;
		equipmentIt = equipment.iterator();
		glPushMatrix();
		final float X_TRANSLATE = 0.0F, yTranslate = y / 2;
		glTranslatef(X_TRANSLATE, yTranslate, 0.0F);
		glScalef(0.65F, 0.65F, 0.65F);
		glTranslatef(-X_TRANSLATE, -yTranslate, 0.0F);
		while (equipmentIt.hasNext()) {
			if (!Lanius.getInstance().getGuiHandler().renderStack(equipmentIt.next(), x, y, true)) {
				continue;
			}
			x += 21 + spaceWidth;
		}
		glPopMatrix();
	}

	@Override
	public void renderEsp(final Entity entity, final float partialTicks) {
		final Entity viewEntity = Lanius.mc.getRenderViewEntity();
		final double viewX = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * partialTicks,
				viewY = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * partialTicks,
				viewZ = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * partialTicks;
		final boolean packetSneak = ((SneakRoutine) Lanius.getInstance().getRoutineRegistry().get("Sneak"))
				.isSneaking();
		final EntityOtherPlayerMP renderEntity = ((FreecamRoutine) Lanius.getInstance().getRoutineRegistry()
				.get("Freecam")).getRenderEntity(),
				posEntity = ((BlinkRoutine) Lanius.getInstance().getRoutineRegistry().get("Blink")).getPosEntity();
		final boolean invisPlayers = getBoolean("Invisible Players"), armor = getBoolean("Armor"),
				heldItem = getBoolean("Held Item");
		final int entityAge = getInt("Entity Age").intValue(),
				lagAge = entityAge + (entityAge <= 0 ? 0 : NetworkUtils.lagTicks());
		final RoutineHandler routineHandler = Lanius.getInstance().getRoutineHandler();
		final FreecamRoutine freecamRoutine = (FreecamRoutine) Lanius.getInstance().getRoutineRegistry().get("Freecam");
		final double camX = freecamRoutine.getPosX(), camY = freecamRoutine.getPosY(), camZ = freecamRoutine.getPosZ();
		if (!(entity instanceof EntityOtherPlayerMP) || entity.equals(renderEntity) || entity.equals(posEntity)) {
			return;
		}
		final EntityOtherPlayerMP player = (EntityOtherPlayerMP) entity;
		if (player.isInvisible() && !invisPlayers || !player.isEntityAlive()
				|| routineHandler.entityAge(player) < lagAge || armor && !InventoryUtils.hasArmor(player)
				|| heldItem && !InventoryUtils.hasHeldItem(player)) {
			return;
		}
		final double playerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks,
				playerY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks,
				playerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks,
				renderX = playerX - viewX, renderY = playerY - viewY, renderZ = playerZ - viewZ;
		final RenderManager renderManager = Lanius.mc.getRenderManager();
		if (renderManager.renderViewEntity == null) {
			return; // Eric: hotfix for a crash when trying to connect to
			// play.mcgamer.net
		}
		final double distanceSq = player.getDistanceSq(renderManager.renderViewEntity);
		String entityName = player.getDisplayName().getFormattedText();
		final String alias = getAlias(entityName);
		final boolean hasAlias = alias != null;
		if (hasAlias) {
			entityName = alias;
		}
		final int prevFunc = glGetInteger(GL_ALPHA_TEST_FUNC);
		final float prevRef = glGetFloat(GL_ALPHA_TEST_REF);
		GlStateManager.alphaFunc(516, 0.1F);
		final FontRenderer fontrenderer = Lanius.mc.fontRenderer;
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder worldrenderer = tessellator.getBuffer();
		final float playerAbsorption = player.getAbsorptionAmount(),
				playerHealth = player.getHealth() + playerAbsorption, displayHealth = playerHealth / 2.0F;
		final int flooredHealth = MathHelper.floor(displayHealth);
		final String healthStr = " " + (displayHealth == flooredHealth ? String.valueOf(flooredHealth)
				: String.format("%,.1f", displayHealth)), heartStr = " " + Character.toString('\u2764');
		final boolean customTags = getBoolean("Custom Tags");
		if (customTags) {
			RenderHelper.disableStandardItemLighting();
			Lanius.mc.entityRenderer.disableLightmap();
		} else {
			Lanius.mc.entityRenderer.enableLightmap();
		}
		final int X1 = 1, X2 = 1, Y1 = -1, y2 = customTags ? 9 : 8;
		float scaleFactor = 1.0F;
		final float MIN_SCALE = -0.025F, cfgScale = getFloat("Scale").floatValue(), multiplier = -MIN_SCALE * cfgScale;
		final boolean scaleFocused = getBoolean("Scale Focused");
		final float focusHeight = getFloat("Focus Height").floatValue();
		// Eric: check for code efficiency
		if (scaleFocused) {
			modelviewMatrix.rewind();
			projectionMatrix.rewind();
			viewport.rewind();
			GlStateManager.pushMatrix();
			GlStateManager.translate(renderX, renderY, renderZ);
			glNormal3f(0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			GlStateManager.translate(-renderX, -renderY, -renderZ);
			glGetFloat(GL_MODELVIEW_MATRIX, modelviewMatrix);
			glGetFloat(GL_PROJECTION_MATRIX, projectionMatrix);
			glGetInteger(GL_VIEWPORT, viewport);
			GlStateManager.popMatrix();
			project((float) (org.bitbucket.lanius.util.math.MathHelper.interpolate(player.posX, player.lastTickPosX,
					partialTicks)
					- org.bitbucket.lanius.util.math.MathHelper.interpolate(viewEntity.posX, viewEntity.lastTickPosX,
							partialTicks)),
					(float) (org.bitbucket.lanius.util.math.MathHelper.interpolate(player.posY, player.lastTickPosY,
							partialTicks)
							+ player.height / 2.0F
							- org.bitbucket.lanius.util.math.MathHelper.interpolate(viewEntity.posY,
									viewEntity.lastTickPosY, partialTicks)),
					(float) (org.bitbucket.lanius.util.math.MathHelper.interpolate(player.posZ, player.lastTickPosZ,
							partialTicks)
							- org.bitbucket.lanius.util.math.MathHelper.interpolate(viewEntity.posZ,
									viewEntity.lastTickPosZ, partialTicks)));
		}
		final float centerX = windowPos.get(0), centerY = windowPos.get(1);
		final ScaledResolution resolution = new ScaledResolution(Lanius.mc);
		final int scaleFac = resolution.getScaleFactor();
		final float scaleRadius = getFloat("Focus Width").floatValue() / 2.0F;
		if (scaleFocused) {
			project((float) (org.bitbucket.lanius.util.math.MathHelper.interpolate(player.posX, player.lastTickPosX,
					partialTicks)
					- scaleRadius
					- org.bitbucket.lanius.util.math.MathHelper.interpolate(viewEntity.posX, viewEntity.lastTickPosX,
							partialTicks)),
					(float) (org.bitbucket.lanius.util.math.MathHelper.interpolate(player.posY, player.lastTickPosY,
							partialTicks)
							- focusHeight / 2.0F + player.height / 2.0F
							- org.bitbucket.lanius.util.math.MathHelper.interpolate(viewEntity.posY,
									viewEntity.lastTickPosY, partialTicks)),
					(float) (org.bitbucket.lanius.util.math.MathHelper.interpolate(player.posZ, player.lastTickPosZ,
							partialTicks)
							- org.bitbucket.lanius.util.math.MathHelper.interpolate(viewEntity.posZ,
									viewEntity.lastTickPosZ, partialTicks)));
		}
		final float negX = windowPos.get(0), negY = windowPos.get(1);
		if (scaleFocused) {
			project((float) (org.bitbucket.lanius.util.math.MathHelper.interpolate(player.posX, player.lastTickPosX,
					partialTicks)
					+ scaleRadius
					- org.bitbucket.lanius.util.math.MathHelper.interpolate(viewEntity.posX, viewEntity.lastTickPosX,
							partialTicks)),
					(float) (org.bitbucket.lanius.util.math.MathHelper.interpolate(player.posY, player.lastTickPosY,
							partialTicks)
							+ focusHeight / 2.0F + player.height / 2.0F
							- org.bitbucket.lanius.util.math.MathHelper.interpolate(viewEntity.posY,
									viewEntity.lastTickPosY, partialTicks)),
					(float) (org.bitbucket.lanius.util.math.MathHelper.interpolate(player.posZ, player.lastTickPosZ,
							partialTicks)
							- org.bitbucket.lanius.util.math.MathHelper.interpolate(viewEntity.posZ,
									viewEntity.lastTickPosZ, partialTicks)));
		}
		final boolean scaleEffects = cfgScale > 0.0F
				&& (!scaleFocused || org.bitbucket.lanius.util.math.MathHelper.distance(
						resolution.getScaledWidth_double() / 2.0D * scaleFac, 0.0D, 0.0D, centerX, 0.0D,
						0.0D) <= org.bitbucket.lanius.util.math.MathHelper.distance(negX, 0.0D, 0.0D, windowPos.get(0),
								0.0D, 0.0D) / 2.0D
						&& org.bitbucket.lanius.util.math.MathHelper.distance(0.0D,
								resolution.getScaledHeight_double() / 2.0D * scaleFac, 0.0D, 0.0D, centerY,
								0.0D) <= org.bitbucket.lanius.util.math.MathHelper.distance(0.0D, negY, 0.0D, 0.0D,
										windowPos.get(1), 0.0D) / 2.0D);
		try {
			final int midAverage = Lanius.mc.fontRenderer.getStringWidth("0123456789") / 2; // TODO(Eric)
			// This
			// literally
			// makes
			// no
			// sense.
			// Better
			// implementation?
			scaleFactor = (float) (org.bitbucket.lanius.util.math.MathHelper.distance(viewEntity.posX + camX,
					viewEntity.posY + viewEntity.getEyeHeight() + camY, viewEntity.posZ
							+ camZ,
					player.posX, player.posY, player.posZ)
					* Math.sin(
							(Float) ReflectionHelper.findMethod(EntityRenderer.class, "getFOVModifier", "func_78481_a",
									Float.TYPE, Boolean.TYPE).invoke(Lanius.mc.entityRenderer, partialTicks, true)
									* Math.PI / 180.0D / 2.0D)
					/ (org.bitbucket.lanius.util.math.MathHelper.distance(-midAverage - X1, Y1, 0.0D, midAverage + X2,
							y2, 0.0D) / 2.0D));
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final float scale = scaleEffects && multiplier * -scaleFactor < MIN_SCALE ? multiplier * -scaleFactor
				: MIN_SCALE;
		final boolean entitySneak = player.isSneaking();
		final int txtColor = hasAlias ? 43690
				: distanceSq >= 4096.0D ? 65280
						: entitySneak ? 16711680
								: (viewEntity.isSneaking() || packetSneak) && !player.canEntityBeSeen(viewEntity)
										? 16776960
										: -1;
		int healthRed = (int) (13 * (player.getMaxHealth() - playerHealth)), healthGreen = (int) (13 * playerHealth);
		if (healthRed < 0) {
			healthRed = 0;
		} else if (healthRed > 255) {
			healthRed = 255;
		}
		if (healthGreen < 0) {
			healthGreen = 0;
		} else if (healthGreen > 255) {
			healthGreen = 255;
		}
		final float rootedDist = MathHelper
				.sqrt(org.bitbucket.lanius.util.math.MathHelper.distance(viewEntity.posX + camX, viewEntity.posY + camY,
						viewEntity.posZ + camZ, player.posX, player.posY, player.posZ));
		final int midName = Math
				.round(fontrenderer.getStringWidth(entityName + (customTags ? healthStr + heartStr : "")) / 2.0F),
				heartCol = player.hurtTime > 0 || player.deathTime > 0 ? 0x848484
						: playerAbsorption > 0.0F ? 0xFFFF00 : 0xFF0000;
		if (entitySneak) {
			GlStateManager.pushMatrix();
			if (customTags) {
				GlStateManager.disableDepth(); // Eric: added
			}
			GlStateManager.translate(
					(float) renderX, (float) renderY + player.height + 0.5F
							- (player.isChild() ? player.height / 2.0F : 0.0F) - (customTags ? 0.0F : 0.25F),
					(float) renderZ);
			glNormal3f(0.0F, 1.0F, 0.0F);
			/*
			 * GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			 * GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
			 */
			GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate((renderManager.options.thirdPersonView == 2 ? -1 : 1) * renderManager.playerViewX,
					1.0F, 0.0F, 0.0F);
			GlStateManager.scale(scale, scale, -scale);
			GlStateManager.translate(0.0F, 9.374999F, 0.0F);
			GlStateManager.depthMask(false);
			GlStateManager.enableBlend();
			GlStateManager.disableTexture2D();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			worldrenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			byte yOffset = (byte) (-Math
					.round(scaleEffects && multiplier * -scaleFactor < MIN_SCALE ? rootedDist : 0.0F)
					- (scaleEffects ? multiplier * -scaleFactor < MIN_SCALE ? 10.0F : 0.0F : 4.0F));
			if (entityName.equals("deadmau5")) {
				yOffset += -10;
			}
			final float ALPHA = 0.25F;
			worldrenderer.pos(-midName - X1, Y1 + yOffset, 0.0D).color(0.0F, 0.0F, 0.0F, ALPHA).endVertex();
			worldrenderer.pos(-midName - X1, y2 + yOffset, 0.0D).color(0.0F, 0.0F, 0.0F, ALPHA).endVertex();
			worldrenderer.pos(midName + X2, y2 + yOffset, 0.0D).color(0.0F, 0.0F, 0.0F, ALPHA).endVertex();
			worldrenderer.pos(midName + X2, Y1 + yOffset, 0.0D).color(0.0F, 0.0F, 0.0F, ALPHA).endVertex();
			tessellator.draw();
			GlStateManager.enableTexture2D();
			if (!customTags) {
				GlStateManager.depthMask(true);
			}
			if (customTags) {
				fontrenderer.drawStringWithShadow(entityName, -midName, yOffset,
						!customTags ? 553648127 : txtColor /*
															 * Eric: added
															 */);
			} else {
				fontrenderer.drawString(entityName, -fontrenderer.getStringWidth(entityName) / 2, yOffset,
						!customTags ? 553648127 : txtColor /*
															 * Eric: added
															 */);
			}
			if (customTags) {
				fontrenderer.drawStringWithShadow(healthStr, -midName + fontrenderer.getStringWidth(entityName),
						yOffset, (healthRed << 16) | (healthGreen << 8));
				fontrenderer.drawStringWithShadow(heartStr,
						-midName + fontrenderer.getStringWidth(entityName) + fontrenderer.getStringWidth(healthStr),
						yOffset, heartCol);
				renderEquipment(player, (byte) (yOffset - 1));
				GlStateManager.depthMask(true); // Eric: added
				GlStateManager.enableDepth(); // Eric: added
			}
			GlStateManager.disableBlend();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.popMatrix();
		} else {
			/*
			 * final Class<?> rendererCls = renderSpecialsEv.renderer.getClass(); final
			 * String[] remappedNames =
			 * ReflectHelper.remapMethodNames(rendererCls.getName(), "(L" +
			 * EntityLivingBase.class.getName().replace('.', '/') + ";DDD)V)",
			 * "func_177069_a"); // TODO(Eric) mappings methods: for (final Method method :
			 * rendererCls.getDeclaredMethods()) { final String methodName =
			 * method.getName(); for (final String remappedName : remappedNames) { if
			 * (remappedName.equals(methodName)) { method.setAccessible(true); try {
			 * method.invoke(renderSpecialsEv.entity, renderSpecialsEv.y -
			 * (renderSpecialsEv.entity.isChild() ? (double)(renderSpecialsEv.entity.height
			 * / 2.0F) : 0.0D), renderSpecialsEv.z, entityName, 0.02666667F, distanceSq); }
			 * catch (IllegalAccessException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } catch (IllegalArgumentException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); } catch
			 * (InvocationTargetException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } break methods; } } }
			 */
			GlStateManager.pushMatrix();
			GlStateManager.translate((float) renderX + 0.0F, (float) renderY + player.height + 0.5F, (float) renderZ);
			glNormal3f(0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
			GlStateManager.scale(scale, scale, -scale);
			GlStateManager.depthMask(false);
			GlStateManager.disableDepth();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			byte yOffset = (byte) (-Math
					.round(scaleEffects && multiplier * -scaleFactor < MIN_SCALE ? rootedDist : 0.0F));
			if (entityName.equals("deadmau5")) {
				yOffset += -10;
			}
			GlStateManager.disableTexture2D();
			worldrenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			worldrenderer.pos(-midName - X1, Y1 + yOffset, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			worldrenderer.pos(-midName - X1, y2 + yOffset, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			worldrenderer.pos(midName + X2, y2 + yOffset, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			worldrenderer.pos(midName + X2, Y1 + yOffset, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			tessellator.draw();
			GlStateManager.enableTexture2D();
			if (customTags) {
				fontrenderer.drawStringWithShadow(entityName, -midName, yOffset, txtColor); // Eric:
				// added
				fontrenderer.drawStringWithShadow(healthStr, -midName + fontrenderer.getStringWidth(entityName),
						yOffset, (healthRed << 16) | (healthGreen << 8));
				fontrenderer.drawStringWithShadow(heartStr,
						-midName + fontrenderer.getStringWidth(entityName) + fontrenderer.getStringWidth(healthStr),
						yOffset, heartCol);
				renderEquipment(player, (byte) (yOffset - 1));
			} else {
				fontrenderer.drawString(entityName, -fontrenderer.getStringWidth(entityName) / 2, yOffset, 553648127);
			}
			GlStateManager.enableDepth();
			GlStateManager.depthMask(true);
			if (!customTags) {
				fontrenderer.drawString(entityName, -fontrenderer.getStringWidth(entityName) / 2, yOffset, -1);
			}
			GlStateManager.disableBlend();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.popMatrix();
		}
		if (!customTags) {
			Lanius.mc.entityRenderer.disableLightmap();
		}
		GlStateManager.alphaFunc(prevFunc, prevRef);
	}

	@Override
	public void save() {
		protectFile.delete();
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter((new FileWriter(protectFile))));
			for (final Entry<String, String> nameEntry : nameEntries()) {
				final String key = nameEntry.getKey();
				out.println((key.equals(Lanius.mc.getSession().getUsername()) ? CLIENT_PLAYER : key) + ":"
						+ nameEntry.getValue());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

}
