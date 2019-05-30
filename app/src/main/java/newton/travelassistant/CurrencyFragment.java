package newton.travelassistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class CurrencyFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        CurrencyAPI currencyAPI = new CurrencyAPI();
        FlagAPI flagAPI = new FlagAPI();

        View view = inflater.inflate(R.layout.fragment_currency,container, false );

        ImageView flag = (ImageView) view.findViewById(R.id.flagView);

        Picasso.get().load(flagAPI.getFlag("CNY")).into(flag);





        return view;

    }




}
