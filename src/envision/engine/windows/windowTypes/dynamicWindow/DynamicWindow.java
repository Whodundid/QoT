package envision.engine.windows.windowTypes.dynamicWindow;

import java.io.File;
import java.io.IOException;

import envision.Envision;
import envision.engine.windows.windowTypes.WindowParent;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowUtil.ObjectPosition;
import eutil.datatypes.EArrayList;
import eutil.datatypes.boxes.Box2;
import eutil.file.EFileUtil;

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
		return built = EFileUtil.tryFileCodeR(dataFile, a -> parseFile());
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	/** Returns true if, and only if, each and every step of the file reading process succeeds. */
	protected boolean parseFile() {
		try {
			String t = "Cannot prase DynamicWindow file: ";
			if (!parseBase()) Envision.error(t + dataFile + " for scripts!", new IOException());
			if (!parseObjects()) Envision.error(t + dataFile + " for objects!", new IOException());
			if (!parseScripts()) Envision.error(t + dataFile + " for base values!", new IOException());
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
