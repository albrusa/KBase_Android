package kumo.kbase_android.utils;

import android.content.Context;

/**
 * Created by dev_2 on 19/11/2015.
 */
public class ObjectPreference {
    private static final String TAG = "ObjectPreference";
    private static Context context;

    public static final String DEFAULT_PREFERENCE = "kGlobal_Preferences";

    private Cookies cookies = null;

    public void init(Context _context) {
        cookies = Cookies.getComplexPreferences(_context,DEFAULT_PREFERENCE , _context.MODE_PRIVATE);
        android.util.Log.i(TAG, "Preference Created.");
    }

    public Cookies getComplexPreference() {
        if(cookies != null) {
            return cookies;
        }
        return null;
    } }