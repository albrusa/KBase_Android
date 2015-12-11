package kumo.kbase_android.utils;


import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import kumo.kbase_android.RegistroActivity;

public class MemoryBoss implements Application.ActivityLifecycleCallbacks,ComponentCallbacks2 {

    private static MemoryBoss ref;
    private Context context;

    public static boolean isInBackground = false;

    private MemoryBoss(Context _context){
        context = _context;
    }

    public static synchronized MemoryBoss init(Context context) {
        if (ref == null) ref = new MemoryBoss(context);
        return ref;
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (isInBackground) {
            Intent intent = new Intent(activity.getBaseContext(), RegistroActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
            isInBackground = false;
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }


    @Override
    public void onLowMemory() {
    }

    @Override
    public void onTrimMemory(final int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            isInBackground = true;
            // We're in the Background
        }
        // you might as well implement some memory cleanup here and be a nice Android dev.
    }
}