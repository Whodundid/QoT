package gameSystems.scriptingSystem.variables.dataTypes;

import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.variables.ScriptVariable;
import util.miscUtil.EDataType;
import util.storageUtil.EArrayList;
import util.storageUtil.StorageBox;

/** A script variable representing a list of characters. */
public class ScriptString extends ScriptVariable {
	
	public ScriptString() { super(EDataType.STRING, ""); }
	public ScriptString(String nameIn) { this(nameIn, ""); }
	public ScriptString(String nameIn, String valueIn) {
		super(nameIn, EDataType.STRING, valueIn);
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Override
	public StorageBox<Object, EDataType> handleFunction(String in, EArrayList<String> args) throws ScriptError {
		switch (in) {
		case "equals": return new StorageBox(equals(args.get(0)), EDataType.BOOLEAN);
		case "charAt": return new StorageBox(charAt(Integer.valueOf((String) args.get(0))), EDataType.STRING);
		case "toList": return new StorageBox(toList(), EDataType.ARRAY);
		case "contains": return new StorageBox(contains(String.valueOf(args.get(0))), EDataType.BOOLEAN);
		case "matches": return new StorageBox(matches(String.valueOf(args.get(0))), EDataType.BOOLEAN);
		case "length": return new StorageBox(length(), EDataType.INT);
		case "isEmpty": return new StorageBox(isEmpty(), EDataType.BOOLEAN);
		case "isNotEmpty": return new StorageBox(isNotEmpty(), EDataType.BOOLEAN);
		case "isChar": return new StorageBox(isChar(), EDataType.BOOLEAN);
		case "ascii": return new StorageBox(ascii(), EDataType.INT);
		case "toLowerCase": return new StorageBox(toLowerCase(), EDataType.STRING);
		case "toUpperCase": return new StorageBox(toUpperCase(), EDataType.STRING);
		case "subString": return new StorageBox(subString(args), EDataType.STRING);
		default:
			return super.handleFunction(in, args);
		}
	}
	
	@Override
	public boolean equals(Object in) {
		String s = (String) get();
		return s.equals(in);
	}
	
	public String charAt(int pos) {
		return ((String) get()).charAt(pos) + "";
	}
	
	public ScriptList toList() {
		ScriptList list = new ScriptList("null", EDataType.STRING);
		String s = (String) get();
		for (int i = 0; i < s.length(); i++) {
			list.add(s.charAt(i));
		}
		list.setName("list:" + list.hashCode());
		return list;
	}
	
	public boolean contains(String in) {
		String s = (String) get();
		return s.contains(in);
	}
	
	public boolean matches(String in) {
		String s = (String) get();
		return s.matches(in);
	}
	
	public int length() {
		String s = (String) get();
		return s.length();
	}
	
	public boolean isEmpty() {
		String s = (String) get();
		return s.isEmpty();
	}
	
	public boolean isNotEmpty() {
		String s = (String) get();
		return s.length() > 0;
	}
	
	public boolean isChar() {
		String s = (String) get();
		return s.length() == 1;
	}
	
	public int ascii() throws ScriptError {
		if (!isChar()) { throw new ScriptError("Given string is not a char! Length must be 1!"); }
		String s = (String) get();
		return s.charAt(0);
	}
	
	public String toLowerCase() {
		String s = (String) get();
		return s.toLowerCase();
	}
	
	public String toUpperCase() {
		String s = (String) get();
		return s.toUpperCase();
	}
	
	public String subString(EArrayList<String> args) {
		String s = (String) get();
		
		if (args.size() == 1) {
			try {
				int start = Integer.valueOf(args.get(0));
				
				return s.substring(start);
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		else if (args.size() == 2) {
			try {
				int start = Integer.valueOf(args.get(0));
				int end = Integer.valueOf(args.get(1));
				
				return s.substring(start, end);
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		
		return null;
	}
	
	public static ScriptString EMPTY() { return new ScriptString(); }
	public static ScriptString of(String val) { return new ScriptString(val); }
	public static ScriptString of(Object val) { return new ScriptString(String.valueOf(val)); }
	
}
