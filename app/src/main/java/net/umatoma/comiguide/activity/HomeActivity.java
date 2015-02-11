package net.umatoma.comiguide.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.model.User;

public class HomeActivity extends ActionBarActivity {

    private User mUser;
    private ImageView mUserIcon;
    private TextView mUserName;
    private ListView mNotificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mUser = new User(this);
        mUserIcon = (ImageView) findViewById(R.id.userIcon);
        mUserName = (TextView) findViewById(R.id.userName);
        mNotificationList = (ListView) findViewById(R.id.notificationList);

        mUserName.setText(mUser.getUserName());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
