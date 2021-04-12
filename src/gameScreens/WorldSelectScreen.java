package gameScreens;

import assets.entities.player.Player;
import envisionEngine.eWindow.windowObjects.actionObjects.WindowButton;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import gameScreens.gameplay.GamePlayScreen;
import gameSystems.mapSystem.GameWorld;
import gameSystems.screenSystem.GameScreen;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import main.Game;
import mathUtil.NumberUtil;
import renderUtil.EColors;
import storageUtil.EDimension;

public class WorldSelectScreen extends GameScreen {
	
	WindowButton defaultWorld, lastEditor, loadWorld;
	WindowButton back;
	
	String error;
	
	@Override
	public void initScreen() {
		error = null;
	}
	
	@Override
	public void initObjects() {
		int w = NumberUtil.clamp(Game.getWidth() / 4, 150, 350);
		defaultWorld = new WindowButton(this, midX - w / 2, midY - 200, w, 45, "Default Map");
		loadWorld = new WindowButton(this, midX - w / 2, defaultWorld.endY + 5, w, 45, "Load World");
		lastEditor = new WindowButton(this, midX - w / 2, loadWorld.endY + 5, w, 45, Game.settings.lastMap.get());
		
		back = new WindowButton(this, 5, endY - 45, 150, 40, "Back");
		
		addObject(defaultWorld, loadWorld);
		if (!lastEditor.getString().isBlank() && !lastEditor.getString().isEmpty()) { addObject(lastEditor); }
		addObject(back);
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		drawRect(EColors.pdgray);
		drawRect(midX - 252, midY - 252, midX + 252, midY - 0, EColors.black);
		drawRect(midX - 250, midY - 250, midX + 250, midY - 2, EColors.steel);
		
		drawStringC("Chose a World", midX, (midY - 250) / 2);
		
		if (error != null) {
			drawStringC(error, midX, endY - 100);
		}
	}
	
	@Override public void onScreenClosed() {}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == defaultWorld) {
			File f = new File(Game.settings.getEditorWorldsDir(), "test.twld");
			if (f.exists()) {
				Player p = Game.setPlayer(new Player("Test"));
				GameWorld w = Game.loadWorld(new GameWorld(f));
				w.addEntity(p);
				Game.displayScreen(new GamePlayScreen(), new MainMenuScreen());
				//Game.displayScreen(new WorldRenderTest(new File("test.twld")), this);
			}
		}
		
		if (object == loadWorld) {
			JFileChooser fc = new JFileChooser() {
				@Override
				protected JDialog createDialog(Component parent) throws HeadlessException {
					JDialog dlg = super.createDialog(parent);
					EDimension d = Game.getWindowDims();
					Dimension fd = getSize();
					dlg.setLocation((int) (d.startX + (d.width - fd.width) / 2), (int) (d.startY + (d.height - fd.height) / 2));
					dlg.setModal(true);
					//dlg.setAlwaysOnTop(true);
					return dlg;
				}
			};
			
			fc.setCurrentDirectory(Game.settings.getEditorWorldsDir());
			fc.setDialogTitle("Map Selection");
			fc.setApproveButtonText("Open");
			fc.showDialog(null, "Open");
			File f = fc.getSelectedFile();
			
			if (f != null && f.exists() && f.getName().endsWith(".twld")) {
				Player p = Game.setPlayer(new Player("Test"));
				GameWorld w = Game.loadWorld(new GameWorld(f));
				w.addEntity(p);
				Game.displayScreen(new GamePlayScreen(), new MainMenuScreen());
			}
		}
		
		if (object == lastEditor) {
			String last = Game.settings.lastMap.get();
			if (last.isBlank() || last.isEmpty()) { error = "There is no last editor map!"; }
			else {
				Player p = Game.setPlayer(new Player("Test"));
				GameWorld w = Game.loadWorld(new GameWorld(new File(last)));
				w.addEntity(p);
				Game.displayScreen(new GamePlayScreen(), new MainMenuScreen());
				//Game.displayScreen(new WorldRenderTest(new File(last)), this);
			}
		}
		
		if (object == back) { closeScreen(); }
	}
	
}
