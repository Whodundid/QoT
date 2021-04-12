package envisionEngine.eWindow.windowObjects.utilityObjects;

import envisionEngine.eWindow.windowObjects.basicObjects.WindowLabel;
import envisionEngine.eWindow.windowTypes.ActionObject;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import envisionEngine.eWindow.windowTypes.interfaces.IWindowObject;
import eutil.EUtil;
import renderUtil.EColors;
import storageUtil.EDimension;

public class WindowSetting<E extends IActionObject> extends ActionObject<E> {
	
	E object;
	WindowLabel label;

	//--------------------------
	//WindowSetting Constructors
	//--------------------------
	
	public WindowSetting(IWindowObject parent, E objectIn, String title) { this(parent, objectIn, title, EColors.lgray.intVal); }
	public WindowSetting(IWindowObject parent, E objectIn, String title, EColors colorIn) { this(parent, objectIn, title, colorIn.intVal); }
	public WindowSetting(IWindowObject parent, E objectIn, String title, int colorIn) {
		super(parent);
		
		if (objectIn != null) {
			object = objectIn;
			EDimension dims = object.getDimensions();
			
			label = new WindowLabel(this, dims.endX + 10, dims.startY + 6, title, colorIn);
			
			setDimensions(dims.startX, dims.startY, label.endX - startX, Math.max(dims.endY, label.endY) - dims.startY);
		}
	}
	
	//----------------------
	//WindowObject Overrides
	//----------------------
	
	@Override
	public void initObjects() {
		addObject(object, label);
	}
	
	@Override
	public WindowSetting setHoverText(String textIn) {
		setHoverText(textIn, object, label);
		return this;
	}
	
	//----------------------
	//ActionObject Overrides
	//----------------------
	
	@Override
	public WindowSetting setActionReceiver(IWindowObject objectIn) {
		super.setActionReceiver(objectIn);
		EUtil.nullDo(object, o -> o.setActionReceiver(objectIn));
		return this;
	}
	
	//---------------------
	//WindowSetting Getters
	//---------------------
	
	public E getObject() { return object; }
	public WindowLabel getLabel() { return label; }
	
	//---------------------
	//WindowSetting Setters
	//---------------------
	
	public WindowSetting setTitle(String title) { return EUtil.nullDoR(label, l -> l.setString(title), this); }
	public WindowSetting setLabelColor(EColors colorIn) { WindowLabel.setColor(colorIn, label); return this; }
	public WindowSetting setLabelColor(int colorIn) { WindowLabel.setColor(colorIn, label); return this; }
	
}
