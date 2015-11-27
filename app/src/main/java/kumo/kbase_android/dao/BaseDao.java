package kumo.kbase_android.dao;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import kumo.kbase_android.db.DatabaseHelper;
import kumo.kbase_android.httpRequest.GsonRequest;

/**
 * Created by dev_2 on 27/11/2015.
 */
public class BaseDao {

    private static BaseDao mInstance;
    protected GsonRequest gson = null;
    protected DatabaseHelper mDatabaseHelper;
    protected static Context mContext = null;

    private BaseDao(Context context) {
        mContext = context;
        mDatabaseHelper = getDataBase();
    }

    public static synchronized BaseDao getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new BaseDao(context);
        }
        return mInstance;
    }

    public DatabaseHelper getDataBase() {
        if (mDatabaseHelper == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mDatabaseHelper = OpenHelperManager.getHelper(mContext, DatabaseHelper.class);
        }
        return mDatabaseHelper;
    }
}
