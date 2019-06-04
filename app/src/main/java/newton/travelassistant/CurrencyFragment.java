package newton.travelassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import newton.travelassistant.currency.CurrencyAPI;
import newton.travelassistant.currency.CurrencyAdapter;
import newton.travelassistant.currency.CurrencyAdd;
import newton.travelassistant.currency.CurrencyData;
import newton.travelassistant.currency.DataHandler;
import newton.travelassistant.currency.FlagAPI;

public class CurrencyFragment extends Fragment {

    private ListView listView;
    private List<CurrencyData> list;
    private CurrencyAdapter currencyAdapter;
    private String inputText;
    private List<String> currencyCodes = new ArrayList<>();
    private CurrencyAPI currencyAPI = new CurrencyAPI();
    private FlagAPI flagAPI = new FlagAPI();
    private Double equalUSDValue = 0.0;
    private EditText inputCurrencyValue;
    private ImageView inputImage;
    private TextView inputCurrencyName;
    private TextView inputCurrencyFullName;
    private ImageButton addCurrency;
    private ArrayList<String> bundle = new ArrayList<>();
    private View view;

    private List<String> defaultCodes = new ArrayList<>();
    private String defaultInputCode;
    private String typedText;
    DataHandler dataHandler = new DataHandler();
    private String addCode;

    float historicX = Float.NaN;
    float historicY = Float.NaN;
    static final int DELTA = 50;



    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        String currentCode = dataHandler.loadString(this.getActivity(), this.getActivity().getString(R.string.saved_user_current_code));
        if (currentCode != null) {
            defaultInputCode = currentCode;
        } else {
            defaultInputCode = "SEK";
            dataHandler.saveString(this.getActivity(), this.getActivity().getString(R.string.saved_user_current_code), defaultInputCode);
        }

        List<String> userList = dataHandler.loadList(this.getActivity(), this.getActivity().getString(R.string.saved_user_currency_list));
        if (userList != null) {
            // Check duplications
            Set<String> set = new HashSet<>(userList);
            if (set.size() < userList.size()) {
                userList.clear();
                userList.addAll(set);
            }
            if (userList.contains(defaultInputCode)) {
                userList.remove(userList.indexOf(defaultInputCode));
            }
            defaultCodes = userList;
        } else {
            defaultCodes.addAll(Arrays.asList("EUR", "USD", "GBP", "NOK"));
            if (defaultCodes.contains(defaultInputCode)) {
                defaultCodes.remove(defaultCodes.indexOf(defaultInputCode));
            }
            dataHandler.saveList(this.getActivity(), this.getActivity().getString(R.string.saved_user_currency_list), defaultCodes);
        }

        String currentValue = dataHandler.loadString(this.getActivity(), this.getActivity().getString(R.string.saved_user_current_value));
        if (currentValue != null) {
            typedText = currentValue;
        } else {
            typedText = "0";
        }

        view = inflater.inflate(R.layout.fragment_currency, container, false);
        inputCurrencyValue = view.findViewById(R.id.input_currency_value);
        inputImage = view.findViewById(R.id.input_image);
        inputCurrencyName = view.findViewById(R.id.input_currency_name);
        inputCurrencyFullName = view.findViewById(R.id.input_currency_fullname);
        listView = view.findViewById(R.id.list_view);
        addCurrency = view.findViewById(R.id.currency_add);


        currencyCodes = defaultCodes;

        updateListView(null);
        setInputBox(view, defaultInputCode);

        // interaction part
        inputCurrencyValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i("APP", editable.toString());
                inputText = editable.toString();
                try {
                    equalUSDValue = getEqualUSDValue(inputText, defaultInputCode);
                } catch (Exception e) {
                    equalUSDValue = 0.0;
                }
                updateListView(equalUSDValue);
                typedText = inputText;
                dataHandler.saveString(getActivity(), getActivity().getString(R.string.saved_user_current_value), typedText);

            }
        });

        inputCurrencyValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputCurrencyValue == null || inputCurrencyValue.equals("0")) {

                } else {
                    inputCurrencyValue.setText("");
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CurrencyData data = (CurrencyData) parent.getItemAtPosition(position);
                currencyCodes.set(position, defaultInputCode);
                defaultInputCode = data.getCurrencyName();

                equalUSDValue = getEqualUSDValue(typedText, defaultInputCode);
                updateListView(equalUSDValue);
                setInputBox(view, defaultInputCode);
                dataHandler.saveList(getActivity(), getActivity().getString(R.string.saved_user_currency_list), currencyCodes);
                dataHandler.saveString(getActivity(), getActivity().getString(R.string.saved_user_current_code), defaultInputCode);
            }
        });

        addCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CurrencyAdd.class);
//                intent.putStringArrayListExtra("key", (ArrayList<String>) defaultCodes);
                startActivity(intent);
            }
        });

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        historicX = event.getX();
                        historicY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (event.getX() - historicX < -DELTA) {
                            int position = listView.pointToPosition((int) historicX, (int) historicY);
                            defaultCodes.remove(position);

                            updateListView(equalUSDValue);
                            setInputBox(view, defaultInputCode);
                            Log.e("SWIPE1", String.valueOf(position));
                            Log.e("SWIPE1", "action1");
                            return true;
                        } else if (event.getX() - historicX > DELTA) {
//                            Function
                            Log.e("SWIPE2", "action2");
                            return true;
                        }
                        break;
                    default:
                        return false;
                }
                return false;
            }
        });

        return view;
    }

    protected List<CurrencyData> getData(List<String> currencyCodes, Double equalUSDValue) {
        List<CurrencyData> list = new ArrayList<>();
        for (String code : currencyCodes) {
            list.add(getLine(code, equalUSDValue));
        }
        return list;
    }

    protected CurrencyData getLine(String currencyCode, Double equalUSDValue) {
        if (equalUSDValue == null) {
            equalUSDValue = 0.0;
        }

        CurrencyData dataLine = new CurrencyData();
        dataLine.setImgUrl(flagAPI.getFlagURL(currencyCode));
        dataLine.setCurrencyName(currencyCode);
        dataLine.setCurrencyFullName(flagAPI.getCurrencyFullName(currencyCode));

        Double calculatedValue =
                Double.parseDouble(currencyAPI.getCurrency(this.getActivity(), currencyCode)) * equalUSDValue;
        if (calculatedValue == 0.0) {
            dataLine.setCurrencyValue("0");
        } else {
            dataLine.setCurrencyValue(calculatedValue.toString());
        }

        return dataLine;
    }

    protected void setInputBox(View view, String currencyCode) {
        Picasso.get().load(flagAPI.getFlagURL(currencyCode)).into(inputImage);
        inputCurrencyName.setText(currencyCode);
        inputCurrencyFullName.setText(flagAPI.getCurrencyFullName(currencyCode));
    }

    protected void updateListView(Double equalUSDValue) {
        list = getData(currencyCodes, equalUSDValue);
        currencyAdapter = new CurrencyAdapter(getActivity(), R.layout.currency_list, list);
        listView.setAdapter(currencyAdapter);

        dataHandler.saveList(getActivity(), getActivity().getString(R.string.saved_user_currency_list), currencyCodes);
        dataHandler.saveString(getActivity(), getActivity().getString(R.string.saved_user_current_code), defaultInputCode);
    }

    protected Double getEqualUSDValue(String inputText, String CurrencyCode) {
        Double inputValue = Double.parseDouble(inputText);
        Double currentCurrencyValue = Double.parseDouble(currencyAPI.getCurrency(this.getActivity(), CurrencyCode));
        equalUSDValue = inputValue / currentCurrencyValue;
        return Double.parseDouble(String.format("%.2f", equalUSDValue));
    }

    public String loadNewCode() {
        FileInputStream inputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder content = new StringBuilder();

        try {
            inputStream = getActivity().openFileInput("add_code");
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("Check:", String.valueOf(currencyCodes));
        addCode = loadNewCode();
        Log.i("ADDCODE", addCode);
        currencyCodes.add(addCode);

        updateListView(equalUSDValue);
        setInputBox(view, defaultInputCode);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        dataHandler.saveString(this.getActivity(), this.getActivity().getString(R.string.saved_user_current_code), defaultInputCode);
//        dataHandler.saveString(this.getActivity(), this.getActivity().getString(R.string.saved_user_current_value), typedText);
//        dataHandler.saveList(this.getActivity(), this.getActivity().getString(R.string.saved_user_currency_list), currencyCodes);
    }
}
