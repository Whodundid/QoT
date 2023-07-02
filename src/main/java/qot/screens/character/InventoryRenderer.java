package qot.screens.character;

import envision.Envision;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.MouseType;
import envision.engine.windows.windowUtil.windowEvents.events.EventMouse;
import envision.game.entities.Entity;
import envision.game.entities.inventory.EntityInventory;
import envision.game.items.Item;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;

public class InventoryRenderer extends WindowObject {
	
	/** The entity for which this inventory pertains to. */
	private Entity theEntity;
	private EntityInventory theInventory;
	
	private EList<InventorySlot> inventorySlots;
	private int maxInventoryCols = 5;
	private int slotSize = 70; // 30x30 px by default
	private InventorySlot slotInside;
	private InventorySlot clickedSlot; // keeping track of moving items between slots
	
	//--------------
	// Constructors
	//--------------
	
	public InventoryRenderer(Entity entIn) {
		theEntity = entIn;
		theInventory = theEntity.getInventory();
		
		int size = theInventory.size();
		double s = slotSize;
		double cols = ENumUtil.clamp(size, size, maxInventoryCols);
		double rows = size / cols;
		
		setDimensions(startX, startY, cols * s, rows * s);
		
		
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initChildren() {
		Envision.getCurrentScreen().registerListener(this);
		buildCharacterInventory();
	}
	
	@Override
	public void drawObject_i(int mXIn, int mYIn) {
		slotInside = null;
		drawRect(EColors.black, -2);
		drawRect(EColors.dgray);
		
		super.drawObject_i(mXIn, mYIn);
		
		// draw hovered item name (if there is one)
		if (slotInside != null) {
			int index = slotInside.getSlotIndex();
			var item = getItemAtIndex(index);
			
			if (item != null) {
				String name = item.getName();
				drawString(name, midX - FontRenderer.strWidth(name) / 2, startY - 40);
			}
		}
		
		if (clickedSlot != null) {
			int index = clickedSlot.getSlotIndex();
			var item = getItemAtIndex(index);
			
			if (item != null) {
				drawTexture(item.getTexture(), mXIn - slotSize / 2, mYIn - slotSize / 2, slotSize, slotSize);
			}
		}
		
		
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		
	}
	
	@Override
	public void onEvent(ObjectEvent e) {
		if (e.getEventParent() == Envision.getCurrentScreen()) {
			if (e instanceof EventMouse me && me.getMouseType() == MouseType.RELEASED) {
				var ms = getMovingSlot();
				if (ms != null && !isMouseInside()) {
					onSlotLeftClick(null);
				}
			}
		}
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Creates a viewable/interactable inventory for the current
	 * character.
	 */
	protected void buildCharacterInventory() {
		getChildren().clear();
		int size = theInventory.size();
		inventorySlots = new EArrayList<>(size);
		
		// create an inventory slot for each slot in the inventory
		for (int i = 0; i < size; i++) {
			var slot = new InventorySlot(this, i);
			inventorySlots.add(slot);
			addObject(slot);
		}

		double s = slotSize;
		
		//the starting X coordinates (top left)
		double invenX = startX;
		//the starting Y coordinates (top left)
		double invenY = startY;
		//used to keep track of the current item column position
		int colPos = 0;
		//keeps track of the current row that items are being positioned on
		int rowNum = 0;
		
		for (int i = 0; i < size; i++) {
			var slot = inventorySlots.get(i);
			
			// reset column position back to zero if over max col length
			// also increment row num
			if (colPos >= maxInventoryCols) {
				colPos = 0;
				rowNum++;
			}
			
			double x = invenX + (colPos * s);
			double y = invenY + (rowNum * s);
			
			slot.setDimensions(x, y, s, s); // w/h same for square slot
			
			colPos++;
		}
	}
	
	public Item getItemAtIndex(int index) { return theInventory.getItemAtIndex(index); }
	public void swapItems(int indexA, int indexB) { theInventory.swapItems(indexA, indexB); }
	public void dropItem(int index) { theEntity.dropItem(index); }
	public void removeItem(int index) { theInventory.setItem(index, null); }
	
	public void useItem(InventorySlot slot) {
		if (slot == null) return;
		int index = slot.getSlotIndex();
		var item = theInventory.getItemAtIndex(index);
		
		if (item == null) return;
		if (!item.isUsable()) return;
		
		item.onItemUse(theEntity);
		
		if (item.diesOnUse()) {
			removeItem(index);
		}
	}
	
	public InventorySlot getMovingSlot() {
		return clickedSlot;
	}
	
	protected void setSlotInside(InventorySlot slot) {
		slotInside = slot;
	}
	
	protected void onSlotLeftClick(InventorySlot slot) {
		clickedSlot = slot;
	}
	
}
