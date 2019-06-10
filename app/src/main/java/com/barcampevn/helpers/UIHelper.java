package com.barcampevn.helpers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.app.NotificationCompat;

import com.barcampevn.R;
import com.barcampevn.activities.MainActivity;
import com.barcampevn.data.models.Schedule;
import com.google.android.material.snackbar.Snackbar;

import static android.content.Intent.ACTION_VIEW;
import static android.os.Build.VERSION_CODES.KITKAT;
import static android.os.Build.VERSION_CODES.M;
import static com.barcampevn.helpers.NotificationPublisher.NOTIFICATION_CHANNEL_ID;

/**
 * Created by andranikas on 5/17/2017.
 */

public final class UIHelper {

    public static void showSnackBar(@NonNull View view, @NonNull String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public static void showSnackBar(@NonNull View view, @StringRes int resId, @StringRes int actionResId, @NonNull View.OnClickListener listener) {
        Snackbar.make(view, resId, Snackbar.LENGTH_LONG).setAction(actionResId, listener).show();
    }

    public static void scheduleNotification(@NonNull Context context, @NonNull Schedule schedule, long triggerAtMillis) {
        final int NOTIFY_ID = 121;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(context.getString(R.string.notification_title, schedule.getEnSpeaker().getSpeaker(), schedule.getRoom()))
                .setContentText(context.getString(R.string.notification_description, schedule.getEnSpeaker().getTopic()))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSmallIcon(R.drawable.ic_notification);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent activity = PendingIntent.getActivity(context, NOTIFY_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(activity);

        Notification notification = builder.build();

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, NOTIFY_ID);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), notificationIntent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if(Build.VERSION.SDK_INT < M) {
            if (Build.VERSION.SDK_INT >= KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }
    }

    public static void openBrowser(@NonNull Context context, @NonNull String url) {
        Intent browserIntent = new Intent(ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }
}
