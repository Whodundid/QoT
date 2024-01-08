package envision.engine.windows.windowObjects.advancedObjects.menuBar;

import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

public class WindowMenuBar extends WindowObject {
    
    //========
    // Fields
    //========
    
    protected final EList<WindowMenuCategory> categories = EList.newList();
    protected final EList<WindowButton> catButtons = EList.newList();
    protected WindowMenuCategory currentlyOpenCategory = null;
    
    //==============
    // Constructors
    //==============
    
    public WindowMenuBar(WindowObject parent) {
        this.setDimensions(parent.startX, parent.startY, parent.width, 40);
    }
    
    //===========
    // Overrides
    //===========

    @Override
    public void initChildren() {
        buildCategoryButtons();
    }
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        drawRect(EColors.black);
        drawRect(EColors.pdgray, 1);
        
        super.drawObject(dt, mXIn, mYIn);
    }
    
    @Override
    public void onAdded() {
        getTopParent().registerListener(this);
        for (var c : categories) addObject(c);
    }
    
    @Override
    public void onClosed() {
        getTopParent().unregisterListener(this);
    }
    
    @Override
    public void onEvent(ObjectEvent e) {
        
    }
    
    //=========
    // Methods
    //=========
    
    /**
     * Creates a new menu category with the given category name and attempts to
     * add it to this menu bar. If a category already exists under then given
     * name, then the new category is not added and null is returned.
     * 
     * @param catNameIn The name of the category to create
     * 
     * @return The newly created and added category or null if one already
     *         existed under that name
     */
    public WindowMenuCategory addMenuCategory(String catNameIn) {
        WindowMenuCategory cat = new WindowMenuCategory(catNameIn);
        if (addMenuCategory(cat)) return cat;
        return null;
    }
    
    /**
     * Adds a new category to this menu bar. New categories cannot share names.
     * If an already existing category of the same name is in this menu bar,
     * then the new category is not added and 'false' is returned.
     * 
     * @param categoryIn The category to add
     * 
     * @return true if successfully added
     */
    public boolean addMenuCategory(WindowMenuCategory categoryIn) {
        if (categoryIn == null) return false;
        // don't add multiple categories of the same name
        if (getCategoryByName(categoryIn.getCategoryName()) != null) return false;
        categories.add(categoryIn);
        if (isInitialized()) {
            buildCategoryButtons();
            addObject(categoryIn);
        }
        return true;
    }
    
    protected void buildCategoryButtons() {
        synchronized (categories) {
            final int size = categories.size();
            double lastX = startX + 1;
            
            for (int i = 0; i < size; i++) {
                final var cat = categories.get(i);
                final String name = cat.getCategoryName();
                WindowButton catButton = getOrCreateCategoryButton(name);
                final double w = strWidth(name);
                catButton.setDimensions(lastX, startY + 1, w + 20, height - 2);
                catButton.setGenericObject(i); // annoying index transfer workaround
                catButton.setAction(() -> openCategory((int) catButton.getGenericObject()));
                cat.setPosition(lastX, endY);
                lastX += w;
            }
            
            //categories.forEach(o -> o.setVisible(false));
        }
    }
    
    public WindowButton getCategoryButton(String catName) {
        if (catName == null) return null;
        synchronized (catButtons) {
            final int size = catButtons.size();
            for (int i = 0; i < size; i++) {
                final var cat = catButtons.get(i);
                if (catName.equals(cat.getObjectName())) return cat;
            }
        }
        return null;
    }
    
    public WindowMenuCategory getCategoryByName(String catName) {
        if (catName == null) return null;
        synchronized (categories) {
            final int len = categories.size();
            for (int i = 0; i < len; i++) {
                var cat = categories.get(i);
                if (catName.equals(cat.getCategoryName())) {
                    return cat;
                }
            }            
        }
        return null;
    }
    
    protected WindowButton getOrCreateCategoryButton(String catName) {
        if (catName == null) throw new IllegalArgumentException("Given category name cannot be null!");
        WindowButton catButton = getCategoryButton(catName);
        if (catButton == null) {
            catButton = new WindowButton(this, catName);
            catButton.setObjectName(catName);
            addObject(catButton);
            catButtons.add(catButton);
        }
        return catButton;
    }
    
    public void removeCategory(String catName) {
        if (catName == null) return;
        synchronized (categories) {
            WindowMenuCategory category = getCategoryByName(catName);
            categories.remove(category);
        }
        synchronized (catButtons) {
            WindowButton catButton = getCategoryButton(catName);
            catButtons.remove(catButton);
            removeObject(catButton);
        }
        buildCategoryButtons();
    }
    
    protected void openCategory(int index) {
        if (index < 0 || index >= catButtons.size()) return;
        
        //categories.forEach(o -> o.setVisible(false));
        
        //double drawX = catButtons.get(index).startX;
        currentlyOpenCategory = categories.get(index);
        //currentlyOpenCategory.setVisible(true);
    }
    
}
