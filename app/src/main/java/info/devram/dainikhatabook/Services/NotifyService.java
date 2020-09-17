package info.devram.dainikhatabook.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;

import info.devram.dainikhatabook.R;

import static android.content.Context.NOTIFICATION_SERVICE;



public class NotifyService
{

    public static final String PRIMARY_CHANNEL_ID = "account_channel";


    public static void createNotification(String title, String message, Context context) {
        NotificationManager mNotifyManager;
        mNotifyManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new
                NotificationCompat.Builder(context,PRIMARY_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
        builder.setColor(context.getColor(R.color.accentSecondary));
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            title,
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    (message);
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
        mNotifyManager.notify(0,builder.build());
    }
}
