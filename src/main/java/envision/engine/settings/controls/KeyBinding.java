package envision.engine.settings.controls;

import envision.engine.inputHandlers.Keyboard;
import envision.engine.settings.controls.util.KeyCategory;
import envision.engine.settings.controls.util.KeyCombo;

public class KeyBinding {
	
	protected String name = "";
	protected String description = "";
	protected KeyCategory category;
	protected boolean isEnabled = true;
	/** If this value is true, this key cannot be deleted. */
	protected boolean builtIn = false;
	protected boolean isPressed = false;
	protected KeyCombo keys;
	protected KeyCombo defaultKeys;
	protected KeyActionType type;
	
	protected KeyBinding() {}
	
	public KeyBinding(String keyNameIn, KeyCombo keyCodeIn, KeyActionType typeIn) { this(keyNameIn, keyCodeIn, true, typeIn, null); }
	public KeyBinding(String keyNameIn, KeyCombo keyCodeIn, KeyActionType typeIn, String builtInSubModTypeIn) { this(keyNameIn, keyCodeIn, true, typeIn, builtInSubModTypeIn); }
	public KeyBinding(String keyNameIn, KeyCombo keyCodeIn, boolean builtInVal, KeyActionType typeIn) { this(keyNameIn, keyCodeIn, builtInVal, typeIn, null); }
	public KeyBinding(String keyNameIn, KeyCombo keyCodeIn, boolean builtInVal, KeyActionType typeIn, String categoryNameIn) {
		name = keyNameIn;
		keys = keyCodeIn;
		defaultKeys = keyCodeIn;
		builtIn = builtInVal;
		type = typeIn;
		if (categoryNameIn != null) { category = new KeyCategory(categoryNameIn); }
	}
	
	public void reset() {
		keys.setKeys(defaultKeys.getKeys());
	}
	
	//getters
	public String getKeyName() { return name; }
	public String getKeyDescription() { return description; }
	public KeyCombo getKeyCombo() { return keys; }
	public KeyCombo getDefaultKeyCombo() { return defaultKeys; }
	public KeyActionType getHotKeyType() { return type; }
	public KeyCategory getKeyCategory() { return category; }
	
	//setters
	public void setEnabled(boolean enable) { isEnabled = enable; }
	public void setKeyCategory(String categoryNameIn) { category = new KeyCategory(categoryNameIn); }
	public void setKeyDescription(String descriptionIn) { description = descriptionIn; }
	
	public void setKeyCombo(KeyCombo comboIn) {
		keys = comboIn;
	}
	
	//general
	public boolean isEnabled() { return isEnabled; }
	public boolean isBuiltInKey() { return builtIn; }
	
	public String getHotKeyStatistics() {
		String returnStats = name + "; ";
		returnStats += type.toString() + "; ";
		for (int i : keys.hotKeyCodes) { returnStats += (i + " "); }
		returnStats += "; ";
		returnStats += String.valueOf(isEnabled) + "; ";
		if (category != null) { returnStats += category.getCategoryName(); }
		returnStats += String.valueOf(builtIn);
		return returnStats;
	}
	
	public boolean isPressed() {
		if (keys.getKeys().isNotEmpty()) {
			for (int k : keys.getKeys()) {
				if (!Keyboard.isKeyDown(k)) { return false; }
			}
			return true;
		}
		return false;
	}
	
	public void executeHotKeyAction() {}
	
}
