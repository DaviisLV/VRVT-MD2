package com.abols.davis.vrvt_md2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION = 1;
    private GoogleMap mMap;
    MapView Map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map = (MapView) findViewById(R.id.mapView);
        if (Map != null) {
            Map.onCreate(null);
            Map.onResume();
            Map.getMapAsync(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.json) {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            mMap.setMyLocationEnabled(true);
        }
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            CameraPosition poz = CameraPosition.builder().target(latLng).zoom(15f).bearing(0).build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(poz));
        }
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
}
