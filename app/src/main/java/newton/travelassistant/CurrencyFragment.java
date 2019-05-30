package newton.travelassistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrencyFragment extends Fragment {

    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        CurrencyAPI currencyAPI = new CurrencyAPI();
        FlagAPI flagAPI = new FlagAPI();
        String[] data = {"USD", "SEK", "EUR"};

        View view = inflater.inflate(R.layout.fragment_currency, container, false );
        listView = view.findViewById(R.id.list_view);


        List<Map<String, Object>> list=getData();
        listView.setAdapter(new ListViewAdapterCurrency(getActivity(), list));

//        ImageView flag = (ImageView) view.findViewById(R.id.flagView);
//        Picasso.get().load(flagAPI.getFlag("CNY")).into(flag);


//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                CurrencyFragment.this, android.R.layout.simple_list_item_1, data
//        );
//        ListView listView = view.findViewById(R.id.list_view);
//        listView.setAdapter(adapter);




        return view;

    }

    public List<Map<String, Object>> getData(){
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("image", "This is an image");
            map.put("title", "Title" + i);
            map.put("info", "Detail Info" + i);
            list.add(map);
        }
        return list;
    }




}
