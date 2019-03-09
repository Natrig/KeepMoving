package rus.app.keepmoving.Trips;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import rus.app.keepmoving.Entities.Trip;
import rus.app.keepmoving.Entities.TripListInfo;
import rus.app.keepmoving.Entities.UserAccount;
import rus.app.keepmoving.R;
import rus.app.keepmoving.Util.KPTripAdapter;

public class TripsActivity extends AppCompatActivity {
    private static final String TAG = "TripsActivity";

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    private List<TripListInfo> mTripListInfo;
    private Trip requestedTrip;
    private ListView tripList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        tripList = (ListView) findViewById(R.id.tripList);

        ImageView backIcon = (ImageView) findViewById(R.id.backIcon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigation back to ProfileActivity");
                finish();
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

        // Read from the database
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getTrips(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void initTripDetails() {
        requestedTrip = new Trip();
        Intent intent = getIntent();

        if (intent.hasExtra("departure_date")) {
            requestedTrip.setDeparture_date(intent.getExtras().getString("departure_date"));
        }

        if (intent.hasExtra("from_place")) {
            requestedTrip.setFrom_place(intent.getExtras().getString("from_place"));
        }

        if (intent.hasExtra("where_place")) {
            requestedTrip.setWhere_place(intent.getExtras().getString("where_place"));
        }
    }

    public void getTrips(DataSnapshot dataSnapshot) {
        mTripListInfo = new ArrayList<TripListInfo>();
        DataSnapshot tableSnapshot = dataSnapshot
                .child(getString(R.string.db_user_trip));

        for (DataSnapshot tripSnapshot : tableSnapshot.getChildren()) {
            Trip trip = tripSnapshot.getValue(Trip.class);

            if (!requestedTrip.getFrom_place().equals(trip.getFrom_place())) {
                continue;
            }

            if (!requestedTrip.getWhere_place().equals(trip.getWhere_place())) {
                continue;
            }

            if (!requestedTrip.getDeparture_date().equals(trip.getDeparture_date())) {
                continue;
            }

            TripListInfo tripInfo = new TripListInfo();
            tripInfo.setTrip_id(tripSnapshot.getKey());
            tripInfo.setCar_model(trip.getCar_model());
            tripInfo.setDeparture_date(trip.getDeparture_date());
            tripInfo.setFrom_place(trip.getFrom_place());
            tripInfo.setWhere_place(trip.getWhere_place());

            DataSnapshot userSnapshot = dataSnapshot
                    .child(getString(R.string.db_user_account)).child(trip.getCreator_id());

            tripInfo.setCreator_name(
                    userSnapshot.getValue(UserAccount.class).getSurname() +
                            " " +
                            userSnapshot.getValue(UserAccount.class).getName()
            );

            mTripListInfo.add(tripInfo);
        }

        KPTripAdapter adapter = new KPTripAdapter(this, R.layout.list_trip, mTripListInfo);
        tripList.setAdapter(adapter);

        tripList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TripListInfo tripListInfo = mTripListInfo.get(position);
                System.out.println("_---------------- " + tripListInfo.getTrip_id() + " ------------------ ");
            }
        });
    }
}
