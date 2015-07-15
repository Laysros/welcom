package com.sabayrean.welcom.helper;

/**
 * Created by LAYLeangsros on 14/07/2015.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "AndroidHiveLogin";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ID = "id";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn, String email, int id) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.putString(KEY_EMAIL, email);
        editor.putInt(KEY_ID, id);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public int getId() {
        return pref.getInt(KEY_ID, 0);
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL, null);
    }
    public void logout() {
        editor.clear();
        editor.commit();
    }



    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}