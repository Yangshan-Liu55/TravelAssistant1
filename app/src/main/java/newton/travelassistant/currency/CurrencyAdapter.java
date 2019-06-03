package newton.travelassistant.currency;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import newton.travelassistant.R;

public class CurrencyAdapter extends ArrayAdapter<CurrencyData> {

    private Activity context;
    private int resource;


    public CurrencyAdapter(Activity context, int resource, List<CurrencyData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        CurrencyData currencyData = getItem(position); // Get the current item of postListView

        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resource, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = view.findViewById(R.id.flag_image);
            viewHolder.currencyNameText = view.findViewById(R.id.currency_name);
            viewHolder.currencyValueText = view.findViewById(R.id.currency_value);
            viewHolder.currencyFullNameText = view.findViewById(R.id.currency_fullname);
            view.setTag(viewHolder); //Save viewholder in view
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); //Get viewholder again.
        }


        Glide.with(context).load(currencyData.getImgUrl()).into(viewHolder.imageView); //glide with url into imgView
        viewHolder.currencyNameText.setText(currencyData.getCurrencyName());
        viewHolder.currencyValueText.setText(currencyData.getCurrencyValue());
        viewHolder.currencyFullNameText.setText(currencyData.getCurrencyFullName());

        return view;
    }

    class ViewHolder {
        ImageView imageView;
        TextView currencyNameText;
        TextView currencyValueText;
        TextView currencyFullNameText;
    }



}

