package net.umatoma.comiguide.api;

import org.json.JSONObject;

public interface OnApiClientPostExecuteListener {
    public void onSuccess(JSONObject result);
    public void onFail();
}
