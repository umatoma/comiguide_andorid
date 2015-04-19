package net.umatoma.comiguide.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import net.umatoma.comiguide.R;

public class WebViewActivity extends ActionBarActivity {

    public static final String IKEY_TITLE = "title";
    public static final String IKEY_FILE_PATH = "file_path";;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();
        if (!intent.hasExtra(IKEY_FILE_PATH)) {
            Toast.makeText(this, "File path is missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!intent.hasExtra(IKEY_TITLE)) {
            Toast.makeText(this, "Title is missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        setSupportActionBar((Toolbar) findViewById(R.id.tool_bar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(intent.getStringExtra(IKEY_TITLE));
        actionBar.setDisplayHomeAsUpEnabled(true);

        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.loadUrl(intent.getStringExtra(IKEY_FILE_PATH));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
