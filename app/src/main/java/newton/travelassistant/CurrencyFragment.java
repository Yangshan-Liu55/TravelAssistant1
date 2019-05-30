package newton.travelassistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class CurrencyFragment extends Fragment {

    private ListView listView;
    private List<CurrencyData> list;
    private CurrencyAdapter currencyAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        CurrencyAPI currencyAPI = new CurrencyAPI();
        FlagAPI flagAPI = new FlagAPI();

        View view = inflater.inflate(R.layout.fragment_currency, container, false );
        listView = view.findViewById(R.id.list_view);

        list = getData();
        currencyAdapter = new CurrencyAdapter(getActivity(), R.layout.currency_list, list);
        listView.setAdapter(currencyAdapter);

        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {
                        System.out.println("Fling called");
                        final int SWIPE_MIN_DISTANCE = 120;
                        final int SWIPE_MAX_OFF_PATH = 250;
                        final int SWIPE_THRESHOLD_VELOCITY = 200;
                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                                return false;
                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                                Log.i(Constants.APP_TAG, "Right to Left");
                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                                Log.i(Constants.APP_TAG, "Left to Right");
                            }
                        } catch (Exception e) {

                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });



        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    //如果垂直滑动，则需要关闭已经打开的layout
                    SwipeLayoutManager.getInstance().closeCurrentLayout();
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

//        ImageView flag = (ImageView) view.findViewById(R.id.flagView);
//        Picasso.get().load(flagAPI.getFlag("CNY")).into(flag);

        return view;
    }

    public List<CurrencyData> getData(){
        List<CurrencyData> list = new ArrayList<>();
        CurrencyData data = new CurrencyData();
        for (int i = 0; i < 5; i++) {
            data.setImgUrl("https://www.countryflags.io/be/flat/64.png");
            data.setCurrencyName("Title");
            data.setCurrencyValue("some info");
            list.add(data);
        }
        return list;
    }




}
