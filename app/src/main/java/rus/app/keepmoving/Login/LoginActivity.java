package rus.app.keepmoving.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.List;

import rus.app.keepmoving.BaseActivity;
import rus.app.keepmoving.Menu.MenuActivity;
import rus.app.keepmoving.R;
import rus.app.keepmoving.SignUp.SignUpActivity;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;

    List<EditText> fields;
    private EditText mEmailField;
    private EditText mPasswordField;

    public final static String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "onCreate: creating.");

        fields = new ArrayList<EditText>();

        mEmailField = findViewById(R.id.emailInput);
        fields.add(mEmailField);

        mPasswordField = findViewById(R.id.passwordInput);
        fields.add(mPasswordField);

        mAuth = FirebaseAuth.getInstance();
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

    public void login(View view) {
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(mEmailField.getText().toString(), mPasswordField.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onStart();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    R.string.failed + " : " +task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        hideProgressDialog();
                    }
                });
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

    public void goSignIn(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);

        startActivity(intent);
    }
}
