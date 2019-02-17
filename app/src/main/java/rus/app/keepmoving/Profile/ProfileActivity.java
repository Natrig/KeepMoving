package rus.app.keepmoving.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import rus.app.keepmoving.BaseActivity;
import rus.app.keepmoving.EditProfile.EditProfileActivity;
import rus.app.keepmoving.Login.LoginActivity;
import rus.app.keepmoving.R;
import rus.app.keepmoving.Util.KPImageLoader;

public class ProfileActivity extends BaseActivity {
    private static final int ACTIVITY_NUM = 3;
    private static final String TAG = "ProfileActivity";

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Log.d(TAG, "onCreate: creating.");

        profileImage = (ImageView) findViewById(R.id.profile_image);

        setupBottomNavBar(ProfileActivity.this, ACTIVITY_NUM);
        setProfileImage();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
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

    public void logout(View view) {
        mAuth.signOut();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
