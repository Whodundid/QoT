package qot.screens.gameplay;

import envision.Envision;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowTypes.WindowParent;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import qot.screens.main.MainMenuScreen;

public class ConfirmationWindow extends WindowParent {
	private GamePlayScreen parent;
	
	private WindowButton<?> yes, no;
	
	private String confirmationText;
	
	private boolean open;
	
	/**
	 * Main constructor takes in the GameScreen that holds Windows, the x and y position, and the confirmation text that we want to ask
	 * the user within the window's screen.
	 * @param parentScreen
	 * @param x
	 * @param y
	 * @param confirmationTextIn
	 */
	public ConfirmationWindow(GamePlayScreen parentScreen, int x, int y, String confirmationTextIn) {
		init(parentScreen, x, y, 550, 200);
		
		setObjectName("Confirmation Window");
		
		setMaxDims(800, 400);
		setMinDims(400, 200);
		
		setMinimizable(false);
		setResizeable(false);
		setMoveable(false);
		
		parent = parentScreen;
		confirmationText = confirmationTextIn;
		
		open = true;
	}
	
	/**
	 * Funnels the Default message "Are You Sure You Wish To Exit?" to the constructor above
	 * @param parentScreen
	 * @param x
	 * @param y
	 */
	public ConfirmationWindow(GamePlayScreen parentScreen, int x, int y) {
		this(parentScreen, x, y, "Are You Sure You Wish To Exit?");
	}
	
	@Override public void initChildren() {
		Envision.pause();
		open = true;
		
		defaultHeader();
		
		var w = width - 460;
		var sX = midX - (w / 2) - 30;
		var gap = 5;
		
		yes = new WindowButton<>(this, sX, startY + 20, w, 50, "Yes");
		no = new WindowButton<>(this, yes.endX + 50, startY + 20, w, 50, "No");
		
		// Makes it so the yes and no button are subscribed to this Window
		IActionObject.setActionReceiver(this, yes, no);
		
		addObject(yes, no);
	}
	
	@Override public void onFirstDraw() {
		var top = getTopParent();
		top.setFocusLockObject(this);
		top.setEscapeStopper(this);
	}
	
	/**
	 * Draws the object rectangle, background, and String. Rendering of the button takes place in initChildren()
	 */
	@Override public void drawObject(int mXIn, int mYIn) {
		drawRect(0, 0, Envision.getWidth(), Envision.getHeight(), EColors.vdgray.opacity(150));
		drawDefaultBackground();
		drawStringC(confirmationText, midX, startY, EColors.blue);
	}
	
	/**
	 * If the button that is clicked is yes, we want to unload the world and display the main menu screen, otherwise we close the ConfirmationWindow
	 */
	@Override public void actionPerformed(IActionObject object, Object... args) {
		if (object == yes) {
			Envision.loadWorld(null);
			Envision.displayScreen(new MainMenuScreen());
		}
		
		if (object == no) { close(); }
	}
	
	/**
	 * When the ConfirmationWindow gets closed, unpause the game.
	 */
	@Override public void onClosed() {
		open = false;
		Envision.unpause();
	}
	
	public boolean isOpen() { return open; }
}