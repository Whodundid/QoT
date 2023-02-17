package envision.engine.scripting.envisionMappings.qot_package;

import envision.engine.scripting.envisionMappings.qot_package.methods.LoadWorld_ENV;
import envision.engine.scripting.envisionMappings.qot_package.methods.TermCall_ENV;
import envision.engine.scripting.envisionMappings.qot_package.methods.TermWritef_ENV;
import envision.engine.scripting.envisionMappings.qot_package.methods.TermWriteln_ENV;
import envision.engine.scripting.envisionMappings.qot_package.objects.QoTWorldClass;
import envision_lang.lang.packages.EnvisionLangPackage;

public class Envision_QoT_Package extends EnvisionLangPackage {

	public static final String packageName = "qot";
	
	public Envision_QoT_Package() {
		super(packageName);
	}
	
	@Override
	public void buildFunctions() {
		define(new TermCall_ENV());
		define(new TermWriteln_ENV());
		define(new LoadWorld_ENV());
		define(new TermWritef_ENV());
	}
	
	@Override
	public void buildClasses() {
		define(new QoTWorldClass());
	}
	
}
