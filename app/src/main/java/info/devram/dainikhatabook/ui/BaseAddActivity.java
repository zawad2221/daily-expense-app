package info.devram.dainikhatabook.ui;

import android.app.DatePickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import info.devram.dainikhatabook.R;

public class BaseAddActivity extends AppCompatActivity {

    //private static final String TAG = "AddActivity";

    protected EditText datePicker;
    protected EditText amountEditText;
    protected EditText descEditText;
    protected Calendar myCalendar;
    protected DatePickerDialog.OnDateSetListener date;
    protected SimpleDateFormat sdf;
    protected int selectedAmount;
    protected long parsedDate;
    protected String selectedDesc;
    protected Spinner spinner;
    protected String selectedType;

    protected void activateToolbar(boolean enableHome) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            Toolbar toolbar = findViewById(R.id.toolbar);

            if (toolbar != null) {
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
            }
        }

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(enableHome);
        }
    }

    protected void setupSpinner(ArrayAdapter<String> arrayAdapter) {
        spinner = findViewById(R.id.addNew_spinner);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);

    }

    protected void setupBase() {
        datePicker = findViewById(R.id.edit_text_date);
        amountEditText = findViewById(R.id.edit_text_amount);
        descEditText = findViewById(R.id.edit_text_desc);
        String myFormat = "dd/MM/yy";
        sdf = new SimpleDateFormat(myFormat, Locale.CANADA);

        myCalendar = Calendar.getInstance();
        updateLabel();

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(v.getContext(),R.style.datePicker ,date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

    }

    private void updateLabel() {

        datePicker.setText(sdf.format(myCalendar.getTime()));
    }

    protected void parseData() {
        selectedType = spinner.getSelectedItem().toString();
        String selectedDate = datePicker.getText().toString();
        try {
            selectedAmount = Integer.parseInt(amountEditText.getText().toString());
            Date dateOBJ = sdf.parse(selectedDate);
            if (dateOBJ != null) {
                parsedDate = dateOBJ.getTime();
            }
        }catch (NumberFormatException | ParseException e) {

        }
        selectedDesc = descEditText.getText().toString();
    }
}
