package envision.engine.windows.windowObjects.utilityObjects;

import envision.engine.windows.windowObjects.basicObjects.WindowLabel;
import envision.engine.windows.windowTypes.ActionObject;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.math.dimensions.Dimension_d;

public class WindowSetting<E extends IActionObject> extends ActionObject<E> {
	
	//--------
	// Fields
	//--------
	
	private E object;
	private WindowLabel<?> label;

	//--------------
	// Constructors
	//--------------
	
	public WindowSetting(IWindowObject<?> parent, E objectIn, String title) { this(parent, objectIn, title, EColors.lgray.intVal); }
	public WindowSetting(IWindowObject<?> parent, E objectIn, String title, EColors colorIn) { this(parent, objectIn, title, colorIn.intVal); }
	public WindowSetting(IWindowObject<?> parent, E objectIn, String title, int colorIn) {
		super(parent);
		
		if (objectIn != null) {
			object = objectIn;
			Dimension_d dims = object.getDimensions();
			
			label = new WindowLabel(this, dims.endX + 10, dims.startY + 6, title, colorIn);
			
			setDimensions(dims.startX, dims.startY, label.endX - startX, Math.max(dims.endY, label.endY) - dims.startY);
		}
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initChildren() {
		addObject(object, label);
	}
	
	@Override
	public void setHoverText(String textIn) {
		setHoverText(textIn);
	}
	
	@Override
	public void setActionReceiver(IWindowObject<?> objectIn) {
		super.setActionReceiver(objectIn);
		EUtil.nullDo(object, o -> o.setActionReceiver(objectIn));
	}
	
	//---------
	// Getters
	//---------
	
	public E getObject() { return object; }
	public WindowLabel getLabel() { return label; }
	
	//---------
	// Setters
	//---------
	
	public void setTitle(String title) { EUtil.nullDo(label, l -> l.setString(title)); }
	public void setLabelColor(EColors colorIn) { WindowLabel.setColor(colorIn, label); }
	public void setLabelColor(int colorIn) { WindowLabel.setColor(colorIn, label); }
	
}
