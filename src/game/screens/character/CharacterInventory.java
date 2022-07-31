package game.screens.character;

import engine.windowLib.windowObjects.advancedObjects.WindowScrollList;
import engine.windowLib.windowTypes.WindowObject;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import game.entities.Entity;

public class CharacterInventory extends WindowObject {
	
	/** The entity for which this inventory pertains to. */
	private Entity theEntity;
	
	private WindowScrollList<InventorySlot> inventory;
	
	//--------------
	// Constructors
	//--------------
	
	public CharacterInventory(Entity entIn) {
		theEntity = entIn;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initObjects() {
		
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Creates a viewable/interactable inventory for the current
	 * character.
	 */
	protected void buildCharacterInventory() {
		//the starting X coordinates (top left)
		int invenX = 0;
		//the starting Y coordinates (top left)
		int invenY = 0;
		//used to keep track of the current item column position
		int colPos = 0;
		
		//the characters inventory will be drawn in a scrollable grid that is at
		//most 5 items long (wide). No restrictions on length because scrollable.
		for (int i = 0; i < colPos; i++) {
			
		}
	}
	
}
