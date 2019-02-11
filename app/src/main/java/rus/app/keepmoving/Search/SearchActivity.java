package rus.app.keepmoving.Search;

import android.os.Bundle;
import android.util.Log;

import rus.app.keepmoving.BaseActivity;
import rus.app.keepmoving.R;

public class SearchActivity extends BaseActivity {
    private static final int ACTIVITY_NUM = 1;
    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Log.d(TAG, "onCreate: creating.");

        setupBottomNavBar(SearchActivity.this, ACTIVITY_NUM);
    }
}
