package envision.engine.rendering.batching;

import envision.Envision;
import envision.engine.rendering.fontRenderer.EStringOutputFormatter;
import envision.engine.rendering.textureSystem.GameTexture;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.misc.Rotation;

//https://www.youtube.com/watch?v=oDh80Hmv7jM
public class BatchManager implements IBatchManager {
	
	public int maxLayers;
	public BatchLayer[] availableLayers;
	public EList<BatchLayer> batchLayerStack;
	protected int maxBatchSize;
	protected int batchTexSlots;
	protected BatchLayer curLayer;
	
	//==============
	// Constructors
	//==============
	
	public BatchManager() {
		this(100, 500, 16);
	}
	
	public BatchManager(int maxLayersIn, int maxBatchSizeIn, int texSlotsIn) {
		maxLayers = maxLayersIn;
		maxBatchSize = maxBatchSizeIn;
		batchTexSlots = texSlotsIn;
		availableLayers = new BatchLayer[maxLayersIn];
		batchLayerStack = new EArrayList<>(maxLayersIn);
		
		for (int i = 0; i < maxLayersIn; i++) {
			availableLayers[i] = new BatchLayer(i, 100, maxBatchSize, batchTexSlots);	
		}
	}
	
	public static void startBatch() { Envision.getRenderEngine().getBatchManager().startBatch_i(); }
	public static void endBatch() { Envision.getRenderEngine().getBatchManager().endBatch_i(); }
	
	protected void startBatch_i() {
		var layer = getUnusedBatch();
		curLayer = layer;
		batchLayerStack.push(layer);
	}
	
	protected void endBatch_i() {
		if (batchLayerStack.isEmpty()) return;
		var layer = batchLayerStack.pop();
		curLayer = batchLayerStack.getFirst();
		if (layer != null) layer.closeLayer();
	}
	
	public BatchLayer getUnusedBatch() {
		for (int i = 0; i < availableLayers.length; i++) {
			var batch = availableLayers[i];
			if (!batch.isClosed && batch.isEmpty()) {
				return batch;
			}
		}
		return null;
	}
	
	//================
	// Layer Handling
	//================
	
	/**
	 * Pushes a new layer onto the batch stack. All subsequent draw calls will
	 * be added to batches of the newly pushed layer index.
	 * 
	 * @return The layer index that was just pushed onto the batch layer stack.
	 */
//	public int pushLayer() { return pushLayer(null); }
//	public int pushLayer(String layerName) {
//		if (batchLayerStack.size() >= maxLayers) {
//			throw new RuntimeException("Out of room in batch layer stack!");
//		}
//		
//		BatchLayer newLayer = availableLayers[batchLayerStack.size()];
//		if (layerName == null) layerName = "";
//		newLayer.setLayerName(layerName);
//		batchLayerStack.push(newLayer);
//		return batchLayerStack.size() - 1;
//	}
//	
	public static void pushLayerIndex() {
		Envision.getRenderEngine().getBatchManager().pushLayerIndex_i();
	}
	
	public void pushLayerIndex_i() {
		var layer = getCurLayer();
		if (layer != null) layer.pushLayerIndex();
	}
	
	/**
	 * Pops off the most recent layer on the batch stack and closes each of
	 * their layers.
	 * 
	 * @return The layer index that was popped off.
	 */
//	public int popLayer() {
//		if (batchLayerStack.size() <= 1) {
//			throw new RuntimeException("Can't pop off last layer {layer=0} in batch layer stack!");
//		}
//		
//		// close the batches on this layer (if there are any)
//		//closeBatchLayer(getCurLayerNum());
//		
//		// pop off the layer
//		return batchLayerStack.pop().layerNum;
//	}
//	
	public static void popLayerIndex() {
		Envision.getRenderEngine().getBatchManager().popLayerIndex_i();
	}
	
	public void popLayerIndex_i() {
		var layer = getCurLayer();
		if (layer != null) layer.popLayerIndex();
	}
	
	//=========
	// Methods
	//=========
	
	/** Draws the toString representation of an object at the specified position. */
	public static double drawString(Object o, double x, double y) { return drawString(toStr(o), x, y, EColors.white.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawString(Object o, double x, double y, EColors colorIn) { return drawString(toStr(o), x, y, colorIn.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawCenteredString(Object o, double x, double y, EColors colorIn) { return drawCenteredString(toStr(o), x, y, colorIn.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringWithShadow(Object o, double x, double y, EColors colorIn) { return drawStringWithShadow(toStr(o), x, y, colorIn.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawCenteredStringWithShadow(Object o, double x, double y, EColors colorIn) { return drawCenteredStringWithShadow(toStr(o), x, y, colorIn.intVal); }
	
	
	/** Draws the toString representation of an object at the specified position. */
	public static double drawString(Object o, double x, double y, int color) { return EStringOutputFormatter.drawString(toStr(o), x, y, 1.0, 1.0, color, false, false, true); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawCenteredString(Object o, double x, double y, int color) { return EStringOutputFormatter.drawString(toStr(o), x, y, 1.0, 1.0, color, true, false, true); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringWithShadow(Object o, double x, double y, int color) { return EStringOutputFormatter.drawString(toStr(o), x, y, 1.0, 1.0, color, false, true, true); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawCenteredStringWithShadow(Object o, double x, double y, int color) { return EStringOutputFormatter.drawString(toStr(o), x, y, 1.0, 1.0, color, true, true, true); }
	
	
	/** Draws the toString representation of an object at the specified position. */
	public static double drawString(Object o, double x, double y, double scaleX, double scaleY, int color) { return EStringOutputFormatter.drawString(toStr(o), x, y, scaleX, scaleY, color, false, false, true); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawCenteredString(Object o, double x, double y, double scaleX, double scaleY, int color) { return EStringOutputFormatter.drawString(toStr(o), x, y, scaleX, scaleY, color, true, false, true); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringWithShadow(Object o, double x, double y, double scaleX, double scaleY, int color) { return EStringOutputFormatter.drawString(toStr(o), x, y, scaleX, scaleY, color, false, true, true); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawCenteredStringWithShadow(Object o, double x, double y, double scaleX, double scaleY, int color) { return EStringOutputFormatter.drawString(toStr(o), x, y, scaleX, scaleY, color, true, true, true); }
	
	
	/** Draws a String at the specified position. */
	public static double drawString(String text, double x, double y) { return drawString(text, x, y, EColors.white.intVal); }
	/** Draws a String at the specified position. */
	public static double drawString(String text, double x, double y, EColors colorIn) { return drawString(text, x, y, colorIn.intVal); }
	/** Draws a String at the specified position. */
	public static double drawCenteredString(String text, double x, double y, EColors colorIn) { return drawCenteredString(text, x, y, colorIn.intVal); }
	/** Draws a String at the specified position. */
	public static double drawStringWithShadow(String text, double x, double y, EColors colorIn) { return drawStringWithShadow(text, x, y, colorIn.intVal); }
	/** Draws a String at the specified position. */
	public static double drawCenteredStringWithShadow(String text, double x, double y, EColors colorIn) { return drawCenteredStringWithShadow(text, x, y, colorIn.intVal); }
	
	
	/** Draws a String at the specified position. */
	public static double drawString(String text, double x, double y, double scaleX, double scaleY) { return drawString(text, x, y, scaleX, scaleY, EColors.white.intVal); }
	/** Draws a String at the specified position. */
	public static double drawString(String text, double x, double y, double scaleX, double scaleY, EColors colorIn) { return drawString(text, x, y, scaleX, scaleY, colorIn.intVal); }
	/** Draws a String at the specified position. */
	public static double drawCenteredString(String text, double x, double y, double scaleX, double scaleY, EColors colorIn) { return drawCenteredString(text, x, y, scaleX, scaleY, colorIn.intVal); }
	/** Draws a String at the specified position. */
	public static double drawStringWithShadow(String text, double x, double y, double scaleX, double scaleY, EColors colorIn) { return drawStringWithShadow(text, x, y, scaleX, scaleY, colorIn.intVal); }
	/** Draws a String at the specified position. */
	public static double drawCenteredStringWithShadow(String text, double x, double y, double scaleX, double scaleY, EColors colorIn) { return drawCenteredStringWithShadow(text, x, y, scaleX, scaleY, colorIn.intVal); }
	
	
	/** Draws a String at the specified position. */
	public static double drawString(String text, double x, double y, int color) { return EStringOutputFormatter.drawString(text, x, y, 1.0, 1.0, color, false, false, true); }
	/** Draws a String at the specified position. */
	public static double drawCenteredString(String text, double x, double y, int color) { return EStringOutputFormatter.drawString(text, x, y, 1.0, 1.0, color, true, false, true); }
	/** Draws a String at the specified position. */
	public static double drawStringWithShadow(String text, double x, double y, int color) { return EStringOutputFormatter.drawString(text, x, y, 1.0, 1.0, color, false, true, true); }
	/** Draws a String at the specified position. */
	public static double drawCenteredStringWithShadow(String text, double x, double y, int color) { return EStringOutputFormatter.drawString(text, x, y, 1.0, 1.0, color, true, true, true); }
	
	
	/** Draws a String at the specified position. */
	public static double drawString(String text, double x, double y, double scaleX, double scaleY, int color) { return EStringOutputFormatter.drawString(text, x, y, scaleX, scaleY, color, false, false, true); }
	/** Draws a String at the specified position. */
	public static double drawCenteredString(String text, double x, double y, double scaleX, double scaleY, int color) { return EStringOutputFormatter.drawString(text, x, y, scaleX, scaleY, color, true, false, true); }
	/** Draws a String at the specified position. */
	public static double drawStringWithShadow(String text, double x, double y, double scaleX, double scaleY, int color) { return EStringOutputFormatter.drawString(text, x, y, scaleX, scaleY, color, false, true, true); }
	/** Draws a String at the specified position. */
	public static double drawCenteredStringWithShadow(String text, double x, double y, double scaleX, double scaleY, int color) { return EStringOutputFormatter.drawString(text, x, y, scaleX, scaleY, color, true, true, true); }
	
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringC(Object o, double x, double y) { return drawStringC(toStr(o), x, y, EColors.white); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringC(Object o, double x, double y, EColors colorIn) { return drawStringC(toStr(o), x, y, colorIn.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringS(Object o, double x, double y, EColors colorIn) { return drawStringS(toStr(o), x, y, colorIn.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringCS(Object o, double x, double y, EColors colorIn) { return drawStringCS(toStr(o), x, y, colorIn.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringC(Object o, double x, double y, int color) { return drawCenteredString(toStr(o), x, y, color); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringS(Object o, double x, double y, int color) { return drawStringWithShadow(toStr(o), x, y, color); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringCS(Object o, double x, double y, int color) { return drawCenteredStringWithShadow(toStr(o), x, y, color); }
	
	
	/** Draws a String at the specified position. */
	public static double drawStringC(String text, double x, double y, EColors colorIn) { return drawStringC(text, x, y, colorIn.intVal); }
	/** Draws a String at the specified position. */
	public static double drawStringS(String text, double x, double y, EColors colorIn) { return drawStringS(text, x, y, colorIn.intVal); }
	/** Draws a String at the specified position. */
	public static double drawStringCS(String text, double x, double y, EColors colorIn) { return drawStringCS(text, x, y, colorIn.intVal); }
	/** Draws a String at the specified position. */
	public static double drawStringC(String text, double x, double y, int color) { return drawCenteredString(text, x, y, color); }
	/** Draws a String at the specified position. */
	public static double drawStringS(String text, double x, double y, int color) { return drawStringWithShadow(text, x, y, color); }
	/** Draws a String at the specified position. */
	public static double drawStringCS(String text, double x, double y, int color) { return drawCenteredStringWithShadow(text, x, y, color); }
	
	
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringC(Object o, double x, double y, double scaleX, double scaleY) { return drawStringC(toStr(o), x, y, scaleX, scaleY, EColors.white); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringC(Object o, double x, double y, double scaleX, double scaleY, EColors colorIn) { return drawStringC(toStr(o), x, y, scaleX, scaleY, colorIn.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringS(Object o, double x, double y, double scaleX, double scaleY, EColors colorIn) { return drawStringS(toStr(o), x, y, scaleX, scaleY, colorIn.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringCS(Object o, double x, double y, double scaleX, double scaleY, EColors colorIn) { return drawStringCS(toStr(o), x, y, scaleX, scaleY, colorIn.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringC(Object o, double x, double y, double scaleX, double scaleY, int color) { return drawCenteredString(toStr(o), x, y, scaleX, scaleY, color); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringS(Object o, double x, double y, double scaleX, double scaleY, int color) { return drawStringWithShadow(toStr(o), x, y, scaleX, scaleY, color); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringCS(Object o, double x, double y, double scaleX, double scaleY, int color) { return drawCenteredStringWithShadow(toStr(o), x, y, scaleX, scaleY, color); }
	
	
	/** Draws a String at the specified position. */
	public static double drawStringC(String text, double x, double y, double scaleX, double scaleY) { return drawStringC(text, x, y, scaleX, scaleY, EColors.white.intVal); }
	/** Draws a String at the specified position. */
	public static double drawStringS(String text, double x, double y, double scaleX, double scaleY) { return drawStringS(text, x, y, scaleX, scaleY, EColors.white.intVal); }
	/** Draws a String at the specified position. */
	public static double drawStringC(String text, double x, double y, double scaleX, double scaleY, EColors colorIn) { return drawStringC(text, x, y, scaleX, scaleY, colorIn.intVal); }
	/** Draws a String at the specified position. */
	public static double drawStringS(String text, double x, double y, double scaleX, double scaleY, EColors colorIn) { return drawStringS(text, x, y, scaleX, scaleY, colorIn.intVal); }
	/** Draws a String at the specified position. */
	public static double drawStringCS(String text, double x, double y, double scaleX, double scaleY, EColors colorIn) { return drawStringCS(text, x, y, scaleX, scaleY, colorIn.intVal); }
	/** Draws a String at the specified position. */
	public static double drawStringC(String text, double x, double y, double scaleX, double scaleY, int color) { return drawCenteredString(text, x, y, scaleX, scaleY, color); }
	/** Draws a String at the specified position. */
	public static double drawStringS(String text, double x, double y, double scaleX, double scaleY, int color) { return drawStringWithShadow(text, x, y, scaleX, scaleY, color); }
	/** Draws a String at the specified position. */
	public static double drawStringCS(String text, double x, double y, double scaleX, double scaleY, int color) { return drawCenteredStringWithShadow(text, x, y, scaleX, scaleY, color); }
	
	/** Returns a 'toString' representation of the given object. Accounts for null objets as well. */
	private static String toStr(Object in) { return (in != null) ? in.toString() : "null"; }
	
	
	public static void drawRect(double sx, double sy, double ex, double ey, EColors color) {
		Envision.getRenderEngine().getBatchManager().drawRect_i(sx, sy, ex, ey, color.intVal);
	}
	public static void drawRect(double sxIn, double syIn, double exIn, double eyIn, int colorIn) {
		Envision.getRenderEngine().getBatchManager().drawRect_batch(sxIn, syIn, exIn, eyIn, colorIn);
	}
	
	protected void drawRect_i(double sx, double sy, double ex, double ey, EColors color) { drawRect_i(sx, sy, ex, ey, color.intVal); }
	protected void drawRect_i(double sxIn, double syIn, double exIn, double eyIn, int colorIn) { drawRect_batch(sxIn, syIn, exIn, eyIn, colorIn); }
	
	private void drawRect_batch(double sxIn, double syIn, double exIn, double eyIn, int colorIn) {
		RenderBatch curBatch = getCurLayerBatch();
		if (curBatch == null) {
			return;
		}
		
		float sx = (float) sxIn;
		float sy = (float) syIn;
		float ex = (float) exIn;
		float ey = (float) eyIn;
		
		float r = (colorIn >> 16 & 255) / 255.0F;
		float g = (colorIn >> 8 & 255) / 255.0F;
		float b = (colorIn & 255) / 255.0F;
		float f = (colorIn >> 24 & 255) / 255.0F;
		
		curBatch.vert(sx, sy, r, g, b, f);
		curBatch.vert(sx, ey, r, g, b, f);
		curBatch.vert(ex, ey, r, g, b, f);
		curBatch.vert(ex, sy, r, g, b, f);
		
		curBatch.totalElements++;
	}
	
	public static void drawTexture(GameTexture tex, double x, double y, double w, double h) { drawTexture(tex, x, y, w, h, false, Rotation.UP, 0xffffffff); }
	public static void drawTexture(GameTexture tex, double x, double y, double w, double h, int color) { drawTexture(tex, x, y, w, h, false, Rotation.UP, color); }
	public static void drawTexture(GameTexture tex, double x, double y, double w, double h, Rotation rotation) { drawTexture(tex, x, y, w, h, false, rotation, 0xffffffff); }
	public static void drawTexture(GameTexture tex, double x, double y, double w, double h, boolean flip) { drawTexture(tex, x, y, w, h, flip, Rotation.UP, 0xffffffff); }
	public static void drawTexture(GameTexture tex, double x, double y, double w, double h, boolean flip, int color) { drawTexture(tex, x, y, w, h, flip, Rotation.UP, color); }
	public static void drawTexture(GameTexture tex, double x, double y, double w, double h, boolean flip, Rotation rotation) { drawTexture(tex, x, y, w, h, flip, rotation, 0xffffffff); }
	public static void drawTexture(GameTexture tex, double x, double y, double w, double h, boolean flip, Rotation rotation, int color) {
		Envision.getRenderEngine().getBatchManager().drawTexture_i(tex, x, y, w, h, flip, rotation, color);
	}
	
	protected void drawTexture_i(GameTexture tex, double x, double y, double w, double h) { drawTexture_i(tex, x, y, w, h, false, Rotation.UP, 0xffffffff); }
	protected void drawTexture_i(GameTexture tex, double x, double y, double w, double h, int color) { drawTexture_i(tex, x, y, w, h, false, Rotation.UP, color); }
	protected void drawTexture_i(GameTexture tex, double x, double y, double w, double h, Rotation rotation) { drawTexture_i(tex, x, y, w, h, false, rotation, 0xffffffff); }
	protected void drawTexture_i(GameTexture tex, double x, double y, double w, double h, boolean flip) { drawTexture_i(tex, x, y, w, h, flip, Rotation.UP, 0xffffffff); }
	protected void drawTexture_i(GameTexture tex, double x, double y, double w, double h, boolean flip, int color) { drawTexture_i(tex, x, y, w, h, flip, Rotation.UP, color); }
	protected void drawTexture_i(GameTexture tex, double x, double y, double w, double h, boolean flip, Rotation rotation) { drawTexture_i(tex, x, y, w, h, flip, rotation, 0xffffffff); }
	protected void drawTexture_i(GameTexture tex, double x, double y, double w, double h, boolean flip, Rotation rotation, int color) {
		RenderBatch curBatch = getCurLayerBatch(tex);
		if (curBatch == null) {
			return;
		}
		
		int texID = curBatch.addTexture(tex);
		if (texID == -1) {
			return;
		}
		
		float sx = (float) x;
		float sy = (float) y;
		float ex = (float) (x + w);
		float ey = (float) (y + h);
		
		float r = (color >> 16 & 255) / 255.0F;
		float g = (color >> 8 & 255) / 255.0F;
		float b = (color & 255) / 255.0F;
		float f = (color >> 24 & 255) / 255.0F;
		
		curBatch.vert(sx, sy, 0f, r, g, b, f, 0f, 0f, (float) texID);
		curBatch.vert(sx, ey, 0f, r, g, b, f, 0f, 1f, (float) texID);
		curBatch.vert(ex, ey, 0f, r, g, b, f, 1f, 1f, (float) texID);
		curBatch.vert(ex, sy, 0f, r, g, b, f, 1f, 0f, (float) texID);
		
		curBatch.totalElements++;
	}
	
	public static void drawTexture(GameTexture texture, double x, double y, double w, double h, double tX, double tY, double tW, double tH, EColors color) {
		Envision.getRenderEngine().getBatchManager().drawTexture_i(texture, x, y, w, h, tX, tY, tW, tH, color.intVal, false);
	}
	
	public static void drawTexture(GameTexture texture, double x, double y, double w, double h, double tX, double tY, double tW, double tH, EColors color, boolean flip) {
		Envision.getRenderEngine().getBatchManager().drawTexture_i(texture, x, y, w, h, tX, tY, tW, tH, color.intVal, flip);
	}
	
	public static void drawTexture(GameTexture texture, double x, double y, double w, double h, double tX, double tY, double tW, double tH, int color) {
		Envision.getRenderEngine().getBatchManager().drawTexture_i(texture, x, y, w, h, tX, tY, tW, tH, color, false);
	}
	
	public static void drawTexture(GameTexture texture, double x, double y, double w, double h, double tX, double tY, double tW, double tH, int color, boolean flip) {
		Envision.getRenderEngine().getBatchManager().drawTexture_i(texture, x, y, w, h, tX, tY, tW, tH, color, flip);
	}
	
	protected void drawTexture_i(GameTexture texture, double x, double y, double w, double h, double tX, double tY, double tW, double tH, int color) {
		drawTexture_i(texture, x, y, w, h, tX, tY, tW, tH, color, false);
	}
	protected void drawTexture_i(GameTexture texture, double x, double y, double w, double h, double tX, double tY, double tW, double tH, int color, boolean flip) {
		drawTexture_batch(texture, x, y, w, h, tX, tY, tW, tH, color, flip);
	}
	
	private void drawTexture_batch(GameTexture texture, double x, double y, double w, double h, double tX, double tY, double tW, double tH, int color, boolean flip) {
		RenderBatch curBatch = getCurLayerBatch(texture);
		if (curBatch == null) {
			return;
		}
		
		int texID = curBatch.addTexture(texture);
		if (texID == -1) {
			return;
		}
		
		float xVal = (float) (tX / (float) texture.getWidth());
		float yVal = (float) (tY / (float) texture.getHeight());
		float wVal = (float) (tW / (float) texture.getWidth());
		float hVal = (float) (tH / (float) texture.getHeight());
		
		float r = (color >> 16 & 255) / 255.0F;
		float g = (color >> 8 & 255) / 255.0F;
		float b = (color & 255) / 255.0F;
		float f = (color >> 24 & 255) / 255.0F;
		
		//garbage duct-tape font bottom fix
		hVal -= 0.005f;
		
		float sx = (float) x;
		float sy = (float) y;
		float ex = (float) (x + w);
		float ey = (float) (y + h);
		
		float tsx = xVal;
		float tex = xVal + wVal;
		float tsy = yVal;
		float tey = yVal + hVal;
		
		float draw_tsx = (flip) ? tex : tsx;
		float draw_tsy = (flip) ? tsy : tsy;
		float draw_tex = (flip) ? tsx : tex;
		float draw_tey = (flip) ? tey : tey;
		
		curBatch.vert(sx, sy, 0f, r, g, b, f, draw_tsx, draw_tsy, (float) texID);
		curBatch.vert(sx, ey, 0f, r, g, b, f, draw_tsx, draw_tey, (float) texID);
		curBatch.vert(ex, ey, 0f, r, g, b, f, draw_tex, draw_tey, (float) texID);
		curBatch.vert(ex, sy, 0f, r, g, b, f, draw_tex, draw_tsy, (float) texID);
		
		curBatch.totalElements++;
	}
	
	//================
	// Helper Methods
	//================
	
	//public int getCurLayerNum() { return batchLayerStack.size() - 1; }
	public BatchLayer getCurLayer() { return batchLayerStack.getFirst(); }
	
	public RenderBatch getCurLayerBatch() {
		if (curLayer == null) return null;
		return curLayer.getWorkingBatch();
	}
	public RenderBatch getCurLayerBatch(GameTexture texture) {
		if (curLayer == null) return null;
		return curLayer.getWorkingBatch(texture);
	}
	
	public static void draw() {
		Envision.getRenderEngine().getBatchManager().draw_i();
	}
	
	protected void draw_i() {
		drawAll();
		resetAllBatches();
	}
	
	private void drawAll() {
		//Collections.sort(batchLayerStack, (a, b) -> Integer.compare(a.layerNum, b.layerNum));
		
		int size = availableLayers.length;
		for (int i = 0; i < size; i++) {
			var layer = availableLayers[i];
			if (layer.isEmpty()) continue;
			//System.out.println("DRAWING LAYER: " + i + " : " + layer.size());
			layer.drawLayer();
		}
	}
	
	private void resetAllBatches() {
		for (int i = 0; i < maxLayers; i++) {
			var layer = availableLayers[i];
			layer.resetLayer();
		}
	}
	
	//private void openBatchLayer(int layer) { batchLayerStack.get(layer).openLayer(); }
	//private void closeBatchLayer(int layer) { batchLayerStack.get(layer).closeLayer(); }
	//private void resetBatchLayer(int layer) { batchLayerStack.get(layer).resetLayer(); }
	
}
