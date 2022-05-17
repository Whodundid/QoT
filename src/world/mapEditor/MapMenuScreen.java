package world.mapEditor;

import assets.textures.EditorTextures;
import engine.QoT;
import engine.screens.screenUtil.GameScreen;
import engine.settings.QoT_Settings;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowObjects.actionObjects.WindowTextField;
import engine.windowLib.windowObjects.basicObjects.WindowLabel;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import eutil.math.EDimension;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

public class MapMenuScreen extends GameScreen {
	
	WindowButton newMap, mapDir, loadCur;
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
		
		back = new WindowButton(this, 5, endY - 45, 150, 40, "Back");
		
		nameLabel = new WindowLabel(this, midX - 150, mapDir.endY + 60, "Map Name");
		nameField = new WindowTextField(this, midX - 150, nameLabel.endY + 20, 300, 35);
		
		loadCur = new WindowButton(this, nameField.endX + 10, nameLabel.endY + 19, nameField.height + 1, nameField.height + 1);
		loadCur.setButtonTexture(EditorTextures.play);
		
		nameField.setText(QoT_Settings.lastEditorMap.get());
		
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
			QoT_Settings.lastEditorMap.set(nameField.getText());
			QoT.saveConfig();
			QoT.displayScreen(new NewMapCreatorScreen(), this);
		}
		
		if (object == loadCur || object == nameField) {
			if (nameField.isNotEmpty()) {
				String lastMap = nameField.getText();
				lastMap = (lastMap.endsWith(".twld")) ? lastMap : lastMap + ".twld";
				
				QoT_Settings.lastEditorMap.set(lastMap);
				QoT.saveConfig();
				
				File f = new File(QoT_Settings.getEditorWorldsDir(), lastMap);
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
					EDimension d = QoT.getWindowDims();
					Dimension fd = getSize();
					dlg.setLocation((int) (d.startX + (d.width - fd.width) / 2), (int) (d.startY + (d.height - fd.height) / 2));
					dlg.setModal(true);
					//dlg.setAlwaysOnTop(true);
					return dlg;
				}
			};
			
			fc.setCurrentDirectory(QoT_Settings.getEditorWorldsDir());
			fc.setDialogTitle("Map Selection");
			fc.setApproveButtonText("Open");
			fc.showDialog(null, "Open");
			File f = fc.getSelectedFile();
			
			if (f != null && f.exists() && f.getName().endsWith(".twld")) {
				QoT_Settings.lastEditorMap.set(f.getName());
				QoT.saveConfig();
				QoT.displayScreen(new MapEditorScreen(f), this);
			}
		}
		
		if (object == back) { closeScreen(); }
	}
	
}
