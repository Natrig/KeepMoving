package rus.app.keepmoving.TripDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import rus.app.keepmoving.Picker.PickerActivity;
import rus.app.keepmoving.R;

public class TripDetailsActivity extends AppCompatActivity {

    private static final String TAG = "PickerActivity";

    private int PREV_ACTIVITY_NUM = 0;

    List<EditText> fields;
    private EditText mCarModelField;
    private EditText mCarNumberField;
    private EditText mDescriptionField;

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

//        mCarModelField = findViewById();
//        fields.add(mCarModelField);
//
//        mCarNumberField = findViewById();
//        fields.add(mCarNumberField);
//
//        mDescriptionField = findViewById();
//        fields.add(mDescriptionField);
    }

    public void stepBack(View view) {
        Log.d(TAG, "onClick: navigation back");
        Intent intent = new Intent(this, PickerActivity.class);
        intent.putExtra("from_place", fromPlace);
        intent.putExtra("where_place", wherePlace);
        intent.putExtra("departure_date", departureDate);
        startActivity(intent);
        finish();
    }


    @Override
    public void onStart() {
        super.onStart();

        initTripDetails();
    }

    public void initTripDetails() {
        Intent intent = getIntent();

        PREV_ACTIVITY_NUM =
                intent.hasExtra("ACTIVITY_NUM")
                        ? intent.getExtras().getInt("ACTIVITY_NUM")
                        : PREV_ACTIVITY_NUM;

        if (intent.hasExtra("departure_date")) {
            departureDate = intent.getExtras().getString("departure_date");
        }

        if (intent.hasExtra("from_place")) {
            fromPlace = intent.getExtras().getString("from_place");
        }

        if (intent.hasExtra("where_place")) {
            wherePlace = intent.getExtras().getString("where_place");
        }
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
