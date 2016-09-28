package com.jksam.jaikishan96.bill;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
//import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BREdit extends Activity {

    //
    // Dialog Constants
    //
    private static final int DATE_PICKER_DIALOG = 0;
    private static final int TIME_PICKER_DIALOG = 1;

    //
    // Date Format
    //
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "kk:mm";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";

    private EditText mTitleText;
    private EditText mBodyText;
    private Button mDateButton;
    private Button mTimeButton;
    private Button mConfirmButton;
    private Long mRowId;
    private RDatabase mDbHelper;
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new RDatabase(this);

        setContentView(R.layout.rem_view);

        mCalendar = Calendar.getInstance();
        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);
        mDateButton = (Button) findViewById(R.id.reminder_date);
        mTimeButton = (Button) findViewById(R.id.reminder_time);

        mConfirmButton = (Button) findViewById(R.id.confirm);

        mRowId = savedInstanceState != null ? savedInstanceState.getLong(RDatabase.KEY_ROWID)
                : null;

        registerButtonListenersAndSetDefaultText();
    }

    private void setRowIdFromIntent() {
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(RDatabase.KEY_ROWID)
                    : null;

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDbHelper.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDbHelper.open();
        setRowIdFromIntent();
        populateFields();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case DATE_PICKER_DIALOG:
                return showDatePicker();
            case TIME_PICKER_DIALOG:
                return showTimePicker();
        }
        return super.onCreateDialog(id);
    }

    private DatePickerDialog showDatePicker() {


        DatePickerDialog datePicker = new DatePickerDialog(BREdit.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateButtonText();
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        return datePicker;
    }

    private TimePickerDialog showTimePicker() {

        TimePickerDialog timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendar.set(Calendar.MINUTE, minute);
                updateTimeButtonText();
            }
        }, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true);

        return timePicker;
    }

    private void registerButtonListenersAndSetDefaultText() {

        mDateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_DIALOG);
            }
        });


        mTimeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(TIME_PICKER_DIALOG);
            }
        });

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                saveState();
                setResult(RESULT_OK);
                Toast.makeText(BREdit.this, getString(R.string.task_saved_message), Toast.LENGTH_SHORT).show();
                finish();
            }

        });

        updateDateButtonText();
        updateTimeButtonText();
    }

    private void populateFields()  {




        if (mRowId != null) {
            Cursor reminder = mDbHelper.fetchReminder(mRowId);
            startManagingCursor(reminder);
            mTitleText.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(RDatabase.KEY_TITLE)));
            mBodyText.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(RDatabase.KEY_BODY)));



            SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
            Date date = null;
            try {
                String dateString = reminder.getString(reminder.getColumnIndexOrThrow(RDatabase.KEY_DATE_TIME));
                date = dateTimeFormat.parse(dateString);
                mCalendar.setTime(date);
            } catch (ParseException e) {
                //Log.e("ReminderEditActivity", e.getMessage(), e);
            }
        } else {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String defaultTitleKey = getString(R.string.pref_task_title_key);
            String defaultTimeKey = getString(R.string.pref_default_time_from_now_key);

            String defaultTitle = prefs.getString(defaultTitleKey, null);
            String defaultTime = prefs.getString(defaultTimeKey, null);

            if(defaultTitle != null)
                mTitleText.setText(defaultTitle);

            if(defaultTime != null)
                mCalendar.add(Calendar.MINUTE, Integer.parseInt(defaultTime));

        }

        updateDateButtonText();
        updateTimeButtonText();

    }

    private void updateTimeButtonText() {

        SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
        String timeForButton = timeFormat.format(mCalendar.getTime());
        mTimeButton.setText(timeForButton);
    }

    private void updateDateButtonText() {

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateForButton = dateFormat.format(mCalendar.getTime());
        mDateButton.setText(dateForButton);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(RDatabase.KEY_ROWID, mRowId);
    }



    private void saveState() {
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        String reminderDateTime = dateTimeFormat.format(mCalendar.getTime());

        if (mRowId == null) {

            long id = mDbHelper.createReminder(title, body, reminderDateTime);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateReminder(mRowId, title, body, reminderDateTime);
        }

        new RemMgr(this).setReminder(mRowId, mCalendar);
    }

}
