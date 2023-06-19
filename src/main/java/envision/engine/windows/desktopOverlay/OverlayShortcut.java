package envision.engine.windows.desktopOverlay;

import java.io.File;

import envision.Envision;
import envision.engine.inputHandlers.Keyboard;
import envision.engine.rendering.RenderingManager;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowTypes.interfaces.IWindowParent;
import envision.engine.windows.windowUtil.ObjectPosition;
import eutil.colors.EColors;
import qot.assets.textures.taskbar.TaskBarTextures;

public class OverlayShortcut extends WindowObject {
	
	//--------
	// Fields
	//--------
	
	private ShortcutType type;
	private File fileTarget;
	private String terminalCommandTarget;
	private String envisionProgramTarget;
	private IWindowParent<?> windowTarget;
	private Class<?> windowClassTarget;
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
	
	public OverlayShortcut(int x, int y, IWindowParent<?> windowIn) {
		type = ShortcutType.WINDOW;
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
		RenderingManager.drawStringC(description, drawX, endY + 6, scale, scale, EColors.chalk);
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
	public void onDoubleClick() {
		openShortcut();
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		super.actionPerformed(object, args);
	}
	
	//---------
	// Methods
	//---------
	
	public void openShortcut() {
		if (type == null) return;
		
		switch (type) {
		case FILE:
			break;
		case COMMAND:
			Envision.getTerminalHandler().executeCommand(null, description);
			break;
		case SCRIPT:
			//try { QoT.envision.runProgram(envisionProgramTarget); }
			//catch (Exception e) { e.printStackTrace(); }
			break;
		case WINDOW:
			if (windowClassTarget != null) {
				try {
					var constructor = windowClassTarget.getConstructor();
					if (constructor != null) {
						Envision.getTopScreen().displayWindow((IWindowParent<?>) constructor.newInstance(), ObjectPosition.EXISTING_OBJECT_INDENT);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (windowTarget != null) {
				Envision.getTopScreen().displayWindow(windowTarget, ObjectPosition.EXISTING_OBJECT_INDENT);
			}
			break;
		}
	}
	
	private void openRCM() {
		
	}
	
	//---------
	// Setters
	//---------
	
	public void setFileTarget(File fileIn) {
		type = ShortcutType.FILE;
		fileTarget = fileIn;
	}
	
	public void setWindowTarget(IWindowParent<?> windowIn) {
		type = ShortcutType.WINDOW;
		windowTarget = windowIn;
		windowClassTarget = null;
	}
	
	public void setWindowTarget(Class<?> windowClassIn) {
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