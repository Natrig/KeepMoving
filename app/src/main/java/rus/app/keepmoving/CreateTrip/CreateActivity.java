package rus.app.keepmoving.CreateTrip;

import android.os.Bundle;
import android.util.Log;

import rus.app.keepmoving.BaseActivity;
import rus.app.keepmoving.R;

public class CreateActivity extends BaseActivity {
    public static final int ACTIVITY_NUM = 2;
    private static final String TAG = "CreateActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Log.d(TAG, "onCreate: creating.");

        setupBottomNavBar(CreateActivity.this, ACTIVITY_NUM);
    }
}
