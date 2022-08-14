package main.settings.controls;

import engine.windowLib.windowUtil.input.KeyboardInputAcceptor;
import engine.windowLib.windowUtil.input.MouseInputAcceptor;
import eutil.datatypes.EArrayList;
import eutil.datatypes.EList;

public class PlayerControlProfile implements MouseInputAcceptor, KeyboardInputAcceptor {
	
	public static final String defaultName = "Unnamed Profile";
	
	private String profileName = "";
	private EList<KeyBinding> keyBindings = new EArrayList<>();
	
	//public KeyBinding moveUp = new KeyBinding();
	//public KeyBinding moveRight = new KeyBinding();
	//public KeyBinding moveDown = new KeyBinding();
	//public KeyBinding moveLeft = new KeyBinding();
	//public KeyBinding openInventory = new KeyBinding();
	//public KeyBinding openTerminal = new KeyBinding();
	
	//interact key
	//open party list
	//open quest log
	
	public PlayerControlProfile() { this(defaultName); }
	public PlayerControlProfile(String nameIn) {
		profileName = nameIn;
		
		//keyBindings.add(moveUp);
		//keyBindings.add(moveRight);
		//keyBindings.add(moveDown);
		//keyBindings.add(moveLeft);
		//keyBindings.add(openInventory);
		//keyBindings.add(openTerminal);
	}
	
	public EList<KeyBinding> getKeyBindings() { return keyBindings; }
	
	@Override
	public void handleMouseInput(int action, int mXIn, int mYIn, int button, int change) {
		
	}
	
	@Override
	public void handleKeyboardInput(int action, char typedChar, int keyCode) {
		
	}
	
}
