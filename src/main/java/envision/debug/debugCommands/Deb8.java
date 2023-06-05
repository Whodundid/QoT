package envision.debug.debugCommands;

import java.io.File;
import java.util.UUID;

import envision.Envision;
import envision.debug.testStuff.DebugScriptRunner;
import envision.engine.terminal.window.ETerminalWindow;
import envision_lang.EnvisionLang;
import envision_lang._launch.EnvisionCodeFile;
import envision_lang._launch.EnvisionProgram;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.parser.EnvisionLangParser;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.tokenizer.Tokenizer;
import eutil.datatypes.util.EList;

@SuppressWarnings("unused")
public class Deb8 extends DebugCommand {

	private static boolean hasStarted = false;
	public static EnvisionCodeFile codeFile;
	public static EnvisionInterpreter interpreter;
	
	//================================================================================
	
	@Override
	public void run(ETerminalWindow termIn, Object... args) throws Exception {
		var inst = DebugScriptRunner.getInstance();
		
		if (DebugScriptRunner.theScriptToUse == null) {
			DebugScriptRunner.theScriptToUse = new EnvisionProgram("program");
		}
		
		inst.setScript(DebugScriptRunner.theScriptToUse);
		inst.setConsoleReceiver(termIn);
		
		EnvisionLang.enableBlockingStatements = true;
		EnvisionLang.enableBlockStatementParsing = true;
		
		if ((interpreter != null && !interpreter.hasNext())
			|| (interpreter != null && interpreter.hasError())
			|| !hasStarted)
		{
			start();
		}
		
		interpreter.executeNext();
	}
	
	private static void start() {
		setupCodeFile();
		stmt("""
				class vec {
					int x, y
					init(x, y)
					func toString() -> "<{x}, {y}>"
					func add(vec v) -> vec(x + v.x, y + v.y)
					operator +(vec v) -> add(v)
				}
				
				v1 = vec(10, 5)
				v2 = vec(32, 43)
				
				#println(v1)
				#println(v2)
				#println(v1 + v2)
			 """);
		
		interpreter.setup(EList.newList());
		hasStarted = true;
	}
	
	//================================================================================
	
	private static void setupCodeFile() {		
		try {
			codeFile = new EnvisionCodeFile(new File(UUID.randomUUID().toString()));
			interpreter = EnvisionInterpreter.build(codeFile, EList.newList());
			
			var validField = codeFile.getClass().getDeclaredField("isValid");
			validField.setAccessible(true);
			validField.set(codeFile, true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void buildCodeFile(String lineToAdd) {
		Tokenizer t = new Tokenizer(lineToAdd);
		codeFile.getLineTokens().clearThenAddAll(t.getLineTokens());
		codeFile.getTokens().clearThenAddAll(t.getTokens());
		codeFile.getLines().clearThenAddAll(t.getLines());
	}
	
	public static <E extends ParsedStatement> E stmt(String line) { return stmt(line, 0); }
	public static <E extends ParsedStatement> E stmt(String line, int stmtIndex) {
		buildCodeFile(line);
		
		var stmts = EnvisionLangParser.parse(codeFile);
		codeFile.getStatements().clearThenAddAll(stmts);
		
		return (E) stmts.get(stmtIndex);
	}
	
	public static void runNext() {
		interpreter.executeNext();
	}

}