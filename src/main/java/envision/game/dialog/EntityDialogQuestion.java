package envision.game.dialog;

import envision.game.entities.Entity;
import eutil.datatypes.util.EList;

public class EntityDialogQuestion extends EntityDialogue {
	
	private EList<DialogueOption> answerOptions = EList.newList();
	
	public EntityDialogQuestion(Entity entityDoingDialog, String dialogToSay) {
		super(entityDoingDialog, dialogToSay);
	}
	
	public void addOption(Entity entityToSpeak, String optionText, String actualText) {
		answerOptions.add(new DialogueOption(optionText, actualText, 0));
	}
	
	public void addOption(DialogueOption dialog) {
		answerOptions.add(dialog);
	}
	
	public EList<DialogueOption> getOptions() {
		return answerOptions;
	}
	
}
