package envision.game.dialog;

import envision.game.entities.Entity;
import eutil.datatypes.util.EList;

public class DialogueConversation {
	
	private EList<EntityDialogue> conversation = EList.newList();
	private int currentDialogue = 0;
	
	public void add(EntityDialogue dialog) {
		conversation.add(dialog);
	}

	public EntityDialogue getCurrentDialogue() {
		return conversation.get(currentDialogue);
	}
	
	public Entity getCurrentlySpeakingEntity() {
		EntityDialogue current = getCurrentDialogue();
		if (current == null) return null;
		return current.getEntityDoingDialog();
	}
	
	public String getCurrentlySpokenText() {
		EntityDialogue current = getCurrentDialogue();
		if (current == null) return null;
		return current.getDialogToSay();
	}
	
	public EntityDialogue advanceConversation() {
		if (currentDialogue >= conversation.size()) return null;
		return conversation.get(++currentDialogue);
	}
	
	public EntityDialogue setConversationPoint(int point) {
		currentDialogue = point;
		return conversation.get(currentDialogue);
	}
	
	public int getConversationLength() {
		return conversation.size();
	}
	
	public int getCurrentDialogPoint() {
		return currentDialogue;
	}
	
}
