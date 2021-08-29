package windowLib.windowTypes.dynamicWindow;

import eutil.EUtil;
import eutil.storage.Box2;
import eutil.storage.EArrayList;
import java.io.File;
import java.io.IOException;
import main.QoT;
import windowLib.windowTypes.WindowParent;
import windowLib.windowTypes.interfaces.IWindowObject;
import windowLib.windowUtil.ObjectPosition;

/** A special type of WindowParent that can be directly constructed from a serialized file. */
public class DynamicWindow extends WindowParent {
	
	private File dataFile;
	private boolean failed = false;
	private boolean built = false;
	
	//parsed values
	
	private Box2<Double, Double> parsedDims = new Box2();
	private ObjectPosition parsedCenterType = null;
	
	private EArrayList<IWindowObject> parsedObjects = new EArrayList();
	
	//--------------------------
	//DynamicWindow Constructors
	//--------------------------
	
	public DynamicWindow(File fileIn) {
		dataFile = fileIn;
	}
	
	//---------------------
	//DynamicWindow Methods
	//---------------------
	
	/** Attempts to construct a WindowParent object from the associated serialized file. */
	public boolean build() {
		return built = EUtil.tryFileCodeR(dataFile, a -> parseFile());
	}
	
	/** Returns true if, and only if, each and every step of the file reading process succeeds. */
	protected boolean parseFile() {
		if (parseBase()) {
			if (parseObjects()) {
				if (parseScripts()) {
					return true;
				}
				else { QoT.error("Cannot parse DynamicWindow file: " + dataFile + " for scripts!", new IOException()); }
			}
			else { QoT.error("Cannot parse DynamicWindow file: " + dataFile + " for objects!", new IOException()); }
		}
		else { QoT.error("Cannot parse DynamicWindow file: " + dataFile + " for base values!", new IOException()); }
		
		failed = true;
		return false;
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
