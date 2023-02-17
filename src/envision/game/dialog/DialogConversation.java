package envision.game.dialog;

import envision.game.objects.entities.Entity;
import eutil.datatypes.util.EList;

public class DialogConversation {
	
	private EList<EntityDialog> conversation = EList.newList();
	private int currentDialog = 0;
	
	public void add(EntityDialog dialog) {
		conversation.add(dialog);
	}
	
	public EntityDialog getCurrentDialog() {
		return conversation.get(currentDialog);
	}
	
	public Entity getCurrentlySpeakingEntity() {
		EntityDialog current = getCurrentDialog();
		if (current == null) return null;
		return current.getEntityDoingDialog();
	}
	
	public String getCurrentlySpokenText() {
		EntityDialog current = getCurrentDialog();
		if (current == null) return null;
		return current.getDialogToSay();
	}
	
	public EntityDialog advanceConversation() {
		if (currentDialog >= conversation.size()) return null;
		return conversation.get(++currentDialog);
	}
	
	public EntityDialog setConversationPoint(int point) {
		currentDialog = point;
		return conversation.get(currentDialog);
	}
	
	public int getConversationLength() {
		return conversation.size();
	}
	
	public int getCurrentDialogPoint() {
		return currentDialog;
	}
	
}
