package net.umatoma.comiguide.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import net.umatoma.comiguide.model.User;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComiGuideApiClient {

    private User mUser;

    public ComiGuideApiClient(Context context) {
        mUser = new User(context);
    }

    public HttpClientTask callGetTask(String path) {
        return new HttpClientTask(mUser).getRequest(path);
    }

    public HttpClientTask callGetTask(String path, List<NameValuePair> params) {
        return new HttpClientTask(mUser).getRequest(path, params);
    }

    public HttpClientTask callPostTask(String path, RequestBody formBody) {
        return new HttpClientTask(mUser).postRequest(path, formBody);
    }

    public HttpClientTask callPutTask(String path, RequestBody formBody) {
        return new HttpClientTask(mUser).putRequest(path, formBody);
    }

    public interface OnHttpClientPostExecuteListener {
        public void onSuccess(JSONObject result);
        public void onFail();
    }

    public class HttpClientTask extends AsyncTask<Request, Void, JSONObject> {

        private static final String TAG = "HttpClientTask";
        private static final String API_SCHEME = "https";
        private static final String API_AUTHORITY = "comiguide.net";
        private static final String API_TOKEN_HEADER = "X-Comiguide-Api-Token";

        private OnHttpClientPostExecuteListener mListener;
        private Request mRequest;
        private Call mCall;
        private User mUser;
        private ProgressDialog mProgressDialog;

        public HttpClientTask(User user) {
            mUser = user;
        }

        @Override
        protected void onPreExecute() {
            if (mProgressDialog != null) {
                mProgressDialog.setMessage("Now processing...");
                mProgressDialog.show();
            }
        }

        @Override
        protected JSONObject doInBackground(Request... params) {
            try {
                mCall = new OkHttpClient().newCall(mRequest);
                Response response = mCall.execute();
                if (response.isSuccessful()) {
                    Log.d(TAG, "Success doInBackground");
                    return new JSONObject(response.body().string());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "Fail doInBackground");
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (mListener != null) {
                if (result != null) {
                    mListener.onSuccess(result);
                } else {
                    mListener.onFail();
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            if (mCall != null && !mCall.isCanceled()) {
                mCall.cancel();
            }

            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }

        public HttpClientTask setProgressDialog(Context context) {
            mProgressDialog = new ProgressDialog(context);
            return this;
        }

        public HttpClientTask setOnHttpClientPostExecuteListener(OnHttpClientPostExecuteListener listener) {
            mListener = listener;
            return this;
        }

        // Get request
        public HttpClientTask getRequest(String path) {
            return getRequest(path, new ArrayList<NameValuePair>());
        }

        public HttpClientTask getRequest(String path, List<NameValuePair> params) {
            Uri.Builder builder = new Uri.Builder()
                    .scheme(API_SCHEME)
                    .authority(API_AUTHORITY)
                    .path(path);

            for (NameValuePair param : params) {
                builder.appendQueryParameter(param.getName(), param.getValue());
            }

            mRequest = new Request.Builder()
                    .url(builder.build().toString())
                    .addHeader(API_TOKEN_HEADER, mUser.getApiToken())
                    .build();

            return this;
        }

        // Post request
        public HttpClientTask postRequest(String path, RequestBody formBody) {
            Uri uri = new Uri.Builder()
                    .scheme(API_SCHEME)
                    .authority(API_AUTHORITY)
                    .path(path)
                    .build();
            mRequest = new Request.Builder()
                    .url(uri.toString())
                    .addHeader(API_TOKEN_HEADER, mUser.getApiToken())
                    .post(formBody)
                    .build();

            return this;
        }

        // Put request
        public HttpClientTask putRequest(String path, RequestBody formBody) {
            Uri uri = new Uri.Builder()
                    .scheme(API_SCHEME)
                    .authority(API_AUTHORITY)
                    .path(path)
                    .build();
            mRequest = new Request.Builder()
                    .url(uri.toString())
                    .addHeader(API_TOKEN_HEADER, mUser.getApiToken())
                    .put(formBody)
                    .build();

            return this;
        }
    }
}
