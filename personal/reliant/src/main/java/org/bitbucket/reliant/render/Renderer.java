package org.bitbucket.reliant.render;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.reliant.CreateD3dOverlayException;
import org.bitbucket.reliant.GameCache;
import org.bitbucket.reliant.Main;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.memory.InvalidDataException;
import org.bitbucket.reliant.memory.MemoryStream;

public final class Renderer {
	public static final int BORDER_WIDTH = 6, BORDER_HEIGHT = 26;
	private static final int VERTEX_FVF = 0x0004 | 0x0040, VERTEX_FVF_TEX = 0x0004 | 0x0040 | 0x0100;
	public static final int SHADOW_OFF = 2;
	//public static final float LINE_WIDTH = 1.0F;
	private final long d3dDevice, line, vertexBuffer, vertexTexBuffer;
	private final Map<Integer, Long> fontMap = new HashMap<Integer, Long>();
	private final Map<String, Long> textureMap = new HashMap<String, Long>();

	public Renderer(final long d3dDevice) throws CreateD3dOverlayException {
		if (d3dDevice == MemoryStream.NULL) {
			throw new CreateD3dOverlayException("D3D device cannot be null.");
		}
		this.d3dDevice = d3dDevice;
		if ((line = createLine()) == MemoryStream.NULL) {
			throw new CreateD3dOverlayException("Line cannot be null.");
		}
		if ((vertexBuffer = createVertexBuffer(6, VERTEX_FVF)) == MemoryStream.NULL) {
			throw new CreateD3dOverlayException("Vertex buffer cannot be null.");
		}
		if ((vertexTexBuffer = createVertexBuffer(6, VERTEX_FVF_TEX)) == MemoryStream.NULL) {
			throw new CreateD3dOverlayException("Vertex texture buffer cannot be null.");
		}
	}

	public void begin() {
		begin(d3dDevice);
	}
	private native void begin(final long d3dDevice);
	private long createFont(final int height, final int weight, final boolean italic, final String facename) {
		return createFont(d3dDevice, height, weight, italic, facename);
	}
	private native long createFont(final long d3dDevice, final int height, final int weight, final boolean italic, final String facename);
	private long createLine() {
		return createLine(d3dDevice);
	}
	private native long createLine(final long d3dDevice);
	private native long createTexture(final long d3dDevice, final String file);
	public long createTexture(String file) {
		if (StringUtils.empty(file)) {
			return MemoryStream.NULL;
		}
		if (file.startsWith("/")) {
			try {
				file = new File(Renderer.class.getResource(file).toURI()).getPath();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				Reliant.instance.getLogger().logError(e);
			}
		}
		if (!textureMap.containsKey(file)) {
			final long texture = createTexture(d3dDevice, file);
			if (texture != MemoryStream.NULL) { // Eric: Do not add the texture to the map if it is invalid
				textureMap.put(file, texture);
			}
		}
		return textureMap.containsKey(file) ? textureMap.get(file) : MemoryStream.NULL;
	}
	private long createVertexBuffer(final int length, final int fvf) {
		return createVertexBuffer(d3dDevice, length, fvf);
	}
	private native long createVertexBuffer(final long d3dDevice, final int length, final int fvf);
	private boolean drawLine(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2, final float w, final int color, final float width) {
		return shouldDraw() && drawLine(x1, y1, z1, x2, y2, z2, w, color, d3dDevice, vertexBuffer, VERTEX_FVF, line, width);
	}
	private native boolean drawLine(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2, final float w, final int color, final long d3dDevice, final long vertexBuffer, final int vertexFvf, final long line, final float width);
	public boolean drawLine(final float x1, final float y1, final float x2, final float y2, final int color, final float width) {
		return drawLine(x1, y1, 0.0F, x2, y2, 0.0F, 1.0F, color, width);
	}
	private boolean drawOutline(final float x, final float y, final float z, final float w, final float width, final float height, final int color, final float lineWidth) {
		final float x2 = x + width, y2 = y + height;
		return drawLine(x, y, z, x2, y, z, w, color, lineWidth) && drawLine(x, y2, z, x2, y2, z, w, color, lineWidth) && drawLine(x, y, z, x, y2, z, w, color, lineWidth) && drawLine(x2, y, z, x2, y2, z, w, color, lineWidth);
	}
	public boolean drawOutline(final float x, final float y, final float width, final float height, final int color, final float lineWidth) {
		return drawOutline(x, y, 0.0F, 1.0F, width, height, color, lineWidth);
	}
	private boolean drawQuad(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2, final float w, final int color, final long texture) {
		return shouldDraw() && drawQuad(x1, y1, z1, x2, y2, z2, w, color, d3dDevice, texture == MemoryStream.NULL ? vertexBuffer : vertexTexBuffer, texture == MemoryStream.NULL ? VERTEX_FVF : VERTEX_FVF_TEX, texture);
	}
	//private native boolean drawTriangle(final float x1, final float y1, final float z1, final float base, final float height, final float w, final int color, final long d3dDevice, final long vertexBuffer, final int vertexFvf, final long texture);
	/*public boolean drawTriangle(final float x1, final float y1, final float base, final float height, final int color, final long texture) {
		return shouldDraw() && drawTriangle(x1, y1, 0.0F, base, height, 1.0F, color, d3dDevice, texture == MemoryStream.NULL ? vertexBuffer : vertexTexBuffer, texture == MemoryStream.NULL ? VERTEX_FVF : VERTEX_FVF_TEX, texture);
	}*/
	private native boolean drawQuad(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2, final float w, final int color, final long d3dDevice, final long vertexBuffer, final int vertexFvf, final long texture);
	public boolean drawQuad(final float x1, final float y1, final float x2, final float y2, final int color, final long texture) {
		return drawQuad(x1, y1, 0.0F, x2, y2, 0.0F, 1.0F, color, texture);
	}
	private native int drawText(final long font, final String text, final int x, final int y, final int color);
	public int drawText(final String text, final int x, final int y, final int color, final String fontName, final int fontSize, final boolean shadow) {
		if (!shouldDraw()) {
			return 0;
		}
		if (!fontMap.containsKey(fontSize)) {
			final long font = createFont(fontSize, 0, false, fontName);
			if (font == MemoryStream.NULL) {
				return 0;
			}
			fontMap.put(fontSize, font);
		}
		final long font = fontMap.get(fontSize);
		if (shadow) {
			drawText(font, text, x + SHADOW_OFF, y + SHADOW_OFF, (color & 16579836) >> 2 | color & -16777216);
		}
		return drawText(font, text, x, y, color);
	}
	public void end() {
		end(d3dDevice);
	}
	private native void end(final long d3dDevice);
	public native boolean hasBorder(final String window);
	public boolean inScreen(final float[] point) {
		final int[] resolution = resolution();
		if (resolution == null) {
			return false;
		}
		return point[0] >= 0.0F && point[0] <= resolution[0] && point[1] >= 0.0F && point[1] <= resolution[1];
	}
	public native int[] resolution();
	public float[] screenPos(final float[] position) {
		final long clientAddr = Reliant.instance.getProcessStream().moduleAddress("client.dll");
		if (clientAddr == MemoryStream.NULL) {
			throw new InvalidDataException("client_addr");
		}
		final float[] screenPos = new float[] {GameCache.viewMatrix[0][0] * position[0] + GameCache.viewMatrix[0][1] * position[1] + GameCache.viewMatrix[0][2] * position[2] + GameCache.viewMatrix[0][3], GameCache.viewMatrix[1][0] * position[0] + GameCache.viewMatrix[1][1] * position[1] + GameCache.viewMatrix[1][2] * position[2] + GameCache.viewMatrix[1][3], GameCache.viewMatrix[2][0] * position[0] + GameCache.viewMatrix[2][1] * position[1] + GameCache.viewMatrix[2][2] * position[2] + GameCache.viewMatrix[2][3], GameCache.viewMatrix[3][0] * position[0] + GameCache.viewMatrix[3][1] * position[1] + GameCache.viewMatrix[3][2] * position[2] + GameCache.viewMatrix[3][3]};
		final float absW = Math.abs(screenPos[3]);
		if (absW != 0.0F) {
			screenPos[0] /= absW;
			screenPos[1] /= absW;
			screenPos[2] /= absW;
		}
		final int[] resolution = resolution();
		if (resolution == null) {
			throw new InvalidDataException("resolution");
		}
		screenPos[0] = 0.5F * (1.0F + screenPos[0]) * resolution[0];
		screenPos[1] = 0.5F * (1.0F - screenPos[1]) * resolution[1];
		if (hasBorder(Main.TARGET_WINDOW_CLASS)) {
			screenPos[0] += BORDER_WIDTH;
			screenPos[1] += BORDER_HEIGHT;
		}
		return screenPos;
	}
	public boolean shouldDraw() {
		return Main.windowIsForeground(Main.getTargetWindow());
	}
	private native int[] textSize(final long font, final String text);
	public int[] textSize(final String text, final String fontName, final int fontSize, final boolean shadow) {
		if (!fontMap.containsKey(fontSize)) {
			final long font = createFont(fontSize, 0, false, fontName);
			if (font == MemoryStream.NULL) {
				return null;
			}
			fontMap.put(fontSize, font);
		}
		final int[] textSz = textSize(fontMap.get(fontSize), text);
		if (shadow && textSz != null) {
			textSz[0] += SHADOW_OFF;
			textSz[1] += SHADOW_OFF;
		}
		return textSz;
	}
}
