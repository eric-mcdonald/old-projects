package org.bitbucket.reliant.routine.impl;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JTextField;

import org.bitbucket.eric_generic.math.MathHelper;
import org.bitbucket.eric_generic.math.Vector;
import org.bitbucket.eric_generic.nio.NioUtils;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Main;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.FloatOption;
import org.bitbucket.reliant.cfg.TextureOption;
import org.bitbucket.reliant.memory.InvalidDataException;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.render.Renderer;
import org.bitbucket.reliant.routine.DisplayRoutine;
import org.bitbucket.reliant.ui.ConsolePanel;
import org.bitbucket.reliant.util.Point;
import org.bitbucket.reliant.util.SdkUtils;

public final class WaypointsRoutine extends DisplayRoutine {
	private final Map<String, Point> waypointMap = new HashMap<String, Point>();

	public WaypointsRoutine() {
		super("Waypoints", "Draws an ESP that traces to a location.", true, true, 1010, new FloatOption("Line Width", "Specifies the width of a line.", new ClampedNumber<Float>(1.0F, 0.1F, 10.0F), 0.1F), new BoolOption("Trim", "Specifies whether or not to draw an outline.", true), new BoolOption("Tracer Lines", "Specifies whether or not to draw a tracer to a waypoint.", true), new TextureOption("Texture", "Specifies the image file to use as a texture.", ""), new BoolOption("Execute Commands", "Specifies whether or not to execute a command when you reach a location.", true), new BoolOption("Bottom", "Specifies whether or not to place the first vertex of a line at the bottom of the screen, rather than in the center.", false), new BoolOption("Fade", "Specifies whether or not to make a line fade out as you move closer to a highlighted waypoint.", true));
		// TODO Auto-generated constructor stub
	}

	public void clearWaypoints() {
		waypointMap.clear();
	}
	@Override
	public int color() {
		// TODO Auto-generated method stub
		return 0xFF00FF00;
	}
	@Override
	public String info() {
		// TODO Auto-generated method stub
		return String.valueOf(waypointMap.size());
	}
	public Point putWaypoint(final String name, final Point point) {
		return waypointMap.put(name, point);
	}
	public Point removeWaypoint(final String name) {
		return waypointMap.remove(name);
	}

	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		if (!post) {
			return;
		}
		final Iterator<Map.Entry<String, Point>> waypointIt = waypointMap.entrySet().iterator();
		final float BOUNDARY = 100.0F / 2.0F;
		final boolean isBottom = getBoolean("Bottom");
		outer_loop:
			while (waypointIt.hasNext()) {
				final Map.Entry<String, Point> waypointEntry = waypointIt.next();
				final String name = waypointEntry.getKey();
				final Point point = waypointEntry.getValue();
				final Vector minsVec = new Vector(point.pos.x - BOUNDARY, point.pos.z - BOUNDARY, point.pos.y), maxsVec = new Vector(point.pos.x + BOUNDARY, point.pos.z + BOUNDARY, point.pos.y + BOUNDARY * 2.0F);
				// Eric: check for collision
				final float[] localMins = new float[3], localMaxs = new float[3];
				((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * localMins.length).put(Reliant.instance.getProcessStream().read(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_vecMins"), MemoryStream.FLOAT_SZ * localMins.length)).rewind()).asFloatBuffer().get(localMins);
				((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * localMaxs.length).put(Reliant.instance.getProcessStream().read(GameCache.getLocalPlayer() + Reliant.instance.getOffsetsRegistry().get("net_var").offset("m_vecMaxs"), MemoryStream.FLOAT_SZ * localMaxs.length)).rewind()).asFloatBuffer().get(localMaxs);
				final Vector localMinsVec = Vector.wrap(localMins), localMaxsVec = Vector.wrap(localMaxs);
				float[] playerAbsOrigin = new float[3];
				((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * playerAbsOrigin.length).put(Reliant.instance.getProcessStream().read(GameCache.getLocalPlayer() + Reliant.instance.getCustomOffsetsRegistry().get("m_vecAbsOrigin"), MemoryStream.FLOAT_SZ * playerAbsOrigin.length)).rewind()).asFloatBuffer().get(playerAbsOrigin);
				final Vector absOriginVec = Vector.wrap(playerAbsOrigin);
				localMinsVec.add(absOriginVec);
				localMaxsVec.add(absOriginVec);
				if (localMinsVec.x <= maxsVec.x && localMaxsVec.x >= minsVec.x && localMinsVec.y <= maxsVec.y && localMaxsVec.y >= minsVec.y && localMinsVec.z <= maxsVec.z && localMaxsVec.z >= minsVec.z) {
					if (Desktop.isDesktopSupported() && getBoolean("Execute Commands") && point.cmd != null) {
						final Desktop desktop = Desktop.getDesktop();
						switch (point.cmd.type) {
						case BROWSE:
							try {
								desktop.browse(new URI(point.cmd.data));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								Reliant.instance.getLogger().logError(e);
							} catch (URISyntaxException e) {
								// TODO Auto-generated catch block
								Reliant.instance.getLogger().logError(e);
							}
							break;
						case CHEAT:
							final ConsolePanel consoleGui = Reliant.instance.getConsoleGui();
							if (consoleGui != null) {
								final JTextField inputField = consoleGui.getInputField();
								inputField.setText(point.cmd.data);
								consoleGui.actionPerformed(new ActionEvent(inputField, ActionEvent.ACTION_PERFORMED, ConsolePanel.CONSOLE_IN_CMD));
							}
							break;
						case EDIT:
							try {
								desktop.edit(new File(point.cmd.data));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								Reliant.instance.getLogger().logError(e);
							} catch (final IllegalArgumentException illegalArgEx) {
								Reliant.instance.getLogger().logError(illegalArgEx);
							}
							break;
						case MAIL:
							try {
								desktop.mail(new URI(point.cmd.data));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								Reliant.instance.getLogger().logError(e);
							} catch (URISyntaxException e) {
								// TODO Auto-generated catch block
								Reliant.instance.getLogger().logError(e);
							}
							break;
						case NATIVE:
							try {
								Runtime.getRuntime().exec(point.cmd.data);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								Reliant.instance.getLogger().logError(e);
							}
							break;
						case GAME:
							Reliant.instance.clientCmdRate.unrestricted = true;
							Reliant.instance.clientCmdRate.execute(point.cmd.data, 0);
							break;
						case OPEN:
							try {
								desktop.open(new File(point.cmd.data));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								Reliant.instance.getLogger().logError(e);
							} catch (final IllegalArgumentException illegalArgEx) {
								Reliant.instance.getLogger().logError(illegalArgEx);
							}
							break;
						case PRINT:
							try {
								desktop.print(new File(point.cmd.data));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								Reliant.instance.getLogger().logError(e);
							} catch (final IllegalArgumentException illegalArgEx) {
								Reliant.instance.getLogger().logError(illegalArgEx);
							}
							break;
						default:
							throw new UnsupportedOperationException("Invalid command type specified: " + point.cmd.type);
						}
					}
					waypointIt.remove();
					Reliant.instance.getConsoleGui().appendOutput("Reached waypoint \"" + name + ".\"");
					continue;
				}
				// Eric: Render the ESP
				final int[] resolution = Reliant.instance.getRenderer().resolution();
				if (resolution == null) {
					throw new InvalidDataException("resolution");
				}
				final float[] screenPoint = Reliant.instance.getRenderer().screenPos(point.pos.data());
				float[] localAbsOrigin = new float[3];
				((ByteBuffer) NioUtils.createBuffer(MemoryStream.FLOAT_SZ * localAbsOrigin.length).put(Reliant.instance.getProcessStream().read(GameCache.getLocalPlayer() + Reliant.instance.getCustomOffsetsRegistry().get("m_vecAbsOrigin"), MemoryStream.FLOAT_SZ * localAbsOrigin.length)).rewind()).asFloatBuffer().get(localAbsOrigin);
				final float playerDist = point.pos.distance(Vector.wrap(localAbsOrigin));
				float alpha = !getBoolean("Fade") || screenPoint[3] < MathHelper.EQUALS_PRECISION ? 1.0F : playerDist / (SdkUtils.MAX_DIST / 3.0F);
				if (alpha > 1.0F) {
					alpha = 1.0F;
				}
				final float borderX, borderY;
				if (Reliant.instance.getRenderer().hasBorder(Main.TARGET_WINDOW_CLASS)) {
					borderX = Renderer.BORDER_WIDTH;
					borderY = Renderer.BORDER_HEIGHT;
				} else {
					borderX = borderY = 0.0F;
				}
				final float centerX = resolution[0] / 2.0F + borderX / 2.0F, y1 = isBottom ? resolution[1] : resolution[1] / 2.0F + borderY;
				final float lineWidth = getFloat("Line Width");
				if ((!isBottom || screenPoint[3] >= MathHelper.EQUALS_PRECISION) && getBoolean("Tracer Lines")) {
					Reliant.instance.getRenderer().drawLine(centerX, y1, screenPoint[0], screenPoint[1], new Color(1.0F, 0.0F, 1.0F, alpha).getRGB(), lineWidth);
				}
				final float[][] screenPos = new float[][] {Reliant.instance.getRenderer().screenPos(new float[] {maxsVec.x, minsVec.z, minsVec.y}), Reliant.instance.getRenderer().screenPos(new float[] {minsVec.x, maxsVec.z, maxsVec.y}), Reliant.instance.getRenderer().screenPos(new float[] {minsVec.x, minsVec.z, minsVec.y}), Reliant.instance.getRenderer().screenPos(new float[] {maxsVec.x, maxsVec.z, maxsVec.y}), Reliant.instance.getRenderer().screenPos(new float[] {maxsVec.x, maxsVec.z, minsVec.y}), Reliant.instance.getRenderer().screenPos(new float[] {minsVec.x, maxsVec.z, minsVec.y}), Reliant.instance.getRenderer().screenPos(new float[] {minsVec.x, minsVec.z, maxsVec.y}), Reliant.instance.getRenderer().screenPos(new float[] {maxsVec.x, minsVec.z, maxsVec.y})};
				for (final float[] pos : screenPos) {
					if (pos == null || pos[3] < MathHelper.EQUALS_PRECISION) {
						continue outer_loop;
					}
				}
				float left = screenPos[0][0], top = screenPos[0][1], right = screenPos[0][0], bottom = screenPos[0][1];
				for (int posIdx = 1; posIdx < screenPos.length; posIdx++) {
					if (left > screenPos[posIdx][0]) {
						left = screenPos[posIdx][0];
					}
					if (top > screenPos[posIdx][1]) {
						top = screenPos[posIdx][1];
					}
					if (right < screenPos[posIdx][0]) {
						right = screenPos[posIdx][0];
					}
					if (bottom < screenPos[posIdx][1]) {
						bottom = screenPos[posIdx][1];
					}
				}
				boolean notInScreen = true;
				for (final float[] boxPoint : new float[][] {new float[] {left, top}, new float[] {left, bottom}, new float[] {right, top}, new float[] {right, bottom}}) {
					if (Reliant.instance.getRenderer().inScreen(boxPoint)) {
						notInScreen = false;
						break;
					}
				}
				if (notInScreen) {
					continue;
				}
				final int COLOR = 0xFFFF00FF;
				final long texture = getTexture("Texture");
				if (texture != MemoryStream.NULL) {
					Reliant.instance.getRenderer().drawQuad(left, top, right, bottom, COLOR, texture);
				}
				Reliant.instance.getRenderer().drawOutline(left, top, right - left, bottom - top, COLOR, lineWidth);
				final String font = Reliant.instance.getWorldFont();
				final int FONT_SZ = 12;
				final boolean SHADOW = true;
				final int SPACING = 2;
				final int[] nameSz = Reliant.instance.getRenderer().textSize(name, font, FONT_SZ, SHADOW);
				if (nameSz != null) {
					final float outlineWidth = lineWidth - Math.min(playerDist / SdkUtils.MAX_VIS_RANGE, lineWidth);
					final boolean outline = getBoolean("Trim");
					final int midWidth = nameSz[0] / 2;
					final int yOffset = nameSz[1] + 4;
					final float outlinedX1 = outline ? (left + right) / 2.0F - midWidth - SPACING - outlineWidth : (left + right) / 2.0F - midWidth - SPACING, outlinedX2 = outline ? (left + right) / 2.0F + midWidth + SPACING - 1 + outlineWidth : (left + right) / 2.0F + midWidth + SPACING - 1;
					final float outlinedY1 = outline ? top - yOffset - outlineWidth : top - yOffset, outlinedY2 = outline ? top - 4.0F + outlineWidth - 1.0F : top - 4.0F - 1.0F;
					if (Reliant.instance.getRenderer().drawQuad(outlinedX1, outlinedY1, outlinedX2, outlinedY2, 0x80000000, MemoryStream.NULL)) {
						Reliant.instance.getRenderer().drawText(name, (int) ((left + right) / 2.0F - midWidth), (int) top - yOffset, 0xFFFFFFFF, font, FONT_SZ, SHADOW);
						if (outline) {
							Reliant.instance.getRenderer().drawOutline(outlinedX1, outlinedY1, outlinedX2 - outlinedX1, outlinedY2 - outlinedY1, 0xFF000000, outlineWidth);
						}
					}
				}
			}
	}
}
