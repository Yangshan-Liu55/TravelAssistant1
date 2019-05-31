package newton.travelassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CurrencyAdd extends AppCompatActivity {


//
//    private LinearLayout linearLayout;
    private EditText searchBar;
    private ListView currencyList;
    private CurrencyAPI currencyAPI = new CurrencyAPI();
    private FlagAPI flagAPI = new FlagAPI();
    private CurrencyAddAdapter addAdapter;
    private ImageView addFlagImage;
    private TextView addCurrencyName;
    private TextView addCurrencyFullName;
    private List<String> defaultCodes = new ArrayList<>();
    private int listViewID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_add);
        if (savedInstanceState != null) {
            Intent intent = getIntent();
            defaultCodes = intent.getStringArrayListExtra("key");
        }

        searchBar = findViewById(R.id.currency_search);
        currencyList = findViewById(R.id.add_currency_list);
        addFlagImage = findViewById(R.id.add_flag_image);
        addCurrencyName = findViewById(R.id.add_currency_name);
        addCurrencyFullName = findViewById(R.id.add_currency_fullname);
        listViewID = R.layout.currency_add_list;

        List<String> allKeys =  flagAPI.getAllCurrencyCode();
        updateListView(allKeys);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Log.i("TYPE IN: ", String.valueOf(charSequence));
//                CurrencyAdd.this.addAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i("TYPE IN after: ", String.valueOf(editable));
                CurrencyAdd.this.addAdapter.getFilter().filter(editable);
            }
        });

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchBar == null || searchBar.equals("")) {

                } else {
                    searchBar.setText("");
                }
            }
        });

        currencyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

                CurrencyData data = (CurrencyData) parent.getItemAtPosition(position);
                String addCode = data.getCurrencyName();
                Log.i("APP5", String.valueOf(addCode));
                sendBackData(addCode);
//                Intent intent = new Intent(CurrencyAdd.this, CurrencyFragment.class);
//                intent.putExtra("key", addedCode);
//                startActivity(intent);
            }
        });

    }

    protected CurrencyData getLine(String currencyCode) {
        CurrencyData dataLine = new CurrencyData();
        dataLine.setImgUrl(flagAPI.getFlagURL(currencyCode));
        dataLine.setCurrencyName(currencyCode);
        dataLine.setCurrencyFullName(flagAPI.getCurrencyFullName(currencyCode));
        return dataLine;
    }

    protected void sendBackData(String addCode) {
        Bundle bundle = new Bundle();
        defaultCodes.add(addCode);
        bundle.putStringArrayList("key", (ArrayList<String>) defaultCodes);
        CurrencyFragment cf = new CurrencyFragment();
        cf.setArguments(bundle);
        finish();
    }

    protected void updateListView(List<String> allKeys) {
        ArrayList<CurrencyData> list = new ArrayList<>();
        listViewID = R.layout.currency_add_list;

        for (String key : allKeys) {
            list.add(getLine(key));
        }
        addAdapter = new CurrencyAddAdapter(CurrencyAdd.this, listViewID, list);
        currencyList.setAdapter(addAdapter);
    }


}
