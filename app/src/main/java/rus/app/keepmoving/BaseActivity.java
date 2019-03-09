package rus.app.keepmoving;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

import rus.app.keepmoving.CreateTrip.CreateActivity;
import rus.app.keepmoving.Menu.MenuActivity;
import rus.app.keepmoving.Picker.PickerActivity;
import rus.app.keepmoving.Profile.ProfileActivity;
import rus.app.keepmoving.Search.SearchActivity;
import rus.app.keepmoving.Util.KPImageLoader;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    public void setupBottomNavBar(final Context context, int ActivityNum) {
        Log.d(TAG, "setupBottomNavBar: setting up Bottom nav bar");

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavBar);
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);

        Menu menu = bottomNavigationViewEx.getMenu();

        System.out.println("--------SADASDA  AS: " + ActivityNum + "-------------");
        MenuItem menuItem = menu.getItem(ActivityNum);
        menuItem.setChecked(true);

        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_profile:
                        Intent profileIntent = new Intent(context, ProfileActivity.class);
                        context.startActivity(profileIntent);
                        break;
                    case R.id.ic_trip:
                        Intent tripIntent = new Intent(context, MenuActivity.class);
                        context.startActivity(tripIntent);
                        break;
                    case R.id.ic_find_trip:
                        Intent findIntent = new Intent(context, PickerActivity.class);
                        findIntent.putExtra("ACTIVITY_NUM", SearchActivity.ACTIVITY_NUM);
                        context.startActivity(findIntent);
                        break;
                    case R.id.ic_create_trip:
                        Intent createIntent = new Intent(context, PickerActivity.class);
                        createIntent.putExtra("ACTIVITY_NUM", CreateActivity.ACTIVITY_NUM);
                        context.startActivity(createIntent);
                        break;
                }

                return false;
            }
        });
    }

    protected void initImageLoader() {
        KPImageLoader kpImageLoader = new  KPImageLoader(BaseActivity.this);
        ImageLoader.getInstance().init(kpImageLoader.getConfig());
    }

}