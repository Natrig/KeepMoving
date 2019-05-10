package rus.app.keepmoving.Location;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.LocationProvider;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
}
