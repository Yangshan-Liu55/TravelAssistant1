package newton.travelassistant;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrencyAPI {

    private JsonObject savedJsonObject = null;
    private long savedTimeStamp = 0;

    protected JsonObject connection() {

        // To avoid updating too often, it costs money
        // updates once per day
        if (savedTimeStamp != 0) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis / 1000 - savedTimeStamp > 86400) {
                if (savedJsonObject != null) {
                    return savedJsonObject;
                }
            }
        }

        String api = "http://apilayer.net/api/live?access_key=07255761cde9523e860e502df81f63cf";
        try {
            URL url = new URL(api);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "ISO-8859-1"));
            String file = br.readLine();

            Gson gson = new Gson();
            JsonElement jsonElement = gson.fromJson(file, JsonElement.class);
            JsonObject obj = jsonElement.getAsJsonObject();

            JsonElement success = obj.get("success");
            boolean isSuccess = success.getAsBoolean();
            System.out.println("isSuccess: " + isSuccess);

            if (isSuccess) {
                savedJsonObject = obj;
                savedTimeStamp = obj.get("timestamp").getAsLong();
                return obj;
            } else {
                return null;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Double getCurrency(String currencyCode) {

        JsonObject obj = connection();
        if (obj == null) {
            return null;
        }

        JsonObject currency = obj.getAsJsonObject("quotes");
        double currencyValue = currency.get("USD" + currencyCode).getAsDouble();
        System.out.println(currencyCode + " rate: " + currencyValue);

        return currencyValue;
    }

    protected String getUpdateTime() {
        JsonObject obj = connection();
        if (obj == null) {
            return null;
        }

        JsonElement time = obj.get("timestamp");
        Date timeStampDate = new Date((time.getAsLong() * 1000));
//            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(timeStampDate);

        return formattedDate;
    }

}
