package envision.engine.terminal.commands.categories.system;

import envision.Envision;
import envision.engine.terminal.commands.CommandResult;
import envision.engine.terminal.commands.ConfirmationCommand;
import envision.engine.terminal.terminalUtil.ArgHelper;
import envision.engine.terminal.terminalUtil.TermArgLengthException;
import envision.engine.terminal.terminalUtil.TermArgParsingException;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.datatypes.util.EList;

public class CMD_Shutdown extends ConfirmationCommand {
	
	public CMD_Shutdown() {
		setCategory("System");
		setConfirmationString("Warning: You are about to shut down the game engine! Do you wish to continue?");
		expectedArgLength = 0;
	}
	
	@Override public String getName() { return "shutdown"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Shuts the engine down"; }
	@Override public String getUsage() { return "ex: shutdown"; }
	
	@Override
	public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
		basicTabComplete(termIn, args, EList.of("true", "false"));
	}
	
	@Override
	public CommandResult runCommand_i(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
        result = new CommandResult(this, args, runVisually);
        term = termIn;

        try {
            argHelper = new ArgHelper(termIn, args, runVisually);
            if (args.isEmpty()) checkConfirm(termIn, args, runVisually);
            //runCommand();
        }
        catch (TermArgParsingException e) {
            var cause = e.getCause();
            String errorString = e.getErrorString();
            javaError(cause);
            if (errorString != null) error(errorString);
        }
        catch (TermArgLengthException e) {
            errorUsage(e.getMessage());
        }
        catch (Exception e) {
            error(e);
        }
        
        return result;
	}

	@Override
	public void runAction(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		Envision.shutdown();
	}
	
}