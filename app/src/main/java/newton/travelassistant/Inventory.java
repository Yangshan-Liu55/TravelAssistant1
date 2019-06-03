package newton.travelassistant;

import java.util.ArrayList;
import java.util.Map;

public class Inventory {
    private String user, userId, tripId, date, title;
    private ArrayList<ParentData> parent_data_list;
    private int total, done;
    private Map<String, Object> categories_map; // Fix crash

    // Constructors
    public Inventory() {
    }

    // Getters
    public String getUser() {
        return user;
    }
    public String getUserId() {
        return userId;
    }
    public String getTripId() {
        return tripId;
    }
    public String getDate() {
        return date;
    }
    public String getTitle() {
        return title;
    }
    public ArrayList<ParentData> getParent_data_list() {
        return parent_data_list;
    }

    public int getTotal() {
        if (parent_data_list != null) {
            total = 0;
            for (ParentData parent: parent_data_list) {
                total += parent.getCount(); // Same as total += parent.getChild_list().size();
            }
        }
        return total;
    }
    public int getDone() {
        if (parent_data_list != null) {
            done = 0;
            for (ParentData parent : parent_data_list) {
                done += parent.getDoneCount(); // getDoneCount() is customized method
            }
        }
        return done;
    }
    // Fix crash
    public Map<String, Object> getCategories_map() {
        return categories_map;
    }

    public void setCategories_map(Map<String, Object> categories_map) {
        this.categories_map = categories_map;
    }

    public void setParent_data_listFromCategories_map(Map<String, Object> categories_map){
        ArrayList<ParentData> parent_data_list = new ArrayList<>();
        Map<String, Object> category_map; // parent map
        Map<String, Object> text_map; // child map
        if (!categories_map.isEmpty()) {
            for(String category: categories_map.keySet()) {
                ParentData temp_parent = new ParentData(); // Loop must have new variable
                ArrayList<ChildData> child_list = new ArrayList<>(); // Loop must have new variable
                temp_parent.setCategory(category); // key

                // Get child_list data from a category_map
                category_map = (Map<String, Object>) categories_map.get(category);
                if (!category_map.isEmpty()) {
                    for (String text: category_map.keySet()) {
                        ChildData temp_child = new ChildData(); // Loop must have new variable
                        temp_child.setText(text); // key

                        text_map = (Map<String, Object>) category_map.get(text);
                        temp_child.setDone(((Long) text_map.get("done")).intValue());
                        temp_child.setQuantity(((Long) text_map.get("quantity")).intValue());

                        child_list.add(temp_child);
                    }
                }

                temp_parent.setChild_list(child_list);
                parent_data_list.add(temp_parent);
            }
        }
        this.parent_data_list = parent_data_list;
    }
    // End Fix crash

    // Setters
    public void setUser(String user) {
        this.user = user;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setTripId(String tripId) {
        this.tripId = tripId;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setParent_data_list(ArrayList<ParentData> parent_data_list) {
        this.parent_data_list = parent_data_list;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public void setDone(int done) {
        this.done = done;
    }
}
