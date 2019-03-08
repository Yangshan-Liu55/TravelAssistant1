package newton.travelassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<ParentData> parent_list = new ArrayList<>();

    // Constructor
    public MyExpandableListAdapter(Context context, ArrayList<ParentData> parent_list) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.parent_list = parent_list;
    }

    // Update data
    public void flashData(ArrayList<ParentData> parent_list){
        this.parent_list = parent_list;
        this.notifyDataSetChanged();
    }

    // Get child data
    @Override
    public Object getChild(int i, int i1) {
        return parent_list.get(i).getChild_list().get(i1);
    }

    // Get child id
    @Override
    public long getChildId(int arg0, int arg1) {
        return arg1;
    }

    // Set child data
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        HolderView childView; // Customize HolderView class bellowing
        if (view == null) {
            childView = new HolderView();
            view = mInflater.inflate(R.layout.activity_inventory_child, viewGroup, false);
            childView.textView = (TextView) view.findViewById(R.id.child_text);
            childView.quantityTextView = (TextView) view.findViewById(R.id.child_quantity);
            view.setTag(childView); // Cache in view
        } else {
            childView = (HolderView) view.getTag();
        }

        childView.textView.setText(parent_list.get(i).getChild_list().get(i1).getText());
        childView.quantityTextView.setText("✖"+String.valueOf(parent_list.get(i).getChild_list().get(i1).getQuantity()));

        return view;
    }
    private class HolderView { // Child View class
        TextView textView;
        TextView quantityTextView;
    }

    // Get children length
    @Override
    public int getChildrenCount(int i) {
        if (parent_list != null){
            if (parent_list.get(i).getChild_list() != null) { // parent_list.get(i).getChild_list().size() > 0
                return parent_list.get(i).getChild_list().size();
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    // Get parent data
    @Override
    public Object getGroup(int i) {
        return parent_list.get(i);
    }

    // Get parents length
    @Override
    public int getGroupCount() {
        if (parent_list != null){
            return parent_list.size();
        } else {
            return 0;
        }
    }

    // Get parent id
    @Override
    public long getGroupId(int i) {
        return i;
    }

    // Set parent view
    private class HolderViewParent {
        TextView categoryTextView;
    }
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        HolderViewParent holderViewParent;
        if (view == null) {
            holderViewParent = new HolderViewParent();
            view = mInflater.inflate(R.layout.activity_inventory_parent, viewGroup,false);
            holderViewParent.categoryTextView = (TextView) view.findViewById(R.id.parent_category);
            view.setTag(holderViewParent);
        } else {
            holderViewParent = (HolderViewParent) view.getTag();
        }

        String undone = String.valueOf(parent_list.get(i).getCount() - parent_list.get(i).getDoneCount());
        holderViewParent.categoryTextView.setText(parent_list.get(i).getCategory()
                + " (" + undone + "/"
                + parent_list.get(i).getCount() + ")");

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return false; // 指定位置相应的组视图
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true; //当选择子节点的时候，调用该方法(点击二级列表)
    }

}
