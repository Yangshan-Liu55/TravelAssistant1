package newton.travelassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class InventoryActivity extends AppCompatActivity {
    private ExpandableListView myExListView;
    private MyExpandableListAdapter myExpandableListAdapter;
    private Inventory inventory = new Inventory();
    private ArrayList<ParentData> parent_list;
    private FirebaseFirestore db;
    private static final String TAG2 = "InventoryActivity";
    private Map<String, Object> categories_map = new HashMap<>(); // parentMapList

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        myExpandableListAdapter = new MyExpandableListAdapter(this, parent_list);
        myExListView = (ExpandableListView) findViewById(R.id.expandable_list);
        myExListView.setAdapter(myExpandableListAdapter);
        initInventory();
        this.registerForContextMenu(myExListView); // Register context menu

        // Parent item click event
        myExListView.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                // TODO Auto-generated method stub
//                Log.d(TAG2, "Click parent");
//                Toast.makeText(InventoryActivity.this, parent_list.get(i).getCategory(), Toast.LENGTH_LONG).show();
                return false;
            }
        });

        // Child item click event
        myExListView.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                // TODO Auto-generated method stub
                int temp_done = parent_list.get(i).getChild_list().get(i1).getDone();
                if (temp_done == 1) {
                    temp_done = 0;
                } else {
                    temp_done = 1;
                }
                editCheckbox(i, i1, temp_done);

//                Log.d(TAG2, "Click child");
//                Toast.makeText(InventoryActivity.this, parent_list.get(i).getChild_list().get(i1).getText(), Toast.LENGTH_LONG).show();
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lists_menu, menu); // Import lists_menu layout
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.lists_menu_add:
                addCategory();
                break;
            case R.id.lists_menu_login:
                if ( inventory.getUserId() == null || inventory.getUserId().isEmpty()) {
                    Intent intent = new Intent(InventoryActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(InventoryActivity.this, AccountActivity.class));
                }

//                Toast.makeText(InventoryActivity.this, "Login", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        ExpandableListView.ExpandableListContextMenuInfo info =(ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) { // set Parent context menu
            menu.add(0, 11, 0, "Add an item");
            menu.add(0, 12, 0, "Edit");
            menu.add(0, 13, 0, "Delete");
        } else if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) { // set Child context menu
            menu.add(0, 21, 0, "Edit");
            menu.add(0, 22, 0, "Delete");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ExpandableListView.ExpandableListContextMenuInfo info=(ExpandableListView.ExpandableListContextMenuInfo)item.getMenuInfo();
        int group_position = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        int child_position = ExpandableListView.getPackedPositionChild(info.packedPosition);

        switch (item.getItemId()) {
            case  11:
//                Toast.makeText(this,"addItem: "+ String.valueOf(group_position), Toast.LENGTH_SHORT).show();
                addItem(group_position);
                break;
            case  12:
//                Toast.makeText(this,"editCategory: "+ String.valueOf(group_position), Toast.LENGTH_SHORT).show();
                editCategory(group_position);
                break;
            case  13:
                deleteCategory(group_position);
//                Toast.makeText(this,"group_position13: "+ String.valueOf(group_position), Toast.LENGTH_SHORT).show();
                break;
            case  21:
                editItem(group_position, child_position);
                break;
            case  22:
                deleteItem(group_position, child_position);
                break;
            default:
                break;
        }

        return super.onContextItemSelected(item);
        //return true;
    }

    private void editCheckbox(int i, int i1,  int done) {
        String selected_tx = parent_list.get(i).getChild_list().get(i1).getText();
        int selected_qunty = parent_list.get(i).getChild_list().get(i1).getQuantity();

        String selected_category = parent_list.get(i).getCategory();
        Map<String, Object> category_map = (Map<String, Object>) categories_map.get(selected_category);
        Map<String, Object> temp_text_map = new HashMap<>(); // new text map
        temp_text_map.put("done", done); // new done value
        temp_text_map.put("quantity", selected_qunty);
        category_map.put(selected_tx, temp_text_map); // Then add new text map 重新放一个相同的key,会自动覆盖value的

//        categories_map.remove(selected_category); // First delete selected category and its child 重新放一个相同的key,会自动覆盖value的
        categories_map.put(selected_category, category_map); // Then add new category map with old category string
        updateCategoryData(); // Update modified category to Firestore
    }

    private void deleteItem(final int group_position, int child_position) {
        final String selected_category = parent_list.get(group_position).getCategory();
        ChildData child = parent_list.get(group_position).getChild_list().get(child_position);
        final String selected_text = child.getText();

        AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(InventoryActivity.this);
        deleteBuilder.setIcon(R.drawable.ic_delete_forever_black_24dp);
        deleteBuilder.setTitle("DELETE ITEM");
        deleteBuilder.setMessage("Forever delete " + selected_text + " in " + selected_category + "?");
        deleteBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Map<String, Object> category_map = (Map<String, Object>) categories_map.get(selected_category);
                category_map.remove(selected_text); // remove selected text
//                categories_map.remove(selected_category); // remove selected category 重新放一个相同的key,会自动覆盖value的
                categories_map.put(selected_category,category_map); // add new category_map with deleted selected_text map

                updateCategoryData(); // Update new categories_map to Firestore
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

    private void editItem(final int group_position, int child_position) {
        final AlertDialog.Builder editBuilder = new AlertDialog.Builder(InventoryActivity.this);
        editBuilder.setIcon(R.drawable.ic_edit_black_24dp);
        editBuilder.setTitle("EDIT ITEM");
        View view = LayoutInflater.from(InventoryActivity.this).inflate(R.layout.dialog_inventory_add, null);
        editBuilder.setView(view);

        EditText title = (EditText)view.findViewById(R.id.dialog_add_title);
        TextView date = (TextView)view.findViewById(R.id.dialog_add_date);
        DatePicker dialog_add_datePicker = (DatePicker)view.findViewById(R.id.dialog_add_datePicker);
        EditText category = (EditText)view.findViewById(R.id.dialog_add_category);
        final EditText text = (EditText)view.findViewById(R.id.dialog_add_text);
        final EditText quantity = (EditText)view.findViewById(R.id.dialog_add_quantity);
        final CheckBox isDone = (CheckBox)view.findViewById(R.id.dialog_add_isdone);
        ImageButton img_btn_delete = (ImageButton)view.findViewById(R.id.dialog_add_delete);
        title.setVisibility(View.GONE);
        date.setVisibility(View.GONE);
        dialog_add_datePicker.setVisibility(View.GONE);
        category.setVisibility(View.GONE);
        text.setVisibility(View.VISIBLE);
        quantity.setVisibility(View.VISIBLE);
        isDone.setVisibility(View.VISIBLE);
        img_btn_delete.setVisibility(View.VISIBLE);

        ChildData child = parent_list.get(group_position).getChild_list().get(child_position);
        final String selected_text = child.getText();
        text.setText(selected_text);
        quantity.setText(String.valueOf(child.getQuantity()));
        if (child.getDone() == 1) {
            isDone.setChecked(true);
        } else {
            isDone.setChecked(false);
        }

        editBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String txt = text.getText().toString();
                int q = Integer.parseInt(quantity.getText().toString());
                int done = isDone.isChecked() ? 1 : 0;
                if (txt.trim().equals("")) { //isEmpty()
                    text.setError("Input item");
                    Toast.makeText(InventoryActivity.this, "Failed! Item is empty", Toast.LENGTH_SHORT).show();
                } else {
                    String selected_category = parent_list.get(group_position).getCategory();
                    Map<String, Object> category_map = (Map<String, Object>) categories_map.get(selected_category);
                    category_map.remove(selected_text); // First delete selected child/text map
                    Map<String, Object> temp_text_map = new HashMap<>(); // new text map
                    temp_text_map.put("done", done);
                    temp_text_map.put("quantity", q);
                    category_map.put(txt, temp_text_map); // Then add new text map with new text string

//                    categories_map.remove(selected_category); // First delete selected category and its child 重新放一个相同的key,会自动覆盖value的
                    categories_map.put(selected_category, category_map); // Then add new category map with old category string
                    updateCategoryData(); // Update modified category to Firestore
                }
            }
        });
        editBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(InventoryActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        });
        final AlertDialog builderShow = editBuilder.show(); // AlertDialog.Builder cannot dismiss

        img_btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCategory(group_position);
                builderShow.dismiss();
            }
        });
    }

    private void addItem(final int group_position) {
        AlertDialog.Builder addBuilder = new AlertDialog.Builder(InventoryActivity.this);
        addBuilder.setIcon(R.drawable.ic_playlist_add_black_24dp);
        addBuilder.setTitle("NEW ITEM");
        View view = LayoutInflater.from(InventoryActivity.this).inflate(R.layout.dialog_inventory_add, null);
        addBuilder.setView(view);

        EditText title = (EditText)view.findViewById(R.id.dialog_add_title);
        TextView date = (TextView)view.findViewById(R.id.dialog_add_date);
        DatePicker dialog_add_datePicker = (DatePicker)view.findViewById(R.id.dialog_add_datePicker);
        EditText category = (EditText)view.findViewById(R.id.dialog_add_category);
        final EditText text = (EditText)view.findViewById(R.id.dialog_add_text);
        final EditText quantity = (EditText)view.findViewById(R.id.dialog_add_quantity);
        final CheckBox isDone = (CheckBox)view.findViewById(R.id.dialog_add_isdone);
        ImageButton img_btn_delete = (ImageButton)view.findViewById(R.id.dialog_add_delete);
        title.setVisibility(View.GONE);
        date.setVisibility(View.GONE);
        dialog_add_datePicker.setVisibility(View.GONE);
        category.setVisibility(View.GONE);
        text.setVisibility(View.VISIBLE);
        quantity.setVisibility(View.VISIBLE);
        isDone.setVisibility(View.VISIBLE);
        img_btn_delete.setVisibility(View.GONE);

        String textString = text.getText().toString();
        if (textString.trim().equals("")) { //isEmpty()
            text.setError("Input text");
        }

        addBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String txt = text.getText().toString();
                if (txt.trim().equals("")) { //isEmpty()
                    Toast.makeText(InventoryActivity.this, "Failed! Item is empty", Toast.LENGTH_SHORT).show();
                } else {
                    String c = parent_list.get(group_position).getCategory();
                    String qString = quantity.getText().toString();
                    int q = (qString.trim().equals("")) ? 1 : Integer.parseInt(qString); // Default quantity value
                    int done = isDone.isChecked() ? 1 : 0;

                    Map<String, Object> text_map = new HashMap<>(); // child
                    text_map.put("done", done);
                    text_map.put("quantity", q);

                    Map<String, Object> category_map = (Map<String, Object>) categories_map.get(c); // parent
                    category_map.put(txt, text_map); // ((Long) text_map.get("done")).intValue()
//                    categories_map.remove(c); // First delete selected category map 重新放一个相同的key,会自动覆盖value的
                    categories_map.put(c, category_map); // Then add new category map with same category string
                    updateCategoryData();

                    ChildData temp_child = new ChildData(txt, q, done); // not need
                    ArrayList<ChildData> temp_child_list = parent_list.get(group_position).getChild_list(); // not need
                    temp_child_list.add(temp_child); // not need
                    parent_list.get(group_position).setChild_list(temp_child_list); // not need
                }
            }
        });
        addBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(InventoryActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        });
        addBuilder.show();
    }

    private void editCategory(final int group_position){
        final AlertDialog.Builder editBuilder = new AlertDialog.Builder(InventoryActivity.this);
        editBuilder.setIcon(R.drawable.ic_edit_black_24dp);
        editBuilder.setTitle("EDIT CATEGORY");
        View view = LayoutInflater.from(InventoryActivity.this).inflate(R.layout.dialog_inventory_add, null);
        editBuilder.setView(view);

        EditText title = (EditText)view.findViewById(R.id.dialog_add_title);
        TextView date = (TextView)view.findViewById(R.id.dialog_add_date);
        DatePicker dialog_add_datePicker = (DatePicker)view.findViewById(R.id.dialog_add_datePicker);
        final EditText category = (EditText)view.findViewById(R.id.dialog_add_category);
        EditText text = (EditText)view.findViewById(R.id.dialog_add_text);
        EditText quantity = (EditText)view.findViewById(R.id.dialog_add_quantity);
        CheckBox isDone = (CheckBox)view.findViewById(R.id.dialog_add_isdone);
        ImageButton img_btn_delete = (ImageButton)view.findViewById(R.id.dialog_add_delete);
        title.setVisibility(View.GONE);
        date.setVisibility(View.GONE);
        dialog_add_datePicker.setVisibility(View.GONE);
        category.setVisibility(View.VISIBLE);
        text.setVisibility(View.GONE);
        quantity.setVisibility(View.GONE);
        isDone.setVisibility(View.GONE);
        img_btn_delete.setVisibility(View.VISIBLE);

        category.setText(parent_list.get(group_position).getCategory());

        editBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String c = category.getText().toString();
                if (c.trim().equals("")) { // trim().equals("") isEmpty()
                    category.setError("Input category");
                    Toast.makeText(InventoryActivity.this, "Failed! Category is empty", Toast.LENGTH_SHORT).show();
                } else {
                    String selected_category = parent_list.get(group_position).getCategory();
//                    Map<String, Object> temp_categories_map = categories_map;
                    Map<String, Object> temp_category_map = (Map<String, Object>) categories_map.get(selected_category);
                    categories_map.remove(selected_category); // First delete selected category and its child
                    categories_map.put(c, temp_category_map); // Then add new category string with old category map
                    parent_list.get(group_position).setCategory(c); // change the category string
                    updateCategoryData(); // Update modified category to Firestore
                }
            }
        });
        editBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(InventoryActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        });
        final AlertDialog builderShow = editBuilder.show(); // AlertDialog.Builder cannot dismiss

        img_btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCategory(group_position);
                builderShow.dismiss();
            }
        });
    }

    private void deleteCategory(final int group_position){
        AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(InventoryActivity.this);
        deleteBuilder.setIcon(R.drawable.ic_delete_forever_black_24dp);
        deleteBuilder.setTitle("DELETE CATEGORY");
        deleteBuilder.setMessage("Forever delete " + parent_list.get(group_position).getCategory() + "?");
        deleteBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                categories_map.remove(parent_list.get(group_position).getCategory()); // remove selected category
//                parent_list.remove(group_position); // Not need
//                inventory.setParent_data_list(parent_list);
                updateCategoryData(); // Update new categories_map to Firestore
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

    private void addCategory(){
        AlertDialog.Builder addBuilder = new AlertDialog.Builder(InventoryActivity.this);
        addBuilder.setIcon(R.drawable.ic_playlist_add_black_24dp);
        addBuilder.setTitle("NEW CATEGORY");
        View view = LayoutInflater.from(InventoryActivity.this).inflate(R.layout.dialog_inventory_add, null);
        addBuilder.setView(view);

        EditText title = (EditText)view.findViewById(R.id.dialog_add_title);
        TextView date = (TextView)view.findViewById(R.id.dialog_add_date);
        DatePicker dialog_add_datePicker = (DatePicker)view.findViewById(R.id.dialog_add_datePicker);
        final EditText category = (EditText)view.findViewById(R.id.dialog_add_category);
        EditText text = (EditText)view.findViewById(R.id.dialog_add_text);
        EditText quantity = (EditText)view.findViewById(R.id.dialog_add_quantity);
        CheckBox isDone = (CheckBox)view.findViewById(R.id.dialog_add_isdone);
        ImageButton img_btn_delete = (ImageButton)view.findViewById(R.id.dialog_add_delete);
        title.setVisibility(View.GONE);
        date.setVisibility(View.GONE);
        dialog_add_datePicker.setVisibility(View.GONE);
        category.setVisibility(View.VISIBLE);
        text.setVisibility(View.GONE);
        quantity.setVisibility(View.GONE);
        isDone.setVisibility(View.GONE);
        img_btn_delete.setVisibility(View.GONE);

        String ctgry = category.getText().toString();
        if (ctgry.trim().equals("")) { //isEmpty()
            category.setError("Input category");
        }

        addBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(InventoryActivity.this, "OK clicked", Toast.LENGTH_SHORT).show();
                String c = category.getText().toString();
                if (c.trim().equals("")) { //isEmpty()
                    category.setError("Input category"); //
                    Toast.makeText(InventoryActivity.this, "Failed! Category is empty", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> category_map = new HashMap<>(); // parent
                    categories_map.put(c, category_map); // Add a category to categories_map
                    updateCategoryData(); // Update new categories_map to Firestore
                }
            }
        });
        addBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(InventoryActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        });
        addBuilder.show();
    }

    private void updateCategoryData() {
        db.collection("users").document(inventory.getUserId()).collection("myLists")
                .document(inventory.getTripId())
                .update("categories", categories_map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(InventoryActivity.this, "Update Succeeded!",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(InventoryActivity.this, "Failed!",Toast.LENGTH_SHORT).show();
                        Log.d(TAG2, "Firestore update error", e);
                    }
                });
    }

    private void initInventory() {
//        Bundle bundle = getIntent().getBundleExtra("inventoryBundle");
        Bundle bundle = getIntent().getExtras();
        inventory.setUser(bundle.getString("user"));
        inventory.setUserId(bundle.getString("userId"));
        inventory.setTripId(bundle.getString("tripId"));
        inventory.setDate(bundle.getString("date"));
        inventory.setTitle(bundle.getString("title"));
        final int bundle_done = bundle.getInt("done"); // done in inventory would be changed
        final int bundle_total = bundle.getInt("total"); // done in inventory would be changed

        this.setTitle(bundle.getString("title"));

        if (parent_list == null) {
            parent_list = new ArrayList<ParentData>();
        }

        parent_list.clear();
        myExpandableListAdapter.flashData(parent_list);
//        myExpandableListAdapter.notifyDataSetChanged();

        db = FirebaseFirestore.getInstance();
        db.collection("users").document(inventory.getUserId()).collection("myLists")
                .document(inventory.getTripId())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG2, "Firestore Listener failed.", e);
                    return;
                }

                // Get Inventory data from document
                if (documentSnapshot != null && documentSnapshot.exists()) {
//                    Log.d(TAG, "source " + " data: " + documentSnapshot.getData());
                    parent_list.clear(); // Listener loops from here
                    categories_map.clear();
                    Map<String, Object> document_map = documentSnapshot.getData();
                    categories_map = (Map<String, Object>) document_map.get("categories");

                    Map<String, Object> category_map; // parent map
                    Map<String, Object> text_map; // child map
                    if (!categories_map.isEmpty()) {
                        for(String category: categories_map.keySet()) {
                            ParentData temp_parent = new ParentData(); // Loop must have new variable
                            ArrayList<ChildData> child_list = new ArrayList<ChildData>(); // Loop must have new variable
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
                            parent_list.add(temp_parent);
//                            inventory.setParent_data_list(parent_list);
                        }
                    }

                    myExpandableListAdapter.flashData(parent_list); // same myExpandableListAdapter.notifyDataSetChanged();

                    inventory.setParent_data_list(parent_list); // for getting inventory.getTotal/getDone
                    int inventory_done = inventory.getDone();
                    int inventory_total = inventory.getTotal();
                    if ((inventory_done != bundle_done) || (inventory_total != bundle_total)) {
                        updateTotalDoneData(inventory_total, inventory_done);
                    }

                } else {
                    Log.d(TAG2, "source " + " data: null or not exists");
                }
            }
        });
    }

    private void updateTotalDoneData(int total, int done) {
        db.collection("users").document(inventory.getUserId()).collection("myLists")
                .document(inventory.getTripId())
                .update("done", done,
                        "total", total)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(InventoryActivity.this, "Update Succeeded!",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(InventoryActivity.this, "Failed!",Toast.LENGTH_SHORT).show();
                        Log.d(TAG2, "Firestore update error", e);
                    }
                });
    }

    private void setInventory() {
        Bundle bundle = getIntent().getExtras();
        inventory.setUser(bundle.getString("user"));
        inventory.setUserId(bundle.getString("userId"));
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

}
