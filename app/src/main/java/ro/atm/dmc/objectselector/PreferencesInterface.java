package ro.atm.dmc.objectselector;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesInterface {

    private static final String appPreferenceName = "Settings";

    public static void writePrefString(Context context, String prefName, String value){
        SharedPreferences pref = context.getSharedPreferences(appPreferenceName, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString(prefName,value);

        editor.commit();
    }

    public static void writePrefInt(Context context, String prefName, int value){
        SharedPreferences pref = context.getSharedPreferences(appPreferenceName, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(prefName,value);

        editor.commit();
    }

    public static String readPrefString(Context context, String prefName){
        SharedPreferences pref = context.getSharedPreferences(appPreferenceName, Context.MODE_PRIVATE);
        String value = pref.getString(prefName,"");
        return value;
    }

    public static int readPrefInt(Context context, String prefName){
        SharedPreferences pref = context.getSharedPreferences(appPreferenceName, Context.MODE_PRIVATE);
        int value = pref.getInt(prefName,-1);
        return value;
    }
}
