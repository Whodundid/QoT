package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import envision.game.manager.rules.GameRule;
import eutil.datatypes.util.EList;

public class CMD_Rules extends TerminalCommand {
	
	public CMD_Rules() {
		setCategory("Game");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "rule"; }
	@Override public EList<String> getAliases() { return EList.of("gr"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Used to display or modify current game rules"; }
	@Override public String getUsage() { return "ex: gr "; }
	
	@Override
	public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
	    var manager = Envision.levelManager;
	    if (manager == null) {
	        basicTabComplete(termIn, args, "No level loaded.");
	        return;
	    }
	    
	    var rules = manager.getGameRules();
	    if (rules == null) {
	        basicTabComplete(termIn, args, "There are no rules!");
	        return;
	    }
	    
	    var names = rules.getRules().map(r -> r.getRuleName());
	    
	    basicTabComplete(termIn, args, names);
	}
	
	@Override
	public void runCommand() {
	    expectBetween(0, 2);
	    
	    var manager = Envision.levelManager;
	    expectNotNull(manager, "Current Game Manager is null!");
		
		var rules = manager.getGameRules();
		expectNotNull(rules, "Current game rules are null!");
		
		if (noArgs()) {
		    var allRules = rules.getRules();
		    allRules.sort((a, b) -> a.getRuleName().compareToIgnoreCase(b.getRuleName()));
		    
		    writeln("Displaying all rules:");
		    int i = 0;
		    for (GameRule<?> r : allRules) {
		        writeln("  ", i++, ": ", r.getRuleName(), " = ", r.getValue());
		    }
		}
		else if (oneArg()) {
		    
		}
		else if (twoArgs()) {
		    
		    GameRule<Boolean> rule = (GameRule<Boolean>) rules.getRule(arg(0));
		    // this should really be a boolean, but I don't want to take a lot of time on this right now
		    boolean val = Boolean.parseBoolean(arg(1));
		    rule.setValue(val);
		    writeln("  ", rule.getRuleName(), " = ", rule.getValue());
		}
	}
	
}
