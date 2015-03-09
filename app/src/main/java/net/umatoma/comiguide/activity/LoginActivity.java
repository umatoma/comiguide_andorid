package net.umatoma.comiguide.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.api.ComiGuideApiClient;
import net.umatoma.comiguide.model.User;
import net.umatoma.comiguide.validator.EmailValidator;
import net.umatoma.comiguide.validator.EmptyValidator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private ComiGuideApiClient.HttpClientTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (!isEmailValid(email)) {
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            RequestBody formBody = new FormEncodingBuilder()
                    .add("user[email]", email)
                    .add("user[password]", password)
                    .build();
            mAuthTask = new ComiGuideApiClient(this).callPostTask("api/v1/users/sign_in", formBody);
            mAuthTask.setOnHttpClientPostExecuteListener(new ComiGuideApiClient.OnHttpClientPostExecuteListener() {
                @Override
                public void onSuccess(JSONObject result) {
                    mAuthTask = null;
                    showProgress(false);

                    try {
                        JSONObject apiTokenObject = result.getJSONObject("api_token");
                        JSONObject userObject = result.getJSONObject("user");

                        User user = new User(LoginActivity.this);
                        user.setApiToken(apiTokenObject.getString("token"));
                        user.setUserId(userObject.getInt("id"));
                        user.setUserName(userObject.getString("username"));
                        user.save();

                        Toast.makeText(LoginActivity.this,
                                getString(R.string.success_login), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFail() {
                    mAuthTask = null;
                    showProgress(false);

                    Toast.makeText(LoginActivity.this,
                            getString(R.string.error_login_fail), Toast.LENGTH_SHORT).show();
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }
            });
            mAuthTask.setApiToken(false);
            mAuthTask.execute();
        }
    }

    private boolean isEmailValid(String email) {
        EmptyValidator emptyValidator = new EmptyValidator(this, email);
        if (!emptyValidator.isValid()) {
            mEmailView.setError(emptyValidator.getErrorMessage());
            return false;
        }

        EmailValidator emailValidator = new EmailValidator(this, email);
        if (!emailValidator.isValid()) {
            mEmailView.setError(emailValidator.getErrorMessage());
            return false;
        }

        return true;
    }

    private boolean isPasswordValid(String password) {
        EmptyValidator emptyValidator = new EmptyValidator(this, password);
        if (!emptyValidator.isValid()) {
            mPasswordView.setError(emptyValidator.getErrorMessage());
            return false;
        }

        if (password.length() <= 4) {
            mPasswordView.setError(getString(R.string.validate_error_min_length));
            return false;
        }

        return true;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    public void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private JSONObject mJsonResponse;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormEncodingBuilder()
                    .add("user[email]", mEmail)
                    .add("user[password]", mPassword)
                    .build();
            Request request = new Request.Builder()
                    .url("https://comiguide.net/api/v1/users/sign_in.json")
                    .post(formBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    mJsonResponse = new JSONObject(response.body().string());
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                try {
                    JSONObject apiTokenObject = mJsonResponse.getJSONObject("api_token");
                    JSONObject userObject = mJsonResponse.getJSONObject("user");

                    User user = new User(LoginActivity.this);
                    user.setApiToken(apiTokenObject.getString("token"));
                    user.setUserId(userObject.getInt("id"));
                    user.setUserName(userObject.getString("username"));
                    user.save();

                    Toast.makeText(LoginActivity.this,
                            getString(R.string.success_login), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Toast.makeText(LoginActivity.this,
                    getString(R.string.error_login_fail), Toast.LENGTH_SHORT).show();
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            mPasswordView.requestFocus();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}



