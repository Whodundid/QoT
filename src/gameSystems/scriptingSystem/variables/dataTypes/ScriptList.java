package gameSystems.scriptingSystem.variables.dataTypes;

import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.exceptions.errors.IndexOutOfBoundsError;
import gameSystems.scriptingSystem.util.enums.Keyword;
import gameSystems.scriptingSystem.variables.ScriptObject;
import util.EUtil;
import util.miscUtil.EDataType;
import util.storageUtil.EArrayList;
import util.storageUtil.StorageBox;

public class ScriptList extends ScriptObject {

	private final EArrayList list = new EArrayList();
	private final EDataType type;
	
	//-----------------------
	//ScriptList Constructors
	//-----------------------
	
	public ScriptList() { this("noname", EDataType.OBJECT); }
	public ScriptList(EDataType typeIn) { this("noname", typeIn); }
	public ScriptList(String nameIn) { this(nameIn, EDataType.OBJECT); }
	public ScriptList(String nameIn, EDataType typeIn) {
		super(EDataType.ARRAY, nameIn);
		type = typeIn;
	}
	
	//--------------------
	//ScriptList Overrides
	//--------------------
	
	@Override public String toString() { return "[" + EUtil.combineAll(list, ", ") + "]"; }
	
	//------------------
	//ScriptList Methods
	//------------------
	
	public ScriptList add(Object... val) { list.add(val); return this; }
	public ScriptList set(int index, Object val) { list.set(index, val); return this; }
	
	public int size() { return list.size(); }
	public Keyword isEmpty() { return list.isEmpty() ? Keyword.TRUE : Keyword.FALSE; }
	public Keyword isNotEmpty() { return list.isNotEmpty() ? Keyword.TRUE : Keyword.FALSE; }
	
	@Override
	public StorageBox<Object, EDataType> handleFunction(String in, EArrayList<String> args) throws ScriptError {
		switch (in) {
		case "toString": return new StorageBox(toString(), EDataType.STRING);
		case "length": return new StorageBox(size(), EDataType.INT);
		case "isEmpty": return new StorageBox(isEmpty(), EDataType.BOOLEAN);
		case "isNotEmpty": return new StorageBox(isNotEmpty(), EDataType.BOOLEAN);
		case "hasOne": return new StorageBox(size() == 1, EDataType.BOOLEAN);
		case "contains": return new StorageBox(list.contains(args.get(0)), EDataType.BOOLEAN);
		case "notContains": return new StorageBox(list.notContains(args.get(0)), EDataType.BOOLEAN);
		case "push": list.push(args.get(0)); return new StorageBox(this, EDataType.THIS);
		case "pop": return new StorageBox(list.pop(), type);
		case "get": return new StorageBox(get(args), type);
		case "getFirst": return new StorageBox(list.getFirst(), type);
		case "getLast": return new StorageBox(list.getLast(), type);
		case "removeFirst": return new StorageBox(list.removeFirst(), type);
		case "removeLast": return new StorageBox(list.removeLast(), type);
		case "getType": return new StorageBox(getListType(), EDataType.ENUM);
		case "add": return new StorageBox(list.add(args.get(0)), EDataType.BOOLEAN);
		case "remove": return new StorageBox(remove(args), type);
		case "set": return new StorageBox(set(args), type);
		case "clear": list.clear(); return new StorageBox(this, EDataType.THIS);
		default:
			return super.handleFunction(in, args);
		}
	}
	
	//------------------
	//ScriptList Getters
	//------------------
	
	public Object get(int index) throws IndexOutOfBoundsError {
		if (index < 0 || index >= list.size()) { throw new IndexOutOfBoundsError(index, -1); }
		return list.remove(index);
	}
	
	public Object get(EArrayList<String> args) throws IndexOutOfBoundsError {
		if (args.hasOnlyOne()) {
			try {
				int index = Integer.parseInt(args.get(0));
				
				if (index < 0 || index >= list.size()) { throw new IndexOutOfBoundsError(index, -1); }
				return list.remove(index);
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		
		return null;
	}
	
	public Object remove(EArrayList<String> args) throws ScriptError {
		if (args.hasOnlyOne()) {
			try {
				int index = Integer.parseInt(args.get(0));
				
				if (index < 0 || index >= list.size()) { throw new IndexOutOfBoundsError(index, -1); }
				return list.remove(index);
			}
			catch (Exception e) {
				return list.remove(args.get(0));
			}
		}
		
		return null;
	}
	
	public Object set(EArrayList<String> args) throws ScriptError {
		if (args.size() == 2) {
			try {
				int index = Integer.parseInt(args.get(0));
				Object val = args.get(1);
				
				if (index < 0 || index >= list.size()) { throw new IndexOutOfBoundsError(index, -1); }
				return list.set(index, val);
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		
		return null;
	}
	
	public EDataType getListType() { return type; }
	public EArrayList getEArrayList() { return list; }
	
	//---------------------------------------
	
	public static ScriptList EMPTY() { return new ScriptList(); }
	public static ScriptList EMPTY(EDataType type) { return new ScriptList(type); }
	
	public static ScriptList of(Object... vals) {
		ScriptList list = new ScriptList();
		list.add(vals);
		return list;
	}
	
	public static <E> ScriptList of(EDataType typeIn, E... vals) {
		ScriptList list = new ScriptList(typeIn);
		list.add(vals);
		return list;
	}
	
}
