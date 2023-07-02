package envision.engine.windows.windowTypes.dynamicWindow;

import java.io.File;
import java.io.IOException;

import envision.Envision;
import envision.engine.windows.windowTypes.WindowParent;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowUtil.ObjectPosition;
import eutil.datatypes.points.Point2d;
import eutil.datatypes.util.EList;
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
	
	private Point2d parsedDims = new Point2d();
	private ObjectPosition parsedCenterType = null;
	
	private EList<IWindowObject> parsedObjects = EList.newList();
	
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
