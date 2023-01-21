package envision.settings.controls.keyTypes;

import envision.settings.controls.KeyActionType;
import envision.settings.controls.KeyBinding;
import envision.settings.controls.util.KeyCombo;

public class ControlSettingKey extends KeyBinding {

	public ControlSettingKey(String name, KeyCombo defaultKeys) {
		super(name, defaultKeys, true, KeyActionType.GAMESETTING, "Game Settings");
	}
	
	@Override
	public void executeHotKeyAction() {
		
	}
	
}
