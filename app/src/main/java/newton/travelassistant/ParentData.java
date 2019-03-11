package newton.travelassistant;

import java.util.ArrayList;

public class ParentData {
    private String category;
    private ArrayList<ChildData>  child_list; //优雅X
    private int count, doneCount;

    // Getters
    public String getCategory() {
        return category;
    }
    public ArrayList<ChildData> getChild_list() {
        return child_list;
    }
    public int getCount() {
        if (child_list != null) {
            count = child_list.size();
        } else {
            count = 0;
        }
        return count;
    }
    public int getDoneCount() {
        doneCount = 0;
        if (child_list != null) {
            for (ChildData child : child_list) {
                doneCount += child.getDone();
            }
        }
        return doneCount;
    }

    // Setters
    public void setCategory(String category) {
        this.category = category;
    }
    public void setChild_list(ArrayList<ChildData> child_list) {
        this.child_list = child_list;
    }

}
