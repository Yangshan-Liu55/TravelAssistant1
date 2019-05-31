package newton.travelassistant;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CurrencyAddAdapter extends ArrayAdapter<CurrencyData> implements Filterable {

    private Activity context;
    private int resource;
    List<String> filteredList = new ArrayList<>();

    public CurrencyAddAdapter(Activity context, int resource, List<CurrencyData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        CurrencyData currencyData = getItem(position); // Get the current item of postListView

        View view;
        CurrencyAddAdapter.ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resource, null);
            viewHolder = new CurrencyAddAdapter.ViewHolder();

            viewHolder.imageView = view.findViewById(R.id.add_flag_image);
            viewHolder.currencyNameText = view.findViewById(R.id.add_currency_name);
            viewHolder.currencyFullNameText = view.findViewById(R.id.add_currency_fullname);
            view.setTag(viewHolder); //Save viewholder in view
        } else {
            view = convertView;
            viewHolder = (CurrencyAddAdapter.ViewHolder) view.getTag(); //Get viewholder again.
        }


        Glide.with(context).load(currencyData.getImgUrl()).into(viewHolder.imageView); //glide with url into imgView
        viewHolder.currencyNameText.setText(currencyData.getCurrencyName());
        viewHolder.currencyFullNameText.setText(currencyData.getCurrencyFullName());

        return view;
    }

    class ViewHolder {
        ImageView imageView;
        TextView currencyNameText;
        TextView currencyFullNameText;
    }

    @Override
    public Filter getFilter() {
        final Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<String> filteredArrayNames = new ArrayList<>();

                constraint = constraint.toString().toLowerCase();
                List<String> allKeys = new FlagAPI().getAllCurrencyCode();
                for (int i = 0; i < allKeys.size(); i++) {
                    String dataNames = allKeys.get(i);
                    if (dataNames.toLowerCase().startsWith(constraint.toString())) {
                        filteredArrayNames.add(dataNames);
                    }
                }
                results.count = filteredArrayNames.size();
                results.values = filteredArrayNames;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (List<String>) filterResults.values;
//                notifyDataSetInvalidated();
                notifyDataSetChanged();
//                CurrencyAdd ca = new CurrencyAdd();
//                ca.updateListView(filteredList);
//                Log.i("ABC", String.valueOf(filteredList.size()));
            }
        };
        return filter;
    }
}
