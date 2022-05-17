package engine.settings.controls.keyTypes;

import engine.settings.controls.KeyActionType;
import engine.settings.controls.KeyBinding;
import engine.settings.controls.util.KeyCombo;

public class ControlSettingKey extends KeyBinding {

	public ControlSettingKey(String name, KeyCombo defaultKeys) {
		super(name, defaultKeys, true, KeyActionType.GAMESETTING, "Game Settings");
	}
	
	@Override
	public void executeHotKeyAction() {
		
	}
	
}
