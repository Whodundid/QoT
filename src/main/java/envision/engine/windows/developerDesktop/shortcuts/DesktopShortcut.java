package envision.engine.windows.developerDesktop.shortcuts;

import java.io.File;

import envision.Envision;
import envision.engine.inputHandlers.Keyboard;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowTypes.interfaces.IWindowParent;
import eutil.colors.EColors;
import qot.assets.textures.taskbar.TaskBarTextures;

public abstract class DesktopShortcut extends WindowObject {
	
	//--------
	// Fields
	//--------
	
	private ShortcutType type;
	private File fileTarget;
	private String terminalCommandTarget;
	private String envisionProgramTarget;
	private IWindowParent windowTarget;
	private Class windowClassTarget;
	private GameTexture icon;
	private String description = "New Shortcut";
	private boolean selected = false;
	
	//--------------
	// Constructors
	//--------------
	
	public DesktopShortcut(int x, int y) {
		init(x, y);
	}
	
	public DesktopShortcut(int x, int y, File fileIn) {
		type = ShortcutType.FILE;
		fileTarget = fileIn;
		init(x, y);
	}
	
	public DesktopShortcut(int x, int y, IWindowParent windowIn) {
		type = ShortcutType.WINDOW;
		windowTarget = windowIn;
		init(x, y);
	}
	
	public DesktopShortcut(int x, int y, ShortcutType typeIn) {
	    type = typeIn;
        init(x, y);
	}
	
	public DesktopShortcut(int x, int y, ShortcutType typeIn, String target) {
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
		double w = 80;
		double h = 80;
		
		init(Envision.getTopScreen(), x, y, w, h);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
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
		
		double scale = 1;
		double drawX = midX;
		
		//draw description text below icon
		drawStringC(description, drawX, endY + 6, scale, scale, EColors.chalk);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
		
		if (button == 0) {
			selected = !selected;
		}
		else if (button == 1) {
		    
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		super.keyPressed(typedChar, keyCode);
		
		if (!selected) return;
		
        //if enter -- attempt to run shortcut
        if (keyCode == Keyboard.KEY_ENTER) {
            tryOpenShortcut();
        }
        
        //if delete -- attempt to remove shortcut from main over
        if (keyCode == Keyboard.KEY_DELETE) {
            close();
        }
	}
	
	@Override
	public void onDoubleClick() {
	    tryOpenShortcut();
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		super.actionPerformed(object, args);
	}
	
	//---------
	// Methods
	//---------
	
	public void tryOpenShortcut() {
	    try {
            openShortcut();         
        }
        catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public abstract void openShortcut() throws Exception;
	
	//---------
	// Setters
	//---------
	
	public void setFileTarget(File fileIn) {
		type = ShortcutType.FILE;
		fileTarget = fileIn;
	}
	
	public void setWindowTarget(IWindowParent windowIn) {
		type = ShortcutType.WINDOW;
		windowTarget = windowIn;
		windowClassTarget = null;
	}
	
	public void setWindowTarget(Class windowClassIn) {
		type = ShortcutType.WINDOW;
		windowClassTarget = windowClassIn;
		windowTarget = null;
	}
	
	public void setCommandTarget(String commandIn) {
		type = ShortcutType.COMMAND;
		terminalCommandTarget = commandIn;
	}
	
	public void setEnvisionTarget(String programIn) {
		type = ShortcutType.SCRIPT;
		envisionProgramTarget = programIn;
	}
	
	public void setIcon(GameTexture iconIn) { icon = iconIn; }
	public void setDescription(String descIn) { description = descIn; }
	public void setSelected(boolean val) { selected = val; }
	
}
