package envision.debug.testStuff;

import envision_lang.EnvisionLang;
import envision_lang._launch.EnvisionConsoleOutputReceiver;
import envision_lang._launch.EnvisionLangErrorCallBack;
import envision_lang._launch.EnvisionProgram;

public class DebugScriptRunner {
	
	public static final EnvisionLang lang = EnvisionLang.getInstance();
	public EnvisionProgram script;
	
	public static EnvisionProgram theScriptToUse;
	
	public static DebugScriptRunner instance;
	
	public static DebugScriptRunner getInstance() {
		if (instance != null) return instance;
		return instance = new DebugScriptRunner();
	}
	
	public DebugScriptRunner() {}
	
	public void setScript(EnvisionProgram scriptIn) {
		script = scriptIn;
	}
	
	public void setErrorCallback(EnvisionLangErrorCallBack callback) {
		EnvisionLang.setErrorCallback(callback);
	}
	
	public void setConsoleReceiver(EnvisionConsoleOutputReceiver receiverIn) {
		EnvisionLang.setConsoleReceiver(receiverIn);
	}
	
	public EnvisionProgram getScript() { return script; }
	
}
