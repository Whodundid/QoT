package envision.game.dialog;

import envision.game.entities.Entity;
import eutil.datatypes.EArrayList;

public class DialogueSystem {
	private EArrayList<EntityDialogue> speakers;
	
	public DialogueSystem() {
		speakers = new EArrayList<EntityDialogue>();
	}
	
	public void speak() {
		
	}
	
	/**
	 * Given the code for the Dialogue id, make the entity speak the dialogue associated with the id attached to the EntityDialogue
	 * instance
	 * @param code
	 */
	public void speak(int code) {
		for (EntityDialogue eDialogue : speakers) {
			if (eDialogue.getId() == code) {
				
			}
		}
	}
	
}
