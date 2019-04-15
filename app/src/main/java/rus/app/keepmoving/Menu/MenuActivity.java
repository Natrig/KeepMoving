package rus.app.keepmoving.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import rus.app.keepmoving.BaseActivity;
import rus.app.keepmoving.Entities.Trip;
import rus.app.keepmoving.Entities.TripListInfo;
import rus.app.keepmoving.Entities.UserAccount;
import rus.app.keepmoving.R;
import rus.app.keepmoving.TripProfile.TripProfileActivity;
import rus.app.keepmoving.Util.KPTripAdapter;

public class MenuActivity extends BaseActivity {
    private static final int ACTIVITY_NUM = 0;
    private static final String TAG = "MenuActivity";

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    private List<TripListInfo> myTripListInfo;
    private List<TripListInfo> requestedTripListInfo;
    private ListView myTripList;
    private ListView requestedTripList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Log.d(TAG, "onCreate: creating.");

        initImageLoader();
        setupBottomNavBar(MenuActivity.this, ACTIVITY_NUM);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

        myTripList = (ListView) findViewById(R.id.myTripList);
        requestedTripList = (ListView) findViewById(R.id.requestedTripList);
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();

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

    public void getTrips(DataSnapshot dataSnapshot) {
        myTripListInfo = new ArrayList<TripListInfo>();
        requestedTripListInfo = new ArrayList<TripListInfo>();

        DataSnapshot tableSnapshot = dataSnapshot
                .child(getString(R.string.db_user_trip));

        for (DataSnapshot tripSnapshot : tableSnapshot.getChildren()) {
            Trip trip = tripSnapshot.getValue(Trip.class);

            if(trip.getTrip_status().equals("FINISHED")) {
                continue;
            }

            if (trip.getCreator_id().equals(currentUser.getUid())) {
                TripListInfo tripListInfo = createTripInfo(tripSnapshot.getKey(), trip, dataSnapshot);

                myTripListInfo.add(tripListInfo);
            } else if (trip.getRequests() != null && trip.getRequests().containsKey(currentUser.getUid())) {
                if (!trip.getRequests().get(currentUser.getUid()).equals("false")) {
                    TripListInfo tripListInfo = createTripInfo(tripSnapshot.getKey(), trip, dataSnapshot);

                    requestedTripListInfo.add(tripListInfo);
                }
            }
        }
        if (myTripListInfo.size() > 0) {
            KPTripAdapter adapter = new KPTripAdapter(this, R.layout.list_trip, myTripListInfo);
            myTripList.setAdapter(adapter);

            myTripList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TripListInfo tripListInfo = myTripListInfo.get(position);

                    toTripProfile(tripListInfo.getTrip_id());
                }
            });
        }

        if (requestedTripListInfo.size() > 0) {
            KPTripAdapter requestAdapter = new KPTripAdapter(this, R.layout.list_trip, requestedTripListInfo);
            requestedTripList.setAdapter(requestAdapter);

            requestedTripList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TripListInfo tripListInfo = requestedTripListInfo.get(position);

                    toTripProfile(tripListInfo.getTrip_id());
                }
            });
        }

        if (requestedTripListInfo.size() == 0 && myTripListInfo.size() == 0) {
            LinearLayout middleBar = (LinearLayout) findViewById(R.id.middleBar);
            middleBar.setVisibility(View.INVISIBLE);

            RelativeLayout middleBarNoTrip = (RelativeLayout) findViewById(R.id.middleBarNoTrip);
            middleBarNoTrip.setVisibility(View.VISIBLE);
        } else {
            LinearLayout middleBar = (LinearLayout) findViewById(R.id.middleBar);
            middleBar.setVisibility(View.VISIBLE);

            RelativeLayout middleBarNoTrip = (RelativeLayout) findViewById(R.id.middleBarNoTrip);
            middleBarNoTrip.setVisibility(View.INVISIBLE);
        }
    }

    private TripListInfo createTripInfo(String tripId, Trip trip, DataSnapshot dataSnapshot) {
        UserAccount account = dataSnapshot
                .child(getString(R.string.db_user_account))
                .child(trip.getCreator_id())
                .getValue(UserAccount.class);

        TripListInfo tripInfo = new TripListInfo();
        tripInfo.setTrip_id(tripId);
        tripInfo.setCar_model(trip.getCar_model());
        tripInfo.setDeparture_date(trip.getDeparture_date());
        tripInfo.setFrom_place(trip.getFrom_place());
        tripInfo.setWhere_place(trip.getWhere_place());
        tripInfo.setCreator_name(account.getSurname() + " " + account.getName());

        return tripInfo;
    }

    private void toTripProfile(String tripId) {
        Intent intent = new Intent(this, TripProfileActivity.class);
        intent.putExtra("trip_id", tripId);
        startActivity(intent);
    }
}
