package com.jksam.jaikishan96.bill;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by jaikishan96 on 22-08-2016.
 */
public class AboutUs extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putLong(RDatabase.KEY_ROWID, mRowId);
    }
}
