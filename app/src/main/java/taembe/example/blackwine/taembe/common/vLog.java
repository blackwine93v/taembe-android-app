package taembe.example.blackwine.taembe.common;

import android.util.Log;

/**
 * Created by Blackwine on 2/21/2017.
 */

public class vLog {
    public static final boolean DEBUG_MODE = true;

    public static void debug(String TAG, String msg){
        if(DEBUG_MODE){
            Log.d(TAG,msg);
        }
    }

    public static void error(String TAG, String msg){
        if(DEBUG_MODE){
            Log.e(TAG,msg);
        }
    }
}
