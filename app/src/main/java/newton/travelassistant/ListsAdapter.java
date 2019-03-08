package newton.travelassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.support.annotation.NonNull;
import android.widget.TextView;
import java.util.List;


public class ListsAdapter extends ArrayAdapter<Inventory> {
    private Context context;
    private int resource;

    // Glide.with(context) must be Activity, so declare Activity context, change argument Context context to Activity
    public ListsAdapter(@NonNull Context context, int resource, @NonNull List<Inventory> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource; // list view id
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Inventory inventory = getItem(position); // Get the current item of myLists ListView
//        View view= LayoutInflater.from(getContext()).inflate(resource, null);
        View view; // 提升 ListView 的运行效率
        ViewHolder viewHolder; // 优化获取findViewById()控件实例. Must create viewHolder class
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resource, null);
            viewHolder = new ViewHolder();
            viewHolder.inventoryDate = (TextView) view.findViewById(R.id.item_date);
            viewHolder.inventoryTitle = (TextView) view.findViewById(R.id.item_title);
            viewHolder.inventoryTotal = (TextView) view.findViewById(R.id.item_total);
            viewHolder.inventoryUndone = (TextView) view.findViewById(R.id.item_undone);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); //Get viewHolder again.
        } // convertView 用于将之前加载的数据进行缓存

        viewHolder.inventoryDate.setText(inventory.getDate());
        viewHolder.inventoryTitle.setText(inventory.getTitle());
        String undone = String.valueOf((inventory.getTotal() - inventory.getDone()));
        viewHolder.inventoryUndone.setText(undone);
        viewHolder.inventoryTotal.setText("/" + inventory.getTotal());

        return view;
    }

    class ViewHolder {
        TextView inventoryDate;
        TextView inventoryTitle;
        TextView inventoryTotal;
        TextView inventoryUndone;
    }

}
