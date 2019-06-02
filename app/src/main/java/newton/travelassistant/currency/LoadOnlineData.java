package newton.travelassistant.currency;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class LoadOnlineData extends AsyncTask<String, Integer, String> {

    private String data;

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
            BufferedReader br = new BufferedReader(inputStreamReader);
            data = br.readLine();
            Log.i("TEST", "URL read success");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

}
