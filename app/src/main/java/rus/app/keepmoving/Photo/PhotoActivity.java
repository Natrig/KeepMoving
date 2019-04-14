package rus.app.keepmoving.Photo;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import rus.app.keepmoving.R;
import rus.app.keepmoving.Util.KPPhotoPermissions;
import rus.app.keepmoving.Util.SectionsPagerAdapter;

public class PhotoActivity extends AppCompatActivity {
    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        if (checkPermissionsArray(KPPhotoPermissions.PERMISSIONS)) {
            setupViewPager();
        } else {
            verifyPermissions(KPPhotoPermissions.PERMISSIONS);
        }
    }

    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new GalleryFragment());
        adapter.addFragment(new PhotoFragment());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsBottom);

        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText(getString(R.string.gallery));
        tabLayout.getTabAt(1).setText(getString(R.string.camera));
    }

    public void verifyPermissions(String [] permissions) {
        ActivityCompat.requestPermissions(
                PhotoActivity.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    public boolean checkPermissionsArray(String [] permissions) {
        for (String permission : permissions) {
            if (!checkSinglePermission(permission)) {
                return false;
            }
        }

        return true;
    }

    public boolean checkSinglePermission(String permission) {
        int permissionRequest = ActivityCompat.checkSelfPermission(PhotoActivity.this, permission);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        return true;
    }
}
