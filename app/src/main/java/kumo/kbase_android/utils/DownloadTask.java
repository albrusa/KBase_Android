package kumo.kbase_android.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import kumo.kbase_android.R;

/**
 * Created by dev_2 on 30/11/2015.
 */
public class DownloadTask extends AsyncTask<String, Integer, String> {
    private ProgressDialog mPDialog;
    private Context mContext;
    private PowerManager.WakeLock mWakeLock;
    private File mTargetFile;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;


    public DownloadTask(Context context,File targetFile,String dialogMessage) {
        this.mContext = context;
        this.mTargetFile = targetFile;
        mPDialog = new ProgressDialog(context);

        mPDialog.setMessage(dialogMessage);
        mPDialog.setIndeterminate(true);
        mPDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mPDialog.setCancelable(true);

        mNotifyManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setContentTitle("Picture Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.cast_ic_notification_0);



        // reference to instance to use inside listener
        final DownloadTask me = this;
        mPDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                me.cancel(true);
            }
        });
        Log.i("DownloadTask", "Constructor done");
    }

    @Override
    protected String doInBackground(String... sUrl) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }
            Log.i("DownloadTask","Response " + connection.getResponseCode());

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(mTargetFile,false);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    Log.i("DownloadTask","Cancelled");
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));


                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return null;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();

        mBuilder.setProgress(100, 0, false);
        mNotifyManager.notify(1, mBuilder.build());
        mPDialog.show();

    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        mPDialog.setIndeterminate(false);
        mPDialog.setMax(100);
        mPDialog.setProgress(progress[0]);
        mBuilder.setProgress(100, progress[0], false);
        mNotifyManager.notify(1, mBuilder.build());

    }

    @Override
    protected void onPostExecute(String result) {
        Log.i("DownloadTask", "Work Done! PostExecute");
        mWakeLock.release();
        mPDialog.dismiss();
        if (result != null)
            Toast.makeText(mContext, "Download error: " + result, Toast.LENGTH_LONG).show();
        else {
            Toast.makeText(mContext, "File Downloaded", Toast.LENGTH_SHORT).show();

            MimeTypeMap myMime = MimeTypeMap.getSingleton();
            Intent newIntent = new Intent(Intent.ACTION_VIEW);
            String mimeType = myMime.getMimeTypeFromExtension(Helpers.fileExt(mTargetFile.getName()));
            newIntent.setDataAndType(Uri.fromFile(mTargetFile),mimeType);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


            /*Intent intent = new Intent(this, UsuariosListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0 , newIntent,
                    PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext)
                    .setSmallIcon(R.drawable.mr_ic_settings_dark)
                    .setContentTitle("GCM Message")
                    .setContentText("Descarga completa")
                    .setAutoCancel(true)
                    .setPriority(0)
                    .setVisibility(-1)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(1, notificationBuilder.build());

        }
    }
}