package envision.engine.internal.windows.windowObjects.advanced.textArea;

public interface DocumentChangeListener {
    
    default void onStringInserted(String string, int pos) {}
    default void onStringDeleted(String string, int pos) {}
    default void onDocumentChanged() {}
    
}
