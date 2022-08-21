package envision.events.types.terminal;

import envision.events.EventType;
import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;

public class TerminalCommandEvent extends TerminalEvent {
	
	private final ETerminal terminal;
	private final TerminalCommand command;
	private final EArrayList<String> args;
	
	public TerminalCommandEvent(ETerminal terminalIn, TerminalCommand commandIn, EArrayList<String> argsIn) {
		super(EventType.TERMINAL_COMMAND, true);
		terminal = terminalIn;
		command = commandIn;
		args = argsIn;
	}
	
	public ETerminal getTerminal() { return terminal; }
	public TerminalCommand getCommand() { return command; }
	public EArrayList<String> getArgs() { return args; }
	
}
