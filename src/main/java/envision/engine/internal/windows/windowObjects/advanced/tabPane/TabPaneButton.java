package envision.engine.internal.windows.windowObjects.advanced.tabPane;

import envision.engine.internal.rendering.fontRenderer.FontRenderer;
import envision.engine.internal.windows.windowObjects.action.WindowButton;
import envision.engine.internal.windows.windowObjects.basic.WindowPanel;
import eutil.colors.EColors;

public class TabPaneButton extends WindowPanel {
    
    //========
    // Fields
    //========
    
    protected WindowTabPane parentTabPane;
    protected WindowButton tabButton;
    protected WindowButton closeButton;
    protected String tabName;
    protected int tabIndex;
    protected boolean isTabCloseable = true;
    protected double closeButtonSize = 27;

    //==============
    // Constructors
    //==============
    
    public TabPaneButton(WindowTabPane parent, int tabIndexIn) { this(parent, tabIndexIn, "New Tab"); }
    public TabPaneButton(WindowTabPane parent, int tabIndexIn, String nameIn) {
        init(parent);
        parentTabPane = parent;
        width = FontRenderer.strWidth(nameIn) + 6;
        height = parent.tabHeight;
        tabIndex = tabIndexIn;
        tabName = nameIn;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void initChildren() {
        tabButton = new TabButton();
        tabButton.setAction(this::setSelected);
        tabButton.setString(tabName);
        addObject(tabButton);
        
        closeButton = new WindowButton(this, "X");
        closeButton.setAction(this::closeTab);
        closeButton.setVisible(isTabCloseable);
        addObject(closeButton);
    }
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        boolean selected = isSelected();
        
        int tabColor = (selected) ? parentTabPane.tabSelectedColor : parentTabPane.tabBackgroundColor;
        
        double ey = (selected) ? endY : endY - 1;
        drawRect(EColors.black);
        drawRect(startX + 1, startY + 1, endX - 1, ey, tabColor);
    }
    
    @Override
    public void setDimensions(double x, double y, double w, double h) {
        super.setDimensions(x, y, w, h);
        
        double closeSize = (isTabCloseable) ? closeButtonSize : 0;
        
        tabButton.setDimensions(startX + 1, startY + 1, width - 3 - closeSize, height - 2);
        
        double csx = tabButton.endX;
        double csy = midY - closeSize / 2;
        closeButton.setDimensions(csx, csy, closeSize, closeSize);
    }
    
    //=========
    // Methods
    //=========
    
    public void setSelected() {
        parentTabPane.setSelectedTab(tabIndex);
    }
    
    public boolean isSelected() {
        return parentTabPane.getSelectedTabIndex() == tabIndex;
    }
    
    public void closeTab() {
        parentTabPane.removeTabAt(tabIndex);
    }
    
    //=========
    // Getters
    //=========
    
    public String getTabName() { return tabName; }
    public int getTabIndex() { return tabIndex; }
    public boolean isTabCloseable() { return isTabCloseable; }
    
    //=========
    // Setters
    //=========
    
    public void setTabCloseable(boolean val) {
        isTabCloseable = val;
    }

    public void setTabName(String name) {
        tabName = name;
        tabButton.setString(name);
    }
    
    public void setTabIndex(int index) { tabIndex = index; }
    
    //==================
    // Internal Classes
    //==================
    
    public class TabButton extends WindowButton {
        @Override
        public void drawObject(float dt, int mXIn, int mYIn) {
            boolean mouseHover = isClickable() && isMouseOver();
            boolean mouseCheck = mouseHover;
            int stringColor = isEnabled() ? (mouseCheck ? textHoverColor : parentTabPane.tabTextColor) : (drawDisabledColor ? disabledColor : parentTabPane.tabTextColor + 0xbbbbbb);
            drawStringC(displayString, midX, midY - FontRenderer.FONT_HEIGHT / 2 + 3, stringColor);
        }
    }
    
}
