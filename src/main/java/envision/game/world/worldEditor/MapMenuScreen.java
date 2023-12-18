package envision.game.world.worldEditor;

import java.io.File;

import envision.Envision;
import envision.engine.EngineSettings;
import envision.engine.screens.GameScreen;
import envision.engine.windows.bundledWindows.fileExplorer.FileExplorerWindow;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.actionObjects.WindowTextField;
import envision.engine.windows.windowObjects.basicObjects.WindowLabel;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowUtil.ObjectPosition;
import eutil.colors.EColors;
import qot.assets.textures.editor.EditorTextures;
import qot.settings.QoTSettings;

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
		
		nameField.setText(EngineSettings.lastEditorMap.get().replace(".twld", ""));
		
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
		EngineSettings.lastEditorMap.set(nameField.getText());
		Envision.saveEngineConfig();
		Envision.displayScreen(new NewMapCreatorScreen(), this);
	}
	
	private void loadMap() {
		if (nameField.isNotEmpty()) {
			String lastMap = nameField.getText();
			//lastMap = (lastMap.endsWith(".twld")) ? lastMap : lastMap + ".twld";
			
			EngineSettings.lastEditorMap.set(lastMap);
			Envision.saveEngineConfig();
			
			File f = new File(QoTSettings.getEditorWorldsDir(), lastMap);
			if (f.exists()) {
				Envision.displayScreen(new MapEditorScreen(f), this);
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
			EngineSettings.lastEditorMap.set(f.getName());
            Envision.saveEngineConfig();
			explorer.close();
			Envision.displayScreen(new MapEditorScreen(f), this);
		}
	}
	
}
