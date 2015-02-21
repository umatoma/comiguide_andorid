package net.umatoma.comiguide.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import net.umatoma.comiguide.R;
import net.umatoma.comiguide.model.User;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent;
        if (User.isLoggedIn(this)) {
            intent = new Intent(this, HomeActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
