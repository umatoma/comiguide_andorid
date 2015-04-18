package net.umatoma.comiguide.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

public class IntentUtil {

    public static boolean checkImplicitIntent(Context context, String url) {
        boolean ret = false;
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);
        if (apps.size() > 0) {
            ret = true;
        }
        return ret;
    }

}
