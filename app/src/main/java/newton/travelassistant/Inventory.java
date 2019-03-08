package newton.travelassistant;

import java.util.ArrayList;

public class Inventory {
    private String user, tripId, date, title;
    private ArrayList<ParentData> parent_data_list;
    private int total, done;

    // Constructors
    public Inventory() {
    }

    // Getters
    public String getUser() {
        return user;
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

    // Setters
    public void setUser(String user) {
        this.user = user;
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
