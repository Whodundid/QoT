package envision.game.world.mapEditor;

import eutil.colors.EColors;
import game.QoT;
import game.assets.textures.editor.EditorTextures;
import game.settings.QoTSettings;

import java.io.File;

import envision.game.screens.GameScreen;
import envision.windowLib.bundledWindows.fileExplorer.FileExplorerWindow;
import envision.windowLib.windowObjects.actionObjects.WindowButton;
import envision.windowLib.windowObjects.actionObjects.WindowTextField;
import envision.windowLib.windowObjects.basicObjects.WindowLabel;
import envision.windowLib.windowTypes.interfaces.IActionObject;
import envision.windowLib.windowUtil.ObjectPosition;

public class MapMenuScreen extends GameScreen {
	
	WindowButton newMap, mapDir, loadCur;
	WindowButton back;
	WindowLabel nameLabel;
	WindowTextField nameField;
	FileExplorerWindow explorer;
	
	String error;
	
	public MapMenuScreen() {
		super();
		aliases.add("mapeditor", "editor", "leveleditor");
	}
	
	@Override
	public void initScreen() {
		error = null;
	}
	
	@Override
	public void initChildren() {
		newMap = new WindowButton(this, midX - 100, midY - 200, 200, 45, "New Map");
		mapDir = new WindowButton(this, midX - 100, newMap.endY + 5, 200, 45, "Map Folder");
		
		back = new WindowButton(this, 5, endY - 45, 150, 40, "Back");
		
		nameLabel = new WindowLabel(this, midX - 150, mapDir.endY + 60, "Map Name");
		nameField = new WindowTextField(this, midX - 150, nameLabel.endY + 20, 300, 35);
		
		loadCur = new WindowButton(this, nameField.endX + 10, nameLabel.endY + 19, nameField.height + 1, nameField.height + 1);
		loadCur.setButtonTexture(EditorTextures.play);
		
		nameField.setText(QoTSettings.lastEditorMap.get().replace(".twld", ""));
		
		addObject(newMap, mapDir);
		addObject(back);
		addObject(nameLabel, nameField);
		addObject(loadCur);
		
		setDefaultFocusObject(nameField);
	}

	@Override
	public void drawScreen(int mXIn, int mYIn) {
		drawRect(EColors.pdgray);
		drawRect(midX - 252, midY - 252, midX + 252, midY + 82, EColors.black);
		drawRect(midX - 250, midY - 250, midX + 250, midY + 80, EColors.dgray);
		
		drawStringC("Map Editor", midX, startY + 50);
		
		if (error != null) {
			drawStringC(error, midX, endY - 100);
		}
	}

	@Override public void onScreenClosed() {}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == newMap) newMap();
		if (object == loadCur || object == nameField) loadMap();
		if (object == mapDir) openMapChooser();
		if (object == back) closeScreen();
		if (object == explorer) onExplorerPick();
	}
	
	//--------------------------
	
	private void newMap() {
		QoTSettings.lastEditorMap.set(nameField.getText());
		QoTSettings.saveConfig();
		QoT.displayScreen(new NewMapCreatorScreen(), this);
	}
	
	private void loadMap() {
		if (nameField.isNotEmpty()) {
			String lastMap = nameField.getText();
			//lastMap = (lastMap.endsWith(".twld")) ? lastMap : lastMap + ".twld";
			
			QoTSettings.lastEditorMap.set(lastMap);
			QoTSettings.saveConfig();
			
			File f = new File(QoTSettings.getEditorWorldsDir(), lastMap);
			if (f.exists()) {
				QoT.displayScreen(new MapEditorScreen(f), this);
			}
			else {
				error = "'" + lastMap + "' does not exist!";
			}
		}
	}
	
	private void openMapChooser() {
		explorer = new FileExplorerWindow(this, QoTSettings.getEditorWorldsDir(), true);
		explorer.setTitle("Map Selection");
		setDefaultFocusObject(null);
		displayWindow(explorer, ObjectPosition.SCREEN_CENTER);
	}
	
	private void onExplorerPick() {
		if (explorer == null) return;
		
		File f = explorer.getSelectedFile();
		
		if (f != null && f.exists()) {
			QoTSettings.lastEditorMap.set(f.getName());
			QoTSettings.saveConfig();
			explorer.close();
			QoT.displayScreen(new MapEditorScreen(f), this);
		}
	}
	
}
