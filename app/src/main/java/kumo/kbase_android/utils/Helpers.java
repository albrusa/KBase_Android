package kumo.kbase_android.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by dev_2 on 16/11/2015.
 */

public class Helpers{

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

       /* public void SavePhotoUri (Context context, Uri imageuri, String Filename){

                File FilePath = new File(context.getDir(Environment.DIRECTORY_PICTURES,Context.MODE_PRIVATE));
                try {
                        Bitmap selectedImage = MediaStore.Images.Media.getBitmap(context.getContentResolver(),imageuri);
                        String destinationImagePath = FilePath+"/"+Filename;
                        FileOutputStream destination = new FileOutputStream(destinationImagePath);
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, destination);
                        destination.close();
                } catch (Exception e) {
                        Log.e("error", e.toString());
                }
        }*/
}

