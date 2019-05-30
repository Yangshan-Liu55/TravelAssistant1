package newton.travelassistant;

public class ChildData {
    private String text;
    private int quantity, done;
//    private Boolean isDone;

    // Constructors
    public ChildData() {
    }
    public ChildData(String text, int quantity, int done) {
        this.text = text;
        this.quantity = quantity;
        this.done = done;
    }

    // Getters
    public String getText() {
        return text;
    }
    public int getQuantity() {
        return quantity;
    }
    public int getDone() {
        return done;
    }

    // Setters
    public void setText(String text) {
        this.text = text;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setDone(int done) {
        this.done = done;
    }

}
