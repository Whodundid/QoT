package qot.screens.character;

import envision.engine.windows.windowObjects.utilityObjects.RightClickMenu;
import envision.engine.windows.windowTypes.WindowObject;
import envision.game.items.Item;
import eutil.colors.EColors;

public class InventorySlot extends WindowObject {
	
	private InventoryRenderer renderer;
	private int itemIndex;
	
	private boolean isMoving;
	
	public InventorySlot(InventoryRenderer rendererIn, int itemSlot) {
		renderer = rendererIn;
		itemIndex = itemSlot;
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		double s = width * 0.08; // 10% offset of w/h
		double sx = startX + s;
		double sy = startY + s;
		double ex = endX - s;
		double ey = endY - s;
		double w = width - s * 2;
		double h = w;
		
		boolean isMoving = renderer.getMovingSlot() == this;
		
		// frame
		drawHRect(EColors.black, 1, 0);
		
		if (!isMoving) {
			if (isMouseInsideGui(mXIn, mYIn)) {
				if (renderer.getMovingSlot() != null) {
					drawRect(sx, sy, ex, ey, EColors.chalk.opacity(50));
				}
				else {
					drawHRect(EColors.yellow);
				}
				
				renderer.setSlotInside(this);
			}
			
			Item theItem = renderer.getItemAtIndex(itemIndex);
			
			if (theItem != null) {
				drawTexture(theItem.getTexture(), sx, sy, w, h);
			}
		}
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (button == 0) { renderer.onSlotLeftClick(this); System.out.println("LEFT"); }
		if (button == 1) {
			var item = renderer.getItemAtIndex(itemIndex);
			if (item != null) {
				var rcm = new RightClickMenu(this);
				rcm.setTitle(item.getName());
				if (item.isUsable()) rcm.addOption("Use", () -> renderer.useItem(this));
				rcm.addOption("Drop", () -> renderer.dropItem(itemIndex));
				rcm.display();
				System.out.println("RIGHT");
			}
		}
		super.mousePressed(mXIn, mYIn, button);
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		if (button == 0) {
			var movingItem = renderer.getMovingSlot();
			if (movingItem != null) {
				renderer.swapItems(itemIndex, movingItem.itemIndex);
				renderer.onSlotLeftClick(null);
			}
		}
		super.mouseReleased(mXIn, mYIn, button);
	}
	
	public int getSlotIndex() { return itemIndex; }
	
}
