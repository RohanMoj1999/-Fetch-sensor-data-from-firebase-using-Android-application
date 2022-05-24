package com.example.SensorNode;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class notifications extends Application {
    public static final String Temperature1 = "temperature1";
    public static final String Temperature2 = "temperature2";
    public static final String pulse1 = "pulse1";
    public static final String pulse2 = "pulse2";
    public static final String spo2 = "spo2";
    public static final String schedule = "schedule";
    public static final String status = "status";


    @Override
    public void onCreate() {
        super.onCreate();

        createNotification();
    }

    private void createNotification() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel1 = new NotificationChannel(Temperature1,"temperature1",NotificationManager.IMPORTANCE_HIGH);
            NotificationChannel channel2 = new NotificationChannel(Temperature2,"temperature2",NotificationManager.IMPORTANCE_HIGH);
            NotificationChannel channel3 = new NotificationChannel(pulse1,"pulse1",NotificationManager.IMPORTANCE_HIGH);
            NotificationChannel channel4 = new NotificationChannel(pulse2,"pulse2",NotificationManager.IMPORTANCE_HIGH);
            NotificationChannel channel5 = new NotificationChannel(spo2,"spo2",NotificationManager.IMPORTANCE_HIGH);
            NotificationChannel channel6 = new NotificationChannel(schedule,"schedule",NotificationManager.IMPORTANCE_HIGH);
            NotificationChannel channel7 = new NotificationChannel(status,"status",NotificationManager.IMPORTANCE_HIGH);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
            manager.createNotificationChannel(channel3);
            manager.createNotificationChannel(channel4);
            manager.createNotificationChannel(channel5);
            manager.createNotificationChannel(channel6);
            manager.createNotificationChannel(channel7);
        }
    }
}
