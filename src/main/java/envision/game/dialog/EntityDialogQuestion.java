package envision.game.dialog;

import envision.game.entities.Entity;
import eutil.datatypes.util.EList;

public class EntityDialogQuestion extends EntityDialog {
	
	private EList<EntityDialog> answerOptions = EList.newList();
	
	public EntityDialogQuestion(Entity entityDoingDialog, String dialogToSay) {
		super(entityDoingDialog, dialogToSay);
	}
	
	public void addOption(Entity entityToSpeak, String optionText) {
		answerOptions.add(new EntityDialogChat(entityToSpeak, optionText));
	}
	
	public void addOption(EntityDialog dialog) {
		answerOptions.add(dialog);
	}
	
	public EList<EntityDialog> getOptions() {
		return answerOptions;
	}
	
}
