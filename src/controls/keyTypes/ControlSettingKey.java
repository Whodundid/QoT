package controls.keyTypes;

import controls.KeyActionType;
import controls.KeyBinding;
import controls.util.KeyCombo;

public class ControlSettingKey extends KeyBinding {

	public ControlSettingKey(String name, KeyCombo defaultKeys) {
		super(name, defaultKeys, true, KeyActionType.GAMESETTING, "Game Settings");
	}
	
	@Override
	public void executeHotKeyAction() {
		
	}
	
}
