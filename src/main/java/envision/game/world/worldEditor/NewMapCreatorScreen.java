package envision.game.world.worldEditor;

import envision.Envision;
import envision.engine.screens.GameScreen;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.actionObjects.WindowCheckBox;
import envision.engine.windows.windowObjects.actionObjects.WindowTextField;
import envision.engine.windows.windowObjects.basicObjects.WindowLabel;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.game.world.GameWorld;
import envision.game.world.worldTiles.WorldTile;
import eutil.colors.EColors;
import qot.world_tiles.categories.NatureTiles;

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
	WorldTile fillTile = NatureTiles.grass;
	
	public NewMapCreatorScreen() { this((String) null); }
	public NewMapCreatorScreen(String nameIn) {
		super();
		name = nameIn;
	}
	
	public NewMapCreatorScreen(GameWorld worldIn) {
		super();
		if (worldIn != null) {
			name = worldIn.getWorldName();
			width = worldIn.getWidth();
			height = worldIn.getHeight();
		}
	}
	
	@Override
	public void initChildren() {
		double x = midX - 150;
		double y = midY - 250;
		
		back = new WindowButton(this, 5, endY - 45, 150, 40, "Back");
		
		nameLabel = new WindowLabel(this, x, y, "Map name");
		nameField = new WindowTextField(this, nameLabel.startX, nameLabel.endY + 5, 300, 35);
		
		widthHeightLabel = new WindowLabel(this, x, nameField.endY + 30, "Width / Height");
		widthField = new WindowTextField(this, x, widthHeightLabel.endY + 5, 100, 35);
		heightField = new WindowTextField(this, widthField.endX + 10, widthHeightLabel.endY + 5, 100, 35);
		
		emptyLabel = new WindowLabel(this, x, widthField.endY + 42, "Empty map");
		emptyMap = new WindowCheckBox(this, emptyLabel.endX + 20, widthField.endY + 30, 50, 50);
		
		fillWithLabel = new WindowLabel(this, x, emptyMap.endY + 35, "Fill with");
		tileSelection = new WindowButton(this, emptyMap.startX, emptyMap.endY + 20, 50, 50);
		
		create = new WindowButton(this, midX - 100, tileSelection.endY + 50, 200, 45, "Create");
		
		tileSelection.setButtonTexture(fillTile.sprite);
		
		empty = emptyMap.getIsChecked();
		fillWithLabel.setVisible(!empty);
		tileSelection.setVisible(!empty);
		
		nameField.setTextWhenEmpty("name");
		//if (name == null) nameField.setText("name");
		//else nameField.setText(name);
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
	public void drawScreen(float dt, int mXIn, int mYIn) {
		drawRect(EColors.dsteel);
		
		drawStringC("Create New Map", midX, startY + 50);
		
		if (drawError) drawStringC(error, midX, endY - 150, EColors.lred);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == back) closeScreen();
		
		if (object == emptyMap) {
			empty = emptyMap.getIsChecked();
			fillWithLabel.setVisible(!empty);
			tileSelection.setVisible(!empty);
		}
		
		if (object == create || object == nameField) {
			if (check()) createMap();
			else drawError = true;
		}
		
		if (object == tileSelection) {
			error = "Not yet implemented!";
			drawError = true;
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
		
		if (name == null || name.isBlank() || name.isEmpty()) {
			good = false;
			error = "Map name cannot be empty!";
		}
		
		if (width < 0 || height < 0) {
			good = false;
			error = "Map width or height cannot be negative!";
		}
		
		return good;
	}
	
	private void createMap() {
		world = new GameWorld(name, width, height);
		if (!empty) world.fillWith(fillTile);
		Envision.displayScreen(new MapEditorScreen(world), this);
	}
	
}
