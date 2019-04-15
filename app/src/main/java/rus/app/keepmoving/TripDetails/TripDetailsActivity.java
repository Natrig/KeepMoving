package rus.app.keepmoving.TripDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rus.app.keepmoving.Entities.Trip;
import rus.app.keepmoving.R;
import rus.app.keepmoving.TripProfile.TripProfileActivity;

public class TripDetailsActivity extends AppCompatActivity {
    private static final String TAG = "PickerActivity";

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mRef;

    List<EditText> fields;
    private EditText mCarModelField;
    private EditText mCarNumberField;
    private EditText mDescriptionField;

    private Trip mTrip;
    private String fromPlace;
    private String wherePlace;
    private String departureDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        ImageView backIcon = (ImageView) findViewById(R.id.backIcon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepBack(view);
            }
        });

        fields = new ArrayList<>();

        mCarModelField = findViewById(R.id.carModelInput);
        fields.add(mCarModelField);

        mCarNumberField = findViewById(R.id.carNumberInput);
        fields.add(mCarNumberField);

        mDescriptionField = findViewById(R.id.tripDescriptionInput);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();

        initTripDetails();
    }

    public void initTripDetails() {
        mTrip = new Trip();
        Intent intent = getIntent();

        if (intent.hasExtra("departure_date")) {
            departureDate = intent.getExtras().getString("departure_date");
            mTrip.setDeparture_date(departureDate);
        }

        if (intent.hasExtra("from_place")) {
            fromPlace = intent.getExtras().getString("from_place");
            mTrip.setFrom_place(fromPlace);
        }

        if (intent.hasExtra("where_place")) {
            wherePlace = intent.getExtras().getString("where_place");
            mTrip.setWhere_place(wherePlace);
        }
    }

    public void goNext(View view) {
        if (!validateForm()) {
            return;
        }

        mTrip.setTrip_status("CREATED");
        mTrip.setCreator_id(currentUser.getUid());
        mTrip.setRequests(new HashMap<String, String>());
        mTrip.setCar_model(mCarModelField.getText().toString());
        mTrip.setCar_number(mCarNumberField.getText().toString());
        mTrip.setDescription(mDescriptionField.getText().toString());

        DatabaseReference tripRef = mRef.child(getString(R.string.db_user_trip)).push();
        tripRef.setValue(mTrip);

        toTripProfile(tripRef.getKey());
    }

    private void toTripProfile(String tripId) {
        Intent intent = new Intent(this, TripProfileActivity.class);
        intent.putExtra("trip_id", tripId);
        startActivity(intent);
        finish();
    }

    public void stepBack(View view) {
        Log.d(TAG, "onClick: navigation back");
        finish();
    }

    private boolean validateForm() {
        boolean valid = true;

        for (EditText field : fields) {
            String fieldValue = field.getText().toString();

            if (TextUtils.isEmpty(fieldValue)) {
                field.setError("Обязательное поле.");
                valid = false;
            } else {
                field.setError(null);
            }
        }

        return valid;
    }
}
