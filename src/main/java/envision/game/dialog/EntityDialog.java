package envision.game.dialog;

import envision.game.entities.Entity;

public abstract class EntityDialog {
	
	protected Entity entityDoingDialog;
	protected String dialogToSay;
	
	protected EntityDialog(Entity entityDoingDialog, String dialogToSay) {
		this.entityDoingDialog = entityDoingDialog;
		this.dialogToSay = dialogToSay;
	}

	public Entity getEntityDoingDialog() { return entityDoingDialog; }
	public String getDialogToSay() { return dialogToSay; }
	
}
