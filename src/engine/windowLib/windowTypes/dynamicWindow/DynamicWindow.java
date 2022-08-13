package engine.windowLib.windowTypes.dynamicWindow;

import java.io.File;
import java.io.IOException;

import engine.windowLib.windowTypes.WindowParent;
import engine.windowLib.windowTypes.interfaces.IWindowObject;
import engine.windowLib.windowUtil.ObjectPosition;
import eutil.datatypes.Box2;
import eutil.datatypes.EArrayList;
import eutil.file.FileUtil;
import main.QoT;

/** A special type of WindowParent that can be directly constructed from a serialized file. */
public class DynamicWindow extends WindowParent {
	
	//--------
	// Fields
	//--------
	
	private File dataFile;
	private boolean failed = false;
	private boolean built = false;
	
	//parsed values
	
	private Box2<Double, Double> parsedDims = new Box2<>();
	private ObjectPosition parsedCenterType = null;
	
	private EArrayList<IWindowObject<?>> parsedObjects = new EArrayList<>();
	
	//--------------
	// Constructors
	//--------------
	
	public DynamicWindow(File fileIn) {
		dataFile = fileIn;
	}
	
	//---------
	// Methods
	//---------
	
	/** Attempts to construct a WindowParent object from the associated serialized file. */
	public boolean build() {
		return built = FileUtil.tryFileCodeR(dataFile, a -> parseFile());
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	/** Returns true if, and only if, each and every step of the file reading process succeeds. */
	protected boolean parseFile() {
		try {
			String t = "Cannot prase DynamicWindow file: ";
			if (!parseBase()) throw QoT.error(t + dataFile + " for scripts!", new IOException());
			if (!parseObjects()) throw QoT.error(t + dataFile + " for objects!", new IOException());
			if (!parseScripts()) throw QoT.error(t + dataFile + " for base values!", new IOException());
		}
		catch (Exception e) {
			failed = true;
			return false;
		}
		
		return true;
	}
	
	protected boolean parseBase() {
		return false;
	}
	
	protected boolean parseObjects() {
		return false;
	}
	
	protected boolean parseScripts() {
		return false;
	}

	protected void finishBuild() {
		
	}
	
}
