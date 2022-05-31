package main.settings.controls.keyTypes;

import main.settings.controls.KeyActionType;
import main.settings.controls.KeyBinding;
import main.settings.controls.util.KeyCombo;

public class ControlSettingKey extends KeyBinding {

	public ControlSettingKey(String name, KeyCombo defaultKeys) {
		super(name, defaultKeys, true, KeyActionType.GAMESETTING, "Game Settings");
	}
	
	@Override
	public void executeHotKeyAction() {
		
	}
	
}
