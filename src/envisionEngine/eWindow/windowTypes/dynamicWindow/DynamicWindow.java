package envisionEngine.eWindow.windowTypes.dynamicWindow;

import envisionEngine.eWindow.windowTypes.WindowParent;
import envisionEngine.eWindow.windowTypes.interfaces.IWindowObject;
import eutil.EUtil;
import java.io.File;
import java.io.IOException;
import main.Game;
import renderUtil.CenterType;
import storageUtil.EArrayList;
import storageUtil.StorageBox;

/** A special type of WindowParent that can be directly constructed from a serialized file. */
public class DynamicWindow extends WindowParent {
	
	private File dataFile;
	private boolean failed = false;
	private boolean built = false;
	
	//parsed values
	
	private StorageBox<Double, Double> parsedDims = new StorageBox();
	private CenterType parsedCenterType = null;
	
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
				else { Game.error("Cannot parse DynamicWindow file: " + dataFile + " for scripts!", new IOException()); }
			}
			else { Game.error("Cannot parse DynamicWindow file: " + dataFile + " for objects!", new IOException()); }
		}
		else { Game.error("Cannot parse DynamicWindow file: " + dataFile + " for base values!", new IOException()); }
		
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
