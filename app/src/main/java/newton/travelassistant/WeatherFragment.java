package newton.travelassistant;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.label305.asynctask.SimpleAsyncTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import newton.travelassistant.Adapter.WeatherForecastAdapter;
import newton.travelassistant.Common.Common;
import newton.travelassistant.Model.WeatherForecastResult;
import newton.travelassistant.Model.WeatherResult;
import newton.travelassistant.Retrofit.IOpenWeatherMap;
import newton.travelassistant.Retrofit.RetrofitClient;
import retrofit2.Retrofit;

public class WeatherFragment extends Fragment {

    public static final int REQUEST_CODE = 99;

    ImageView img_weather;
    TextView txt_city_name, txt_humidity, txt_sunrise, txt_sunset, txt_preassure, txt_temperature, txt_description , txt_date_time,txt_wind, txt_geo_coord;
    ProgressBar loading;
    LinearLayout weather_panel;
    RecyclerView recycler_forecast;
    MaterialSearchBar mMaterialSearchBar;

    CompositeDisposable mCompositeDisposable;
    IOpenWeatherMap mService;

    List<String> listCities;

    public WeatherFragment() {
        // Required public constructor
        mCompositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView= inflater.inflate(R.layout.fragment_weather, container, false);

        img_weather = itemView.findViewById(R.id.img_weather);
        txt_city_name = itemView.findViewById(R.id.txt_city_name);
        txt_humidity = itemView.findViewById(R.id.txt_humidity);
        txt_date_time = itemView.findViewById(R.id.txt_date_time);
        txt_description = itemView.findViewById(R.id.txt_description);
        txt_geo_coord = itemView.findViewById(R.id.txt_geo_coord);
        txt_wind = itemView.findViewById(R.id.txt_wind);
        txt_preassure = itemView.findViewById(R.id.txt_preassure);
        txt_sunrise = itemView.findViewById(R.id.txt_sunrise);
        txt_sunset = itemView.findViewById(R.id.txt_sunset);
        txt_temperature = itemView.findViewById(R.id.txt_temperature);
        weather_panel = itemView.findViewById(R.id.weather_panel);
        loading = itemView.findViewById(R.id.loading);

        //Search bar
        mMaterialSearchBar = itemView.findViewById(R.id.searchBar);
        mMaterialSearchBar.setEnabled(false);

        //loads citys from city_list.gz in raw folder
        new LoadCities().execute();




        //Recyclerview
        recycler_forecast = itemView.findViewById(R.id.recycler_forecast);
        recycler_forecast.setHasFixedSize(true);
        recycler_forecast.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        //called to populate our view before entering city name
        checkLocationPermission();

        return itemView;
    }


    @Override
    public void onResume() {
        super.onResume();
        if ( checkLocationPermission()){
            getForecastWeatherInformation();
            getWeatherInformation();
        }

    }

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {


                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE);
            }
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            //check if we have a result and check if the result is that permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Clima", "onRequestPermissionsResult() Permission granted");

                // Permission was granted.
                if (ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                }


            }else{
                Log.d("Clima", "onRequestPermissionsResult() Permission denied");
            }
        }


    }

    public void getForecastWeatherInformation() {

            mCompositeDisposable.add(mService.getForecastWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()),
                    String.valueOf(Common.current_location.getLongitude()),
                    Common.APP_ID,
                    "metric")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<WeatherForecastResult>() {
                                   @Override
                                   public void accept(WeatherForecastResult weatherForecastResult) throws Exception {
                                       displayForecastWeather(weatherForecastResult);

                                   }
                               }, new Consumer<Throwable>() {
                                   @Override
                                   public void accept(Throwable throwable) throws Exception {
                                       Log.d("Error", "" + throwable.getMessage());

                                   }
                               }

                    )
            );


    }

    private void displayForecastWeather(WeatherForecastResult weatherForecastResult) {
            WeatherForecastAdapter adapter = new WeatherForecastAdapter(getContext(),weatherForecastResult);
            recycler_forecast.setAdapter(adapter);
    }

    public void getWeatherInformation() {

            mCompositeDisposable.add(mService.getWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()),
                    String.valueOf(Common.current_location.getLongitude()),
                    Common.APP_ID,
                    "metric")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<WeatherResult>() {
                        @Override
                        public void accept(WeatherResult weatherResult) throws Exception {
                            //fetching img from Owm api and setting them to imgview
                            Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                                    .append(weatherResult.getWeather().get(0).getIcon())
                                    .append(".png").toString()).into(img_weather);

                            //Load information and build the strings
                            txt_city_name.setText(weatherResult.getName());
                            txt_description.setText(new StringBuilder("Weather in ")
                                    .append(weatherResult.getName()).toString());
                            txt_temperature.setText(new StringBuilder(
                                    String.valueOf(weatherResult.getMain().getTemp())).append("°C").toString());
                            txt_date_time.setText(Common.convertUnixToDate(weatherResult.getDt()));
                            txt_preassure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure())).append(" hpa").toString());
                            txt_humidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity())).append(" %").toString());
                            txt_sunrise.setText(Common.convertUnixToHour(weatherResult.getSys().getSunrise()));
                            txt_sunset.setText(Common.convertUnixToHour(weatherResult.getSys().getSunset()));
                            txt_geo_coord.setText(new StringBuilder(weatherResult.getCoord().toString()));


                            //Display panel
                            weather_panel.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);


                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Toast.makeText(getActivity(), "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("GetWeatherInformation", "" + throwable.getMessage());//
                        }
                    })

            );

    }


    private class LoadCities extends SimpleAsyncTask<List<String>> {

        @Override
        protected List<String> doInBackgroundSimple() {
            //Creates list and appends results from city_list raw file which is a bit slow but still faster then Owm own raw file
            listCities = new ArrayList<>();
            try {
                StringBuilder builder = new StringBuilder();
                InputStream inputStream = getResources().openRawResource(R.raw.city_list);
                GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);

                InputStreamReader reader = new InputStreamReader(gzipInputStream);
                BufferedReader bufferedReader = new BufferedReader(reader);

                String readed;
                while ((readed = bufferedReader.readLine())!=null)
                    builder.append(readed);
                listCities = new Gson().fromJson(builder.toString(),new TypeToken<List<String>>(){}.getType());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return listCities;
        }

        @Override
        protected void onSuccess(final List<String> listCity) {
            super.onSuccess(listCity);
            mMaterialSearchBar.setEnabled(true);
            mMaterialSearchBar.addTextChangeListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //Suggestions from listCity, works sometimes
                    List<String> suggest = new ArrayList<>();
                    for (String search : listCity){
                        if (search.toLowerCase().contains(mMaterialSearchBar.getText().toLowerCase()))
                            suggest.add(search);
                    }
                    mMaterialSearchBar.setLastSuggestions(suggest);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            mMaterialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
                @Override
                public void onSearchStateChanged(boolean enabled) {

                }

                @Override
                public void onSearchConfirmed(CharSequence text) {
                    //gets weather and forecast by city name
                    getWeatherByCityName(text.toString());
                    getForecastWeatherByCityName(text.toString());
                }

                @Override
                public void onButtonClicked(int buttonCode) {

                }
            });
        }
    }

    private void getForecastWeatherByCityName(String cityName) {
        mCompositeDisposable.add(mService.getForecastWeatherByCityName(cityName,
                Common.APP_ID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherForecastResult>(){
                    @Override
                    public void accept(WeatherForecastResult weatherForecastResult) throws Exception {
                        displayForecastWeather(weatherForecastResult);
                    }
                }, new Consumer<Throwable>(){

                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("Error",""+throwable.getMessage());
                    }
                })
        );
    }

    private void getWeatherByCityName(String cityName) {
        mCompositeDisposable.add(mService.getWeatherByCityName(cityName,
                Common.APP_ID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                    @Override
                    public void accept(WeatherResult weatherResult) throws Exception {

                        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                                .append(weatherResult.getWeather().get(0).getIcon())
                                .append(".png").toString()).into(img_weather);

                        //Load information
                        txt_city_name.setText(weatherResult.getName());
                        txt_description.setText(new StringBuilder("Weather in ")
                                .append(weatherResult.getName()).toString());
                        txt_temperature.setText(new StringBuilder(
                                String.valueOf(weatherResult.getMain().getTemp())).append("°C").toString());
                        txt_date_time.setText(Common.convertUnixToDate(weatherResult.getDt()));
                        txt_preassure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure())).append(" hpa").toString());
                        txt_humidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity())).append(" %").toString());
                        txt_sunrise.setText(Common.convertUnixToHour(weatherResult.getSys().getSunrise()));
                        txt_sunset.setText(Common.convertUnixToHour(weatherResult.getSys().getSunset()));
                        txt_geo_coord.setText(new StringBuilder(weatherResult.getCoord().toString()));


                        //Display panel
                        weather_panel.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(),""+throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        Log.d("GetWeatherInformation", ""+throwable.getMessage());
                    }
                })

        );
    }
    @Override
    public void onStop() {
        mCompositeDisposable.clear();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mCompositeDisposable.clear();
        super.onDestroy();
    }


}
