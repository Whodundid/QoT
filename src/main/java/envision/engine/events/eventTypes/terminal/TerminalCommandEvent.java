package envision.engine.events.eventTypes.terminal;

import envision.engine.events.EventType;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.datatypes.EArrayList;

public class TerminalCommandEvent extends TerminalEvent {
	
	private final ETerminalWindow terminal;
	private final TerminalCommand command;
	private final EArrayList<String> args;
	
	public TerminalCommandEvent(ETerminalWindow terminalIn, TerminalCommand commandIn, EArrayList<String> argsIn) {
		super(EventType.TERMINAL_COMMAND, true);
		terminal = terminalIn;
		command = commandIn;
		args = argsIn;
	}
	
	public ETerminalWindow getTerminal() { return terminal; }
	public TerminalCommand getCommand() { return command; }
	public EArrayList<String> getArgs() { return args; }
	
}
