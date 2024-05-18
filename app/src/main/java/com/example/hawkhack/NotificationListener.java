package com.example.hawkhack;

import android.app.Notification;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.TextView;

public class NotificationListener extends NotificationListenerService {
    private TextView location;
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Notification notification = sbn.getNotification();
        String packageName = sbn.getPackageName();

        // Here you can filter notifications from specific apps if needed
        if ("com.example.targetapp".equals(packageName)) {
            // Trigger screenshot capture
            Intent intent = new Intent(this, ScreenshotService.class);
            startService(intent);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
