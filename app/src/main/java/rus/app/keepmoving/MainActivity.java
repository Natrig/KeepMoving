package rus.app.keepmoving;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Метод авторизации
    public void login(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        EditText editText = findViewById(R.id.emailInput);

        String message = editText.getText().toString();

        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void goSignIn(View view) {
        Intent intent = new Intent(this, SignInActivity.class);

        startActivity(intent);
    }
}
