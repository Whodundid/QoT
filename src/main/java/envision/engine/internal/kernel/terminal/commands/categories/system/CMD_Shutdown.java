package envision.engine.internal.kernel.terminal.commands.categories.system;

import envision.Envision;
import envision.engine.internal.kernel.terminal.commands.ConfirmationCommand;
import envision.engine.internal.kernel.terminal.terminalUtil.ArgHelper;
import envision.engine.internal.kernel.terminal.terminalUtil.TermArgLengthException;
import envision.engine.internal.kernel.terminal.terminalUtil.TermArgParsingException;
import envision.engine.internal.kernel.terminal.window.ETerminalWindow;
import eutil.datatypes.util.EList;
import eutil.debug.Broken;

@Broken(reason="Due to commands being converted into processes, the previous confirmation command paradigm no longer works")
public class CMD_Shutdown extends ConfirmationCommand {
    
    public CMD_Shutdown() {
        setCategory("System");
        setConfirmationString("Warning: You are about to shut down the game engine! Do you wish to continue?");
        expectedArgLength = 0;
    }
    
    @Override public String getName() { return "shutdown"; }
    @Override public String getHelpInfo(boolean runVisually) { return "Shuts the engine down"; }
    @Override public String getUsage() { return "ex: shutdown"; }
    @Override public byte requiredPermissionLevel() { return 2; }
    
    @Override
    public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
        basicTabComplete(termIn, args, EList.of("true", "false"));
    }
    
    @Override
    public void preRun(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
        term = termIn;
        argHelper = new ArgHelper(termIn, args, runVisually);

        try {
            if (args.isEmpty()) checkConfirm(termIn, args, runVisually);
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
    }

    @Override
    public void runAction(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
        Envision.shutdown();
    }
    
}