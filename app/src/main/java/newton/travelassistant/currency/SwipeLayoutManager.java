package newton.travelassistant.currency;

public class SwipeLayoutManager {

    private SwipeLayoutManager() {
    }

    private static SwipeLayoutManager mInstance = new SwipeLayoutManager();

    public static SwipeLayoutManager getInstance() {
        return mInstance;
    }

    private SwipeLayout currentLayout;

    public void setSwipeLayout(SwipeLayout layout) {
        this.currentLayout = layout;
    }


    public void clearCurrentLayout() {
        currentLayout = null;
    }


    public void closeCurrentLayout() {
        if (currentLayout != null) {
            currentLayout.close();
        }
    }


    public boolean isShouldSwipe(SwipeLayout swipeLayout) {
        if (currentLayout == null) {
            return true;
        } else {
            return currentLayout == swipeLayout;
        }
    }
}
