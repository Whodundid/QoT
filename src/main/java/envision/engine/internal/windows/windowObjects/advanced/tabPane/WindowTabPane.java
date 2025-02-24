package envision.engine.internal.windows.windowObjects.advanced.tabPane;

import envision.engine.internal.windows.windowObjects.basic.WindowPanel;
import envision.engine.internal.windows.windowTypes.WindowObject;
import envision.engine.internal.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.internal.windows.windowUtil.layouts.WindowBorderLayout;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.boxes.BoxList;
import eutil.strings.EStringUtil;

public class WindowTabPane<T extends IWindowObject> extends WindowObject {
    
    //========
    // Fields
    //========
    
    private TabPaneHeader tabPaneHeader;
    private WindowPanel contentPanel;
    
    private BoxList<String, T> tabs = BoxList.newList();
    private int selectedTabIndex = -1;
    
    // default height is 28 pixels
    protected double tabHeight = 32;
    // default gap is 0 pixels
    protected double tabGap = 0;
    
    // the maximum width a tab can have
    protected double maxTabWidth = 200;
    // the minimum width a tab can have
    protected double minTabWidth = 80;
    
    public int tabHeaderBackground = EColors.pdgray.intVal;
    public int tabTextColor = EColors.white.intVal;
    public int tabSelectedColor = EColors.mgray.intVal;
    public int tabNotSelectedColor = EColors.dgray.intVal;
    public int tabBackgroundColor = EColors.pdgray.intVal;    
    //==============
    // Constructors
    //==============
    
    public WindowTabPane() {
        this.setLayout(new WindowBorderLayout());
    }
    public WindowTabPane(IWindowObject parent, double x, double y, double w, double h) {
        this();
        init(parent, x, y, w, h);
    }    
    //===========
    // Overrides
    //===========
    
    @Override
    public void initChildren() {
        tabPaneHeader = new TabPaneHeader(this);
        contentPanel = new WindowPanel(new WindowBorderLayout());
        
        addObject(tabPaneHeader, WindowBorderLayout.NORTH);
        addObject(contentPanel, WindowBorderLayout.CENTER);
    }
    
    @Override
    public void drawObject_i(float dt, int mXIn, int mYIn) {
        // draw tabs header
        drawRect(startX, startY, endX, startY + tabHeight, EColors.black);
        
        //drawRect(startX, startY + 1 + tabHeight, endX, endY, EColors.black);
        //drawRect(EColors.pdgray, 1);
        //drawRect(startX + 1, startY + 1 + tabHeight, endX - 1, startY + 2 + tabHeight, EColors.black);
        //drawRect(startX + 1, startY + 1, endX - 1, startY + 1 + tabHeight, EColors.dgray);
        super.drawObject_i(dt, mXIn, mYIn);
        //drawHRect(startX, startY - 1, endX, endY, 1, EColors.black);
    }

    //=========
    // Methods
    //=========
    
    /**
     * Creates and adds a new tab to this container with the given tab
     * name.
     * 
     * @param tabName
     */
    public void addTab(String tabName) {
        addTab(tabName, null, EColors.white.intVal);
    }
    
    public void addTab(String tabName, T object) {
        addTab(tabName, object, EColors.white.intVal);
    }
    
    public void addTab(String tabName, T object, EColors color) {
        addTab(tabName, object, color.intVal);
    }
    
    public void addTab(String tabName, T object, int color) {
        if (EStringUtil.isNotPopulated(tabName)) return;
        
        boolean makeDefault = tabs.isEmpty();
        
        TabPaneButton b = new TabPaneButton(this, tabs.size(), tabName);
        tabs.add(tabName, object);
        if (object != null) object.setVisible(false);
        object.setDimensions(contentPanel.getDimensions());
        contentPanel.addObject(object);
        
        tabPaneHeader.addTabButton(b);
        tabPaneHeader.positionTabButtons();
        
        if (makeDefault) {
            setSelectedTab(b.tabIndex);
        }
    }
    
    /**
     * Removes the tab at the specified index. If there are no tabs
     * present, null is returned.
     * 
     * @param index The index of the tab to remove
     * 
     * @return The removed tab
     */
    public IWindowObject removeTabAt(int index) {
        if (tabs.isEmpty()) {
            selectedTabIndex = 0;
            return null;
        }
        
        // don't allow bad indexes
        if (index < 0 || index >= tabs.size()) return null;
        
        boolean isCurrent = selectedTabIndex == index;
        
        // remove the tab from the header
        tabPaneHeader.removeTabButtonByIndex(index);
        
        // remove the tab from this tab pane
        var tabBox = tabs.remove(index);
        var tabObject = tabBox.getB();
        contentPanel.removeObject(tabObject);
        
        if (isCurrent) {
            selectedTabIndex--;
            if (selectedTabIndex < 0) selectedTabIndex = 0;
            setSelectedTab(selectedTabIndex);
        }
        
        return tabObject;
    }
    
    /**
     * Attempts to remove a tab with the given name. If there are no tabs
     * present, null is returned.
     * 
     * @param tabName The name of the tab to remove
     * @return The removed tab
     */
    public IWindowObject removeTabByName(String tabName) {
        var index = getTabIndexByName(tabName);
        return removeTabAt(index);
    }
    
    public IWindowObject getTabAt(int index) {
        // don't allow bad indexes
        if (index < 0 || index >= tabs.size()) return null;
        
        return tabs.getB(index);
    }
    
    public int getTabIndexByName(String name) {
        for (int i = 0; i < tabs.size(); i++) {
            if (EUtil.isEqual(name, tabs.getA(i))) return i;
        }
        return -1;
    }

    //=========
    // Getters
    //=========
    
    public TabPaneButton getTabButtonAt(int index) {
        // don't allow bad indexes
        if (index < 0 || index >= tabs.size()) return null;
        
        return tabPaneHeader.getTabPaneButtonAt(index);
    }
    
    public void setTabButtonAt(int index, TabPaneButton button) {
        // don't allow bad indexes
        if (index < 0 || index >= tabs.size()) return;
        
        tabPaneHeader.setTabPaneButtonAt(index, button);
    }
    
    /**
     * Note: Any modifications made to the BoxList returned by this method will
     * have no affect on the actual state of this TabPane.
     * 
     * @return a copy of the tabs in this tabPane in the form of a
     *             BoxList&lttabName, tabObject&gt.
     */
    public BoxList<String, T> getTabs() {
        return tabs.copy();
    }
    
    public T getSelectedTab() {
        if (tabs.isEmpty()) return null;
        if (selectedTabIndex >= tabs.size()) return null;
        return tabs.getB(selectedTabIndex);
    }
    
    public String getSelectedTabName() {
        var button = getTabButtonAt(selectedTabIndex);
        return (button != null) ? button.getTabName() : null;
    }
    
    public int getSelectedTabIndex() {
        return selectedTabIndex;
    }
    
    public double getTabGap() { return tabGap; }
    public double getMinTabWidth() { return minTabWidth; }
    public double getMaxTabWidth() { return maxTabWidth; }

    //=========
    // Setters
    //=========

    public void setSelectedTab(int index) {
        // if empty, always set selected tab to null
        if (tabs.isEmpty()) {
            selectedTabIndex = -1;
            contentPanel.removeAll();
            return;
        }
        
        // don't allow bad indexes
        if (index < 0 || index >= tabs.size()) return;
        
        int previousIndex = selectedTabIndex;
        
        // get tab and set
        selectedTabIndex = index;
        IWindowObject object = tabs.getB(index);
        for (var b : tabs) b.getB().setVisible(false);
        object.setVisible(true);
        
        // if the tab is already selected, don't select it again
        if (previousIndex == selectedTabIndex) return;
        
        object.setDimensions(contentPanel.getDimensions());
//        object.reInitChildren();
    }
    
    public void setSelectedTab(String tabName) {
        // if empty, always set selected tab index to 0
        if (tabs.isEmpty()) {
            selectedTabIndex = 0;
            return;
        }
        
        // don't allow bad inputs
        if (EStringUtil.isNotPopulated(tabName)) {
            return;
        }
        
        // set tab by index
        int index = getTabIndexByName(tabName);
        setSelectedTab(index);
    }
    
}
