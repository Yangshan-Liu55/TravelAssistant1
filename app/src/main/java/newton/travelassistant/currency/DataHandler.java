package newton.travelassistant.currency;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataHandler {



    public void saveString(Context context, String key, String data) {
        SharedPreferences.Editor editor = context.getSharedPreferences("data", context.MODE_PRIVATE).edit();
        editor.putString(key, data);
        editor.apply();
    }

    public void saveList(Context context, String key, List<String> list) {
        SharedPreferences.Editor editor = context.getSharedPreferences("data", context.MODE_PRIVATE).edit();
        Set<String> set = new HashSet<>();
        set.addAll(list);
        editor.putStringSet("set", set);
        editor.apply();
    }


    public String loadString(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", context.MODE_PRIVATE);
        String result = sharedPreferences.getString(key, null);
        return result;
    }

    public List<String> loadList(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", context.MODE_PRIVATE);
        Set<String> set = sharedPreferences.getStringSet(key, null);
        if (set == null) {
            return null;
        } else {
            List<String> list = new ArrayList<>();
            list.addAll(set);
            return list;
        }
    }

}
