package engine.screens;

import engine.renderEngine.fontRenderer.FontRenderer;
import engine.screens.screenUtil.GameScreen;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import eutil.math.EDimension;
import eutil.math.NumberUtil;
import game.entities.Player;
import main.QoT;
import main.settings.QoT_Settings;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

import world.GameWorld;

public class WorldSelectScreen extends GameScreen {
	
	WindowButton defaultWorld, loadWorld, lastEditor, lastWorld;
	WindowButton back;
	
	String error;
	
	public WorldSelectScreen() {
		super();
		aliases.add("worlds", "worldselect", "newgame");
	}
	
	@Override
	public void initScreen() {
		error = null;
	}
	
	@Override
	public void initObjects() {
		int w = NumberUtil.clamp(QoT.getWidth() / 4, 150, 350);
		double bw = midX - w / 2;
		
		defaultWorld = new WindowButton(this, bw, midY - 200, w, 45, "Default Map");
		loadWorld = new WindowButton(this, bw, defaultWorld.endY + 5, w, 45, "Load World");
		lastEditor = new WindowButton(this, bw, loadWorld.endY + 100, w, 45, QoT_Settings.lastEditorMap.get());
		lastWorld = new WindowButton(this, bw, lastEditor.endY + 40, w, 45, QoT_Settings.lastMap.get());
		
		back = new WindowButton(this, 5, endY - 45, 150, 40, "Back");
		
		addObject(defaultWorld, loadWorld);
		if (!lastEditor.getString().isBlank() && !lastEditor.getString().isEmpty()) {
			addObject(lastEditor);
		}
		addObject(lastWorld);
		addObject(back);
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		drawRect(EColors.pdgray);
		drawRect(midX - 252, midY - 252, midX + 252, midY + 178, EColors.black);
		drawRect(midX - 250, midY - 250, midX + 250, midY + 176, EColors.steel);
		
		//divider between default/load and last maps
		drawRect(midX - 250, midY - 68, midX + 250, midY - 66, EColors.black);
		
		drawStringC("Chose a World", midX, (midY - 250) / 2, EColors.aquamarine);
		drawStringC("Last Editor World", midX, lastEditor.startY - FontRenderer.FONT_HEIGHT - 4, EColors.skyblue);
		drawStringC("Last World", midX, lastWorld.startY - FontRenderer.FONT_HEIGHT - 4, EColors.skyblue);
		
		if (error != null) {
			drawStringC(error, midX, endY - 100);
		}
	}
	
	@Override public void onScreenClosed() {}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == defaultWorld) {
			File f = new File(QoT_Settings.getEditorWorldsDir(), "test.twld");
			if (f.exists()) {
				Player p = QoT.setPlayer(new Player("Test"));
				GameWorld w = QoT.loadWorld(new GameWorld(f));
				w.addEntity(p);
				QoT.displayScreen(new GamePlayScreen(), new MainMenuScreen());
				//Game.displayScreen(new WorldRenderTest(new File("test.twld")), this);
			}
		}
		
		if (object == loadWorld) {
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
				Player p = QoT.setPlayer(new Player("Test"));
				GameWorld w = QoT.loadWorld(new GameWorld(f));
				w.addEntity(p);
				QoT.displayScreen(new GamePlayScreen(), new MainMenuScreen());
			}
		}
		
		if (object == lastEditor) {
			String last = QoT_Settings.lastEditorMap.get();
			if (last.isBlank() || last.isEmpty()) error = "There is no last editor map!";
			else {
				Player p = QoT.setPlayer(new Player("Test"));
				GameWorld w = QoT.loadWorld(new GameWorld(new File(last)));
				w.addEntity(p);
				QoT.displayScreen(new GamePlayScreen(), new MainMenuScreen());
				//Game.displayScreen(new WorldRenderTest(new File(last)), this);
			}
		}
		
		if (object == lastWorld) {
			String last = QoT_Settings.lastMap.get();
			if (last.isBlank() || last.isEmpty()) error = "There is no last editor map!";
			else {
				Player p = QoT.setPlayer(new Player("Test"));
				GameWorld w = QoT.loadWorld(new GameWorld(new File(last)));
				w.addEntity(p);
				QoT.displayScreen(new GamePlayScreen(), new MainMenuScreen());
				//Game.displayScreen(new WorldRenderTest(new File(last)), this);
			}
		}
		
		if (object == back) closeScreen();
	}
	
}
