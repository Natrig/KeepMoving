package rus.app.keepmoving.Picker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rus.app.keepmoving.CreateTrip.CreateActivity;
import rus.app.keepmoving.Map.MapsActivity;
import rus.app.keepmoving.R;
import rus.app.keepmoving.Search.SearchActivity;
import rus.app.keepmoving.TripDetails.TripDetailsActivity;
import rus.app.keepmoving.Trips.TripsActivity;
import rus.app.keepmoving.Util.KPDatePicker;

public class PickerActivity extends AppCompatActivity {
    private static final String TAG = "PickerActivity";

    private int PREV_ACTIVITY_NUM = 0;

    List<EditText> fields;
    private EditText mDepartureDateField;
    private EditText mFromField;
    private EditText mWhereField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        ImageView backIcon = (ImageView) findViewById(R.id.backIcon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigation back");
                finish();
            }
        });

        fields = new ArrayList<>();

        mDepartureDateField = findViewById(R.id.yearInput);
        fields.add(mDepartureDateField);

        mFromField = findViewById(R.id.fromPlaceInput);
        fields.add(mFromField);

        mWhereField = findViewById(R.id.wherePlaceInput);
        fields.add(mWhereField);
    }

    @Override
    public void onStart() {
        super.onStart();

        initPicker();
    }

    public void initPicker() {
        Intent intent = getIntent();

        PREV_ACTIVITY_NUM =
                intent.hasExtra("ACTIVITY_NUM")
                        ? intent.getExtras().getInt("ACTIVITY_NUM")
                        : PREV_ACTIVITY_NUM;

        if (intent.hasExtra("departure_date")) {
            mDepartureDateField.setText(intent.getExtras().getString("departure_date"));
        }

        if (intent.hasExtra("from_place")) {
            mFromField.setText(intent.getExtras().getString("from_place"));
        }

        if (intent.hasExtra("where_place")) {
            mWhereField.setText(intent.getExtras().getString("where_place"));
        }
    }

    public void showDatePickerDialog(View view) {
        DialogFragment datePickerFragment = new KPDatePicker();

        if (PREV_ACTIVITY_NUM == CreateActivity.ACTIVITY_NUM) {
            ((KPDatePicker) datePickerFragment).minDate = new Date();
        }

        ((KPDatePicker) datePickerFragment).startFrom = new Date();

        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showFromPlaceMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("selector_status", "from_place");
        startActivityForResult(intent, 33);
    }

    public void showWherePlaceMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("selector_status", "where_place");
        startActivityForResult(intent, 22);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 33:
                    mFromField.setText(data.getStringExtra("locality"));
                    break;
                case 22:
                    mWhereField.setText(data.getStringExtra("locality"));
                    break;
            }
        }
    }

    public void goNext(View view) {
        if (!validateForm()) {
            return;
        }

        Intent intent = null;
        if (PREV_ACTIVITY_NUM == CreateActivity.ACTIVITY_NUM) {
            intent = new Intent(this, TripDetailsActivity.class);
        } else if (PREV_ACTIVITY_NUM == SearchActivity.ACTIVITY_NUM) {
            intent = new Intent(this, TripsActivity.class);
        }

        intent.putExtra("from_place", mFromField.getText().toString());
        intent.putExtra("where_place", mWhereField.getText().toString());
        intent.putExtra("departure_date", mDepartureDateField.getText().toString());
        startActivity(intent);
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
