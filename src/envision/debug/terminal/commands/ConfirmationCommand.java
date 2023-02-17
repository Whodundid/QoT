package envision.debug.terminal.commands;

import envision.debug.terminal.window.ETerminalWindow;
import eutil.datatypes.util.EList;

public abstract class ConfirmationCommand extends TerminalCommand {

	protected boolean responseReceived = false;
	protected boolean confirmed = false;
	protected String confirmString = "Do you want to continue? (Y, N)";
	protected String yes = "y", no = "n";
	
	protected ConfirmationCommand() {}
	
	@Override
	public void onConfirmation(String response) {
		if (response != null) {
			responseReceived = true;
			confirmed = response.equalsIgnoreCase(yes);
		}
	}
	
	public boolean checkConfirm(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (responseReceived) {
			if (confirmed) { runAction(termIn, args, runVisually); }
			else { termIn.info("User requested cancellation, command aborted!\n"); }
			responseReceived = false;
			confirmed = false;
		}
		else { termIn.setRequiresCommandConfirmation(this, confirmString + " (" + yes + ", " + no + ")", args, runVisually); }
		return false;
	}
	
	public abstract void runAction(ETerminalWindow termIn, EList<String> args, boolean runVisually);

	public void setConfirmationString(String in) { setConfirmationString(in, "y", "n"); }
	public void setConfirmationString(String in, String yesMessage, String noMessage) {
		confirmString = in;
		yes = yesMessage;
		no = noMessage;
	}
	
}
