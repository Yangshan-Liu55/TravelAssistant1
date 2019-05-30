package newton.travelassistant.Common;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {

    public static final String APP_ID ="8044f706afa069ab633d2d955791653c";
    public static Location current_location=null;

    public static String convertUnixToDate(long dt) {
        Date date = new Date(dt*1000L);
        SimpleDateFormat sdf =new SimpleDateFormat("HH:mm dd EEE MM yyyy");
        String formated = sdf.format(date);
        return formated;
    }

    public static String convertUnixToHour(long dt) {
        Date date = new Date(dt*1000L);
        SimpleDateFormat sdf =new SimpleDateFormat("HH:mm");
        String formated = sdf.format(date);
        return formated;
    }
}
