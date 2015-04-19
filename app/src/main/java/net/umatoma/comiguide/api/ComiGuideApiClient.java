package net.umatoma.comiguide.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
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

    private static final int MEMORY_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
    private static LruCache<String, String> RESPONSE_MEMORY_CACHE = new LruCache<String, String>(MEMORY_CACHE_SIZE) {
        @Override
        protected int sizeOf(String key, String value) {
            return value.getBytes().length;
        }
    };

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

    public HttpClientTask callDeleteTask(String path) {
        return new HttpClientTask(mUser).deleteRequest(path);
    }

    public class HttpClientTask extends AsyncTask<Request, Void, JSONObject> {

        private static final String TAG = "HttpClientTask";
        private static final String API_SCHEME = "https";
        private static final String API_AUTHORITY = "comiguide.net";
        private static final String API_TOKEN_HEADER = "X-Comiguide-Api-Token";

        private OnApiClientPostExecuteListener mListener;
        private OkHttpClient mClient;
        private Request.Builder mRequesBuilder;
        private Call mCall;
        private User mUser;
        private ProgressDialog mProgressDialog;
        private boolean mUseCache = false;
        private boolean mSetApiToken = true;

        public HttpClientTask(User user) {
            mUser = user;
        }

        @Override
        protected void onPreExecute() {
            mClient = new OkHttpClient();

            if (mSetApiToken) {
                mRequesBuilder.addHeader(API_TOKEN_HEADER, mUser.getApiToken());
            }

            if (mProgressDialog != null) {
                mProgressDialog.setMessage("Now processing...");
                mProgressDialog.show();
            }
        }

        @Override
        protected JSONObject doInBackground(Request... params) {
            try {
                Request request = mRequesBuilder.build();

                if (mUseCache) {
                    String cachedResponse =  RESPONSE_MEMORY_CACHE.get(request.urlString());
                    if (cachedResponse != null) {
                        Log.d(TAG, "Hit response cache");
                        return new JSONObject(cachedResponse);
                    }
                    Log.d(TAG, "Missing response cache");
                }

                mCall = mClient.newCall(mRequesBuilder.build());
                Response response = mCall.execute();
                if (response.isSuccessful()) {
                    String bodyString = response.body().string();

                    if (mUseCache) {
                        RESPONSE_MEMORY_CACHE.put(request.urlString(), bodyString);
                        Log.d(TAG, "Put response cache");
                    }

                    return new JSONObject(bodyString);
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

        public HttpClientTask setCache() {
            mUseCache = true;
            return this;
        }

        public HttpClientTask setProgressDialog(Context context) {
            mProgressDialog = new ProgressDialog(context);
            return this;
        }

        public HttpClientTask setApiToken(boolean setApiToken) {
            mSetApiToken = setApiToken;
            return this;
        }

        public HttpClientTask setOnApiClientPostExecuteListener(OnApiClientPostExecuteListener listener) {
            mListener = listener;
            return this;
        }

        // Get request
        public HttpClientTask getRequest(String path) {
            return getRequest(path, new ArrayList<NameValuePair>());
        }

        public HttpClientTask getRequest(String path, List<NameValuePair> params) {
            Uri.Builder builder = getDefaultUriBuider(path);

            for (NameValuePair param : params) {
                builder.appendQueryParameter(param.getName(), param.getValue());
            }

            mRequesBuilder = new Request.Builder()
                    .url(builder.build().toString());

            return this;
        }

        // Post request
        public HttpClientTask postRequest(String path, RequestBody formBody) {
            Uri uri = getDefaultUriBuider(path).build();
            mRequesBuilder = new Request.Builder()
                    .url(uri.toString())
                    .post(formBody);

            return this;
        }

        // Put request
        public HttpClientTask putRequest(String path, RequestBody formBody) {
            Uri uri = getDefaultUriBuider(path).build();
            mRequesBuilder = new Request.Builder()
                    .url(uri.toString())
                    .put(formBody);

            return this;
        }

        // Delete request
        public HttpClientTask deleteRequest(String path) {
            Uri uri = getDefaultUriBuider(path).build();
            mRequesBuilder = new Request.Builder()
                    .url(uri.toString())
                    .delete();

            return this;
        }

        private Uri.Builder getDefaultUriBuider(String path) {
            return new Uri.Builder()
                    .scheme(API_SCHEME)
                    .authority(API_AUTHORITY)
                    .path(path);
        }
    }
}
