package com.example.SensorNode;

import android.app.Activity;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class HomePageTotal extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;// creating a variable for our Firebase Database.
    DatabaseReference databaseReference;// creating a variable for our Database Reference for Firebase.

    EditText value;
    //    TextView retrieve7;
    Button submit;
    DatabaseReference reference, reference2;
    String status;
    DHT dht;

    private NotificationManagerCompat notificationManager;

    // variable for Text view.
    TextView retrieveLowTemp, retrieve2, retrieve3, retrieveHighTemp, retrieve5, retrieve6, retrieve7, retrieve8, retrieve9, retrieveStatus;

    // variables for alert
    float temp, temp2, pul, pul2, sp, interval;

    // variable for call
    private FloatingActionButton btn, lan;

    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        btn = findViewById(R.id.loc);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location();
            }
        });

        notificationManager = NotificationManagerCompat.from(this);

        // below line is used to get the instance
        // of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // initializing our object class variable.
        retrieveLowTemp = findViewById(R.id.idTVRetriveTemp);
        retrieve2 = findViewById(R.id.idTVRetrivepulse);
        retrieve3 = findViewById(R.id.idTVRetriveoxygen);

        retrieveHighTemp = findViewById(R.id.idTVRetriveTemp2);
        retrieve5 = findViewById(R.id.idTVRetrivepulse2);
        retrieve6 = findViewById(R.id.idTVRetriveoxygen2);

        retrieve7 = findViewById(R.id.idTVRetriveTempavg);
        retrieve8 = findViewById(R.id.idTVRetrivepulavg);
        retrieve9 = findViewById(R.id.idTVRetrivespo2avg);

        retrieveStatus = findViewById(R.id.status);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("DHT");

        // accepts temperature
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String temperature3 = snapshot.child("tempavg").getValue().toString();
                retrieve7.setText(String.format("%.2f",Float.parseFloat(temperature3)));

                String temperature = snapshot.child("TEMP").getValue().toString();
                retrieveLowTemp.setText(String.format("%.2f",Float.parseFloat(temperature3)-0.5));

                String temperature2 = snapshot.child("temhigh").getValue().toString();
                retrieveHighTemp.setText(String.format("%.2f",Float.parseFloat(temperature3)+0.5));

                temp = Float.parseFloat(temperature);
                temp2 = Float.parseFloat(temperature2);
                Toast.makeText(HomePageTotal.this, "Temperature updated successfully !", Toast.LENGTH_SHORT).show();
                if (temp < 36)
                {
                    notificationalert();
                    btn.setVisibility(View.VISIBLE);
                }
                else if (temp2 > 37.2)
                {
                    notificationalert2();
                    btn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomePageTotal.this, "Temperature data upload failed ..", Toast.LENGTH_SHORT).show();
            }
        });
        // accepts pulse
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String pulse3 = snapshot.child("pulavg").getValue().toString();
                retrieve8.setText(String.format("%.2f",Float.parseFloat(pulse3)));

                String pulse = snapshot.child("pul").getValue().toString();
                retrieve2.setText(String.format("%.2f",Float.parseFloat(pulse3)-0.5));

                String pulse2 = snapshot.child("pulhigh").getValue().toString();
                retrieve5.setText(String.format("%.2f",Float.parseFloat(pulse3)+0.5));

                Toast.makeText(HomePageTotal.this, "Humidity updated successfully !", Toast.LENGTH_SHORT).show();
                pul = Float.parseFloat(pulse);
                pul2 = Float.parseFloat(pulse2);
                if (pul < 60)
                {
                    notificationpul();
                    btn.setVisibility(View.VISIBLE);
                }
                else if (pul2 > 90)
                {
                    notificationpul2();
                    btn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomePageTotal.this, "Pulse data upload failed ..", Toast.LENGTH_SHORT).show();
            }
        });


        //accepts spo2
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String spo23 = snapshot.child("spo2avg").getValue().toString();
                retrieve9.setText(String.format("%.2f",Float.parseFloat(spo23)));

                String spo2 = snapshot.child("Spo2").getValue().toString();
                retrieve3.setText(String.format("%.2f",Float.parseFloat(spo23)-0.5));

                String spo22 = snapshot.child("spo2high").getValue().toString();
                retrieve6.setText(String.format("%.2f",Float.parseFloat(spo23)+0.5));

                Toast.makeText(HomePageTotal.this, "CO updated successfully !", Toast.LENGTH_SHORT).show();
                sp = Float.parseFloat(spo2);
                if (sp < 95)
                {
                    notificationsp();
                    btn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomePageTotal.this, "SpO2 data upload failed ..", Toast.LENGTH_SHORT).show();
            }
        });



        lan = findViewById(R.id.language);
        lan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                ChangeLanguage();
            }
        });


        value = findViewById(R.id.value);
        submit = findViewById(R.id.submit);
        dht = new DHT();

        reference = FirebaseDatabase.getInstance().getReference().child("DHT").child("interval");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interval = Float.parseFloat(value.getText().toString().trim());
                dht.setValue(interval);
                reference.setValue(dht);
                notificationinterval();
            }
        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String schedule = snapshot.child("value").getValue().toString();

                retrieveStatus.setText(String.format("%.2f",Float.parseFloat(schedule)));

                Toast.makeText(HomePageTotal.this,"New Interval set for "+String.format("%.2f",Float.parseFloat(schedule))+" minutes",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomePageTotal.this,"Data upload failed ..",Toast.LENGTH_SHORT).show();
            }
        });

        reference2 = FirebaseDatabase.getInstance().getReference().child("DHT");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String statInt = snapshot.child("Status").getValue().toString();
//                retrieve7.setText(statInt);
//                Toast.makeText(HomePageTotal.this,"sensor node ! " + status,Toast.LENGTH_LONG).show();

                notificationstatus();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomePageTotal.this,"Data upload failed ..",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //notification for higher pulse
    private void notificationpul2() {

        Notification notification = new NotificationCompat.Builder(this, notifications.pulse2)
                .setContentText("SENSOR NODE")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_notifications_).setAutoCancel(true)
                .setContentText("Abnormal pulse reading " + pul2)
                .build();

        notificationManager.notify(1, notification);
    }

    //notification for lower pulse
    private void notificationpul() {

        Notification notification2 = new NotificationCompat.Builder(this, notifications.pulse1)
                .setContentText("SENSOR NODE")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_notifications_).setAutoCancel(true)
                .setContentText("Abnormal pulse reading " + pul)
                .build();

        notificationManager.notify(2, notification2);
    }

    //notification for spo2
    private void notificationsp() {
        Notification notification3 = new NotificationCompat.Builder(this, notifications.spo2)
                .setContentText("SENSOR NODE")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_notifications_).setAutoCancel(true)
                .setContentText("Abnormal SpO2 reading " + sp + " %")
                .build();

        notificationManager.notify(3, notification3);

    }

    //notification for lower temp
    private void notificationalert() {
        Notification notification4 = new NotificationCompat.Builder(this, notifications.Temperature1)
                .setContentText("SENSOR NODE")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_notifications_).setAutoCancel(true)
                .setContentText("Abnormal temperature reading " + temp + " °C")
                .build();

        notificationManager.notify(4, notification4);

    }

    //notification for higher temp
    private void notificationalert2() {
        Notification notification5 = new NotificationCompat.Builder(this, notifications.Temperature2)
                .setContentText("SENSOR NODE")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_notifications_).setAutoCancel(true)
                .setContentText("Abnormal temperature reading " + temp2 + " °C")
                .build();

        notificationManager.notify(5, notification5);

    }

    private void Location() {
        Intent myLink = new Intent(Intent.ACTION_VIEW);
        myLink.setData(Uri.parse("https://goo.gl/maps/TBCp1F9t5UVLKDXi6"));
        startActivity(myLink);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Location();
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void ChangeLanguage() {
        final String[] listitems = {"हिंदी","বাংলা", "ENGLISH","عربى"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(HomePageTotal.this);
        mBuilder.setTitle("Choose Language ...");
        mBuilder.setSingleChoiceItems(listitems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0)
                {
                    setLocale("hi");
                    recreate();
                }
                else if (i == 1)
                {
                    setLocale("bn");
                    recreate();
                }
                else if (i == 2)
                {
                    setLocale("en");
                    recreate();
                }
                else if (i == 3)
                {
                    setLocale("ar");
                    recreate();
                }

                dialogInterface.dismiss();

            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    public void loadLocale()
    {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        setLocale(language);
    }


    private void notificationinterval()
    {
        Notification notification = new NotificationCompat.Builder(this,notifications.schedule)
                .setContentText("SENSOR NODE")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_notifications_).setAutoCancel(true)
                .setContentText("New interval set for " + interval + " minutes !")
                .build();

        notificationManager.notify(1,notification);

    }

    private void notificationstatus()
    {
        Notification notification2 = new NotificationCompat.Builder(this,notifications.status)
                .setContentText("SENSOR NODE")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_notifications_).setAutoCancel(true)
                .setContentText("Sensor Node status " + status)
                .build();

        notificationManager.notify(2,notification2);

    }
}
