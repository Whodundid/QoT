package engine.topOverlay.desktopOverlay;

import java.io.File;

import assets.textures.TaskBarTextures;
import engine.inputHandlers.Keyboard;
import engine.renderEngine.fontRenderer.FontRenderer;
import engine.renderEngine.textureSystem.GameTexture;
import engine.windowLib.windowTypes.WindowObject;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import main.QoT;

public class OverlayShortcut extends WindowObject {
	
	//--------
	// Fields
	//--------
	
	private ShortcutType type;
	private File fileTarget;
	private String terminalCommandTarget;
	private String envisionProgramTarget;
	private WindowObject<?> windowTarget;
	private GameTexture icon;
	private String description = "New Shortcut";
	private boolean selected = false;
	
	//--------------
	// Constructors
	//--------------
	
	public OverlayShortcut(int x, int y) {
		init(x, y);
	}
	
	public OverlayShortcut(int x, int y, File fileIn) {
		type = ShortcutType.FILE;
		fileTarget = fileIn;
		init(x, y);
	}
	
	public OverlayShortcut(int x, int y, WindowObject<?> windowIn) {
		type = ShortcutType.PROGRAM;
		windowTarget = windowIn;
		init(x, y);
	}
	
	public OverlayShortcut(int x, int y, ShortcutType typeIn, String target) {
		if (typeIn == ShortcutType.COMMAND) terminalCommandTarget = target;
		else if (typeIn == ShortcutType.SCRIPT) envisionProgramTarget = target;
		else throw new IllegalArgumentException("Invalid shortcut type! '" + typeIn + "'!");
		type = typeIn;
		init(x, y);
	}
	
	//------
	// init
	//------
	
	private void init(int x, int y) {
		double w = 64;
		double h = 64;
		
		init(QoT.getTopRenderer(), 0, 0, w, h);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		super.drawObject(mXIn, mYIn);
		
		//draw icon
		GameTexture tex = icon;
		if (tex == null) tex = TaskBarTextures.window;
		drawTexture(icon);
		
		//draw hover color
		var color = EColors.lgray;
		if (isMouseInside()) drawHRect(color.opacity(200));
		if (selected) {
			if (isMouseInside()) drawRect(color.opacity(100));
			else drawRect(color.opacity(60));
		}
		
		//draw description text below icon
		drawStringC(description, midX, endY + FontRenderer.FONT_HEIGHT + 6, EColors.chalk);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
		
		//only care if left mouse button
		if (button == 0) {
			selected = !selected;
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		super.keyPressed(typedChar, keyCode);
		
		if (selected) {
			//if enter -- attempt to run shortcut
			if (keyCode == Keyboard.KEY_ENTER) {
				openShortcut();
			}
			
			//if delete -- attempt to remove shortcut from main over
			if (keyCode == Keyboard.KEY_DELETE) {
				close();
			}
		}
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		super.actionPerformed(object, args);
		
		
	}
	
	//---------
	// Methods
	//---------
	
	public void openShortcut() {
		switch (type) {
		case FILE:
			
		case COMMAND:
			QoT.getTerminalHandler().executeCommand(null, description);
		case SCRIPT:
		case PROGRAM:
		}
	}
	
	private void openRCM() {
		
	}
	
	//---------
	// Setters
	//---------
	
	public void setIcon(GameTexture iconIn) { icon = iconIn; }
	public void setDescription(String descIn) { description = descIn; }
	public void setSelected(boolean val) { selected = val; }
	
}
