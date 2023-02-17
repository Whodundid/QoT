package envision.engine.scripting.envisionMappings;

import envision.debug.terminal.window.ETerminalWindow;
import envision_lang._launch.EnvisionLangErrorCallBack;
import envision_lang.lang.language_errors.EnvisionLangError;
import qot.QoT;

public class Envision_QoT_ErrorCallback implements EnvisionLangErrorCallBack {

	@Override
	public void handleError(EnvisionLangError e) {
		handleException(e);
	}

	@Override
	public void handleException(Exception e) {
		ETerminalWindow term = (ETerminalWindow) QoT.getTopRenderer().getWindowInstance(ETerminalWindow.class);
		if (term != null) {
			term.javaError(e.toString());
		}
		
		e.printStackTrace();
	}
	
}
