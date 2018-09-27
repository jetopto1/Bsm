package com.jetopto.bsm.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jetopto.bsm.R;

public class MapFragment extends Fragment implements OnMapReadyCallback,LocationListener {

    private static final String TAG = MapFragment.class.getSimpleName();
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private static final float DEFAULT_ZOOM_SCALE = 17;

    private SupportMapFragment mapFragment;
    private LocationManager mLocationManager;
    private GoogleMap mGoogleMap;
    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(this);
        }
        // R.id.map is a FrameLayout, not a Fragment
        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (mLocationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);

        if (mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        return view;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        LatLng latLng = new LatLng(25.0727017, 121.5745552);
        mGoogleMap.addMarker(new MarkerOptions().position(latLng)
                .title("JET OPTOELECTRONICS CO.,LTD."));
        mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(),R.raw.map_style));
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,DEFAULT_ZOOM_SCALE));
        mGoogleMap.setMyLocationEnabled(true);
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM_SCALE);
//        mGoogleMap.animateCamera(cameraUpdate);
//        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
