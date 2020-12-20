package gameSystems.scriptingSystem.interpreter;

import gameSystems.scriptingSystem.EScript;
import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.interpreter.langUtil.ScriptLineParser;
import gameSystems.scriptingSystem.interpreter.langUtil.checkers.ScopeChecker;
import gameSystems.scriptingSystem.util.ScriptRunner;
import gameSystems.scriptingSystem.util.enums.Keyword;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import util.EUtil;
import util.storageUtil.StorageBox;
import util.storageUtil.StorageBoxHolder;

/** The head class that manages parsing script files. */
public class ScriptInterpreter {

	ScriptRunner runner;
	ScriptLineParser parser;
	EScript mainScript;
	File dataFile;
	StorageBoxHolder<String, Integer> lines = new StorageBoxHolder(); //line, line number
	int curLine = -1;
	String name = "";
	
	int checkLine = 1;
	String error = "";
	
	/** Creates an interpreter to read the specified file. */
	public ScriptInterpreter(ScriptRunner runnerIn, File dataFileIn) throws FileNotFoundException, ScriptError {
		runner = runnerIn;
		dataFile = dataFileIn;
		
		if (checkFile()) {
			int lineNum = 0;
			
			//parse through the file
			try (Scanner reader = new Scanner(dataFile)) {
				//trims each line as it is read
				while (reader.hasNextLine()) {
					String line = reader.nextLine().trim();
					lines.addIf(!line.isEmpty(), line, ++lineNum);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			build();
		}
		else { throw new FileNotFoundException("System cannot find the specified file: " + dataFileIn); }
	}
	
	public boolean rebuild() {
		try {
			build();
			return true;
		}
		catch (Exception e) { e.printStackTrace(); }
		return false;
	}
	
	private void build() throws ScriptError {
		
		//clear the old script
		if (mainScript != null) { mainScript.free(); }
		
		//set the line number
		curLine = 0;
		
		//name = findScriptName();
		
		//throw error if the script's name could not be found
		//if (name == null) { throw new ScriptError("Cannot identify script's name!"); }
		
		//next check for proper scopes
		StorageBox<Integer, Integer> opens = ScopeChecker.checkScopes(lines.getAVals());
		if (opens.getA() > 0 || opens.getB() > 0) {
			throw new ScriptError("Script scope not closed! Number of open scopes: '" + opens.getA() + "' & Number of open parens: '" + opens.getB() + "' !");
		}
		
		//next check for proper syntax
		//if (!Syntaxer.checkSyntax(lines)) { throw new ScriptError("Syntax Error"); }
		
		mainScript = new EScript("unnamed").setRunner(runner);
		parser = new ScriptLineParser(mainScript);
	}
	
	/** Returns true if this interpreter actually has lines to read. */
	public boolean canRun() { return curLine >= 0 && curLine < lines.size(); }
	
	/** Returns the instance of the main script. */
	public EScript getScriptInstance() { return mainScript; }
	
	/** Returns the name of the main script. */
	public String getScriptName() { return (mainScript != null) ? mainScript.getName() : "null"; }
	
	/** Attempts to parse the next line of code. */
	public void runNextLine() throws ScriptError {
		if (curLine < lines.size()) {
			parser.parseLine(lines.getObject(curLine), lines.getValue(curLine));
			curLine++;
		}
	}
	
	//----------------------------------
	//ScriptInterpreter Internal Methods
	//----------------------------------
	
	/** Ensures that the file actually exists. */
	private boolean checkFile() { return EUtil.fileNullExists(dataFile); }
	
	/** Attempts to find the start of the script as marked by 'Script' followed by the script's name. */
	private String findScriptName() throws ScriptError {
		checkLine = 1;
		boolean found = false;
		String name = null;
		
		for (String s : lines.getAVals()) {
			
			//enforces specific script definition syntax
			if (s.startsWith(Keyword.SCRIPT.keyword) && s.endsWith("{")) {
				
				if (!found) {
					found = true;
					
					//get the part of the string after the 'Script '
					String namePart = EUtil.subStringToSpace(s, 7);
					
					//isolate the name from the line
					name = "";
					for (int i = 0; i < namePart.length(); i++) {
						char c = namePart.charAt(i);
						
						//don't allow braces in names
						if (c == '}') { throw new ScriptError("Illegal '{' character in script name!"); }
						
						if (c == '{') {
							//ensure that this is actually at the end of the line
							if (i < namePart.length() - 1) { throw new ScriptError("Illegal '{' character in script name!"); }
							break;
						}
						
						name += c;
					}
					
					if (name.isEmpty()) { throw new ScriptError("No main script found within file!"); }
				}
				else { throw new ScriptError("Main script already defined at line: '" + checkLine + "' !"); }
			}
			
			checkLine++;
		}
		
		//check if the name wasn't found
		if (name == null) { throw new ScriptError("Cannot find Main script within file!"); }
		
		//ensure that the script name does not have illegal characters
		if (!name.equals("main")) { throw new ScriptError("Invalid script name: '" + name + "'. Name does not exist or it contains reserved characters!"); }
		
		return name;
	}
	
	public ScriptLineParser getLineParser() { return parser; }
	
}
