package envision.engine.internal.windows.windowObjects.advanced.tabPane;

import envision.engine.internal.windows.windowTypes.WindowObject;
import eutil.EUtil;
import eutil.datatypes.util.EList;

public class TabPaneHeader extends WindowObject {
    
    //========
    // Fields
    //========
    
    private WindowTabPane tabPane;
    private EList<TabPaneButton> tabButtons = EList.newList();
    protected double buttonPadding = 16;
    
    //==============
    // Constructors
    //==============
    
    public TabPaneHeader(WindowTabPane tabPaneIn) {
        this.tabPane = tabPaneIn;
        this.setMinHeight(tabPane.tabHeight);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        drawRect(tabPane.tabHeaderBackground);
        //drawHRect(EColors.black);
        
        //drawStringC(getDimensions());
    }
    
    //==================
    // Internal Methods
    //==================
    
    protected void positionTabButtons() {
        double sx = startX;
        for (int i = 0; i < tabButtons.size(); i++) {
            var button = tabButtons.get(i);
            
            double padding = buttonPadding + ((button.isTabCloseable) ? button.closeButtonSize : 0);
            double w = strWidth(button.getTabName());
            
            button.setPosition(sx, startY);
            button.setDimensions(sx, startY + 1, w + padding, height - 1);
            
            // shift over by button's width and whatever tab gap there is defined
            sx += button.width + tabPane.tabGap;
        }
    }
    
    //=========
    // Methods
    //=========
    
    public void addTabButton(TabPaneButton button) {
        if (button == null) return;
        tabButtons.add(button);
        addObject(button);
        positionTabButtons();
    }
    
    public void removeTabButton(TabPaneButton button) {
        if (button == null) return;
        boolean removed = tabButtons.remove(button);
        if (removed) {
            removeObject(button);
            positionTabButtons();
        }
    }
    
    public TabPaneButton getTabPaneButtonAt(int index) {
        return tabButtons.get(index);
    }
    
    public void setTabPaneButtonAt(int index, TabPaneButton button) {
        tabButtons.set(index, button);
    }
    
    public void removeTabButtonByIndex(int index) {
        if (tabButtons.isEmpty()) return;
        var button = tabButtons.getFirst(b -> b.tabIndex == index);
        removeTabButton(button);
    }
    
    public void removeTabButtonByName(String name) {
        if (tabButtons.isEmpty()) return;
        tabButtons.removeFirst(b -> EUtil.isEqual(b.tabName, name));
    }
    
    public void clearButtons() {
        tabButtons.clear();
    }
    
}
