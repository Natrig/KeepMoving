package rus.app.keepmoving;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import rus.app.keepmoving.Util.KPDatePicker;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    public void signIn(View view) {

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new KPDatePicker();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
