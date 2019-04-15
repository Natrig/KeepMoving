package rus.app.keepmoving.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import rus.app.keepmoving.BaseActivity;
import rus.app.keepmoving.EditProfile.EditProfileActivity;
import rus.app.keepmoving.Entities.UserAccount;
import rus.app.keepmoving.Login.LoginActivity;
import rus.app.keepmoving.R;
import rus.app.keepmoving.Util.KPImageLoader;

public class ProfileActivity extends BaseActivity {
    private static final int ACTIVITY_NUM = 3;
    private static final String TAG = "ProfileActivity";

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    private UserAccount userAccount;

    private String userID;
    private TextView mNameField;
    private TextView mSurnameField;
    private TextView mEmailField;
    private TextView mBirthField;
    private TextView mPhoneField;
    private TextView mDescriptionField;
    private ImageView mImageField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Log.d(TAG, "onCreate: creating.");

        mNameField = (TextView) findViewById(R.id.textName);
        mEmailField = (TextView) findViewById(R.id.email_label);
        mBirthField = (TextView) findViewById(R.id.birth_label);
        mPhoneField = (TextView) findViewById(R.id.phone_label);
        mSurnameField = (TextView) findViewById(R.id.textSurname);
        mImageField = (ImageView) findViewById(R.id.profile_image);
        mDescriptionField = (TextView) findViewById(R.id.description_label);

        setupBottomNavBar(ProfileActivity.this, ACTIVITY_NUM);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();

        Intent intent = getIntent();

        userID = intent.hasExtra("userID")
                ? intent.getExtras().getString("userID")
                : currentUser.getUid();

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

        if (!userID.equals(currentUser.getUid())) {
            TextView editBtn = (TextView) findViewById(R.id.textEdit);
            editBtn.setVisibility(View.INVISIBLE);

            RelativeLayout exitBtnLayout = (RelativeLayout) findViewById(R.id.exitLayout);
            exitBtnLayout.setVisibility(View.INVISIBLE);
        }

        UserAccount userSnapshot = dataSnapshot
                .child(getString(R.string.db_user_account)).child(userID).getValue(UserAccount.class);

        userAccount.setName(userSnapshot.getName());
        userAccount.setEmail(userSnapshot.getEmail());
        userAccount.setPhone(userSnapshot.getPhone());
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
        mEmailField.setText(userAccount.getEmail());
        mBirthField.setText(userAccount.getBirth());
        mSurnameField.setText(userAccount.getSurname());
        mDescriptionField.setText(userAccount.getDescription());
        KPImageLoader.setImage(userAccount.getProfile_image(), mImageField, null, "");
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
