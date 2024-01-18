package envision.engine.windows.windowObjects.advancedObjects.textArea;

public interface DocumentChangeListener {
    
    default void onStringInserted(String string, int pos) {}
    default void onStringDeleted(String string, int pos) {}
    default void onDocumentChanged() {}
    
}
