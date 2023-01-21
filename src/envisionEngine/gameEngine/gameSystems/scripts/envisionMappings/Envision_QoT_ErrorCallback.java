package envisionEngine.gameEngine.gameSystems.scripts.envisionMappings;

import envisionEngine.terminal.window.ETerminal;
import envision_lang._launch.EnvisionLangErrorCallBack;
import envision_lang.exceptions.EnvisionLangError;
import qot.QoT;

public class Envision_QoT_ErrorCallback implements EnvisionLangErrorCallBack {

	@Override
	public void handleError(EnvisionLangError e) {
		handleException(e);
	}

	@Override
	public void handleException(Exception e) {
		ETerminal term = (ETerminal) QoT.getTopRenderer().getWindowInstance(ETerminal.class);
		if (term != null) {
			term.javaError(e.toString());
		}
		
		e.printStackTrace();
	}
	
}
