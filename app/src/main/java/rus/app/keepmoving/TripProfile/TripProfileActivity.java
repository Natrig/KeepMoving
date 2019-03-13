package rus.app.keepmoving.TripProfile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import rus.app.keepmoving.Entities.Trip;
import rus.app.keepmoving.Entities.UserAccount;
import rus.app.keepmoving.R;
import rus.app.keepmoving.Util.KPImageLoader;

public class TripProfileActivity extends AppCompatActivity {
    private static final String TAG = "TripProfileActivity";

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    private String tripId;
    private Trip requestedTrip;
    private UserAccount tripCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_profile);

        ImageView backIcon = (ImageView) findViewById(R.id.backIcon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepBack(view);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();

        initTripDetails();
        setProfileImage();

        // Read from the database
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getTripProfile(dataSnapshot, tripId);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void initTripDetails() {
        Intent intent = getIntent();

        if (intent.hasExtra("trip_id")) {
            tripId = intent.getExtras().getString("trip_id");
        }
    }

    // TODO :: SET CORRECT IMG
    private void setProfileImage() {
        Log.d(TAG, "set Profile image: setting profile image");
        String imgUrl = "https://pp.userapi.com/c836223/v836223879/50278/IJj5HjbXWaA.jpg?ava=1";

        KPImageLoader.setImage(imgUrl, (ImageView) findViewById(R.id.profile_image), null, "");
    }

    private void getTripProfile(DataSnapshot dataSnapshot, String tripId) {
        requestedTrip = dataSnapshot
                .child(getString(R.string.db_user_trip))
                .child(tripId)
                .getValue(Trip.class);

        tripCreator = dataSnapshot
                .child(getString(R.string.db_user_account))
                .child(requestedTrip.getCreator_id())
                .getValue(UserAccount.class);

        TextView tvName = (TextView) findViewById(R.id.nameInput);
        TextView tvFromPlace = (TextView) findViewById(R.id.fromPlaceInput);
        TextView tvWherePlace = (TextView) findViewById(R.id.wherePlaceInput);
        TextView tvCarModel = (TextView) findViewById(R.id.carModelInput);
        TextView tvDepartureDate = (TextView) findViewById(R.id.departureInput);

        tvName.setText(" " + tripCreator.getName() + " " + tripCreator.getSurname());
        tvFromPlace.setText(requestedTrip.getFrom_place());
        tvWherePlace.setText(" > " + requestedTrip.getWhere_place());
        tvCarModel.setText(" " + requestedTrip.getCar_model());
        tvDepartureDate.setText(" " + requestedTrip.getDeparture_date());
    }

    public void stepBack(View view) {
        Log.d(TAG, "onClick: navigation back");

        finish();
    }
}
