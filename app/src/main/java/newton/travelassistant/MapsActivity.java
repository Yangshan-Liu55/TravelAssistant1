package newton.travelassistant;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code = 99;
    double latitude = 0, longitude = 0;
    private int ProximityRadius = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkUserLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    public void onClick(View v) {

        String air_port = "air_port", hotel = "hotel", bus_stop = "bus_stop", museum = "museum",
                train_station = "train_station", restaurant = "restaurant", theater = "theater";

        Object transferData[] = new Object[2];

        FindPlaces findPlaces = new FindPlaces();




        switch (v.getId()) {
            case R.id.bus_stops:

                mMap.clear();

                Toast.makeText(getApplicationContext(), "So you want to take the bus?", Toast.LENGTH_LONG).show();
                break;


            case R.id.hotels: {
                mMap.clear();

                String url = getURL(latitude, longitude, hotel);
                transferData[0] = mMap;
                transferData[1] = url;
                findPlaces.execute(transferData);

                Toast.makeText(getApplicationContext(), "So you want to look for hotels?", Toast.LENGTH_LONG).show();


                break;
            }

            case R.id.museums: {
                mMap.clear();

                String url = getURL(latitude, longitude, museum);
                transferData[0] = mMap;
                transferData[1] = url;
                findPlaces.execute(transferData);

                Toast.makeText(getApplicationContext(), "So you want to look for museums?", Toast.LENGTH_LONG).show();
                break;
            }

            case R.id.air_ports: {
                mMap.clear();

                String url = getURL(latitude, longitude, air_port);
                transferData[0] = mMap;
                transferData[1] = url;
                findPlaces.execute(transferData);


                Toast.makeText(getApplicationContext(), "So you want to take the plane?", Toast.LENGTH_LONG).show();
                break;
            }

            case R.id.restaurants: {
                mMap.clear();

                String url = getURL(latitude, longitude, restaurant);
                transferData[0] = mMap;
                transferData[1] = url;
                findPlaces.execute(transferData);

                Toast.makeText(getApplicationContext(), "So you want to look for restaurants?", Toast.LENGTH_LONG).show();
                break;
            }

            case R.id.theaters: {
                mMap.clear();

                String url = getURL(latitude, longitude, theater);
                transferData[0] = mMap;
                transferData[1] = url;
                findPlaces.execute(transferData);

                Toast.makeText(getApplicationContext(), "So you want to go to the theater?", Toast.LENGTH_LONG).show();
                break;
            }

            case R.id.train_stations: {
                mMap.clear();
/*
                String url = getURL(latitude, longitude, train_station);
                transferData[0] = mMap;
                transferData[1] = url;
                findPlaces.execute(transferData);
*/
                Toast.makeText(getApplicationContext(), "So you want to take the train?", Toast.LENGTH_LONG).show();
                break;
            }

        }

    }

    private String getURL(double latitude,double longitude,String nearbyPlace){

        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location"+ latitude +","+ longitude);
        googleURL.append("&radius=" +ProximityRadius);
        googleURL.append("&type=" +nearbyPlace);
        googleURL.append("&tsensor=true");
        googleURL.append("&key"+ "AIzaSyDSUd1qJXTR5HJsKioiGxgnzF8n4UJ1V6I");

        Log.d("googleMapsactivity","url ="+ googleURL.toString());

        return googleURL.toString();
    }


        @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {



            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);

        }

    }



    public boolean checkUserLocationPermission(){

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);


            }

            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }

            return false;
        }

        else
        {

            return true;
        }



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case Request_User_Location_Code:
                if(grantResults.length > 0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if(googleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);

                    }
                }
                else {
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();

    }


    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        lastLocation = location;

        if (currentUserLocationMarker != null)
        {
            currentUserLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("You are here.");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        currentUserLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(14));


        if (googleApiClient != null) {

            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);

        }


    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
