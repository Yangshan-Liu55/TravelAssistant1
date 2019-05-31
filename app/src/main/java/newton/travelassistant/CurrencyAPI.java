package newton.travelassistant;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrencyAPI {

    private JsonObject savedJsonObject = null;
    private long savedTimeStamp = 0;
    public static final String API = "http://apilayer.net/api/live?access_key=07255761cde9523e860e502df81f63cf";

    protected JsonObject connection() {

        if (savedTimeStamp != 0) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis / 1000 - savedTimeStamp > 86400) {
                if (savedJsonObject != null) {
                    return savedJsonObject;
                }
            }
        }

        /*
        try {

            URL url = new URL(API);
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
        } catch (Exception e){
            e.printStackTrace();
        } */
        return getTempCurrency();
    }

    protected String getCurrency(String currencyCode) {

        JsonObject obj = connection();
        if (obj == null) {
            return null;
        }

        JsonObject currency = obj.getAsJsonObject("quotes");
        Double currencyValue = currency.get("USD" + currencyCode).getAsDouble();

        return String.format("%.2f", currencyValue);
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

    protected JsonObject getTempCurrency(){
        String tempCurrency = "{\"success\":true," +
                "\"terms\":\"https:\\/\\/currencylayer.com\\/terms\",\"privacy\":\"https:\\/\\/currencylayer.com\\/privacy\",\"timestamp\":1559256547,\"source\":\"USD\",\"quotes\":{\"USDAED\":3.673199,\"USDAFN\":79.294587,\"USDALL\":110.199925,\"USDAMD\":480.2698,\"USDANG\":1.874597,\"USDAOA\":330.453503,\"USDARS\":44.553002,\"USDAUD\":1.446602,\"USDAWG\":1.8,\"USDAZN\":1.705021,\"USDBAM\":1.755598,\"USDBBD\":1.99125,\"USDBDT\":84.480171,\"USDBGN\":1.757099,\"USDBHD\":0.377005,\"USDBIF\":1851,\"USDBMD\":1,\"USDBND\":1.350801,\"USDBOB\":6.90885,\"USDBRL\":3.981495,\"USDBSD\":0.99985,\"USDBTC\":0.000121,\"USDBTN\":69.778255,\"USDBWP\":10.885974,\"USDBYN\":2.09515,\"USDBYR\":19600,\"USDBZD\":2.015295,\"USDCAD\":1.34985,\"USDCDF\":1655.562923,\"USDCHF\":1.007801,\"USDCLF\":0.025727,\"USDCLP\":709.895005,\"USDCNY\":6.901898,\"USDCOP\":3364.5,\"USDCRC\":587.920256,\"USDCUC\":1,\"USDCUP\":26.5,\"USDCVE\":99.425498,\"USDCZK\":23.207198,\"USDDJF\":177.720252,\"USDDKK\":6.70841,\"USDDOP\":52.501063,\"USDDZD\":119.750297,\"USDEGP\":16.777495,\"USDERN\":15.000111,\"USDETB\":29.090199,\"USDEUR\":0.8983,\"USDFJD\":2.1613,\"USDFKP\":0.791685,\"USDGBP\":0.792992,\"USDGEL\":2.804957,\"USDGGP\":0.793028,\"USDGHS\":5.294991,\"USDGIP\":0.791685,\"USDGMD\":49.744952,\"USDGNF\":9224.999511,\"USDGTQ\":7.70855,\"USDGYD\":208.919703,\"USDHKD\":7.84745,\"USDHNL\":24.585019,\"USDHRK\":6.66745,\"USDHTG\":91.221987,\"USDHUF\":291.693036,\"USDIDR\":14407,\"USDILS\":3.632497,\"USDIMP\":0.793028,\"USDINR\":69.733011,\"USDIQD\":1190,\"USDIRR\":42105.000296,\"USDISK\":124.419664,\"USDJEP\":0.793028,\"USDJMD\":134.979626,\"USDJOD\":0.709006,\"USDJPY\":109.585019,\"USDKES\":101.150498,\"USDKGS\":69.850071,\"USDKHR\":4056.503458,\"USDKMF\":442.549957,\"USDKPW\":899.998074,\"USDKRW\":1188.480015,\"USDKWD\":0.30433,\"USDKYD\":0.833215,\"USDKZT\":380.949632,\"USDLAK\":8648.499135,\"USDLBP\":1511.650223,\"USDLKR\":176.390097,\"USDLRD\":189.950143,\"USDLSL\":14.630259,\"USDLTL\":2.95274,\"USDLVL\":0.60489,\"USDLYD\":1.395015,\"USDMAD\":9.732401,\"USDMDL\":18.184012,\"USDMGA\":3649.999825,\"USDMKD\":55.345027,\"USDMMK\":1527.149945,\"USDMNT\":2641.725402,\"USDMOP\":8.08225,\"USDMRO\":356.99954,\"USDMUR\":35.634972,\"USDMVR\":15.449809,\"USDMWK\":720.155004,\"USDMXN\":19.18025,\"USDMYR\":4.1865,\"USDMZN\":62.23969,\"USDNAD\":14.62994,\"USDNGN\":359.999711,\"USDNIO\":33.280079,\"USDNOK\":8.76753,\"USDNPR\":111.784951,\"USDNZD\":1.53628,\"USDOMR\":0.384965,\"USDPAB\":0.99975,\"USDPEN\":3.35815,\"USDPGK\":3.37875,\"USDPHP\":52.349491,\"USDPKR\":150.770206,\"USDPLN\":3.85425,\"USDPYG\":6307.895687,\"USDQAR\":3.64075,\"USDRON\":4.269503,\"USDRSD\":105.969844,\"USDRUB\":65.199599,\"USDRWF\":902.5,\"USDSAR\":3.74975,\"USDSBD\":8.105649,\"USDSCR\":13.67899,\"USDSDG\":45.103991,\"USDSEK\":9.53205,\"USDSGD\":1.37895,\"USDSHP\":1.320897,\"USDSLL\":8879.999966,\"USDSOS\":582.501099,\"USDSRD\":7.458034,\"USDSTD\":21050.59961,\"USDSVC\":8.74875,\"USDSYP\":514.999881,\"USDSZL\":14.629832,\"USDTHB\":31.779964,\"USDTJS\":9.43225,\"USDTMT\":3.51,\"USDTND\":3.0073,\"USDTOP\":2.297595,\"USDTRY\":5.89132,\"USDTTD\":6.77155,\"USDTWD\":31.580945,\"USDTZS\":2295.999791,\"USDUAH\":26.948026,\"USDUGX\":3759.300118,\"USDUSD\":1,\"USDUYU\":35.129774,\"USDUZS\":8484.99984,\"USDVEF\":9.987495,\"USDVND\":23417.7,\"USDVUV\":115.881341,\"USDWST\":2.664756,\"USDXAF\":588.896076,\"USDXAG\":0.068856,\"USDXAU\":0.000776,\"USDXCD\":2.70255,\"USDXDR\":0.726363,\"USDXOF\":588.000432,\"USDXPF\":107.549741,\"USDYER\":249.849909,\"USDZAR\":14.72935,\"USDZMK\":9001.190122,\"USDZMW\":13.423008,\"USDZWL\":322.355011}}";
        return new Gson().fromJson(tempCurrency, JsonElement.class).getAsJsonObject();
    }

}
