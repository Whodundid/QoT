package scripting.langMappings;

import envision.interpreter.util.Scope;
import envision.lang.langPackages.EnvisionLangPackage;

public class QoT_Package extends EnvisionLangPackage {
	
	public static void defineOn(Scope in) { new QoT_Package().definePackage(in); }
	
	private QoT_Package() {
		add(new GameWait());
	}
}
