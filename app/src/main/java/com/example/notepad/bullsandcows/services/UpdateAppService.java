package com.example.notepad.bullsandcows.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.holders.AppInfoHolder;
import com.example.notepad.bullsandcows.data.models.RequestUpdateModel;
import com.example.notepad.bullsandcows.ui.activity.activiteis.MainActivity;

import static com.example.notepad.bullsandcows.utils.Constants.TAG;

public class UpdateAppService extends Service implements UploadNewVersionAppCallback {

    public static final int UPLOAD_NOTIFICATION_ID = 1;

    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: start UpdateAppService");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String chanelId = "UploadChanel";
        mBuilder = new NotificationCompat.Builder(UpdateAppService.this, chanelId).
                setSmallIcon(R.drawable.bullgood).
                setContentTitle(getResources().getString(R.string.START_DOWNLOAD_NEW_VERSION)).
                setContentText(getResources().getString(R.string.NEW_VERSION_TITLE) +
                        AppInfoHolder.getInstance().getVersionApp().getNameOfApp()).
                setProgress(0, 0, true);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        String[] newFeatures = AppInfoHolder.getInstance().getVersionApp().getmNewVersionFeatures();

        inboxStyle.setBigContentTitle(AppInfoHolder.getInstance().getVersionApp().getNameOfApp());

        for (String newFeature : newFeatures) {
            inboxStyle.addLine(newFeature);
        }

        mBuilder.setStyle(inboxStyle);

        Intent resultIntent = new Intent(UpdateAppService.this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(UpdateAppService.this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (mNotificationManager != null) {
            mNotificationManager.notify(UPLOAD_NOTIFICATION_ID, mBuilder.build());
        }

        RequestUpdateModel request = new RequestUpdateModel();
        request.setCallback(this);
        request.setVersionApp(AppInfoHolder.getInstance().getVersionApp());
        new NewAppVersionLoader().execute(request);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: UpdateAppService");
    }

    @Override
    public void sendUploadResultsCallback(RequestUpdateModel pRequest) {
        if (pRequest.getException() == null) {
            mBuilder.setProgress(0, 0, false)
                    .setContentTitle(getResources().getString(R.string.SUCCESS_UPLOAD));
            mNotificationManager.notify(UPLOAD_NOTIFICATION_ID, mBuilder.build());
        } else {
            mBuilder.setProgress(0, 0, false)
                    .setContentTitle(pRequest.getException().getMessage())
                    .setContentText(getResources().getString(R.string.TRY_AGAIN));
            mNotificationManager.notify(UPLOAD_NOTIFICATION_ID, mBuilder.build());
        }
        stopSelf();
    }
}
