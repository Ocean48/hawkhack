package com.example.hawkhack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

public class LocationReceiver extends BroadcastReceiver {

    private TextView location;

    public LocationReceiver(TextView textView) {
        this.location = textView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.example.hawkhack.LOCATION_UPDATE".equals(intent.getAction())) {
            double latitude = intent.getDoubleExtra("latitude", 0.0);
            double longitude = intent.getDoubleExtra("longitude", 0.0);
            location.setText("Location: " + String.valueOf(latitude) + ", " + String.valueOf(longitude));
        }
    }
}
