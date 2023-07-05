package envision.engine.terminal.terminalUtil;

import java.io.File;

import envision.engine.terminal.window.ETerminalWindow;
import eutil.datatypes.util.EList;
import eutil.strings.EStringBuilder;
import eutil.strings.EStringUtil;

public class ArgHelper {
	
	private final ETerminalWindow term;
	private final EList<String> arguments;
	private final EList<String> groupedByString;
	private final boolean runVisually;
	protected final int size;
	protected final int groupedSize;
	protected final File curDir;
	
	public ArgHelper(ETerminalWindow termIn, EList<String> argsIn, boolean runVisuallyIn) {
		term = termIn;
		arguments = EList.unmodifiableList(argsIn);
		groupedByString = EList.unmodifiableList(groupArgsByString(arguments));
		runVisually = runVisuallyIn;
		curDir = term.getDir();
		size = argsIn.size();
		groupedSize = groupedByString.size();
	}
	
	public boolean visually() { return runVisually; }
	public int length() { return size; }
	public int stringLength() { return groupedSize; }
	public File curDir() { return curDir; }
	public String first() { return arguments.getFirst(); }
	public String firstString() { return groupedByString.getFirst(); }
	public String last() { return arguments.getLast(); }
	public String lastString() { return groupedByString.getLast(); }
	public String arg(int index) { return (index >= 0 && index < size) ? arguments.get(index) : null; }
	public String stringArg(int index) { return (index >= 0 && index < groupedSize) ? groupedByString.get(index) : null; }
	public EList<String> args() { return arguments; }
	public EList<String> stringArgs() { return groupedByString; }
	
	public static EList<String> groupArgsByString(EList<String> arguments) {
        EList<String> grouped = EList.newList();
        
        String orig = EStringUtil.combineAll(arguments, " ");
        final int len = orig.length();
        
        var curArg = new EStringBuilder();
        char strChar = '\'';
        char prevChar = '\0';
        boolean inStr = false;
        for (int i = 0; i < len; i++) {
            char c = orig.charAt(i);
            if (c == '\'') {
                if (prevChar == '\\'); // ignore
                else if (inStr && strChar == c) {
                    grouped.add(curArg.clear());
                    inStr = false;
                    prevChar = c;
                    continue;
                }
                else {
                    inStr = true;
                    prevChar = c;
                    continue;
                }
            }
            else if (c == '"') {
                if (prevChar == '\\'); // ignore
                else if (inStr && strChar == c) {
                    grouped.add(curArg.trimClear());
                    inStr = false;
                    prevChar = c;
                    continue;
                }
                else {
                    inStr = true;
                    prevChar = c;
                    continue;
                }
            }
            else if (c == ' ' && !inStr) {
                if (curArg.isNotEmpty()) grouped.add(curArg.trimClear());
                prevChar = c;
                continue;
            }
            
            curArg.a(c);
            prevChar = c;
        }
        
        if (curArg.isNotEmpty()) {
            grouped.add(curArg.toString());
        }
        
        return grouped;
    }
	
}
