package rus.app.keepmoving.Menu;

import android.os.Bundle;
import android.util.Log;

import rus.app.keepmoving.BaseActivity;
import rus.app.keepmoving.R;

public class MenuActivity extends BaseActivity {
    private static final int ACTIVITY_NUM = 0;
    private static final String TAG = "MenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Log.d(TAG, "onCreate: creating.");

        setupBottomNavBar(MenuActivity.this, ACTIVITY_NUM);
    }
}
