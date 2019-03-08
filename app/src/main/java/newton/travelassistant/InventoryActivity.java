package newton.travelassistant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Nullable;

public class InventoryActivity extends AppCompatActivity {
    private ExpandableListView myExListView;
    private MyExpandableListAdapter myExpandableListAdapter;
    private Inventory inventory = new Inventory();
    private ArrayList<ParentData> parent_list;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG2 = "InventoryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);


        myExpandableListAdapter = new MyExpandableListAdapter(this, parent_list);
        myExListView = (ExpandableListView) findViewById(R.id.expandable_list);
        myExListView.setAdapter(myExpandableListAdapter);
        initInventory();

        // Parent item click event
        myExListView.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                // TODO Auto-generated method stub
                Log.d(TAG2, "Click parent");
                Toast.makeText(InventoryActivity.this, parent_list.get(i).getCategory(), Toast.LENGTH_LONG).show();
                return false;
            }
        });

        // Child item click event
        myExListView.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                // TODO Auto-generated method stub
                Log.d(TAG2, "Click child");
                Toast.makeText(InventoryActivity.this, parent_list.get(i).getChild_list().get(i1).getText(), Toast.LENGTH_LONG).show();
                return false;
            }
        });

    }

    private void initInventory() {
//        Bundle bundle = getIntent().getBundleExtra("inventoryBundle");
        Bundle bundle = getIntent().getExtras();
        inventory.setUser(bundle.getString("user"));
        inventory.setTripId(bundle.getString("tripId"));
        inventory.setDate(bundle.getString("date"));
        inventory.setTitle(bundle.getString("title"));

        this.setTitle(bundle.getString("title"));

        if (parent_list == null) {
            parent_list = new ArrayList<ParentData>();
        }

        parent_list.clear();
        myExpandableListAdapter.flashData(parent_list);
//        myExpandableListAdapter.notifyDataSetChanged();

        db.collection("users").document(inventory.getUser()).collection("myLists")
                .document(inventory.getTripId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG2, "Firestore Listener failed.", e);
                    return;
                }

                // Get Inventory data from document
                if (documentSnapshot != null && documentSnapshot.exists()) {
//                    Log.d(TAG, "source " + " data: " + documentSnapshot.getData());
                    Map<String, Object> document_map = documentSnapshot.getData();
                    Map<String, Object> categories_map = (Map<String, Object>) document_map.get("categories");

                    Map<String, Object> category_map;
                    Map<String, Object> text_map;
                    for(String category: categories_map.keySet()) {
                        ParentData temp_parent = new ParentData(); // Loop must have new variable
                        ChildData temp_child = new ChildData(); // Loop must have new variable
                        ArrayList<ChildData> child_list = new ArrayList<ChildData>(); // Loop must have new variable

                        temp_parent.setCategory(category);

                        // Get child_list data from a category_map
                        category_map = (Map<String, Object>) categories_map.get(category);
                        for (String text: category_map.keySet()) {
                            temp_child.setText(text);

                            text_map = (Map<String, Object>) category_map.get(text);
                            temp_child.setDone(((Long) text_map.get("done")).intValue());
                            temp_child.setQuantity(((Long) text_map.get("quantity")).intValue());

                            child_list.add(temp_child);
                        }

                        temp_parent.setChild_list(child_list);
                        parent_list.add(temp_parent);
                        inventory.setParent_data_list(parent_list);
                    }

                    inventory.setParent_data_list(parent_list);
                    myExpandableListAdapter.flashData(parent_list);
//                    myExpandableListAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG2, "source " + " data: null or not exists");
                }
            }
        });
    }

    private void setInventory() {
        Bundle bundle = getIntent().getExtras();
        inventory.setUser(bundle.getString("user"));
        inventory.setTripId(bundle.getString("tripId"));
        inventory.setDate(bundle.getString("date"));
        inventory.setTitle(bundle.getString("title"));

        if (parent_list == null) {
            parent_list = new ArrayList<ParentData>();
        }

        parent_list.clear();
        ArrayList<ChildData> child_list = new ArrayList<ChildData>();
        ParentData temp_parent = new ParentData();
        ChildData temp_child = new ChildData();
        temp_child.setText("child1");
        temp_child.setQuantity(300);
        temp_child.setDone(1);
        child_list.add(temp_child);
        ChildData temp_child2 = new ChildData();
        temp_child2.setText("child2");
        temp_child2.setQuantity(2);
        temp_child2.setDone(1);
        child_list.add(temp_child2);
        temp_parent.setCategory("parent1");
        temp_parent.setChild_list(child_list);
        parent_list.add(temp_parent);

        ArrayList<ChildData> child_list2 = new ArrayList<ChildData>();
        ParentData temp_parent2 = new ParentData();
        ChildData temp_child3 = new ChildData();
        temp_child3.setText("child3");
        temp_child3.setQuantity(2);
        temp_child3.setDone(0);
        child_list2.add(temp_child3);
        ChildData temp_child4 = new ChildData();
        temp_child4.setText("child4");
        temp_child4.setQuantity(10);
        temp_child4.setDone(0);
        child_list2.add(temp_child4);
        temp_parent2.setCategory("parent2");
        temp_parent2.setChild_list(child_list2);
        parent_list.add(temp_parent2);
        inventory.setParent_data_list(parent_list);

        myExpandableListAdapter.flashData(parent_list);
    }

    private void updateDatabase() {

    }
}
