package envision.engine.rendering.batching;

import envision.engine.rendering.GLSettings;
import envision.engine.rendering.textureSystem.GameTexture;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class BatchLayer {
	
	protected String layerName;
	protected EList<RenderBatch> batches = EList.newList();
	protected EList<Integer> layerIndexes;
	protected int layerNum;
	protected int maxBatches;
	protected int maxBatchSize;
	protected int texSlots;
	protected boolean isClosed = false;
	
	protected BatchLayer(int layerNumIn, int maxBatchesIn, int maxIn, int texSlotsIn) {
		this("", layerNumIn, maxBatchesIn, maxIn, texSlotsIn);
	}
	protected BatchLayer(String layerNameIn, int layerNumIn, int maxBatchesIn, int maxIn, int texSlotsIn) {
		layerName = layerNameIn;
		layerNum = layerNumIn;
		maxBatches = maxBatchesIn;
		maxBatchSize = maxIn;
		texSlots = texSlotsIn;
		
		layerIndexes = new EArrayList<>(maxBatchesIn);
		pushLayerIndex();
	}
	
	public void pushLayerIndex() {
		if (layerIndexes.size() >= maxBatches) {
			throw new RuntimeException("Out of room in batch layer stack!");
		}
		
		layerIndexes.push(layerIndexes.size());
	}
	
	public void popLayerIndex() {
		if (layerIndexes.size() <= 1) {
			throw new RuntimeException("Can't pop off last layer index {index=0} in batch layer stack!");
		}
		
		layerIndexes.pop();
	}
	
	protected RenderBatch getWorkingBatch() { return getWorkingBatch(null); }
	protected RenderBatch getWorkingBatch(GameTexture texture) {
		boolean hasTexture = texture != null;
		int size = batches.size();
		int index = getCurLayerIndex();
		//System.out.println("DRAWING ON LAYER: '" + layerNum + "' AT INDEX: '" + index + "' : " + batches.size());
		
		for (int i = 0; i < size; i++) {
			var batch = batches.get(i);
			
			if (GLSettings.scissor) {
				if (batch.isScissorBatch && batch.hasRoomOnLayerIndex(index) && !batch.alreadyDrawn) {
					if (hasTexture) {
						// check if this batch already contains the texture or if it could be added
						if (batch.containsTexture(texture) || batch.canAddTexture(texture)) {
							//System.out.println("Using scissor batch: " + i + ", size: " + batch.size());
							return batch;
						}
						else {
							batch.closeBatch();
						}
					}
					else {
						//System.out.println("Using scissor batch: " + i + ", size: " + batch.size());
						return batch;
					}
				}
				else if (batch.isEmpty()) {
					//System.out.println("Creating new scissor batch: " + i + ", size: " + batch.size());
					batch.setScissorBatch(true);
					batch.applyScissorBoundsFromGLSettings();
					batch.setBatchLayerIndex(index);
					return batch;
				}
			}
			else
			if (batch.hasRoomOnLayerIndex(index)) {
				if (batch.isScissorBatch) continue;
				// checks for whether or not adding a texture
				else if (hasTexture) {
					// check if this batch already contains the texture or if it could be added
					if (batch.containsTexture(texture) || batch.canAddTexture(texture)) {
						//System.out.println("Using batch: " + i);
						return batch;
					}
					// # This saved everything :')
					else {
						batch.closeBatch();
					}
				}
				else {
					//System.out.println("Using batch: " + i);
					return batch;
				}
			}
			else if (batch.isEmpty() && !batch.isClosed) {
				//System.out.println("Setting empty batch: " + i + " to layer: " + curLayer);
				batch.setBatchLayerIndex(index);
				return batch;
			}
		}
		
		if (batches.size() < maxBatches) {
			for (int i = 0; i < size; i++) {
				batches.get(i).closeBatch();
			}
			var newBatch = new RenderBatch(maxBatchSize, texSlots);
			newBatch.setBatchLayer(layerNum);
			newBatch.setBatchLayerIndex(getCurLayerIndex());
			batches.add(newBatch);
			
			return getWorkingBatch(texture);
		}
		else {
			System.out.println("Can't add another batch to this layer! layer=" + layerNum);
		}
		
		System.out.println("We outta room!");
		return null;
	}
	
	public void drawLayer() {
		int size = batches.size();
		for (int i = 0; i < size; i++) {
			var b = batches.get(i);
			if (b.isEmpty()) continue;
			//System.out.println("\tdrawing index: " + i + " : " + b.size() + " : tex=" + b.textures.size());
			b.draw();
		}
	}
	
	public boolean isEmpty() {
		if (batches.isEmpty()) return true;
		
		int size = batches.size();
		for (int i = 0; i < size; i++) {
			var b = batches.get(i);
			if (!b.isEmpty()) return false;
		}
		
		return true;
	}
	
	public int size() {
		int total = 0;
		
		int size = batches.size();
		for (int i = 0; i < size; i++) {
			var b = batches.get(i);
			total += b.size();
		}
		
		return total;
	}
	
	public void openLayer() { isClosed = false; batches.forEach(RenderBatch::openBatch); }
	public void closeLayer() { isClosed = true; batches.forEach(RenderBatch::closeBatch); }
	public void resetLayer() { isClosed = false; batches.forEach(RenderBatch::resetBatch); }
	
	public String getLayerName() { return layerName; }
	public int getLayerNum() { return layerNum; }
	public int getCurLayerIndex() { return layerIndexes.getFirst(); }
	
	public void setLayerName(String nameIn) { layerName = nameIn; }
	
}
