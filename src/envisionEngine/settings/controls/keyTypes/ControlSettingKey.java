package envisionEngine.settings.controls.keyTypes;

import envisionEngine.settings.controls.KeyActionType;
import envisionEngine.settings.controls.KeyBinding;
import envisionEngine.settings.controls.util.KeyCombo;

public class ControlSettingKey extends KeyBinding {

	public ControlSettingKey(String name, KeyCombo defaultKeys) {
		super(name, defaultKeys, true, KeyActionType.GAMESETTING, "Game Settings");
	}
	
	@Override
	public void executeHotKeyAction() {
		
	}
	
}
