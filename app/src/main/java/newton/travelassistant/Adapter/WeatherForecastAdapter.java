package newton.travelassistant.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import newton.travelassistant.Common.Common;
import newton.travelassistant.Model.WeatherForecastResult;
import newton.travelassistant.R;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.MyViewHolder> {

    Context mContext;
    WeatherForecastResult mWeatherForecastResult;

    public WeatherForecastAdapter(Context context, WeatherForecastResult weatherForecastResult) {
        mContext = context;
        mWeatherForecastResult = weatherForecastResult;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.card_weather_forecast,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                .append(mWeatherForecastResult.list.get(position).weather.get(0).getIcon())
                .append(".png").toString()).into(holder.img_weather_card);

        holder.txt_date_time_card.setText(new StringBuilder(Common.convertUnixToDate(mWeatherForecastResult
        .list.get(position).dt)));

        holder.txt_description_card.setText(new StringBuilder(mWeatherForecastResult.list.get(position).
                weather.get(0).getDescription()));

        holder.txt_temperature_card.setText(new StringBuilder(String.valueOf(mWeatherForecastResult.list.get(position).
                main.getTemp())).append("°C") );
    }

    @Override
    public int getItemCount() {
        return mWeatherForecastResult.list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txt_date_time_card,txt_description_card,txt_temperature_card;
        ImageView img_weather_card;

        public  MyViewHolder(View itemView ){
            super(itemView);
            img_weather_card = itemView.findViewById(R.id.img_weather_card);
            txt_date_time_card = itemView.findViewById(R.id.txt_date_card);
            txt_description_card = itemView.findViewById(R.id.txt_description_card);
            txt_temperature_card = itemView.findViewById(R.id.txt_temperature_card);
        }
    }

}
