package rus.app.keepmoving.EditProfile;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;

import rus.app.keepmoving.BaseActivity;
import rus.app.keepmoving.R;
import rus.app.keepmoving.Util.KPImageLoader;

public class EditProfileActivity extends BaseActivity {
    private static final String TAG = "EditProfileActivity";
    private ImageView profileImage;

    List<EditText> fields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_edit_profile);

        profileImage = (ImageView) findViewById(R.id.profile_image);

        ImageView backIcon = (ImageView) findViewById(R.id.backIcon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigation back to ProfileActivity");

                finish();
            }
        });

        setProfileImage();
    }

    private void setProfileImage() {
        Log.d(TAG, "set Profile image: setting profile image");
        String imgUrl = "https://ih1.redbubble.net/image.159500319.3826/flat,750x1000,075,t.u5.jpg";

        KPImageLoader.setImage(imgUrl, profileImage, null, "");
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
