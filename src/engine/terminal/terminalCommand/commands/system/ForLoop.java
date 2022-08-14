package engine.terminal.terminalCommand.commands.system;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;
import eutil.datatypes.EList;
import eutil.math.NumberUtil;
import eutil.strings.StringUtil;
import main.QoT;

//Author: Hunter Bragg

public class ForLoop extends TerminalCommand {
	
	public ForLoop() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 2;
	}

	@Override public String getName() { return "for"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Runs a command n number of times in given range replacing any '#' arguments with current value."; }
	@Override public String getUsage() { return "ex: for 0-9-1 'cmd'"; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		if (args.size() < 2) {
			termIn.error("Not enought arguments for loop!");
			termIn.info(getUsage());
		}
		else if (args.size() >= 2) {
			String vals = args.get(0);
			
			EList<String> otherArgs = new EArrayList<>(args.subList(1, args.size()));
			
			//String cmd = "";
			//for (String s : otherArgs) { cmd += (s + " "); }
			//if (otherArgs.size() >= 1) { cmd = cmd.substring(0, cmd.length() - 1); }
			
			if (vals.length() < 3) { termIn.error("Not enough arguments for loop!"); }
			else if (vals.length() >= 3 && vals.contains("-")) {
				int pos = StringUtil.findStartingIndex(vals, "-");
				String firstArg = vals.substring(0, pos);
				String secondParse = vals.substring(pos + 1);
				
				String secondArg = StringUtil.subStringToString(secondParse, 0, "-");
				System.out.println(secondArg);
				
				
				String thirdArg = StringUtil.subStringToString(secondParse, 0, "-", true);
				
				System.out.println(thirdArg);
				
				
				
				boolean positive = true;
				int firstI = 0;
				int secondI = 0;
				int thirdI = 0;
				
				Class type = checkClasses(termIn, firstArg, secondArg, thirdArg);
				
				if (type == null) { termIn.error("Could not parse range value types!"); return; }
				if (type == Exception.class) { termIn.error("Inconsistent range datatype values!"); return; }
				
				if (type == Integer.class) {
					firstI = Integer.parseInt(firstArg);
					secondI = Integer.parseInt(secondArg);
					thirdI = Integer.parseInt(thirdArg);
					
					positive = (secondI - firstI > 0); //check direction
					
					if (positive) {
						try {
							for (int i = firstI; i <= secondI; i = i + thirdI) {
								runLoop(termIn, i, otherArgs);
							}
						}
						catch (Exception e) {
							error(termIn, e);
						}
					}
					else {
						try {
							for (int i = firstI; i >= secondI; i = i - thirdI) {
								runLoop(termIn, i, otherArgs);
							}
						}
						catch (Exception e) {
							error(termIn, e);
						}
					}
				}
				else if (type == String.class) {
					
				}
			}
			else {
				termIn.error("Invalid for loop range argument!");
			}
			
			/*
			if (!cmd.isEmpty()) {
				try {
					if (!(cmd.equals("clear") || cmd.equals("clr") || cmd.equals("cls"))) { termIn.writeln("> " + cmd); }
					
				} catch (Exception e) { e.printStackTrace(); }
			}
			*/
		}
		
		
	}
	
	private Class checkClasses(ETerminal termIn, String firstArg, String secondArg, String thirdArg) {
		try {
			Class first = String.class;
			Class second = String.class;
			Class third = String.class;
			
			if (NumberUtil.isInteger(firstArg, 10)) { first = Integer.class; }
			if (NumberUtil.isInteger(secondArg, 10)) { second = Integer.class; }
			if (NumberUtil.isInteger(thirdArg, 10)) { third = Integer.class; }
			
			if (!first.equals(second) || !first.equals(third)) { return Exception.class; } //error and return if the parsed range types are not the same
			else if (first == Integer.class) { return Integer.class; } //try for integer range
			else if (first == Integer.class) { return String.class; } //try for character range instead
			
		}
		catch (Exception e) {
			error(termIn, e);
		}
		return null;
	}
	
	private void runLoop(ETerminal termIn, Object curVal, EList<String> argsIn) {
		String cmd = replaceValsInArgs(argsIn, curVal);
		termIn.writeln("> " + cmd);
		QoT.getTerminalHandler().executeCommand(termIn, cmd, false);
	}
	
	private String replaceValsInArgs(EList<String> argsIn, Object curVal) {
		String cmd = "";
		for (String s : argsIn) {
			s = s.replaceAll("\\#", "" + curVal);
			cmd += s + " ";
		}
		if (argsIn.size() > 0 && cmd.length() > 0) { cmd = cmd.substring(0, cmd.length() - 1); }
		return cmd;
	}
	
}
