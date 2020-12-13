package gameScreens.mapEditor;

import envisionEngine.eWindow.windowObjects.actionObjects.WindowButton;
import envisionEngine.eWindow.windowObjects.actionObjects.WindowTextField;
import envisionEngine.eWindow.windowObjects.basicObjects.WindowLabel;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import gameScreens.mapEditor.editorScreen.MapEditorScreen;
import gameSystems.gameRenderer.GameScreen;
import gameSystems.mapSystem.GameWorld;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import main.Game;
import util.renderUtil.EColors;
import util.storageUtil.EDimension;

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
		
		nameField.setText(Game.settings.lastMap.get());
		
		addObject(convert);
		addObject(newMap, loadMap);
		addObject(back);
		addObject(nameLabel, nameField);
		
		setDefaultFocusObject(nameField);
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
		if (object == newMap) {
			Game.settings.lastMap.set(nameField.getText());
			Game.saveConfig();
			Game.displayScreen(new NewMapCreatorScreen(), this);
		}
		
		if (object == nameField) {
			if (nameField.isNotEmpty()) {
				String lastMap = nameField.getText();
				lastMap = (lastMap.endsWith(".twld")) ? lastMap : lastMap + ".twld";
				
				Game.settings.lastMap.set(lastMap);
				Game.saveConfig();
				
				File f = new File(Game.settings.getEditorWorldsDir(), lastMap);
				if (f.exists()) {
					Game.displayScreen(new MapEditorScreen(f), this);
				}
				else {
					error = "'" + lastMap + "' does not exist!";
				}
			}
		}
		
		if (object == loadMap) {
			JFileChooser fc = new JFileChooser() {
				@Override
				protected JDialog createDialog(Component parent) throws HeadlessException {
					JDialog dlg = super.createDialog(parent);
					EDimension d = Game.getWindowDims();
					Dimension fd = getSize();
					dlg.setLocation((int) (d.startX + (d.width - fd.width) / 2), (int) (d.startY + (d.height - fd.height) / 2));
					return dlg;
				}
			};
			
			fc.setCurrentDirectory(Game.settings.getEditorWorldsDir());
			fc.setDialogTitle("Map Selection");
			fc.setApproveButtonText("Open");
			fc.showDialog(null, "Open");
			File f = fc.getSelectedFile();
			
			if (f != null && f.exists() && f.getName().endsWith(".twld")) {
				Game.settings.lastMap.set(f.getName());
				Game.displayScreen(new MapEditorScreen(f), this);
			}
		}
		
		if (object == convert) {
			if (nameField.isNotEmpty()) {
				String lastMap = nameField.getText();
				lastMap = (lastMap.endsWith(".twld")) ? lastMap : lastMap + ".twld";
				
				Game.settings.lastMap.set(lastMap);
				Game.saveConfig();
				
				File f = new File(Game.settings.getEditorWorldsDir(), lastMap);
				if (f.exists()) {
					GameWorld w = new GameWorld(f);
					w.convertFileA();
					error = null;
				}
				else {
					error = "'" + lastMap + "' does not exist!";
				}
			}
		}
		
		if (object == back) { closeScreen(); }
	}
	
}
