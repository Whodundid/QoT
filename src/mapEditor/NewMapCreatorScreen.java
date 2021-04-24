package mapEditor;

import assets.screens.GameScreen;
import assets.worldTiles.WorldTile;
import assets.worldTiles.WorldTiles;
import main.QoT;
import renderUtil.EColors;
import windowLib.windowObjects.actionObjects.WindowButton;
import windowLib.windowObjects.actionObjects.WindowCheckBox;
import windowLib.windowObjects.actionObjects.WindowTextField;
import windowLib.windowObjects.basicObjects.WindowLabel;
import windowLib.windowTypes.interfaces.IActionObject;
import world.GameWorld;

public class NewMapCreatorScreen extends GameScreen {

	GameWorld world;
	WindowLabel nameLabel, widthHeightLabel, emptyLabel, fillWithLabel;
	WindowTextField nameField;
	WindowTextField widthField, heightField;
	WindowCheckBox emptyMap;
	WindowButton tileSelection;
	WindowButton back, create;
	
	boolean drawError = false;
	String error = "No error!?";
	
	String name;
	int width = 25, height = 25;
	boolean empty;
	WorldTile fillTile = WorldTiles.grass;
	
	public NewMapCreatorScreen() { this((String) null); }
	public NewMapCreatorScreen(String nameIn) {
		super();
		name = nameIn;
	}
	
	public NewMapCreatorScreen(GameWorld worldIn) {
		super();
		if (worldIn != null) {
			name = worldIn.getName();
			width = worldIn.getWidth();
			height = worldIn.getHeight();
		}
	}
	
	@Override public void initScreen() {}
	
	@Override
	public void initObjects() {
		double x = midX - 150;
		double y = midY - 250;
		
		back = new WindowButton(this, 5, endY - 45, 150, 40, "Back");
		
		nameLabel = new WindowLabel(this, x, y, "Map name");
		nameField = new WindowTextField(this, nameLabel.startX, nameLabel.endY + 20, 300, 35);
		
		widthHeightLabel = new WindowLabel(this, x, nameField.endY + 20, "Width / Height");
		widthField = new WindowTextField(this, x, widthHeightLabel.endY + 25, 100, 35);
		heightField = new WindowTextField(this, widthField.endX + 10, widthHeightLabel.endY + 25, 100, 35);
		
		emptyLabel = new WindowLabel(this, x, widthField.endY + 20, "Empty map");
		emptyMap = new WindowCheckBox(this, emptyLabel.endX + 20, widthField.endY + 20, 50, 50);
		
		fillWithLabel = new WindowLabel(this, x, emptyMap.endY + 20, "Fill with..");
		tileSelection = new WindowButton(this, x, fillWithLabel.endY + 20, 45, 45);
		
		create = new WindowButton(this, midX - 100, tileSelection.endY + 50, 200, 45, "Create");
		
		tileSelection.setButtonTexture(fillTile.getTexture());
		
		empty = emptyMap.isChecked();
		fillWithLabel.setVisible(!empty);
		tileSelection.setVisible(!empty);
		
		nameField.setText(name);
		widthField.setText(width + "");
		heightField.setText(height + "");
		
		addObject(back);
		addObject(nameLabel, nameField);
		addObject(widthHeightLabel, widthField, heightField);
		addObject(emptyLabel, emptyMap);
		addObject(fillWithLabel, tileSelection);
		addObject(create);
	}

	@Override
	public void drawScreen(int mXIn, int mYIn) {
		drawRect(EColors.dsteel);
		
		drawStringC("Create New Map", midX, startY + 50);
		
		if (drawError) { drawStringC(error, midX, endY - 150); }
	}
	
	@Override public void onScreenClosed() {}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == back) { closeScreen(); }
		
		if (object == emptyMap) {
			empty = emptyMap.isChecked();
			fillWithLabel.setVisible(!empty);
			tileSelection.setVisible(!empty);
		}
		
		if (object == create || object == nameField) {
			if (check()) { createMap(); }
			else { drawError = true; }
		}
	}
	
	private boolean check() {
		boolean good = true;
		
		try {
			name = nameField.getText();
			width = Integer.parseInt(widthField.getText());
			height = Integer.parseInt(heightField.getText());
		}
		catch (Exception e) {
			good = false;
			e.printStackTrace();
			error = e.toString();
		}
		
		if (name == null || name.isBlank() || name.isEmpty()) { good = false; error = "Map name cannot be empty!"; }
		if (width < 0 || height < 0) { good = false; error = "Map width or height cannot be negative!"; }
		
		return good;
	}
	
	private void createMap() {
		world = new GameWorld(name, width, height);
		if (!empty) { world.fillWith(fillTile); }
		QoT.displayScreen(new MapEditorScreen(world), this);
	}
	
}
