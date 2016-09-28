package com.jksam.jaikishan96.bill;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Random;
//import android.util.Log;

public class MReminder extends WakeRem {

    public MReminder() {
        super("RemMgr");
    }

    @Override
    public void doReminderWork(Intent intent) {
        //Log.d("ReminderService", "Doing work.");
        Long rowId = intent.getExtras().getLong(com.jksam.jaikishan96.bill.RDatabase.KEY_ROWID);

        NotificationManager mgr = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, com.jksam.jaikishan96.bill.BREdit.class);

        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

        notificationIntent.putExtra(com.jksam.jaikishan96.bill.RDatabase.KEY_ROWID, rowId);

        PendingIntent pi = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        Notification note=new Notification(android.R.drawable.stat_sys_warning, getString(R.string.notify_new_task_message), System.currentTimeMillis());

        Notification.Builder builder = new Notification.Builder(this);
        builder.setAutoCancel(false);
        builder.setContentTitle("You have a bill reminder");
        builder.setContentText("Tap to check.");
        builder.setSmallIcon(R.drawable.app_icon);
        builder.setContentIntent(pi);
        builder.setOngoing(true);
        //builder.setSubText("This is subtext...");
        //builder.setNumber(100);
        builder.build();

        note=builder.getNotification();
        //end
        note.defaults |= Notification.DEFAULT_SOUND;
        note.flags |= Notification.FLAG_AUTO_CANCEL;

        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        int id = (m);
        mgr.notify(id, note);



    }
}
