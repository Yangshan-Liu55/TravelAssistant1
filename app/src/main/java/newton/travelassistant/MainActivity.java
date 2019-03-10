package newton.travelassistant;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    private List<Inventory> myLists = new ArrayList<Inventory>();
    private List<String> tripIdList = new ArrayList<String>();
    private ListsAdapter listsAdapter;
    private static final String TAG = "MainActivity";
    private String userName = "admin";
    private FirebaseFirestore db;
    private  int list_position;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Set listView
        listsAdapter = new ListsAdapter(MainActivity.this, R.layout.lists_item, myLists);
        ListView myListView = (ListView)findViewById(R.id.my_list_view);
        myListView.setAdapter(listsAdapter);
        initMyLists();
        this.registerForContextMenu(myListView); // Register context menu

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
                bundle.putInt("done", inventory.getDone());
                bundle.putInt("total", inventory.getTotal());
//                intent.putExtra("inventoryBundle", bundle);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.lists_menu, menu); // Import lists_menu layout
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.lists_menu_add:
                addList();
                break;
            case R.id.lists_menu_login:
                Toast.makeText(MainActivity.this, "Login", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        list_position = info.position; // Get item position

//        menu.setHeaderTitle("Context Menu"); // Set menu layout
//        menu.setHeaderIcon(R.drawable.ic_delete_forever_black_24dp);
        MenuItem item_delete = menu.add(1, 1, 1, "Delete"); // menu.add(groupId,itemId,order,title)
//        item_delete.setIcon(R.drawable.ic_delete_forever_black_24dp); // 在API>=11时，是不显示图标的
        item_delete.setTitle("Delete "+myLists.get(list_position).getTitle());
        menu.add(1, 2, 1, "Edit "+myLists.get(list_position).getTitle());

        super.onCreateContextMenu(menu, v, menuInfo); // Must inflate the menu
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
//                Toast.makeText(MainActivity.this, "Delete Clicked", Toast.LENGTH_SHORT).show();
                deleteList();
                break;
            case 2:
//                Toast.makeText(MainActivity.this, "Delete Clicked", Toast.LENGTH_SHORT).show();
                editList();
                break;
            default:
                break;
        }
        return true;
//        return super.onContextItemSelected(item); // true or false
    }

    private void addList() {
        AlertDialog.Builder addBuilder = new AlertDialog.Builder(MainActivity.this);
        addBuilder.setIcon(R.drawable.ic_playlist_add_black_24dp);
        addBuilder.setTitle("NEW LIST");
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_inventory_add, null);
        addBuilder.setView(view);

        final EditText title = (EditText)view.findViewById(R.id.dialog_add_title);
        final EditText date = (EditText)view.findViewById(R.id.dialog_add_date);
        EditText category = (EditText)view.findViewById(R.id.dialog_add_category);
        EditText text = (EditText)view.findViewById(R.id.dialog_add_text);
        EditText quantity = (EditText)view.findViewById(R.id.dialog_add_quantity);
        CheckBox isDone = (CheckBox)view.findViewById(R.id.dialog_add_isdone);
        ImageButton img_btn_delete = (ImageButton)view.findViewById(R.id.dialog_add_delete);
        title.setVisibility(View.VISIBLE);
        date.setVisibility(View.VISIBLE);
        category.setVisibility(View.GONE);
        text.setVisibility(View.GONE);
        quantity.setVisibility(View.GONE);
        isDone.setVisibility(View.GONE);
        img_btn_delete.setVisibility(View.GONE);

        if (title.getText().toString().trim().equals("")) { //isEmpty()
                   title.setError("Input title");
        }

        addBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String a = title.getText().toString();
                String b = date.getText().toString();
//                Log.w(TAG, "Add a list: "+a);
                if (a.trim().equals("")) { //isEmpty()
//                   title.setError("Input title");
                    Toast.makeText(MainActivity.this, "Failed! Title is empty", Toast.LENGTH_SHORT).show();
                }  else {
                    uploadListData(b, a);
                }
            }
        });
        addBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        });
        addBuilder.show();
    }

    private  void uploadListData(String date, String title) {
        Map<String, Object> list = new HashMap<>();
        list.put("date", date);
        list.put("done", 0);
        list.put("title", title);
        list.put("total", 0);
        Map<String, Object> categories = new HashMap<>();
        list.put("categories", categories);

        db.collection("users").document(userName).collection("myLists")
                .add(list)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
//                        listsAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"Error uploading to Firestore: ", e);
                    }
                });
    }

    private void deleteList() {
        AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(MainActivity.this);
        deleteBuilder.setIcon(R.drawable.ic_delete_forever_black_24dp);
        deleteBuilder.setTitle("DELETE LIST");
        deleteBuilder.setMessage("Forever delete " + myLists.get(list_position).getTitle() + "?");
        deleteBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                removeListData();
            }
        });
        deleteBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(MainActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        });
        deleteBuilder.show();
    }

    private void removeListData() {
        if(list_position >= 0) {
            String tripId = myLists.get(list_position).getTripId();
            if (!tripId.isEmpty()) {
                db.collection("users").document(userName).collection("myLists")
                        .document(tripId)
                        .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Firestore error deleting document", e);
                        Toast.makeText(MainActivity.this, "Failed deleting!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void editList() {
        final AlertDialog.Builder editBuilder = new AlertDialog.Builder(MainActivity.this);
        editBuilder.setIcon(R.drawable.ic_edit_black_24dp);
        editBuilder.setTitle("EDIT LIST");
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_inventory_add, null);
        editBuilder.setView(view);

        final EditText editText_title = (EditText)view.findViewById(R.id.dialog_add_title);
        final EditText date = (EditText)view.findViewById(R.id.dialog_add_date);
        EditText category = (EditText)view.findViewById(R.id.dialog_add_category);
        EditText text = (EditText)view.findViewById(R.id.dialog_add_text);
        EditText quantity = (EditText)view.findViewById(R.id.dialog_add_quantity);
        CheckBox isDone = (CheckBox)view.findViewById(R.id.dialog_add_isdone);
        ImageButton img_btn_delete = (ImageButton)view.findViewById(R.id.dialog_add_delete);
        editText_title.setVisibility(View.VISIBLE);
        date.setVisibility(View.VISIBLE);
        category.setVisibility(View.GONE);
        text.setVisibility(View.GONE);
        quantity.setVisibility(View.GONE);
        isDone.setVisibility(View.GONE);
        img_btn_delete.setVisibility(View.VISIBLE);

        Inventory edit_inventory = myLists.get(list_position);
        editText_title.setText(edit_inventory.getTitle());
        date.setText(edit_inventory.getDate());

//        String edit_title = editText_title.getText().toString();
//        final String b = date.getText().toString();
//        if (edit_title.trim().equals("")) {
//            editText_title.setError("Input title");
//        }

        editBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String a = editText_title.getText().toString();
                String b = date.getText().toString();
                if (a.trim().equals("")) { // trim().equals("") isEmpty()
//                    editText_title.setError("Input title");
                    Toast.makeText(MainActivity.this, "Failed! Title is empty", Toast.LENGTH_SHORT).show();
                } else {
                    updateListData(b, a);
                }
            }
        });
        editBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
//                editBuilder.setCancelable(true); // Default is true
            }
        });
//        editBuilder.show();
        final AlertDialog builderShow = editBuilder.show(); // AlertDialog.Builder cannot dismiss

        img_btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteList();
                builderShow.dismiss();
            }
        });
    }

    private void updateListData(String date, String title) {
        if(list_position >= 0) {
            String tripId = myLists.get(list_position).getTripId();
            if (!tripId.isEmpty()) {
                db.collection("users").document(userName).collection("myLists")
                        .document(tripId)
                        .update("date", date,"title",title);
            }
        }
    }

    private  void initMyLists() { // Firestore listener
        myLists.clear();
        tripIdList.clear();
        listsAdapter.notifyDataSetChanged(); // Update ListView，otherwise history remains on ListView

//        FirebaseApp.initializeApp(getBaseContext()); //  classpath 'com.google.gms:google-services:4.2.0' because 4.1.0 has issue
        db = FirebaseFirestore.getInstance();
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
                                    Log.d(TAG, "tripId: "+String.valueOf(tripIndex));
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

    private void initialiseMyLists() { // Firestore read/get data
        myLists.clear();
        tripIdList.clear();
//        listsAdapter.notifyDataSetChanged(); // Update ListView，otherwise history remains on ListView

//        FirebaseApp.initializeApp(getBaseContext()); //  classpath 'com.google.gms:google-services:4.2.0' because 4.1.0 has issue
        db = FirebaseFirestore.getInstance();
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
