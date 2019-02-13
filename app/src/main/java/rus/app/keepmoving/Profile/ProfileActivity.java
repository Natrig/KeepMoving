package rus.app.keepmoving.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import rus.app.keepmoving.BaseActivity;
import rus.app.keepmoving.EditProfile.EditProfileActivity;
import rus.app.keepmoving.R;
import rus.app.keepmoving.Util.KPImageLoader;

public class ProfileActivity extends BaseActivity {
    private static final int ACTIVITY_NUM = 3;
    private static final String TAG = "ProfileActivity";

    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Log.d(TAG, "onCreate: creating.");

        profileImage = (ImageView) findViewById(R.id.profile_image);

        setupBottomNavBar(ProfileActivity.this, ACTIVITY_NUM);
        setProfileImage();
    }

    private void setProfileImage() {
        Log.d(TAG, "setProfileImage: setting...");
        String imgUrl = "https://ih1.redbubble.net/image.159500319.3826/flat,750x1000,075,t.u5.jpg";

        KPImageLoader.setImage(imgUrl, profileImage, null, "");
    }

    public void goEdit(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }
}
