package gameSystems.scriptingSystem.interpreter.langUtil.checkers;

import util.storageUtil.EArrayList;
import util.storageUtil.StorageBox;

public class ScopeChecker {
	
	private static int openScopes = 0;
	private static int openParens = 0;
	
	/** Checks through each line to make sure that there is an equal number of '{' as there are '}' as well as '(' and ')'. */
	public static StorageBox<Integer, Integer> checkScopes(EArrayList<String> lines) {
		openScopes = 0;
		openParens = 0;
		
		for (int i = 0; i < lines.size(); i++) {
			String s = lines.get(i);
			
			//parse through the current line to check for curly braces and parenthesis
			for (int j = 0; j < s.length(); j++) {
				char c = s.charAt(j);
				
				//increment or decrement openScopes depending on the character
				if (c == '{') { openScopes++; }
				else if (c == '}') { openScopes--; }
				else if (c == '(') { openParens++; }
				else if (c == ')') { openParens--; }
			}
		}
		
		//return true if the number of openScopes and openParens equals 0
		return new StorageBox(openScopes, openParens);
	}
	
}
