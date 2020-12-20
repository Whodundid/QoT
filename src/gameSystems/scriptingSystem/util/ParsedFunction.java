package gameSystems.scriptingSystem.util;

import util.miscUtil.EDataType;
import util.storageUtil.StorageBoxHolder;

public class ParsedFunction {

	public String name;
	public EDataType returnType;
	public StorageBoxHolder<EDataType, String> parameters;
	
	public ParsedFunction() {}
	public ParsedFunction(String nameIn, EDataType typeIn, StorageBoxHolder<EDataType, String> paramsIn) {
		name = nameIn;
		returnType = typeIn;
		parameters = paramsIn;
	}
	
}
