package envision.game.dialog;

import java.util.HashMap;

public class DialogueManager {
	private static HashMap<Integer, EntityDialogue> map;
	
	private static int currentId;
	
	
	public DialogueManager() {
		map = new HashMap<Integer, EntityDialogue>();
	}
	
}
