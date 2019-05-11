package rus.app.keepmoving.Location;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.Distance;
import com.google.maps.model.Duration;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rus.app.keepmoving.Entities.UserLocation;
import rus.app.keepmoving.R;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "LocationActivity";
    private static final int DEFAULT_ZOOM = 10;
    private static final int USE_CURRENT_ZOOM = -1;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mRef;

    private LocationProvider mLocationProvider;
    private Marker placeMarker;
    private Address mAddress;
    private String UserId;
    private String wherePlace;
    private String fromPlace;
    private GeoApiContext mGeoApiContext = null;

    private UserLocation userLocation;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();

        if (mGeoApiContext == null) {
            mGeoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.google_maps_key))
                    .build();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap = googleMap;
        currentUser = mAuth.getCurrentUser();

        initExtras();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getUserLocation(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void initExtras() {
        Intent intent = getIntent();

        if (intent.hasExtra("userID")) {
            UserId = intent.getExtras().getString("userID");
        }

        if (intent.hasExtra("wherePlace")) {
            wherePlace = intent.getExtras().getString("wherePlace");
        }

        if (intent.hasExtra("fromPlace")) {
            fromPlace = intent.getExtras().getString("fromPlace");
        }

        TextView tvFrom = (TextView) findViewById(R.id.fromPlaceInput);
        TextView tvWhere = (TextView) findViewById(R.id.wherePlaceInput);

        tvFrom.setText(fromPlace);
        tvWhere.setText(wherePlace);
    }

    private void getUserLocation(DataSnapshot dataSnapshot) {
        userLocation = dataSnapshot
                .child(getString(R.string.db_user_location))
                .child(UserId)
                .getValue(UserLocation.class);

        LatLng point = new LatLng(
                Double.parseDouble(userLocation.getLatitude()), Double.parseDouble(userLocation.getLongitude())
        );

        moveCamera(point, DEFAULT_ZOOM);

        if (placeMarker != null) {
            placeMarker.setPosition(point);
        } else {
            placeMarker = mMap.addMarker(
                    new MarkerOptions()
                            .position(point)
                            .title("Авто")
                            .icon(bitmapDescriptorFromVector(this, R.drawable.ic_trip))
            );
        }

        // FOR TEST ONLY
        Address destination = getDestinationPoint();
        LatLng destinationPoint = new LatLng(destination.getLatitude(), destination.getLongitude());
        Marker marker = mMap.addMarker(new MarkerOptions().position(destinationPoint));
        calculateDirections(marker);
    }

    private void moveCamera(LatLng latLng, float zoom) {
        if (zoom != USE_CURRENT_ZOOM) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        } else {
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void calculateDirections(Marker marker) {
        Log.d(TAG, "calculateDirections: calculating directions.");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                marker.getPosition().latitude,
                marker.getPosition().longitude
        );

        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        directions.language("RU");
        directions.optimizeWaypoints(true);
        directions.mode(TravelMode.DRIVING);
        directions.origin(
                new com.google.maps.model.LatLng(
                        Double.parseDouble(userLocation.getLatitude()),
                        Double.parseDouble(userLocation.getLongitude())
                )
        );

        Log.d(TAG, "calculateDirections: destination: " + destination.toString());

        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.d(TAG, "calculateDirections: routes: " + result.routes[0].toString());
                Log.d(TAG, "calculateDirections: duration: " + result.routes[0].legs[0].duration);
                Log.d(TAG, "calculateDirections: distance: " + result.routes[0].legs[0].distance);
                Log.d(TAG, "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());

                addPolylinesToMap(result);
                addDirectionInfo(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "calculateDirections: Failed to get directions: " + e.getMessage());
            }
        });
    }

    private void addPolylinesToMap(final DirectionsResult result) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                DirectionsRoute route = result.routes[0];

                List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                List<LatLng> newDecodedPath = new ArrayList<>();

                // This loops through all the LatLng coordinates of ONE polyline.
                for (com.google.maps.model.LatLng latLng : decodedPath) {
                    newDecodedPath.add(new LatLng(
                            latLng.lat,
                            latLng.lng
                    ));
                }

                Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                polyline.setColor(ContextCompat.getColor(LocationActivity.this, R.color.colorError));
                polyline.setClickable(true);
            }
        });
    }

    private void addDirectionInfo(final DirectionsResult result) {
        Duration duration = result.routes[0].legs[0].duration;
        Distance distance = result.routes[0].legs[0].distance;

        TextView tvTime = (TextView) findViewById(R.id.destinationTimeInput);
        TextView tvLength = (TextView) findViewById(R.id.destinationDistanceInput);

        tvTime.setText("Оставшееся время : " + duration.humanReadable);
        tvLength.setText("Дистанция : " + distance.humanReadable);
    }

    private Address getDestinationPoint() {
        String searchString = wherePlace;

        Geocoder geocoder = new Geocoder(LocationActivity.this);
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (list.size() > 0) {
            Address address = list.get(0);

            return address;
        }

        return null;
    }

}
