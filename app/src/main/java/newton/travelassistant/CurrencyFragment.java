package newton.travelassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CurrencyFragment extends Fragment {

    private ListView listView;
    private List<CurrencyData> list;
    private CurrencyAdapter currencyAdapter;
    private String inputText;
    private List<String> currencyCodes = new ArrayList<>();
    private CurrencyAPI currencyAPI = new CurrencyAPI();
    private FlagAPI flagAPI = new FlagAPI();
    private List<String> defaultCodes = new ArrayList<>();
    private String defaultInputCode;
    private Double equalUSDValue = 0.0;
    private EditText inputCurrencyValue;
    private ImageView inputImage;
    private TextView inputCurrencyName;
    private TextView inputCurrencyFullName;
    private ImageButton addCurrency;
    private String typedText;
    private ArrayList<String> bundle = new ArrayList<>();
    private View view;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            bundle = getArguments().getStringArrayList("key");
            defaultCodes = bundle;
        } else {
            
            defaultCodes.addAll(Arrays.asList("EUR", "USD", "GBP", "NOK"));
        }
        defaultInputCode = "SEK";
        typedText = "0";
        view = inflater.inflate(R.layout.fragment_currency, container, false );
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
            }
        });

        addCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CurrencyAdd.class);
                intent.putStringArrayListExtra("key", (ArrayList<String>) defaultCodes);
                startActivity(intent);
            }
        });

//
//        final GestureDetector gesture = new GestureDetector(getActivity(),
//                new GestureDetector.SimpleOnGestureListener() {
//
//                    @Override
//                    public boolean onDown(MotionEvent e) {
//                        return true;
//                    }
//
//                    @Override
//                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//                                           float velocityY) {
//                        Log.i("MyApp", "Fling called");
//                        final int SWIPE_MIN_DISTANCE = 120;
//                        final int SWIPE_MAX_OFF_PATH = 250;
//                        final int SWIPE_THRESHOLD_VELOCITY = 200;
//                        try {
//                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
//                                return false;
//                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
//                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                                Log.i("MyApp", "Right to Left");
//                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
//                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                                Log.i("MyApp", "Left to Right");
//                            }
//                        } catch (Exception e) {
//
//                        }
//                        return super.onFling(e1, e2, velocityX, velocityY);
//                    }
//
////                    @Override
////                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
////                                            float distanceY) {
////                        SwipeLayoutManager.getInstance().closeCurrentLayout();
////                        return super.onScroll(e1, e2, distanceX, distanceY);
////                    }
//
//
//                });
//
//
//        listView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                return gesture.onTouchEvent(motionEvent);
//            }
//        });
//
//
//
//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int i) {
//                if (i == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    //如果垂直滑动，则需要关闭已经打开的layout
//                    SwipeLayoutManager.getInstance().closeCurrentLayout();
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//
//            }
//        });




        return view;
    }

    protected List<CurrencyData> getData(List<String> currencyCodes, Double equalUSDValue){
//        Picasso.get().load(flagAPI.getFlagURL("CNY")).into(flag);

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

        Double calculatedValue = Double.parseDouble(currencyAPI.getCurrency(currencyCode)) * equalUSDValue;
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
    }

    protected Double getEqualUSDValue(String inputText, String CurrencyCode) {
        Double inputValue = Double.parseDouble(inputText);
        Double currentCurrencyValue = Double.parseDouble(currencyAPI.getCurrency(CurrencyCode));
        equalUSDValue =  inputValue / currentCurrencyValue;
        return Double.parseDouble(String.format("%.2f", equalUSDValue));
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.i("Check:", String.valueOf(currencyCodes));
        updateListView(equalUSDValue);
        setInputBox(view, defaultInputCode);
    }
}
