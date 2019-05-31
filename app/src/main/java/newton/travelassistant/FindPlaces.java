package newton.travelassistant;


import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class FindPlaces extends AsyncTask<Object, String, String> {


    private String googleplaceData, url;
    private GoogleMap mMap;


    @Override
    protected String doInBackground(Object... objects) {
        mMap =(GoogleMap) objects[0];
        url = (String) objects[1];

        GetURL getURL = new GetURL();
        try {
            googleplaceData = getURL.ReadTheURL(url);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return googleplaceData;
    }


    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String, String>> nearbyPlacesList= null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList =dataParser.parse(s);

        DisplayNearByPlaces(nearbyPlacesList);
    }

private void DisplayNearByPlaces(List<HashMap<String, String>> nearByPlacesList)
{
    for (int i=0; i<nearByPlacesList.size();i++)
    {
        MarkerOptions markerOptions = new MarkerOptions();

        HashMap<String, String> googleNearByPlace = nearByPlacesList.get(i);
        String nameOfPlace = googleNearByPlace.get("place_name");
        String vicinity = googleNearByPlace.get("vicinity");
        double lat = Double.parseDouble(googleNearByPlace.get("lat"));
        double lng = Double.parseDouble(googleNearByPlace.get("lng"));


        LatLng latLng = new LatLng(lat,lng);
        Log.d("latlng", String.valueOf(latLng));
        markerOptions.position(latLng);
        markerOptions.title(nameOfPlace +" : " + vicinity);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));




    }
}
}
