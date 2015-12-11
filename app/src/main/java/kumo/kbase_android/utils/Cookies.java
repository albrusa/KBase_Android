package kumo.kbase_android.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import static android.content.SharedPreferences.Editor;

/**
 * Created by dev_2 on 19/11/2015.
 */
public class Cookies {
    private static Cookies cookie;
    private final Context context;
    private final SharedPreferences preferences;
    private final Editor  editor;
    private static Gson GSON = new Gson();
    Type typeOfObject = new TypeToken<Object>(){}.getType();


    private Cookies(Context context, String namePreferences, int mode) {
        this.context = context;
        if (namePreferences == null || namePreferences.equals("")) {
            namePreferences = ObjectPreference.DEFAULT_PREFERENCE;
        }
        preferences = context.getSharedPreferences(namePreferences, mode);
        editor = preferences.edit();
    }

    public static Cookies getComplexPreferences(Context context, String namePreferences, int mode) {
        if (cookie == null) {
            cookie = new Cookies(context,
                    namePreferences, mode);
        }
        return cookie;
    }

    public void putObject(String key, Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Object is null");
        }
        if (key.equals("") || key == null) {
            throw new IllegalArgumentException("Key is empty or null");
        }
        editor.putString(key, GSON.toJson(object));
    }

    public void removeObject(String key){
        if (key.equals("") || key == null) {
            throw new IllegalArgumentException("Key is empty or null");
        }
        if(preferences.contains(key)) {
            editor.remove(key);
        }
    }

    public void commit() {
        editor.commit();
    }

    public <T> T getObject(String key, Class<T> a) {
        String gson = preferences.getString(key, null);
        if (gson == null) {
            return null;
        }
        else {
            try {
                return GSON.fromJson(gson, a);
            }
            catch (Exception e) {
                throw new IllegalArgumentException("Object stored with key "
                        + key + " is instance of other class");
            }
        }
    } }
