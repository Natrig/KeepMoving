package rus.app.keepmoving.Util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import rus.app.keepmoving.R;

public class KPFirebase {
    private static final String TAG = "KPFirebase";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;

    private Context mContext;

    public KPFirebase(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mContext = context;

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    public void registerNewEmail(final String email, String password ) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "onComplete: " + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, R.string.failed, Toast.LENGTH_SHORT).show();
                        }
                        else if (task.isSuccessful()) {
                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onComplete: Authstate changed: " + userID);
                        }
                    }
                });
    }
}
