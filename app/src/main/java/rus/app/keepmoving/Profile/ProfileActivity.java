package rus.app.keepmoving.Profile;

import android.os.Bundle;
import android.util.Log;

import rus.app.keepmoving.BaseActivity;
import rus.app.keepmoving.R;

public class ProfileActivity extends BaseActivity {
    private static final int ACTIVITY_NUM = 3;
    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Log.d(TAG, "onCreate: creating.");

        setupBottomNavBar(ProfileActivity.this, ACTIVITY_NUM);
    }
}
