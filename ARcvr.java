package com.jksam.jaikishan96.bill;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
//import android.util.Log;

public class ARcvr extends BroadcastReceiver {

    private static final String TAG = ComponentInfo.class.getCanonicalName();


    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.d(TAG, "Received wake up from alarm manager.");

        long rowid = intent.getExtras().getLong(RDatabase.KEY_ROWID);

        com.jksam.jaikishan96.bill.WakeRem.acquireStaticLock(context);

        Intent i = new Intent(context, MReminder.class);
        i.putExtra(RDatabase.KEY_ROWID, rowid);
        context.startService(i);

    }
}
