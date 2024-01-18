package envision.engine.windows.windowObjects.actionObjects;

import envision.engine.windows.windowTypes.ActionObject;

public class WindowToggleSetting extends ActionObject {
    
    //========
    // Fields
    //========
    
    private WindowCheckBox checkbox;
    //private WindowLabel label;
    private String settingString = "Setting";
    //private String stringToSet;
    private boolean wasValueSet = false;
    private boolean valueToSet = false;
    
    //==============
    // Constructors
    //==============
    
    public WindowToggleSetting() {}
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void initChildren() {
        checkbox = new WindowCheckBox(this, startX, startY, height, height);
        //label = new WindowLabel(this, checkbox.endX + 5, checkbox.midY - FontRenderer.FONT_HEIGHT * 0.4);
        
        if (wasValueSet) {
            checkbox.setIsChecked(valueToSet);
            wasValueSet = false;
        }
        
        //if (stringToSet != null) label.setString(stringToSet);
        //else label.setString("Setting");
        
        checkbox.setAction(this::updateState);
        
        addObject(checkbox);
    }
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        scissor();
        drawString(settingString, checkbox.endX + 5, checkbox.midY - FONT_MID_Y);
        endScissor();
    }
    
    //=========
    // Methods
    //=========
    
    public boolean toggle() {
        if (checkbox == null) {
            valueToSet = !valueToSet;
            wasValueSet = true;
            return valueToSet;
        }
        
        updateState(!checkbox.getIsChecked());
        return checkbox.getIsChecked();
    }
    
    //=========
    // Getters
    //=========
    
    public boolean getValue() {
        if (checkbox == null) return valueToSet;
        return checkbox.getIsChecked();
    }
    
    //=========
    // Setters
    //=========
    
    protected void updateState() {
        performAction();
    }
    
    public void updateState(boolean value) {
        if (checkbox == null) {
            valueToSet = value;
            wasValueSet = true;
            return;
        }
        
        checkbox.setIsChecked(value);
    }
    
    public void setString(String value) {
        settingString = value;
        //if (label != null) label.setString(value);
        //else stringToSet = value;
    }
    
}
