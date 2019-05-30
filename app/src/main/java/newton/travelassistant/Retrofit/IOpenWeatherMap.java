package newton.travelassistant.Retrofit;

import io.reactivex.Observable;
import newton.travelassistant.Model.WeatherForecastResult;
import newton.travelassistant.Model.WeatherResult;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherMap {
    //our interface for making 4 different calls
    // 2 weather/forecast calls each one by coords other by city name
    @GET("weather")
    Observable<WeatherResult> getWeatherByLatLng(@Query("lat") String lat,
                                                 @Query("lon") String lon,
                                                 @Query("appid") String appid,
                                                 @Query("units") String unit);

    @GET("weather")
    Observable<WeatherResult> getWeatherByCityName(@Query("q") String cityName,
                                                   @Query("appid") String appid,
                                                   @Query("units") String unit);
    @GET("forecast")
    Observable<WeatherForecastResult> getForecastWeatherByLatLng(@Query("lat") String lat,
                                                                 @Query("lon") String lon,
                                                                 @Query("appid") String appid,
                                                                 @Query("units") String unit);
    @GET("forecast")
    Observable<WeatherForecastResult> getForecastWeatherByCityName(@Query("q") String cityName,
                                                                   @Query("appid") String appid,
                                                                   @Query("units") String unit);
}
