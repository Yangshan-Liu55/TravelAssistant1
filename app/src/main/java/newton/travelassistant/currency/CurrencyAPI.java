package newton.travelassistant.currency;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import newton.travelassistant.R;

public class CurrencyAPI {

    private static final String API = "http://apilayer.net/api/live?access_key" +
            "=07255761cde9523e860e502df81f63cf";
    private String returnedJsonString;
    DataHandler dataHandler = new DataHandler();
    Gson gson = new Gson();

    public JsonObject connection(Context context) {

        // Try to load local data, if not NULL, and updated in 24 hours, return it
        String savedData = dataHandler.loadString(context, context.getString(R.string.saved_currency_data));
        if (savedData != null) {
            JsonObject obj = gson.fromJson(savedData, JsonElement.class).getAsJsonObject();
            long savedTimeStamp = obj.get("timestamp").getAsLong();

            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis / 1000 - savedTimeStamp < 86400) {
                return obj;
            }
        }


        // If local data is null, OR data is expired(saved more than 24 hours),
        // then, Load new data from API, and save it to local data
        try {
            returnedJsonString = new LoadOnlineData().execute(API, "currency").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        dataHandler.saveString(context,context.getString(R.string.saved_currency_data), returnedJsonString);

        // Check if Loading data from API is successful, if do, save also the timeStamp
        try {
            JsonObject obj = gson.fromJson(returnedJsonString, JsonElement.class).getAsJsonObject();
            boolean isSuccess = obj.get("success").getAsBoolean();
            System.out.println("isSuccess: " + isSuccess);

            if (isSuccess) {
                dataHandler.saveString(context, context.getString(R.string.saved_time_stamp), String.valueOf(obj.get("timestamp").getAsLong()));
                return obj;
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return getTempCurrency();
    }

    public String getCurrency(Context context, String currencyCode) {

        JsonObject obj = connection(context);
        if (obj == null) {
            return null;
        }

        JsonObject currency = obj.getAsJsonObject("quotes");
        Double currencyValue = currency.get("USD" + currencyCode).getAsDouble();

        return String.format("%.2f", currencyValue);
    }

    public String getUpdateTime(Context context) {
        JsonObject obj = connection(context);
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

    public JsonObject getTempCurrency() {
        String tempCurrency = "{\"success\":true,\"terms\":\"https:\\/\\/currencylayer" +
                ".com\\/terms\",\"privacy\":\"https:\\/\\/currencylayer.com\\/privacy\"," +
                "\"timestamp\":1559381944,\"source\":\"USD\",\"quotes\":{\"USDAED\":3.673204," +
                "\"USDAFN\":79.350404,\"USDALL\":109.750403,\"USDAMD\":480.103986,\"USDANG\":1" +
                ".875604,\"USDAOA\":330.453504,\"USDARS\":44.694041,\"USDAUD\":1.440504," +
                "\"USDAWG\":1.8,\"USDAZN\":1.705041,\"USDBAM\":1.75525,\"USDBBD\":2.0022," +
                "\"USDBDT\":84.438041,\"USDBGN\":1.751204,\"USDBHD\":0.377015,\"USDBIF\":1849," +
                "\"USDBMD\":1,\"USDBND\":1.35055,\"USDBOB\":6.91225,\"USDBRL\":3.925041," +
                "\"USDBSD\":1.00025,\"USDBTC\":0.000117,\"USDBTN\":69.578947,\"USDBWP\":10" +
                ".908041,\"USDBYN\":2.09995,\"USDBYR\":19600,\"USDBZD\":2.016404,\"USDCAD\":1" +
                ".35435,\"USDCDF\":1654.50392,\"USDCHF\":0.999765,\"USDCLF\":0.02572," +
                "\"USDCLP\":709.680396,\"USDCNY\":6.905104,\"USDCOP\":3378,\"USDCRC\":586.960395," +
                "\"USDCUC\":1,\"USDCUP\":26.5,\"USDCVE\":99.150394,\"USDCZK\":23.142804," +
                "\"USDDJF\":177.720394,\"USDDKK\":6.68615,\"USDDOP\":53.000359,\"USDDZD\":119" +
                ".780393,\"USDEGP\":16.705504,\"USDERN\":15.000358,\"USDETB\":29.103876," +
                "\"USDEUR\":0.89298,\"USDFJD\":2.172504,\"USDFKP\":0.794905,\"USDGBP\":0.791265," +
                "\"USDGEL\":2.780391,\"USDGGP\":0.791282,\"USDGHS\":5.44504,\"USDGIP\":0.794905," +
                "\"USDGMD\":49.78039,\"USDGNF\":9230.000355,\"USDGTQ\":7.712504,\"USDGYD\":208" +
                ".86504,\"USDHKD\":7.83855,\"USDHNL\":24.610389,\"USDHRK\":6.64735,\"USDHTG\":90" +
                ".947504,\"USDHUF\":290.920388,\"USDIDR\":14285.5,\"USDILS\":3.62815,\"USDIMP\":0" +
                ".791282,\"USDINR\":69.579604,\"USDIQD\":1190,\"USDIRR\":42105.000352," +
                "\"USDISK\":123.670386,\"USDJEP\":0.791282,\"USDJMD\":135.440386,\"USDJOD\":0" +
                ".70904,\"USDJPY\":108.27504,\"USDKES\":101.330385,\"USDKGS\":69.849904," +
                "\"USDKHR\":4057.503799,\"USDKMF\":440.950384,\"USDKPW\":899.786883," +
                "\"USDKRW\":1188.970384,\"USDKWD\":0.30435,\"USDKYD\":0.83364,\"USDKZT\":382" +
                ".820383,\"USDLAK\":8648.000349,\"USDLBP\":1507.303779,\"USDLKR\":176.360382," +
                "\"USDLRD\":191.203775,\"USDLSL\":14.570382,\"USDLTL\":2.95274,\"USDLVL\":0" +
                ".60489,\"USDLYD\":1.395039,\"USDMAD\":9.712104,\"USDMDL\":18.272039," +
                "\"USDMGA\":3650.000347,\"USDMKD\":55.322039,\"USDMMK\":1529.55038," +
                "\"USDMNT\":2634.193039,\"USDMOP\":8.08085,\"USDMRO\":357.000346,\"USDMUR\":35" +
                ".803504,\"USDMVR\":15.503741,\"USDMWK\":720.000345,\"USDMXN\":19.615504," +
                "\"USDMYR\":4.190504,\"USDMZN\":62.235039,\"USDNAD\":14.570377,\"USDNGN\":360" +
                ".000344,\"USDNIO\":33.303725,\"USDNOK\":8.755804,\"USDNPR\":111.760376," +
                "\"USDNZD\":1.530204,\"USDOMR\":0.38531,\"USDPAB\":1.00035,\"USDPEN\":3.381504," +
                "\"USDPGK\":3.375039,\"USDPHP\":52.105039,\"USDPKR\":146.875038,\"USDPLN\":3" +
                ".834404,\"USDPYG\":6309.103699,\"USDQAR\":3.641038,\"USDRON\":4.250504," +
                "\"USDRSD\":105.460373,\"USDRUB\":65.475604,\"USDRWF\":905,\"USDSAR\":3.752038," +
                "\"USDSBD\":8.19795,\"USDSCR\":13.687504,\"USDSDG\":45.123038,\"USDSEK\":9" +
                ".496904,\"USDSGD\":1.373904,\"USDSHP\":1.320904,\"USDSLL\":8885.000339," +
                "\"USDSOS\":583.503667,\"USDSRD\":7.458038,\"USDSTD\":21050.59961,\"USDSVC\":8" +
                ".753804,\"USDSYP\":515.000338,\"USDSZL\":14.57037,\"USDTHB\":31.454038," +
                "\"USDTJS\":9.439038,\"USDTMT\":3.5,\"USDTND\":2.983504,\"USDTOP\":2.29785," +
                "\"USDTRY\":5.840368,\"USDTTD\":6.77495,\"USDTWD\":31.537038,\"USDTZS\":2300" +
                ".000336,\"USDUAH\":26.948038,\"USDUGX\":3766.203631,\"USDUSD\":1,\"USDUYU\":35" +
                ".220367,\"USDUZS\":8490.000335,\"USDVEF\":9.987504,\"USDVND\":23412.5," +
                "\"USDVUV\":113.987486,\"USDWST\":2.689401,\"USDXAF\":588.690365,\"USDXAG\":0" +
                ".068578,\"USDXAU\":0.000766,\"USDXCD\":2.70255,\"USDXDR\":0.72589,\"USDXOF\":588" +
                ".000332,\"USDXPF\":107.250364,\"USDYER\":249.903597,\"USDZAR\":14.584604," +
                "\"USDZMK\":9001.203593,\"USDZMW\":13.179037,\"USDZWL\":322.355011}}";
        return new Gson().fromJson(tempCurrency, JsonElement.class).getAsJsonObject();
    }

}
