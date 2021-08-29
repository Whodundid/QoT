package mapEditor;

import assets.textures.EditorTextures;
import eutil.colors.EColors;
import eutil.storage.EDims;
import gameScreens.screenUtil.GameScreen;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import main.QoT;
import windowLib.windowObjects.actionObjects.WindowButton;
import windowLib.windowObjects.actionObjects.WindowTextField;
import windowLib.windowObjects.basicObjects.WindowLabel;
import windowLib.windowTypes.interfaces.IActionObject;
import world.GameWorld;

public class MapMenuScreen extends GameScreen {
	
	WindowButton newMap, mapDir, convert, loadCur;
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
		mapDir = new WindowButton(this, midX - 100, newMap.endY + 5, 200, 45, "Map Folder");
		convert = new WindowButton(this, 10, 10, 200, 45, "Convert");
		
		back = new WindowButton(this, 5, endY - 45, 150, 40, "Back");
		
		nameLabel = new WindowLabel(this, midX - 150, mapDir.endY + 60, "Map Name");
		nameField = new WindowTextField(this, midX - 150, nameLabel.endY + 20, 300, 35);
		
		loadCur = new WindowButton(this, nameField.endX + 10, nameLabel.endY + 19, nameField.height + 1, nameField.height + 1);
		loadCur.setButtonTexture(EditorTextures.play);
		
		nameField.setText(QoT.settings.lastMap.get());
		
		addObject(convert);
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
		if (object == newMap) {
			QoT.settings.lastMap.set(nameField.getText());
			QoT.saveConfig();
			QoT.displayScreen(new NewMapCreatorScreen(), this);
		}
		
		if (object == loadCur || object == nameField) {
			if (nameField.isNotEmpty()) {
				String lastMap = nameField.getText();
				lastMap = (lastMap.endsWith(".twld")) ? lastMap : lastMap + ".twld";
				
				QoT.settings.lastMap.set(lastMap);
				QoT.saveConfig();
				
				File f = new File(QoT.settings.getEditorWorldsDir(), lastMap);
				if (f.exists()) {
					QoT.displayScreen(new MapEditorScreen(f), this);
				}
				else {
					error = "'" + lastMap + "' does not exist!";
				}
			}
		}
		
		if (object == mapDir) {
			JFileChooser fc = new JFileChooser() {
				@Override
				protected JDialog createDialog(Component parent) throws HeadlessException {
					JDialog dlg = super.createDialog(parent);
					EDims d = QoT.getWindowDims();
					Dimension fd = getSize();
					dlg.setLocation((int) (d.startX + (d.width - fd.width) / 2), (int) (d.startY + (d.height - fd.height) / 2));
					dlg.setModal(true);
					//dlg.setAlwaysOnTop(true);
					return dlg;
				}
			};
			
			fc.setCurrentDirectory(QoT.settings.getEditorWorldsDir());
			fc.setDialogTitle("Map Selection");
			fc.setApproveButtonText("Open");
			fc.showDialog(null, "Open");
			File f = fc.getSelectedFile();
			
			if (f != null && f.exists() && f.getName().endsWith(".twld")) {
				QoT.settings.lastMap.set(f.getName());
				QoT.saveConfig();
				QoT.displayScreen(new MapEditorScreen(f), this);
			}
		}
		
		if (object == convert) {
			if (nameField.isNotEmpty()) {
				String lastMap = nameField.getText();
				lastMap = (lastMap.endsWith(".twld")) ? lastMap : lastMap + ".twld";
				
				QoT.settings.lastMap.set(lastMap);
				QoT.saveConfig();
				
				File f = new File(QoT.settings.getEditorWorldsDir(), lastMap);
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
