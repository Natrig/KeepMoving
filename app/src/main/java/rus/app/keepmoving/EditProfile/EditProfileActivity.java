package rus.app.keepmoving.EditProfile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import rus.app.keepmoving.BaseActivity;
import rus.app.keepmoving.Entities.UserAccount;
import rus.app.keepmoving.Profile.ProfileActivity;
import rus.app.keepmoving.R;
import rus.app.keepmoving.Util.KPImageLoader;

public class EditProfileActivity extends BaseActivity {
    private static final String TAG = "EditProfileActivity";

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    private UserAccount userAccount;

    List<EditText> fields;
    private EditText mNameField;
    private EditText mSurnameField;
    private EditText mBirthField;
    private EditText mPhoneField;
    private EditText mDescriptionField;
    private ImageView mImageField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_edit_profile);

        fields = new ArrayList<>();

        mNameField = (EditText) findViewById(R.id.nameInput);
        fields.add(mNameField);

        mSurnameField = (EditText) findViewById(R.id.surnameInput);
        fields.add(mNameField);

        mBirthField = (EditText) findViewById(R.id.yearInput);
        fields.add(mNameField);

        mPhoneField = (EditText) findViewById(R.id.phoneInput);
        fields.add(mNameField);

        mDescriptionField = (EditText) findViewById(R.id.descriptionInput);
        fields.add(mNameField);

        mImageField = (ImageView) findViewById(R.id.profile_image);
        fields.add(mNameField);

        ImageView backIcon = (ImageView) findViewById(R.id.backIcon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigation back to ProfileActivity");
                finish();
            }
        });

        setProfileImage();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();

        // Read from the database
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("entered");
                getUserInfo(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void getUserInfo(DataSnapshot dataSnapshot) {
        userAccount = new UserAccount();
        DataSnapshot userSnapshot = dataSnapshot
                .child(getString(R.string.db_user_account)).child(currentUser.getUid());

        userAccount.setEmail(userSnapshot.getValue(UserAccount.class).getEmail());
        userAccount.setName(userSnapshot.getValue(UserAccount.class).getName());
        userAccount.setPhone(userSnapshot.getValue(UserAccount.class).getPhone());
        userAccount.setSurname(userSnapshot.getValue(UserAccount.class).getSurname());
        userAccount.setProfile_image(userSnapshot.getValue(UserAccount.class).getProfile_image());
        userAccount.setDescription(userSnapshot.getValue(UserAccount.class).getDescription());
        userAccount.setUser_id(userSnapshot.getValue(UserAccount.class).getUser_id());
        userAccount.setBirth(userSnapshot.getValue(UserAccount.class).getBirth());

        initProfileUI();
    }

    public void initProfileUI() {
        mNameField.setText(userAccount.getName());
        mSurnameField.setText(userAccount.getSurname());
        mDescriptionField.setText(userAccount.getDescription());
        mPhoneField.setText(userAccount.getPhone());
        mBirthField.setText(userAccount.getBirth());
    }

    // TODO :: SET CORRECT IMG
    private void setProfileImage() {
        Log.d(TAG, "set Profile image: setting profile image");
        String imgUrl = "https://ih1.redbubble.net/image.159500319.3826/flat,750x1000,075,t.u5.jpg";

        KPImageLoader.setImage(imgUrl, mImageField, null, "");
    }

    public void doEdit(View view) {
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        userAccount.setName(mNameField.getText().toString());
        userAccount.setPhone(mPhoneField.getText().toString());
        userAccount.setSurname(mSurnameField.getText().toString());
        userAccount.setProfile_image("https://ih1.redbubble.net/image.159500319.3826/flat,750x1000,075,t.u5.jpg");
        userAccount.setDescription(mDescriptionField.getText().toString());
        userAccount.setBirth(mBirthField.getText().toString());

        mRef.child(getString(R.string.db_user_account)).child(userAccount.getUser_id()).setValue(userAccount);

        hideProgressDialog();

        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("userID", userAccount.getUser_id());
        startActivity(intent);
        finish();
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
