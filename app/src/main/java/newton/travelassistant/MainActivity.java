package newton.travelassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    private List<Inventory> myLists = new ArrayList<Inventory>();
    private List<String> tripIdList = new ArrayList<String>();
    private ListsAdapter listsAdapter;
    private static final String TAG = "MainActivity";
    private String userName = "admin";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    // Start intent to dashboard activity
                    return true;
                case R.id.navigation_currency:
                    // Start intent to currency activity
                    return true;
                case R.id.navigation_notifications:
                    // Start intent to notifications activity
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); //
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title_bar); //

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Set listView
        listsAdapter = new ListsAdapter(MainActivity.this, R.layout.lists_item, myLists);
        ListView myListView = (ListView)findViewById(R.id.my_list_view);
        myListView.setAdapter(listsAdapter);
        initMyLists();

        //ListView click event
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Inventory inventory = myLists.get(i); //Get the current position of myListView

                Intent intent = new Intent(MainActivity.this, InventoryActivity.class);
//                intent.putExtra("tripId", inventory.getTripId());
                Bundle bundle = new Bundle();
                bundle.putString("user", inventory.getUser());
                bundle.putString("tripId", inventory.getTripId());
                bundle.putString("date", inventory.getDate());
                bundle.putString("title", inventory.getTitle());
//                intent.putExtra("inventoryBundle", bundle);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lists_menu, menu); // Import lists_menu layout
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.lists_menu_add:
                Intent intent = new Intent(MainActivity.this, InventoryAddActivity.class);
                startActivity(intent);
                break;
            case R.id.lists_menu_login:
                Toast.makeText(MainActivity.this, "Login", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private  void initMyLists() {
        myLists.clear();
        tripIdList.clear();
        listsAdapter.notifyDataSetChanged(); // Update ListView，otherwise history remains on ListView

//        FirebaseApp.initializeApp(getBaseContext()); //  classpath 'com.google.gms:google-services:4.2.0' because 4.1.0 has issue
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userName).collection("myLists")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Firestore Listener error:", e);
//                            Toast.makeText(MainActivity.this,"Database Listener error", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String document_id;
                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                            document_id = dc.getDocument().getId();
                            Map<String, Object> document_map = dc.getDocument().getData();

                            // Get Inventory data from document
                            Inventory temp_inventory = new Inventory(); // Loop must get new Inventory() to have new data
                            temp_inventory.setUser(userName);
                            temp_inventory.setTripId(document_id);
                            temp_inventory.setDate((String) document_map.get("date"));
                            temp_inventory.setTitle((String) document_map.get("title"));
                            temp_inventory.setTotal(((Long) document_map.get("total")).intValue());
                            temp_inventory.setDone(((Long) document_map.get("done")).intValue());

                            switch (dc.getType()) {
                                case ADDED:
//                                    Log.d(TAG, "New city: " + dc.getDocument().getData());
                                    // Add data to myLists
                                    if (!tripIdList.contains(document_id)) {
                                        myLists.add(temp_inventory);
                                        tripIdList.add(document_id);
                                        listsAdapter.notifyDataSetChanged();
                                    }
                                    break;
                                case MODIFIED:
//                                    Log.d(TAG, "Modified city: " + dc.getDocument().getData());
                                    // Modify data in myLists
                                    int tripIndex = tripIdList.indexOf(document_id);
                                    if (tripIndex >= 0) {
                                        temp_inventory.setUser(myLists.get(tripIndex).getUser());
                                        myLists.set(tripIndex, temp_inventory);
                                        listsAdapter.notifyDataSetChanged();
                                    }
                                    break;
                                case REMOVED:
//                                    Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                                    int tripPosition= tripIdList.indexOf(document_id);
                                    if (tripPosition >= 0) {
                                        myLists.remove(tripPosition);
                                        tripIdList.remove(tripPosition);
                                        listsAdapter.notifyDataSetChanged();
                                    }
                                    break;
                            }
                        }
//                        listsAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void initialiseMyLists() {
        myLists.clear();
        tripIdList.clear();
//        listsAdapter.notifyDataSetChanged(); // Update ListView，otherwise history remains on ListView

//        FirebaseApp.initializeApp(getBaseContext()); //  classpath 'com.google.gms:google-services:4.2.0' because 4.1.0 has issue
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userName).collection("myLists")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Inventory temp_inventory = new Inventory();
                            String document_id;

                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                document_id = documentSnapshot.getId();
                                Map<String, Object> document_map = documentSnapshot.getData();

                                // Get Inventory data from document
                                temp_inventory.setUser(userName);
                                temp_inventory.setTripId(document_id);
                                temp_inventory.setDate((String) document_map.get("date"));
                                temp_inventory.setTitle((String) document_map.get("title"));
                                temp_inventory.setTotal(((Long) document_map.get("total")).intValue());
                                temp_inventory.setDone(((Long) document_map.get("done")).intValue());

                                // Add data to myLists
                                if (!tripIdList.contains(document_id)) {
                                    myLists.add(temp_inventory);
                                    tripIdList.add(document_id);
                                    listsAdapter.notifyDataSetChanged();
                                }
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void  setMyLists() {
        myLists.clear();
        tripIdList.clear();
//        listsAdapter.notifyDataSetChanged();

        Inventory inventory = new Inventory();
        inventory.setUser(userName);
        inventory.setTripId("test1");
        inventory.setDate("2019-05-01");
        inventory.setTitle("Test1");
        inventory.setTotal(3);
        inventory.setDone(1);
        myLists.add(inventory);
        inventory.setUser(userName);
        inventory.setTripId("test2");
        inventory.setDate("2019-08-01");
        inventory.setTitle("Test2");
        inventory.setTotal(5);
        inventory.setDone(4);
        myLists.add(inventory);
        listsAdapter.notifyDataSetChanged();
    }
}
