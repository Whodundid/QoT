package envision.engine.windows.windowObjects.advancedObjects.menuBar;

import envision.engine.registry.types.Sprite;
import envision.engine.windows.windowTypes.WindowObject;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;

public class WindowMenuCategory extends WindowObject {
    
    //========
    // Fields
    //========
    
    protected String categoryName;
    protected final EList<WindowMenuEntry> entries = EList.newList();
    
    public static final String DEFAULT_MENU_CATEGORY_NAME = "New Menu";
    public static final int DEFAULT_MENU_CATEGORY_PIXEL_HEIGHT = 30;
    public static final int DEFAULT_MENU_CATEGORY_MAX_PIXEL_WIDTH = 400;
    
    protected int entryHeight = DEFAULT_MENU_CATEGORY_PIXEL_HEIGHT;
    protected int entryMaxWidth = DEFAULT_MENU_CATEGORY_MAX_PIXEL_WIDTH;
    
    protected boolean isSelected = false;
    
    //==============
    // Constructors
    //==============
    
    public WindowMenuCategory(String nameIn) {
        categoryName = nameIn;
        setSelected(false);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void initChildren() {
        
    }
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        //System.out.println(this.startX + " : " + this.startY + " : " + width + " : " + height);
        drawRect(EColors.black);
        drawRect(EColors.pdgray, 1);
        
        //super.drawObject(dt, mXIn, mYIn);
    }
    
    //=========
    // Methods
    //=========
    
    public void addMenuEntry(String entryName) { addMenuEntry(entryName, null, null); }
    public void addMenuEntry(String entryName, Runnable pressAction) { addMenuEntry(entryName, null, pressAction); }
    public void addMenuEntry(String entryName, Sprite menuSprite) { addMenuEntry(entryName, menuSprite, null); }
    public void addMenuEntry(String entryName, Sprite menuSprite, Runnable pressAction) {
        WindowMenuEntry entry = new WindowMenuEntry(entryName, menuSprite, pressAction);
        addMenuEntry(entry);
    }
    
    public void addMenuEntry(WindowMenuEntry entryIn) {
        if (entryIn == null) return;
        synchronized (entries) {
            entries.add(entryIn);
            entryIn.setPosition(startX, startY + (entries.size() - 1) * entryHeight);
            entryIn.setVisible(false);
            addObject(entryIn);
            double longestWidth = 0;
            for (var e : entries) {
                if (e.width > longestWidth) {
                    longestWidth = e.width;
                }
            }
            longestWidth = ENumUtil.clamp(longestWidth, 10, entryMaxWidth);
            for (var e : entries) {
                e.width = longestWidth;
            }
            setSize(longestWidth, entries.size() * entryHeight);
        }
    }
    
    /**
     * Removes the first entry under the given entry title name.
     * 
     * @param entryTitle The entry title to search for
     */
    public void removeMenuEntry(String entryTitle) {
        if (entryTitle == null) return;
        
        synchronized (entries) {
            final int len = entries.size();
            for (int i = 0; i < len; i++) {
                final var entry = entries.get(i);
                if (entryTitle.equals(entry.getEntryTitle())) {
                    entries.remove(i);
                    removeObject(entry);
                    break;
                }
            }
        }
    }
    
    public void removeMenuEntry(WindowMenuEntry entryIn) {
        synchronized (entries) {
            entries.remove(entryIn);
            removeObject(entryIn);
        }
    }
    
    public void removeAllEntries() {
        synchronized (entries) {
            var it = entries.iterator();
            while (it.hasNext()) {
                final var e = it.next();
                entries.remove(e);
                removeObject(e);
            }
        }
    }
    
    public int getEntrySize() {
        return entries.size();
    }
    
    //=========
    // Getters
    //=========
    
    public String getCategoryName() { return categoryName; }
    /** Returns a shallow copy of this menu category's entries. */
    public EList<WindowMenuEntry> getMenuEntries() { return entries.copy(); }
    
    public boolean isSelected() { return isSelected; }
    
    //=========
    // Setters
    //=========
    
    public void setCategoryName(String nameIn) { categoryName = nameIn; }
    
    public void setSelected(boolean selected) {
        isSelected = selected;
        setVisible(isSelected);
        for (var e : entries) {
            e.setVisible(isSelected);
        }
    }
    
}
