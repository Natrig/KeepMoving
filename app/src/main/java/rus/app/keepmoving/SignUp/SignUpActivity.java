package rus.app.keepmoving.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import rus.app.keepmoving.BaseActivity;
import rus.app.keepmoving.Entities.UserAccount;
import rus.app.keepmoving.Menu.MenuActivity;
import rus.app.keepmoving.R;
import rus.app.keepmoving.Util.KPDatePicker;

public class SignUpActivity extends BaseActivity {
    private static final String TAG = "SignUpActivity";

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    List<EditText> fields;
    private EditText mNameField;
    private EditText mSurnameField;
    private EditText mEmailField;
    private EditText mBirthField;
    private EditText mPhoneField;
    private EditText mConfirmField;
    private EditText mPasswordField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Log.d(TAG, "onCreate: creating.");

        fields = new ArrayList<>();

        mSurnameField = findViewById(R.id.surnameInput);
        fields.add(mSurnameField);

        mNameField = findViewById(R.id.nameInput);
        fields.add(mNameField);

        mEmailField = findViewById(R.id.emailInput);
        fields.add(mEmailField);

        mBirthField = findViewById(R.id.yearInput);
        fields.add(mBirthField);

        mPhoneField = findViewById(R.id.phoneInput);
        fields.add(mPhoneField);

        mPasswordField = findViewById(R.id.passwordInput);
        fields.add(mPasswordField);

        mConfirmField = findViewById(R.id.confirmationInput);
        fields.add(mConfirmField);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void signUp(View view) {
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(mEmailField.getText().toString(), mPasswordField.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserInfo(mAuth.getCurrentUser().getUid());
                            onStart();
                        } else {
                            Toast.makeText(SignUpActivity.this,
                                    "Не удалось создать аккаунт: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }

                        hideProgressDialog();
                    }
                });
    }

    public void saveUserInfo(String userID) {
        UserAccount userAccount = new UserAccount();
        userAccount.setEmail(mEmailField.getText().toString());
        userAccount.setName(mNameField.getText().toString());
        userAccount.setPhone(mPhoneField.getText().toString());
        userAccount.setSurname(mSurnameField.getText().toString());
        userAccount.setProfile_image("none");
        userAccount.setDescription("no description");
        userAccount.setUser_id(userID);
        userAccount.setBirth(mBirthField.getText().toString());

        System.out.println(userAccount.toString());
        System.out.println(userID);

        mRef.child(getString(R.string.db_user_account)).child(userID).setValue(userAccount);
    }

    private boolean validateForm() {
        boolean valid = true;

        for (EditText field : fields) {
            String fieldValue = field.getText().toString();

            switch (field.getId()) {
                case R.id.confirmationInput:
                    if (!fieldValue.equals(mPasswordField.getText().toString())) {
                        field.setError("Пароли должны совпадать.");
                        valid = false;
                    } else {
                        field.setError(null);
                    }
            }

            if (TextUtils.isEmpty(fieldValue)) {
                field.setError("Обязательное поле.");
                valid = false;
            } else {
                field.setError(null);
            }
        }

        return valid;
    }

    public void showDatePickerDialog(View v) {
        DialogFragment datePickerFragment = new KPDatePicker();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
