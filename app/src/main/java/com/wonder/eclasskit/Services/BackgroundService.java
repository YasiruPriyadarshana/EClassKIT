package com.wonder.eclasskit.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wonder.eclasskit.MainActivity;
import com.wonder.eclasskit.R;
import com.wonder.eclasskit.Settings;

public class BackgroundService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    long evL=1;
    long v=1;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        String TAG = "MyServiceTag";

        handler = new Handler();
            runnable = new Runnable() {
                public void run() {
//                    Toast.makeText(context, "Service is still running", Toast.LENGTH_LONG).show();

                    if (evL!=v){
                    notifime();
                    }
                    run2();
                    v = evL;


                    handler.postDelayed(runnable, 10000);
                }
            };

            handler.postDelayed(runnable, 15000);



    }




private void run2(){
    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("event");
    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            evL=dataSnapshot.getChildrenCount();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

}

public void notifime(){
    int NOTIFICATION_ID = 234;
    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        mChannel.setDescription(Description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        mChannel.setShowBadge(false);
        notificationManager.createNotificationChannel(mChannel);
    }

    NotificationCompat.Builder builder = new NotificationCompat.Builder(BackgroundService.this, "my_channel_01")
            .setTicker("TickerTitle")
            .setContentTitle("Check New Event")
            .setContentText("Learn With Chirath Science Seminar")
            .setSmallIcon(R.mipmap.logo)
            .setAutoCancel(true);



    Intent resultIntent = new Intent(BackgroundService.this, MainActivity.class);
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(BackgroundService.this);
    stackBuilder.addParentStack(Settings.class);
    stackBuilder.addNextIntent(resultIntent);
    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
    builder.setContentIntent(resultPendingIntent);
    notificationManager.notify(NOTIFICATION_ID, builder.build());
}

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }
}
