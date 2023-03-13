package envision.engine.scripting.envisionMappings;

import envision.Envision;
import envision.engine.terminal.window.ETerminalWindow;
import envision_lang._launch.EnvisionLangErrorCallBack;
import envision_lang.lang.language_errors.EnvisionLangError;

public class Envision_QoT_ErrorCallback implements EnvisionLangErrorCallBack {

	@Override
	public void handleError(EnvisionLangError e) {
		handleException(e);
	}

	@Override
	public void handleException(Exception e) {
		ETerminalWindow term = (ETerminalWindow) Envision.getTopScreen().getWindowInstance(ETerminalWindow.class);
		if (term != null) {
			term.javaError(e.toString());
		}
		
		e.printStackTrace();
	}
	
}
