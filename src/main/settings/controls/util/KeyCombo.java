package main.settings.controls.util;

import eutil.datatypes.EArrayList;
import eutil.datatypes.EList;

/*
 * Holds a variable amount of lwjgl.Keyboard keyCodes used to trigger an event.
 * Additional modifier keys can be set which include Ctrl, Shift, and Alt.
 */
public class KeyCombo {
	
	public EList<Integer> hotKeyCodes = new EArrayList<>();
	
	/**
	 * Takes in a variable amount of lwjgl.Keyboard keyCodes used to trigger an event.
	 * @param codeIn {@code Integer[]}
	 */
	public KeyCombo(int... codeIn) {
		setKeys(codeIn);
	}
	
	public void setKeys(EList<Integer> codeIn) {
		if (codeIn != null) {
			hotKeyCodes.clear();
			codeIn.forEach(i -> hotKeyCodes.add(i));
		}
	}
	
	public void setKeys(int... codeIn) {
		synchronized (hotKeyCodes) {
			hotKeyCodes.clear();
			for (int i : codeIn) { hotKeyCodes.add(i); }
		}
	}
	
	public boolean checkKeys(int[] keys) { return checkKeys(new EArrayList(keys)); }
	
	public boolean checkKeys(EList<Integer> checkKeys) {
		if (hotKeyCodes.size() == checkKeys.size()) {
			boolean pass = true;
			for (int i : hotKeyCodes) {
				if (!checkKeys.contains(i)) { pass = false; }
			}
			return pass;
		}
		return false;
	}
	
	public EList<Integer> getKeys() { return hotKeyCodes; }
	
}