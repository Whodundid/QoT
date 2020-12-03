package gameScreens.mapTest;

import eWindow.windowObjects.actionObjects.WindowButton;
import eWindow.windowObjects.actionObjects.WindowTextField;
import eWindow.windowObjects.basicObjects.WindowLabel;
import eWindow.windowTypes.interfaces.IActionObject;
import gameScreens.mapTest.mapEditor.MapEditorScreen;
import gameSystems.gameRenderer.GameScreen;
import gameSystems.mapSystem.GameWorld;
import java.io.File;
import main.Game;
import util.renderUtil.EColors;

public class MapMenuScreen extends GameScreen {
	
	WindowButton newMap, loadMap, convert;
	WindowButton back;
	WindowLabel nameLabel;
	WindowTextField nameField;
	
	String error;
	
	@Override
	public void initScreen() {
		error = null;
	}
	
	@Override
	public void initObjects() {
		newMap = new WindowButton(this, midX - 100, midY - 200, 200, 45, "New Map");
		loadMap = new WindowButton(this, midX - 100, newMap.endY + 5, 200, 45, "Load Map");
		convert = new WindowButton(this, 10, 10, 200, 45, "Convert");
		
		back = new WindowButton(this, 5, endY - 45, 150, 40, "Back");
		
		nameLabel = new WindowLabel(this, midX - 150, loadMap.endY + 60, "Map Name");
		nameField = new WindowTextField(this, midX - 150, nameLabel.endY + 20, 300, 35);
		
		addObject(convert);
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
		
		if (error != null) {
			drawStringC(error, midX, endY - 100);
		}
	}

	@Override public void onScreenClosed() {}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == newMap) { Game.displayScreen(new NewMapCreatorScreen(nameField.getText()), this); }
		if (object == loadMap) {
			if (nameField.isNotEmpty()) {
				String test = nameField.getText();
				test = (test.endsWith(".twld")) ? test : test + ".twld";
				File f = new File(Game.settings.getEditorWorldsDir(), test);
				if (f.exists()) {
					Game.displayScreen(new MapEditorScreen(f), this);
				}
				else {
					error = "'" + test + "' does not exist!";
				}
			}
		}
		
		if (object == nameField) { actionPerformed(loadMap); }
		
		if (object == convert) {
			if (nameField.isNotEmpty()) {
				String test = nameField.getText();
				test = (test.endsWith(".twld")) ? test : test + ".twld";
				File f = new File(Game.settings.getEditorWorldsDir(), test);
				if (f.exists()) {
					GameWorld w = new GameWorld(f);
					w.convertFile();
				}
				else {
					error = "'" + test + "' does not exist!";
				}
			}
		}
		
		if (object == back) { closeScreen(); }
	}
	
}
