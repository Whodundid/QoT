package gameScreens.mapTest;

import eWindow.windowObjects.actionObjects.WindowButton;
import eWindow.windowObjects.actionObjects.WindowTextField;
import eWindow.windowObjects.basicObjects.WindowLabel;
import eWindow.windowTypes.interfaces.IActionObject;
import gameSystems.gameRenderer.GameScreen;
import java.io.File;
import main.Game;
import util.renderUtil.EColors;

public class MapMenuScreen extends GameScreen {
	
	WindowButton newMap, loadMap;
	WindowButton back;
	WindowLabel nameLabel;
	WindowTextField nameField;
	
	@Override public void initScreen() {}
	
	@Override
	public void initObjects() {
		newMap = new WindowButton(this, midX - 100, midY - 200, 200, 45, "New Map");
		loadMap = new WindowButton(this, midX - 100, newMap.endY + 5, 200, 45, "Load Map");
		
		back = new WindowButton(this, 5, endY - 45, 150, 40, "Back");
		
		nameLabel = new WindowLabel(this, midX - 150, loadMap.endY + 60, "Map Name");
		nameField = new WindowTextField(this, midX - 150, nameLabel.endY + 20, 300, 35);
		
		addObject(newMap, loadMap);
		addObject(back);
		addObject(nameLabel, nameField);
	}

	@Override
	public void drawScreen(int mXIn, int mYIn) {
		drawRect(EColors.pdgray);
		drawRect(midX - 252, midY - 252, midX + 252, midY + 52, EColors.black);
		drawRect(midX - 250, midY - 250, midX + 250, midY + 50, EColors.dgray);
		
		drawStringC("Map Editor", midX, startY + 50);
	}

	@Override public void onScreenClosed() {}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == newMap) { Game.displayScreen(new NewMapCreatorScreen(nameField.getText()), this); }
		if (object == loadMap) { Game.displayScreen(new MapEditorScreen(new File(nameField.getText())), this); }
		
		if (object == back) { closeScreen(); }
	}
	
}
