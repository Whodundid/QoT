package gameSystems.scriptingSystem.variables;

import gameSystems.scriptingSystem.exceptions.ScriptError;
import util.miscUtil.EDataType;
import util.storageUtil.EArrayList;
import util.storageUtil.StorageBox;

public class ScriptObject {

	protected final EDataType type;
	protected String name;
	protected boolean isStatic = false;
	protected boolean isFinal = false;
	
	//Constructors
	
	public ScriptObject() { this(EDataType.OBJECT, "noname"); }
	public ScriptObject(String nameIn) { this(EDataType.OBJECT, nameIn); }
	public ScriptObject(EDataType typeIn) { this(typeIn, "noname"); }
	public ScriptObject(EDataType typeIn, String nameIn) {
		type = typeIn;
		name = nameIn;
	}
	
	@Override public String toString() { return getObjectHash(); }
	@Override
	public boolean equals(Object in) {
		if (in instanceof ScriptObject) {
			return ((ScriptObject) in).hashEquals(this);
		}
		return false;
	}
	
	//Methods
	
	public boolean nameEquals(String in) { return name.equalsIgnoreCase(in); }
	
	@SuppressWarnings("unlikely-arg-type")
	public StorageBox<Object, EDataType> handleFunction(String in, EArrayList<String> args) throws ScriptError {
		switch (in) {
		case "toString": return new StorageBox(toString(), EDataType.STRING);
		case "hash": return new StorageBox(getObjectHash(), EDataType.STRING);
		case "getDataType": return new StorageBox(getDataType(), type);
		case "isStatic": return new StorageBox(isStatic(), EDataType.BOOLEAN);
		case "isFinal": return new StorageBox(isFinal(), EDataType.BOOLEAN);
		case "getName": return new StorageBox(getName(), EDataType.STRING);
		case "nameEquals": return new StorageBox(nameEquals(args.get(0)), EDataType.BOOLEAN);
		case "equals": return new StorageBox(equals(args.get(0)), EDataType.BOOLEAN);
		default: throw new ScriptError("No function '" + in + "' defined in type ScriptList!");
		}
	}
	
	public boolean hashEquals(ScriptObject in) { return (in != null) ? getObjectHash().equals(in.getObjectHash()) : false; }
	public boolean hashEquals(String in) { return (in != null) ? getObjectHash().equals(in) : false; }
	
	//Getters
	
	public String getObjectHash() { return type + "_" + hashCode(); }
	public EDataType getDataType() { return type; }
	public String getName() { return name; }
	public boolean isStatic() { return isStatic; }
	public boolean isFinal() { return isFinal; }
	
	//Setters
	
	public ScriptObject setName(String nameIn) { name = nameIn; return this; }
	public ScriptObject setStatic(boolean val) { isStatic = val; return this; }
	public ScriptObject setFinal(boolean val) { isFinal = val; return this; }
	
}
