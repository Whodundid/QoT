package envision.engine.internal.settings.controls.keyTypes;

import envision.engine.internal.settings.controls.KeyActionType;
import envision.engine.internal.settings.controls.KeyBinding;
import envision.engine.internal.settings.controls.util.KeyCombo;

public class ControlSettingKey extends KeyBinding {

    public ControlSettingKey(String name, KeyCombo defaultKeys) {
        super(name, defaultKeys, true, KeyActionType.GAMESETTING, "Game Settings");
    }
    
    @Override
    public void executeHotKeyAction() {
        
    }
    
}
