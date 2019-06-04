package newton.travelassistant.currency;

import android.app.Fragment;
import android.content.Context;
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

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import newton.travelassistant.CurrencyFragment;
import newton.travelassistant.R;

public class CurrencyAdd extends AppCompatActivity {


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
    DataHandler dataHandler = new DataHandler();
    Fragment currencyFragment;
    //    Context context;
    List<String> allKeys = new ArrayList<>();
    List<String> userList = new ArrayList<>();
    String defaultInputCode;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_add);


//        if (savedInstanceState != null) {
//            Intent intent = getIntent();
//            defaultCodes = intent.getStringArrayListExtra("key");
//        }

        searchBar = findViewById(R.id.currency_search);
        currencyList = findViewById(R.id.add_currency_list);
        addFlagImage = findViewById(R.id.add_flag_image);
        addCurrencyName = findViewById(R.id.add_currency_name);
        addCurrencyFullName = findViewById(R.id.add_currency_fullname);
        listViewID = R.layout.currency_add_list;


        allKeys = flagAPI.getAllCurrencyCode();
//        userList = dataHandler.loadList(context, context.getString(R.string
//        .saved_user_currency_list));
//        defaultInputCode = dataHandler.loadString(context, context.getString(R.string
//        .saved_user_current_code));

        if (defaultInputCode != null) {
            if (allKeys.contains(defaultInputCode)) {
                allKeys.remove(allKeys.indexOf(defaultInputCode));
            }
        }

        if (userList != null) {
            for (String ul : userList) {
                if (allKeys.contains(ul)) {
                    allKeys.remove(allKeys.indexOf(ul));
                }
            }
        }

        updateListView(allKeys);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i0, int i1, int i2) {
//                Log.i("TYPE IN: ", String.valueOf(charSequence));
//                CurrencyAdd.this.addAdapter.getFilter().filter(charSequence);
                ArrayList<String> newList = new ArrayList<>();
//                String searchString = searchBar.getText().toString().trim();
                String searchString = charSequence.toString();
                if (searchString.length() > 0) {
                    for (int i = 0; i < allKeys.size(); i++) {
                        String currentString = allKeys.get(i);
                        if (searchString.equalsIgnoreCase(currentString)) {
                            newList.add(allKeys.get(i));
                        }
                    }
                    updateListView(newList);

                } else {
                    updateListView(allKeys);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                Log.i("TYPE IN after: ", String.valueOf(editable));
//                CurrencyAdd.this.addAdapter.getFilter().filter(editable);
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
                userList.add(addCode);
                saveNewCode(addCode);
//                sendBackData(addCode);
                finish();
            }
        });

    }

    public CurrencyData getLine(String currencyCode) {
        CurrencyData dataLine = new CurrencyData();
        dataLine.setImgUrl(flagAPI.getFlagURL(currencyCode));
        dataLine.setCurrencyName(currencyCode);
        dataLine.setCurrencyFullName(flagAPI.getCurrencyFullName(currencyCode));
        return dataLine;
    }

    public void sendBackData(String addCode) {
        Bundle bundle = new Bundle();
        defaultCodes.add(addCode);
        bundle.putStringArrayList("key", (ArrayList<String>) defaultCodes);
        CurrencyFragment cf = new CurrencyFragment();
        cf.setArguments(bundle);
        finish();
    }

    public void updateListView(List<String> allKeys) {
        ArrayList<CurrencyData> list = new ArrayList<>();
        listViewID = R.layout.currency_add_list;

        for (String key : allKeys) {
            list.add(getLine(key));
        }
        addAdapter = new CurrencyAddAdapter(CurrencyAdd.this, listViewID, list);
        currencyList.setAdapter(addAdapter);
    }

    public void saveNewCode(String addCode) {
        FileOutputStream outputStream = null;
        BufferedWriter bufferedWriter = null;

        try {
            outputStream = openFileOutput("add_code", Context.MODE_PRIVATE);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(addCode);
            Log.i("ADDCODE", addCode);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
