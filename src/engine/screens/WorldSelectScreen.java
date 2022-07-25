package engine.screens;

import java.io.File;

import engine.renderEngine.fontRenderer.FontRenderer;
import engine.screens.screenUtil.GameScreen;
import engine.windowLib.bundledWindows.fileExplorer.FileExplorerWindow;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import engine.windowLib.windowUtil.ObjectPosition;
import eutil.colors.EColors;
import eutil.math.NumberUtil;
import game.entities.Player;
import main.QoT;
import main.settings.QoTSettings;
import world.GameWorld;

public class WorldSelectScreen extends GameScreen {
	
	WindowButton defaultWorld, loadWorld, lastEditor, lastWorld;
	WindowButton back;
	FileExplorerWindow explorer;
	
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
		lastEditor = new WindowButton(this, bw, loadWorld.endY + 100, w, 45, QoTSettings.lastEditorMap.get());
		lastWorld = new WindowButton(this, bw, lastEditor.endY + 40, w, 45, QoTSettings.lastMap.get());
		
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
		if (object == defaultWorld) loadDefault();
		if (object == loadWorld) openMapChooser();
		if (object == lastEditor) loadLastEditor();
		if (object == lastWorld) loadLastWorld();
		if (object == back) closeScreen();
		if (object == explorer) onExplorerPick();
	}
	
	private void loadDefault() {
		File f = new File(QoTSettings.getEditorWorldsDir(), "test.twld");
		if (f.exists()) {
			Player p = QoT.setPlayer(new Player("Test"));
			GameWorld w = QoT.loadWorld(new GameWorld(f));
			w.addEntity(p);
			QoT.displayScreen(new GamePlayScreen(), new MainMenuScreen());
			//Game.displayScreen(new WorldRenderTest(new File("test.twld")), this);
		}
	}
	
	private void openMapChooser() {
		explorer = new FileExplorerWindow(this, QoTSettings.getEditorWorldsDir(), true);
		explorer.setTitle("Map Selection");
		setDefaultFocusObject(null);
		displayWindow(explorer, ObjectPosition.SCREEN_CENTER);
	}
	
	private void loadLastEditor() {
		String last = QoTSettings.lastEditorMap.get();
		if (last.isBlank() || last.isEmpty()) error = "There is no last editor map!";
		else {
			Player p = QoT.setPlayer(new Player("Test"));
			GameWorld w = QoT.loadWorld(new GameWorld(new File(last)));
			w.addEntity(p);
			QoT.displayScreen(new GamePlayScreen(), new MainMenuScreen());
			//Game.displayScreen(new WorldRenderTest(new File(last)), this);
		}
	}
	
	private void loadLastWorld() {
		String last = QoTSettings.lastMap.get();
		if (last.isBlank() || last.isEmpty()) error = "There is no last editor map!";
		else {
			Player p = QoT.setPlayer(new Player("Test"));
			GameWorld w = QoT.loadWorld(new GameWorld(new File(last)));
			w.addEntity(p);
			QoT.displayScreen(new GamePlayScreen(), new MainMenuScreen());
			//Game.displayScreen(new WorldRenderTest(new File(last)), this);
		}
	}
	
	private void onExplorerPick() {
		if (explorer == null) return;
		
		File f = explorer.getSelectedFile();
		
		if (f != null && f.exists() && f.getName().endsWith(".twld")) {
			explorer.close();
			QoTSettings.lastMap.set(f.getName());
			QoTSettings.saveConfig();
			
			Player p = QoT.setPlayer(new Player("Test"));
			GameWorld w = QoT.loadWorld(new GameWorld(f));
			w.addEntity(p);
			
			QoT.displayScreen(new GamePlayScreen(), new MainMenuScreen());
		}
	}
	
}
