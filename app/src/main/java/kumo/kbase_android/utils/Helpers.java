package kumo.kbase_android.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by dev_2 on 16/11/2015.
 */

public class Helpers{

    public static int[] conexiones = new int []{
            ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_MOBILE};


        public static String getRealPathFromURI(Context context,Uri contentUri){
                Cursor cursor=null;
                try{
                        String[]proj={MediaStore.Images.Media.DATA};
                        cursor=context.getContentResolver().query(contentUri,proj,null,null,null);
                        int column_index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        return cursor.getString(column_index);
                }finally{
                        if(cursor!=null){
                                cursor.close();
                        }
                }
        }

        public static boolean isNetworkAvailable(Context context, int[] networkTypes) {
            try {
                ConnectivityManager cm =
                        (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                for (int networkType : networkTypes) {

                    if(activeNetwork.getType() == networkType){
                        return true;
                    }

                }
            } catch (Exception e) {
                return false;
            }
            return false;
        }

        public static String fileExt(String url) {
            if (url.indexOf("?") > -1) {
                url = url.substring(0, url.indexOf("?"));
            }
            if (url.lastIndexOf(".") == -1) {
                return null;
            } else {
                String ext = url.substring(url.lastIndexOf("."));
                if (ext.indexOf("%") > -1) {
                    ext = ext.substring(0, ext.indexOf("%"));
                }
                if (ext.indexOf("/") > -1) {
                    ext = ext.substring(0, ext.indexOf("/"));
                }
                return ext.toLowerCase();

            }
        }
}

