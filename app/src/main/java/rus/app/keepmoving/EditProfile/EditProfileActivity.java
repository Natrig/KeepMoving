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
import rus.app.keepmoving.Photo.PhotoActivity;
import rus.app.keepmoving.Profile.ProfileActivity;
import rus.app.keepmoving.R;
import rus.app.keepmoving.Util.KPImageLoader;

public class EditProfileActivity extends BaseActivity {
    private static final String TAG = "EditProfileActivity";

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mRef;

    private UserAccount userAccount;

    List<EditText> fields;
    private EditText mNameField;
    private EditText mBirthField;
    private EditText mPhoneField;
    private ImageView mImageField;
    private EditText mSurnameField;
    private EditText mDescriptionField;

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

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();

        // Read from the database
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
        UserAccount userSnapshot = dataSnapshot
                .child(getString(R.string.db_user_account)).child(currentUser.getUid()).getValue(UserAccount.class);

        userAccount.setName(userSnapshot.getName());
        userAccount.setPhone(userSnapshot.getPhone());
        userAccount.setEmail(userSnapshot.getEmail());
        userAccount.setBirth(userSnapshot.getBirth());
        userAccount.setSurname(userSnapshot.getSurname());
        userAccount.setUser_id(userSnapshot.getUser_id());
        userAccount.setDescription(userSnapshot.getDescription());
        userAccount.setProfile_image(userSnapshot.getProfile_image());

        initProfileUI();
    }

    public void initProfileUI() {
        mNameField.setText(userAccount.getName());
        mPhoneField.setText(userAccount.getPhone());
        mBirthField.setText(userAccount.getBirth());
        mSurnameField.setText(userAccount.getSurname());
        mDescriptionField.setText(userAccount.getDescription());
        KPImageLoader.setImage(userAccount.getProfile_image(), mImageField, null, "");
    }

    public void doEdit(View view) {
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        userAccount.setName(mNameField.getText().toString());
        userAccount.setPhone(mPhoneField.getText().toString());
        userAccount.setBirth(mBirthField.getText().toString());
        userAccount.setSurname(mSurnameField.getText().toString());
        userAccount.setDescription(mDescriptionField.getText().toString());

        mRef.child(getString(R.string.db_user_account)).child(userAccount.getUser_id()).setValue(userAccount);

        hideProgressDialog();

        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("userID", userAccount.getUser_id());
        startActivity(intent);
        finish();
    }

    public void toPhotoActivity(View view) {
        Intent intent = new Intent(this, PhotoActivity.class);

        startActivity(intent);
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
