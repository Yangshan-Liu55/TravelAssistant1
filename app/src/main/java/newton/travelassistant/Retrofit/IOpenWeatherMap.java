package newton.travelassistant.Retrofit;

import io.reactivex.Observable;
import newton.travelassistant.Model.WeatherForecastResult;
import newton.travelassistant.Model.WeatherResult;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherMap {
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
