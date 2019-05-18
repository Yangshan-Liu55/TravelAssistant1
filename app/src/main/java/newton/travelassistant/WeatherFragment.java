package newton.travelassistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WeatherFragment extends Fragment {

    TextView mCityTV;
    EditText mCityET;
    Button mButton;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather , container, false );


        mCityTV = view.findViewById(R.id.cityTV);
        mCityET = view.findViewById(R.id.cityET);
        mButton = view.findViewById(R.id.myButton);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCityTV.getText().toString().equals("")){
                    mCityTV.setText(mCityET.getText().toString());
                }else{
                    mCityTV.setText("");
                }
            }
        });

        return view;
    }





}
