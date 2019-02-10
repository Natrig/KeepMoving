package rus.app.keepmoving;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import rus.app.keepmoving.Util.KPDatePicker;

public class SignUpActivity extends BaseActivity {
    private FirebaseAuth mAuth;

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

        fields = new ArrayList<EditText>();

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
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // TODO
//            Intent intent = new Intent(this, MenuActivity.class);
//            startActivity(intent);
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
                            saveUserInfo();
                            onStart();
                        } else {
                            Toast.makeText(SignUpActivity.this,
                                    "Не удалось создать аккаунт", Toast.LENGTH_SHORT).show();
                        }

                        hideProgressDialog();
                    }
                });
    }

    public void saveUserInfo() {

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
