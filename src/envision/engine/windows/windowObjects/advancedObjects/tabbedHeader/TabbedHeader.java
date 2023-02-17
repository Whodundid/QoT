package envision.engine.windows.windowObjects.advancedObjects.tabbedHeader;

import envision.engine.windows.windowTypes.WindowObject;

/**
 * A header bar that can be added to any object's side and is used to
 * hold categorized tabs of data.
 * <p>
 * For instance, think of a 'File', 'Edit', 'etc' bar at the top of
 * many applications.
 * <p>
 * By default, a TabbedHeader when created is built around a
 * particular existing WindowObject using its dimensions as well as
 * the specific side of that object for which the header will be
 * displayed on. By adding a new Tab from the 'addTab(String tabName)'
 * method, a new Tab button and HeaderTab space are created. Existing
 * tab buttons are resized and potentially scroll-able (depending on
 * the amount of tabs and the width of the header itself).
 * <p>
 * HeaderTabs can be manually created and added to this TabbedHeader
 * using the 'addTab(HeaderTab tab)' method.
 * <p>
 * There are no restrictions on tab names as it pertains to duplicate names.
 * The only restriction on new tabs is that the same tab cannot be added
 * multiple times to the same header.
 * 
 * @author Hunter Bragg
 *
 * @param <E>
 */
public class TabbedHeader<E> extends WindowObject<E> {
	
}
