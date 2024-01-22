package envision.engine.rendering.batching;

import org.joml.Vector2f;

import envision.Envision;
import envision.engine.rendering.textureSystem.GameTexture;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.debug.Inefficient;
import eutil.misc.Rotation;

// order
// 1. world map and entities horizontal layer by layer
// 2. game hud
// 3. top overlay
// 4. debug text ?

//https://www.youtube.com/watch?v=oDh80Hmv7jM
public class BatchManager implements IBatchManager {
	
    /**
     * 255.0F but in decimal form
     * <p>
     * Because division is inherently slower to process than multiplication,
     * this value is used instead: both [value / 255F] and [value * F_255]
     * produce the same result.
     */
	private static final float F_255 = 0.0039215686274509803921568627451f;
	
	public int maxLayers;
	public BatchLayer[] availableLayers;
	public EList<BatchLayer> batchLayerStack;
	protected int maxBatchSize;
	protected int batchTexSlots;
	protected BatchLayer curLayer;
	protected boolean isEnabled = false;
	
	private int usedLayers = 1;
	
	//==============
	// Constructors
	//==============
	
	public BatchManager() {
		this(30, 400, 32);
	}
	
	public BatchManager(int maxLayersIn, int maxBatchSizeIn, int texSlotsIn) {
		maxLayers = maxLayersIn;
		maxBatchSize = maxBatchSizeIn;
		batchTexSlots = texSlotsIn;
		availableLayers = new BatchLayer[maxLayersIn];
		batchLayerStack = new EArrayList<>(maxLayersIn);
		
		for (int i = 0; i < maxLayersIn; i++) {
			availableLayers[i] = new BatchLayer(i, 200, maxBatchSize, batchTexSlots);	
		}
	}
	
	private static BatchManager instance() { return Envision.getRenderEngine().getBatchManager(); }
	
	public static void startBatch() { instance().startBatch_i(); }
	public static void endBatch() { instance().endBatch_i(); }
	public static boolean isEnabled() { return instance().isEnabled; }
	public static void enable() { instance().isEnabled = true; }
	public static void disable() { instance().isEnabled = false; }
	
	public static BatchLayer getCurrentLayer() {
	    return instance().curLayer;
	}
	
	public static void startLayer(int index) { instance().startLayer_i(index); }
	protected void startLayer_i(int index) {
	    var layer = availableLayers[index];
	    curLayer = layer;
	}
	
	public static void endLayer(int index) { instance().endLayer_i(index); }
	protected void endLayer_i(int index) {
	    var layer = availableLayers[index];
        curLayer = null;
        layer.closeLayer();
	}
	
	protected void startBatch_i() {
		var layer = getUnusedLayer();
		curLayer = layer;
		System.out.println("layerNum: " + curLayer.layerNum);
		batchLayerStack.push(layer);
		usedLayers += 1;
	}
	
	protected void endBatch_i() {
		if (batchLayerStack.isEmpty()) return;
		var layer = batchLayerStack.pop();
		curLayer = batchLayerStack.getFirst();
		if (layer != null) layer.closeLayer();
	}
	
	protected BatchLayer getUnusedLayer() {
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
	public int pushLayer() { return pushLayer(null); }
	public int pushLayer(String layerName) {
		if (batchLayerStack.size() >= maxLayers) {
			throw new RuntimeException("Out of room in batch layer stack!");
		}
		
		BatchLayer newLayer = availableLayers[batchLayerStack.size()];
		if (layerName == null) layerName = "";
		newLayer.setLayerName(layerName);
		batchLayerStack.push(newLayer);
		return batchLayerStack.size() - 1;
	}
	
	public static void pushLayerIndex() {
		instance().pushLayerIndex_i();
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
		instance().popLayerIndex_i();
	}
	
	public void popLayerIndex_i() {
		var layer = getCurLayer();
		if (layer != null) layer.popLayerIndex();
	}
	
	//=========
	// Methods
	//=========
	
	public static void drawLine(double startX, double startY, double endX, double endY, int thickness, int color) {
		instance().drawLine_batch(startX, startY, endX, endY, thickness, color);
	}
	
	@Inefficient(reason="Not great -- doesn't really probably account for line thickness right now..")
	private void drawLine_batch(double sX, double sY, double eX, double eY, int thickness, int color) {
		RenderBatch curBatch = getCurLayerBatch();
		if (curBatch == null) {
			return;
		}
		
		float sx = (float) sX;
		float sy = (float) sY;
		float ex = (float) eX;
		float ey = (float) eY;
		
		//float dx = ex - sx;
		//float dy = ey - sy;
		//System.out.println("(" + sX + ", " + sY + ") : (" + eX + ", " + eY + ") [" + dx + ", " + dy + "] " + thickness);
		
		float r = (color >> 16 & 255) * F_255;
		float g = (color >> 8 & 255) * F_255;
		float b = (color & 255) * F_255;
		float f = (color >> 24 & 255) * F_255;
		
		float tx = thickness;
		float ty = thickness;
		
		curBatch.vert(sx - tx, sy + ty, r, g, b, f);
		curBatch.vert(sx, sy, r, g, b, f);
		curBatch.vert(ex + tx, ey - ty, r, g, b, f);
		curBatch.vert(ex, ey, r, g, b, f);
		
		curBatch.totalElements++;
	}
	
	/** Draws a solid ellipse expanding out from the center. */
	public static void drawFilledEllipse(double posX, double posY, double radiusX, double radiusY, int detail, int color) {
		instance().drawFilledEllipse_batch(posX, posY, radiusX, radiusY, detail, color);
	}
	
	/** Draws a solid ellipse expanding out from the center. */
	private void drawFilledEllipse_batch(double posX, double posY, double radiusX, double radiusY, int detail, int color) {
		RenderBatch curBatch = getCurLayerBatch();
		if (curBatch == null) {
			return;
		}
		
		float r = (color >> 16 & 255) * F_255;
		float g = (color >> 8 & 255) * F_255;
		float b = (color & 255) * F_255;
		float f = (color >> 24 & 255) * F_255;
		
		float posx = (float) posX;
		float posy = (float) posY;
		double detailD = detail;
		
		//define points
		curBatch.vert(posx, posy, r, g, b, f);
		
		for (int i = 0; i < detail + 1; i++) {
			float theta = (float) (2.0D * Math.PI * i / detailD);
			float x = (float) (radiusX * Math.cos(theta));
			float y = (float) (radiusY * Math.sin(theta));
			curBatch.vert(x + posx, y + posy, r, g, b, f);
		}
		
		curBatch.totalElements++;
	}
	
	public static void drawRect(double sx, double sy, double ex, double ey, EColors color) {
		instance().drawRect_i(sx, sy, ex, ey, color.intVal);
		
		System.out.println("CAT");
	}
	public static void drawRect(double sxIn, double syIn, double exIn, double eyIn, int colorIn) {
		instance().drawRect_batch(sxIn, syIn, exIn, eyIn, colorIn);
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
		
		float r = (colorIn >> 16 & 255) * F_255;
		float g = (colorIn >> 8 & 255) * F_255;
		float b = (colorIn & 255) * F_255;
		float f = (colorIn >> 24 & 255) * F_255;
		
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
		instance().drawTexture_i(tex, x, y, w, h, flip, rotation, color);
	}
	
	protected void drawTexture_i(GameTexture texture, double x, double y, double w, double h) { drawTexture_i(texture, x, y, w, h, false, Rotation.UP, 0xffffffff); }
	protected void drawTexture_i(GameTexture texture, double x, double y, double w, double h, int color) { drawTexture_i(texture, x, y, w, h, false, Rotation.UP, color); }
	protected void drawTexture_i(GameTexture texture, double x, double y, double w, double h, Rotation rotation) { drawTexture_i(texture, x, y, w, h, false, rotation, 0xffffffff); }
	protected void drawTexture_i(GameTexture texture, double x, double y, double w, double h, boolean flip) { drawTexture_i(texture, x, y, w, h, flip, Rotation.UP, 0xffffffff); }
	protected void drawTexture_i(GameTexture texture, double x, double y, double w, double h, boolean flip, int color) { drawTexture_i(texture, x, y, w, h, flip, Rotation.UP, color); }
	protected void drawTexture_i(GameTexture texture, double x, double y, double w, double h, boolean flip, Rotation rotation) { drawTexture_i(texture, x, y, w, h, flip, rotation, 0xffffffff); }
	protected void drawTexture_i(GameTexture texture, double x, double y, double w, double h, boolean flip, Rotation rotation, int color) {
		RenderBatch curBatch = getCurLayerBatch(texture);
		if (curBatch == null) {
			return;
		}
		
		int texID = curBatch.addTexture(texture);
		if (texID == -1) {
			return;
		}
		
		float sx = (float) x;
		float sy = (float) y;
		float ex = (float) (x + w);
		float ey = (float) (y + h);
		
		float r = (color >> 16 & 255) * F_255;
		float g = (color >> 8 & 255) * F_255;
		float b = (color & 255) * F_255;
		float f = (color >> 24 & 255) * F_255;
		
		float tu1 = 0f, tv1 = 0f;
		float tu2 = 0f, tv2 = 0f;
		float tu3 = 0f, tv3 = 0f;
		float tu4 = 0f, tv4 = 0f;
		
		switch (rotation) {
		case UP:
			tu1 = flip ? 1f : 0f; tv1 = 0f;
			tu2 = flip ? 1f : 0f; tv2 = 1f;
			tu3 = flip ? 0f : 1f; tv3 = 1f;
			tu4 = flip ? 0f : 1f; tv4 = 0f;
			break;
		case LEFT:
			tu1 = flip ? 0f : 1f; tv1 = 0f;
			tu2 = flip ? 1f : 0f; tv2 = 0f;
			tu3 = flip ? 1f : 0f; tv3 = 1f;
			tu4 = flip ? 0f : 1f; tv4 = 1f;
			break;
		case RIGHT:
			tu1 = flip ? 0f : 1f; tv1 = 1f;
			tu2 = flip ? 1f : 0f; tv2 = 1f;
			tu3 = flip ? 1f : 0f; tv3 = 0f;
			tu4 = flip ? 0f : 1f; tv4 = 0f;
			break;
		case DOWN:
			tu1 = flip ? 0f : 1f; tv1 = 1f;
			tu2 = flip ? 0f : 1f; tv2 = 0f;
			tu3 = flip ? 1f : 0f; tv3 = 0f;
			tu4 = flip ? 1f : 0f; tv4 = 1f;
			break;
		}
		
		float tID = texID;
		
		curBatch.vert(sx, sy, 0f, r, g, b, f, tu1, tv1, tID);
		curBatch.vert(sx, ey, 0f, r, g, b, f, tu2, tv2, tID);
		curBatch.vert(ex, ey, 0f, r, g, b, f, tu3, tv3, tID);
		curBatch.vert(ex, sy, 0f, r, g, b, f, tu4, tv4, tID);
		
		curBatch.totalElements++;
	}
	
	public static void drawTexture(GameTexture texture, double x, double y, double w, double h, double tX, double tY, double tW, double tH, EColors color) {
		instance().drawTexture_i(texture, x, y, w, h, tX, tY, tW, tH, color.intVal, false);
	}
	public static void drawTexture(GameTexture texture, double x, double y, double w, double h, double tX, double tY, double tW, double tH, EColors color, boolean flip) {
		instance().drawTexture_i(texture, x, y, w, h, tX, tY, tW, tH, color.intVal, flip);
	}
	public static void drawTexture(GameTexture texture, double x, double y, double w, double h, double tX, double tY, double tW, double tH, int color) {
		instance().drawTexture_i(texture, x, y, w, h, tX, tY, tW, tH, color, false);
	}
	public static void drawTexture(GameTexture texture, double x, double y, double w, double h, double tX, double tY, double tW, double tH, int color, boolean flip) {
		instance().drawTexture_i(texture, x, y, w, h, tX, tY, tW, tH, color, flip);
	}
	
    public static void drawTexture(GameTexture texture, double x, double y, double w, double h, Vector2f[] coords, EColors color, Rotation rotation, boolean flip) {
        instance().drawTexture_i(texture, x, y, w, h, coords, color.intVal, rotation, flip);
    }
    public static void drawTexture(GameTexture texture, double x, double y, double w, double h, Vector2f[] coords, int color, Rotation rotation, boolean flip) {
        instance().drawTexture_i(texture, x, y, w, h, coords, color, rotation, flip);
    }
	
	protected void drawTexture_i(GameTexture texture, double x, double y, double w, double h, double tX, double tY, double tW, double tH, int color) {
		drawTexture_batch(texture, x, y, w, h, tX, tY, tW, tH, color, false);
	}
	protected void drawTexture_i(GameTexture texture, double x, double y, double w, double h, double tX, double tY, double tW, double tH, int color, boolean flip) {
		drawTexture_batch(texture, x, y, w, h, tX, tY, tW, tH, color, flip);
	}
    protected void drawTexture_i(GameTexture texture, double x, double y, double w, double h, Vector2f[] coords, int color, Rotation rotation, boolean flip) {
        drawTexture_batch(texture, x, y, w, h, coords, color, rotation, flip);
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
		
		float xVal = (float) (tX / texture.getWidth());
		float yVal = (float) (tY / texture.getHeight());
		float wVal = (float) (tW / texture.getWidth());
		float hVal = (float) (tH / texture.getHeight());
		
		float r = (color >> 16 & 255) * F_255;
		float g = (color >> 8 & 255) * F_255;
		float b = (color & 255) * F_255;
		float f = (color >> 24 & 255) * F_255;
		
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
		float draw_tsy = (flip) ? tey : tsy;
		float draw_tex = (flip) ? tsx : tex;
		float draw_tey = (flip) ? tsy : tey;
		
		float tID = texID;
		
		curBatch.vert(sx, sy, 0f, r, g, b, f, draw_tsx, draw_tsy, tID);
		curBatch.vert(sx, ey, 0f, r, g, b, f, draw_tsx, draw_tey, tID);
		curBatch.vert(ex, ey, 0f, r, g, b, f, draw_tex, draw_tey, tID);
		curBatch.vert(ex, sy, 0f, r, g, b, f, draw_tex, draw_tsy, tID);
		
		curBatch.totalElements++;
	}
	
    private void drawTexture_batch(GameTexture texture, double x, double y, double w, double h, Vector2f[] coords, int color, Rotation rotation, boolean flip) {
        RenderBatch curBatch = getCurLayerBatch(texture);
        if (curBatch == null) {
            return;
        }
        
        int texID = curBatch.addTexture(texture);
        if (texID == -1) {
            return;
        }
        
        float r = (color >> 16 & 255) * F_255;
        float g = (color >> 8 & 255) * F_255;
        float b = (color & 255) * F_255;
        float f = (color >> 24 & 255) * F_255;
        
        float sx = (float) x;
        float sy = (float) y;
        float ex = (float) (x + w);
        float ey = (float) (y + h);
        
        float v1x = coords[0].x, v1y = coords[0].y;
        float v2x = coords[1].x, v2y = coords[1].y;
        float v3x = coords[2].x, v3y = coords[2].y;
        float v4x = coords[3].x, v4y = coords[3].y;
        
        float tID = texID;
        
        // rotation and flip logic
        float x1, x2, x3, x4;
        float y1, y2, y3, y4;
        
        switch (rotation) {
        case LEFT:
            x1 = flip ? v4x : v1x;      y1 = v2y;
            x2 = flip ? v1x : v4x;      y2 = v3y;
            x3 = flip ? v2x : v3x;      y3 = v4y;
            x4 = flip ? v3x : v2x;      y4 = v1y;
            break;
        case RIGHT:
            x1 = flip ? v1x : v3x;      y1 = v4y;
            x2 = flip ? v3x : v1x;      y2 = v1y;
            x3 = flip ? v4x : v2x;      y3 = v2y;
            x4 = flip ? v2x : v4x;      y4 = v3y;
            break;
        case DOWN:
            x1 = flip ? v3x : v1x;      y1 = v1y;
            x2 = flip ? v4x : v2x;      y2 = v2y;
            x3 = flip ? v1x : v3x;      y3 = v3y;
            x4 = flip ? v2x : v4x;      y4 = v4y;
            break;
        // grouping 'UP' with 'default' to ensure there is ~always~ a valid case
        case UP:
        default:
            x1 = flip ? v1x : v3x;      y1 = v3y;
            x2 = flip ? v2x : v4x;      y2 = v4y;
            x3 = flip ? v3x : v1x;      y3 = v1y;
            x4 = flip ? v4x : v2x;      y4 = v2y;
        }
        
        curBatch.vert(sx, sy, 0f, r, g, b, f, x1, y1, tID);
        curBatch.vert(sx, ey, 0f, r, g, b, f, x2, y2, tID);
        curBatch.vert(ex, ey, 0f, r, g, b, f, x3, y3, tID);
        curBatch.vert(ex, sy, 0f, r, g, b, f, x4, y4, tID);
        
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
	
	/**
	 * Draws the contents of the current layers that have content in them and
	 * then finally closes them.
	 * <p>
	 * NOTE: This method does not reset layer data and is intended to be used
	 * in between frame draws.
	 */
	public static void drawCurrentLayers() {
		instance().drawCurrentLayers_i();
	}
	
	public void drawCurrentLayers_i() {
		final int size = availableLayers.length;
		for (int i = 0; i < size; i++) {
			var layer = availableLayers[i];
			if (layer.isEmpty() || layer.isClosed) continue;
			
			layer.drawLayer();
			layer.closeLayer();
		}
	}
	
	public static void draw() {
		instance().draw_i();
	}
	
	protected void draw_i() {
		drawAll();
		resetAllBatches();
	}
	
	private void drawAll() {
		//Collections.sort(batchLayerStack, (a, b) -> Integer.compare(a.layerNum, b.layerNum));
		//System.out.println("USED LAYERS: " + usedLayers);
		final int size = availableLayers.length;
		for (int i = 0; i < size; i++) {
			var layer = availableLayers[i];
			if (layer.isEmpty()) continue;
			//System.out.println("DRAWING LAYER: " + i + " : " + layer.size());
			layer.drawLayer();
		}
	}
	
	private void resetAllBatches() {
		for (int i = 0; i < maxLayers; i++) {
			//System.out.println("layer: " + i);
			var layer = availableLayers[i];
			layer.resetLayer();
		}
		usedLayers = 1;
	}
	
	//private void openBatchLayer(int layer) { batchLayerStack.get(layer).openLayer(); }
	//private void closeBatchLayer(int layer) { batchLayerStack.get(layer).closeLayer(); }
	//private void resetBatchLayer(int layer) { batchLayerStack.get(layer).resetLayer(); }
	
}
