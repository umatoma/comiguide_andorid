package net.umatoma.comiguide.activity;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.adapter.ComiketCircleArrayAdapter;
import net.umatoma.comiguide.fragment.ComiketCircleFormFragment;
import net.umatoma.comiguide.fragment.ComiketCircleListFragment;
import net.umatoma.comiguide.fragment.ComiketCircleMapFragment;
import net.umatoma.comiguide.fragment.OnComiketCircleCreateListener;
import net.umatoma.comiguide.fragment.OnComiketCircleUpdateListener;
import net.umatoma.comiguide.model.ComiketCircle;
import net.umatoma.comiguide.model.ComiketLayout;
import net.umatoma.comiguide.model.User;
import net.umatoma.comiguide.view.MapImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ComiketCircleActivity extends ActionBarActivity
        implements ComiketCircleMapFragment.OnFragmentInteractionListener,
            ComiketCircleListFragment.OnFragmentInteractionListener,
            OnComiketCircleUpdateListener, OnComiketCircleCreateListener {

    private int mComiketId = 87;
    private int mCmapId = 1;
    private int mDay = 1;
    private DrawerLayout mDrawerLayout;
    private ComiketCircleArrayAdapter mCircleArrayAdapter;
    private LoadComiketCirclesTask mLoadComiketCirclesTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comiket_circle);

        mCircleArrayAdapter = new ComiketCircleArrayAdapter(this);
        mLoadComiketCirclesTask = new LoadComiketCirclesTask(this);
        mLoadComiketCirclesTask.execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content_frame,
                new ComiketCircleMapFragment(), ComiketCircleMapFragment.TAG);
        transaction.replace(R.id.left_drawer,
                new ComiketCircleListFragment(mCircleArrayAdapter), ComiketCircleListFragment.TAG);
        transaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_comiket_circle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            FragmentManager manager = getSupportFragmentManager();
            Fragment fragment = manager.findFragmentByTag(ComiketCircleFormFragment.TAG);
            if (fragment != null) {
                manager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .remove(fragment)
                        .commit();
                return false;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onStop() {
        mLoadComiketCirclesTask = null;
        super.onStop();
    }

    @Override
    public void onFunctionsButtonClicke(int id) {
        switch (id) {
            case R.id.button_circle_list:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                return;
            case R.id.button_create_circle:
                showComiketCircleCreateForm();
                return;
        }
    }

    @Override
    public void onComiketCircleSelected(ComiketCircle circle) {
        FragmentManager manager = getSupportFragmentManager();
        ComiketCircleMapFragment fragment
                = (ComiketCircleMapFragment) manager.findFragmentByTag(ComiketCircleMapFragment.TAG);

        if (fragment != null) {
            MapImageView mapImageView = (MapImageView) findViewById(R.id.circle_map);
            ComiketLayout layout = circle.getComiketLayout();
            float dx = (float) layout.getPosX();
            float dy = (float) layout.getPosY();
            mapImageView.setCurrentPosition(dx, dy);

            fragment.setComiketCircle(circle);
            fragment.showFooterView();
        } else {
            // On edit or create circle mode.
        }

        mDrawerLayout.closeDrawers();
    }

    @Override
    public void onFooterViewClick(ComiketCircle circle) {
        ComiketCircleFormFragment fragment = new ComiketCircleFormFragment(circle)
                .setOnComiketCircleUpdateListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.content_frame, fragment, ComiketCircleFormFragment.TAG)
                .commit();
    }

    @Override
    public void onComiketCircleUpdate(ComiketCircle circle) {
        mCircleArrayAdapter.updateItem(circle);

        FragmentManager manager = getSupportFragmentManager();
        ComiketCircleMapFragment fragment
                = (ComiketCircleMapFragment) manager.findFragmentByTag(ComiketCircleMapFragment.TAG);
        if (fragment != null) {
            fragment.setComiketCircle(circle);
            fragment.showFooterView();
        }
    }

    @Override
    public void onComiketCircleCreate(ComiketCircle circle) {
        mCircleArrayAdapter.add(circle);
    }

    private void showComiketCircleCreateForm() {
        ComiketCircleFormFragment fragment = new ComiketCircleFormFragment(mComiketId, mDay, mCmapId);
        fragment.setOnComiketCircleCreateListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.content_frame, fragment, ComiketCircleFormFragment.TAG)
                .commit();
    }

    private class LoadComiketCirclesTask extends AsyncTask<Void, Void, JSONObject> {

        private User mUser;

        public LoadComiketCirclesTask(Context context) {
            mUser = new User(context);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            int comiketId = 87;
            int cMapId = 1;
            String apiToken = mUser.getApiToken();
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .authority("comiguide.net")
                    .path(String.format("api/v1/comikets/%d/ccircle_checklists.json", comiketId))
                    .appendQueryParameter("cmap_id", String.valueOf(cMapId))
                    .build();
            Request request = new Request.Builder()
                    .url(uri.toString())
                    .addHeader("X-Comiguide-Api-Token", apiToken)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return new JSONObject(response.body().string());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (result != null) {
                try {
                    JSONArray comiketCircles = result.getJSONArray("ccircle_checklists");
                    int length = comiketCircles.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject comiketCircle = comiketCircles.getJSONObject(i);
                        mCircleArrayAdapter.add(new ComiketCircle(comiketCircle));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(ComiketCircleActivity.this, "Fail to load...", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mLoadComiketCirclesTask = null;
        }
    }
}
