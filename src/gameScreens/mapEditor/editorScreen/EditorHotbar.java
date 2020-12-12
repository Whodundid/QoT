package gameScreens.mapEditor.editorScreen;

import envisionEngine.eWindow.windowObjects.advancedObjects.header.WindowHeader;
import envisionEngine.eWindow.windowTypes.WindowObject;
import gameSystems.fontRenderer.FontRenderer;
import gameSystems.mapSystem.worldTiles.WorldTile;
import main.Game;
import util.EUtil;
import util.mathUtil.NumUtil;
import util.renderUtil.EColors;
import util.storageUtil.EArrayList;
import util.storageUtil.StorageBox;

public class EditorHotbar extends WindowObject {
	
	MapEditorScreen editor;
	
	/** The actual contents of the hotbar. */
	EArrayList<WorldTile> hotbar;
	/** The number of hotbar slots. (Default is 10). */
	int size = 10;
	/** The size of each hotbar slot int pixels. (They are squares). */
	int cellSize = 50;
	/** The current item the hotbar is on. */
	int curItem = 0;
	
	WindowHeader header;
	
	long startTime = -1l;
	
	//---------------------------------
	
	boolean isMoving = false;
	StorageBox<Integer, Integer> oldPos = new StorageBox();
	
	//--------------
	// Constructors
	//--------------
	
	public EditorHotbar(MapEditorScreen editorIn) {
		editor = editorIn;
		//determine dims -- depends on number of slots -- is always at the bottom middle by default
		double width = size * cellSize;
		double x = (Game.getWidth() / 2) - width / 2;
		double y = editor.getBotHeader().startY - 5 - cellSize;
		
		init(editor, x, y, width, cellSize);
		
		//set initial size
		hotbar = new EArrayList(size);
		EUtil.repeat(hotbar, h -> h.add((WorldTile) null), size);
		
		startTime = System.currentTimeMillis();
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initObjects() {
		header = new WindowHeader(this, false, 10) {
			@Override
			public void mouseReleased(int mXIn, int mYIn, int button) {
				super.mouseReleased(mXIn, mYIn, button);
				transferFocus(editor);
			}
		};
		header.setDrawTitle(false);
		header.setDrawParentFocus(false);
		
		addObject(header);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (hasFocus()) { transferFocus(editor); }
		
		header.setVisible(isMouseOver() || header.isMouseOver());
		drawHotbar();
		
		updateText();
		
		super.drawObject(mXIn, mYIn);
	}
	
	/** Used to reposition the hotbar itself. */
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		curItem = (int) ((mXIn - startX) / cellSize);
		super.mousePressed(mXIn, mYIn, button);
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		super.mouseReleased(mXIn, mYIn, button);
		transferFocus(editor);
	}
	
	/** Hook into scroll event if the mouse is directly hoving over this. */
	@Override
	public void mouseScrolled(int change) {
		scrollHotbar(change);
	}
	
	/** Used to select hotbar position by keypress.
	 *  This method of selection will not work for hotbar sizes larger than 10 (1-0 on keyboard)! */
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (Character.isDigit(typedChar)) {
			try {
				int num = Character.digit(typedChar, 10);
				
				if (num == 0) { num = size - 1; }
				else { num = NumUtil.clamp(num - 1, 0, num); }
				
				if (num < size) { curItem = num; }
				
				startTime = System.currentTimeMillis();
			}
			catch (Exception e) {}
		}
		
		super.keyPressed(typedChar, keyCode);
	}
	
	//---------
	// Methods
	//---------
	
	private void updateText() {
		if (startTime > 0) {
			if (System.currentTimeMillis() - startTime >= 2000) {
				startTime = -1;
			}
			
			WorldTile t = getCurrent();
			if (t != null) {
				drawStringC(t.getName(), midX, startY - FontRenderer.FONT_HEIGHT - ((header.isVisible()) ? header.height : 0));
			}
		}
	}
	
	/** Draws each cell along with its contents. */
	private void drawHotbar() {
		//black bacground
		drawRect(EColors.black);
		
		
		//draw each cell
		for (int i = 0; i < size; i++) {
			double posX = startX + (i * cellSize);
			drawHRect(posX, startY, posX + cellSize, endY, 3, EColors.dgray);
			
			WorldTile tile = hotbar.get(i);
			
			//only draw if it's not null and there's actually a texture
			if (tile != null && tile.getTexture() != null) {
				//draw it centered slightly
				drawTexture(posX + 7, startY + 7, cellSize - 14, cellSize - 14, tile.getTexture());
			}
		}
		
		drawHRect(EColors.black);
		
		//draw the selected cell
		if (size > 0) {
			double posX = startX + (curItem * cellSize);
			drawHRect(posX, startY, posX + cellSize, startY + cellSize, 3, EColors.white);
		}
	}
	
	/** Used to update the dimensions of the hotbar whenever the number of slots changes. */
	private void reDimension() {
		double width = size * cellSize;
		double x = midX - width / 2;
		
		//same height and y
		setDimensions(x, startY, width, height);
	}
	
	public void scrollHotbar(int amount) {
		//restrict to changes of at most 1 per action
		amount = (int) Math.signum(amount);
		
		if (curItem == 0 && amount > 0) { curItem = size - 1; }
		else if (curItem == size - 1 && amount < 0) { curItem = 0; }
		else { curItem -= amount; }
		
		startTime = System.currentTimeMillis();
	}
	
	/** Clears the hotbar contents but does not modify the size. */
	public void clear() {
		hotbar.clear();
	}
	
	//---------
	// Getters
	//---------
	
	/** Returns the current hotbar contents. */
	public EArrayList<WorldTile> getHotbar() { return hotbar; }
	
	public WorldTile getCurrent() { return hotbar.get(curItem); }
	
	/** Returns the item at the given position. Does not do bounds checking! */
	public WorldTile getItemAtPos(int pos) { return hotbar.get(pos); }
	
	//---------
	// Setters
	//---------
	
	/** Replaces the current hotbar with a new one, this also will redimension the hotbar if the size is different.
	 *  If the incomming list is null or empty, the hotbar's contents are cleared but its size is not changed. */
	public EditorHotbar setHotbar(EArrayList<WorldTile> in) {
		if (in != null) {
			if (in.isNotEmpty()) {
				hotbar = new EArrayList(in);
				
				boolean noDim = hotbar.size() == size;
				size = hotbar.size();
				
				//If the old size is the name as the new size, there is no need to redimension
				if (!noDim) {
					reDimension();
				}
				return this;
			}
		}
		clear();
		
		return this;
	}
	
	/** Sets the item at the hotbar position. Does not do bounds checking! */
	public EditorHotbar setItemAtPos(WorldTile tile, int pos) {
		hotbar.set(pos, tile);
		return this;
	}
	
}
