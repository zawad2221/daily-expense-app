package info.devram.dainikhatabook.ui;

import android.app.DatePickerDialog;
import android.gesture.GestureOverlayView;
import android.util.Log;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateSelector implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "DateSelector";

    private Calendar myCalendar;
    private SimpleDateFormat sdf;
    private String myDate;

    public DateSelector() {
        myCalendar = Calendar.getInstance();
        String myFormat = "dd/MM/yy";
        sdf = new SimpleDateFormat(myFormat, Locale.CANADA);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.d(TAG, "onDateSet: starts ");
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        myDate = sdf.format(myCalendar.getTime());
        Log.d(TAG, "onDateSet: ends ");
    }

    public String getDate() {
        return sdf.format(myCalendar.getTime());
    }

    public Calendar getMyCalendar() {
        return myCalendar;
    }
}
