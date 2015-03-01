package net.umatoma.comiguide.api;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import net.umatoma.comiguide.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ComiGuideApiClient {

    private User mUser;

    public ComiGuideApiClient(Context context) {
        mUser = new User(context);
    }

    public HttpClientTask callGetTask(String path) {
        return new HttpClientTask(mUser).getRequest(path);
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
        private User mUser;

        public HttpClientTask(User user) {
            mUser = user;
        }

        // Get request
        public HttpClientTask getRequest(String path) {
            OkHttpClient client = new OkHttpClient();
            String apiToken = mUser.getApiToken();

            Uri uri = new Uri.Builder()
                    .scheme(API_SCHEME)
                    .authority(API_AUTHORITY)
                    .path(path)
                    .build();
            mRequest = new Request.Builder()
                    .url(uri.toString())
                    .addHeader(API_TOKEN_HEADER, apiToken)
                    .build();

            return this;
        }

        // Post request
        public HttpClientTask postRequest(String path, RequestBody formBody) {
            OkHttpClient client = new OkHttpClient();
            String apiToken = mUser.getApiToken();

            Uri uri = new Uri.Builder()
                    .scheme(API_SCHEME)
                    .authority(API_AUTHORITY)
                    .path(path)
                    .build();
            mRequest = new Request.Builder()
                    .url(uri.toString())
                    .addHeader(API_TOKEN_HEADER, apiToken)
                    .post(formBody)
                    .build();

            return this;
        }

        // Put request
        public HttpClientTask putRequest(String path, RequestBody formBody) {
            OkHttpClient client = new OkHttpClient();
            String apiToken = mUser.getApiToken();

            Uri uri = new Uri.Builder()
                    .scheme(API_SCHEME)
                    .authority(API_AUTHORITY)
                    .path(path)
                    .build();
            mRequest = new Request.Builder()
                    .url(uri.toString())
                    .addHeader(API_TOKEN_HEADER, apiToken)
                    .put(formBody)
                    .build();

            return this;
        }

        @Override
        protected JSONObject doInBackground(Request... params) {
            try {
                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(mRequest).execute();
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

        }

        public HttpClientTask setOnHttpClientPostExecuteListener(OnHttpClientPostExecuteListener listener) {
            mListener = listener;
            return this;
        }
    }
}
