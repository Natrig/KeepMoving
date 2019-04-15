package rus.app.keepmoving.TripProfile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rus.app.keepmoving.Entities.RequestListInfo;
import rus.app.keepmoving.Entities.Trip;
import rus.app.keepmoving.Entities.UserAccount;
import rus.app.keepmoving.Profile.ProfileActivity;
import rus.app.keepmoving.R;
import rus.app.keepmoving.Util.KPImageLoader;
import rus.app.keepmoving.Util.KPRequestAdapter;

public class TripProfileActivity extends AppCompatActivity {
    private static final String TAG = "TripProfileActivity";

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
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
        mRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();

        initTripDetails();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getTripProfile(dataSnapshot, tripId);
                toggleUserView(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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

    private void getTripProfile(DataSnapshot dataSnapshot, String tripId) {
        requestedTrip = dataSnapshot
                .child(getString(R.string.db_user_trip))
                .child(tripId)
                .getValue(Trip.class);

        if (requestedTrip == null) {
            return;
        }

        if (requestedTrip.getDescription().equals("")) {
            requestedTrip.setDescription("Не указаны");
        }

        tripCreator = dataSnapshot
                .child(getString(R.string.db_user_account))
                .child(requestedTrip.getCreator_id())
                .getValue(UserAccount.class);

        TextView tvName = (TextView) findViewById(R.id.nameInput);
        TextView tvStatus = (TextView) findViewById(R.id.statusInput);
        TextView tvCarModel = (TextView) findViewById(R.id.carModelInput);
        TextView tvCarNumber = (TextView) findViewById(R.id.carNumberInput);
        TextView tvFromPlace = (TextView) findViewById(R.id.fromPlaceInput);
        TextView tvWherePlace = (TextView) findViewById(R.id.wherePlaceInput);
        TextView tvDepartureDate = (TextView) findViewById(R.id.departureInput);
        TextView tvDescription = (TextView) findViewById(R.id.descriptionInput);

        tvName.setText(" " + tripCreator.getFullName());
        tvCarModel.setText(requestedTrip.getCar_model());
        tvCarNumber.setText(requestedTrip.getCar_number());
        tvFromPlace.setText(requestedTrip.getFrom_place());
        tvDescription.setText(requestedTrip.getDescription());
        tvStatus.setText(" " + requestedTrip.getTrip_status());
        tvWherePlace.setText(" > " + requestedTrip.getWhere_place());
        tvDepartureDate.setText(" " + requestedTrip.getDeparture_date());

        KPImageLoader.setImage(tripCreator.getProfile_image(),
                (ImageView) findViewById(R.id.profile_image), null, "");
    }

    private void toggleUserView(DataSnapshot dataSnapshot) {
        if (currentUser.getUid().equals(requestedTrip.getCreator_id())) {
            toggleCreatorPermissions(dataSnapshot);
        } else {
            toggleGuestPermissions();
        }
    }

    public void stepBack(View view) {
        Log.d(TAG, "onClick: navigation back");

        finish();
    }

    private void deleteTrip() {
        DatabaseReference tripRef = mRef.child(getString(R.string.db_user_trip)).child(tripId);
        tripRef.removeValue();

        finish();
    }

    private void startTrip() {
        requestedTrip.setTrip_status("ON ROAD");

        DatabaseReference tripRef = mRef.child(getString(R.string.db_user_trip)).child(tripId);
        tripRef.setValue(requestedTrip);

        finish();
    }

    private void requestTrip() {
        HashMap<String, String> requests = requestedTrip.getRequests() != null
                ? requestedTrip.getRequests()
                : new HashMap<String, String>();

        requests.put(currentUser.getUid(), "true");

        requestedTrip.setRequests(requests);

        DatabaseReference tripRef = mRef.child(getString(R.string.db_user_trip)).child(tripId);
        tripRef.setValue(requestedTrip);
    }

    private void lookTrip() {
        // TODO make maps ACTIVITY With Car location
    }

    private void toggleCreatorPermissions(DataSnapshot dataSnapshot) {
        Button actionButton = (Button) findViewById(R.id.requestButton);
        RelativeLayout requestsLayout = (RelativeLayout) findViewById(R.id.requestsLayout);

        if (!requestedTrip.getTrip_status().toUpperCase().equals("ON ROAD")
                && !requestedTrip.getTrip_status().toUpperCase().equals("PREPARED")) {
            actionButton.setBackgroundResource(R.drawable.kp_error_button_plain);
            actionButton.setText(R.string.trip_cancel);
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteTrip();
                }
            });

            initRequestsList(dataSnapshot);

            return;
        }

        requestsLayout.setVisibility(View.INVISIBLE);

        if (requestedTrip.getTrip_status().toUpperCase().equals("ON ROAD")) {
            actionButton.setBackgroundResource(R.drawable.kp_button_plain);
            actionButton.setText(R.string.trip_look_car);
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lookTrip();
                }
            });

            return;
        }

        actionButton.setVisibility(View.INVISIBLE);
    }

    private void toggleGuestPermissions() {
        Button actionButton = (Button) findViewById(R.id.requestButton);
        RelativeLayout requestsLayout = (RelativeLayout) findViewById(R.id.requestsLayout);
        requestsLayout.setVisibility(View.INVISIBLE);
        boolean requested = false;

        if (requestedTrip.getRequests() != null) {
                requested = requestedTrip.getRequests().containsKey(currentUser.getUid());
        }

        if (requestedTrip.getTrip_status().toUpperCase().equals("CREATED") && !requested) {
            actionButton.setBackgroundResource(R.drawable.kp_button_plain);
            actionButton.setText(R.string.trip_send_request);
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestTrip();
                }
            });

            return;
        }

        if (requestedTrip.getTrip_status().toUpperCase().equals("PREPARED") && requested) {
            DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

            try {
                if (new Date().equals(sdf.parse(requestedTrip.getDeparture_date()))) {
                    actionButton.setBackgroundResource(R.drawable.kp_button_plain);
                    actionButton.setText(R.string.trip_start);
                    actionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startTrip();
                        }
                    });
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return;
        }

        actionButton.setVisibility(View.INVISIBLE);
    }

    private void initRequestsList(DataSnapshot dataSnapshot) {
        final List<RequestListInfo> requestsList = new ArrayList<RequestListInfo>();
        final ListView requestListView = (ListView) findViewById(R.id.requestsListView);

        if (requestedTrip.getRequests() != null) {
            for (Map.Entry<String, String> entryString : requestedTrip.getRequests().entrySet()) {
                String userId = entryString.getKey();
                String status = entryString.getValue();

                if (status.equals("false")) {
                    continue;
                }

                RequestListInfo requestInfo = new RequestListInfo();
                requestInfo.setTrip_id(tripId);
                requestInfo.setUser_id(userId);

                UserAccount account = dataSnapshot
                        .child(getString(R.string.db_user_account)).child(requestInfo.getUser_id()).getValue(UserAccount.class);

                requestInfo.setImgUrl(account.getProfile_image());
                requestInfo.setUser_name(account.getSurname() + " " + account.getName());

                requestsList.add(requestInfo);
            }
        }

        KPRequestAdapter adapter = new KPRequestAdapter(this, R.layout.list_request, requestsList);

        requestListView.setAdapter(adapter);
        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RequestListInfo requestInfo = requestsList.get(position);

                toUserProfile(requestInfo.getUser_id());
            }
        });
    }

    private void toUserProfile(String userId) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("userID", userId);
        startActivity(intent);
    }

    public void rejectUser() {

    }

    public void acceptUser() {

    }
}
