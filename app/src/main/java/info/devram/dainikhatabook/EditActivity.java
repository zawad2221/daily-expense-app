package info.devram.dainikhatabook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import info.devram.dainikhatabook.Models.Expense;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "EditActivity";

    private Spinner spinner;
    private EditText datePicker;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private ArrayAdapter<CharSequence> adapter;
    private EditText amountEditText;
    private EditText descEdittext;
    private int getAdapterPosition;
    private SimpleDateFormat sdf;
    private Expense expense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String activityTitle = getIntent().getStringExtra("title");

        setTitle(activityTitle);

        expense = (Expense) getIntent().getSerializableExtra(Expense.class.getSimpleName());

        if (activityTitle.toLowerCase().equals("edit expense")) {
            adapter = ArrayAdapter
                    .createFromResource(this,
                            R.array.expense_type, android.R.layout.simple_spinner_item);
        }else {
            adapter = ArrayAdapter
                    .createFromResource(this,
                            R.array.income_type, android.R.layout.simple_spinner_item);
        }

        for (int i = 0; i < adapter.getCount(); i++) {

            String adapterItem = adapter.getItem(i).toString();

            Log.d(TAG, "onCreate: " + adapterItem.equalsIgnoreCase(expense.getExpenseType()));
            if (adapterItem.equals(expense.getExpenseType())) {
                getAdapterPosition = i;
                break;
            }
        }


        spinner = findViewById(R.id.edit_spinner);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        datePicker = findViewById(R.id.editDateView);
        Button editItem = findViewById(R.id.editActivityBtn);
        amountEditText = findViewById(R.id.editAmountView);
        descEdittext = findViewById(R.id.editDescView);
        String myFormat = "dd/MM/yy";
        sdf = new SimpleDateFormat(myFormat, Locale.CANADA);


        populateUI();

        myCalendar = Calendar.getInstance();

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
                new DatePickerDialog(v.getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = spinner.getSelectedItem().toString();
                int amount = 0;
                long checkedDate = 0;
                try {
                    amount = Integer.parseInt(amountEditText.getText().toString());
                    Date selectedDate = sdf.parse(datePicker.getText().toString());
                    if (selectedDate != null) {
                        checkedDate = selectedDate.getTime();
                    }
                }catch (NumberFormatException e) {
                    Log.e(TAG, "onClick parsing string to int " + e.getMessage());
                }catch (ParseException e) {
                    Log.e(TAG, "date parsing error " + e.getMessage());
                }
                String desc = descEdittext.getText().toString();
                Intent resultIntent = getIntent();
                expense.setExpenseType(type);
                expense.setExpenseAmount(amount);
                expense.setExpenseDate(checkedDate);
                expense.setExpenseDesc(desc);
                resultIntent.putExtra(Expense.class.getSimpleName(),expense);
                setResult(1, resultIntent);
                finish();
            }
        });
    }

    private void populateUI() {
        Log.d(TAG, "populateUI: " + expense);
        spinner.setSelection(getAdapterPosition);
        Long date = (long) expense.getExpenseDate();
        datePicker.setText(sdf.format(date));
        amountEditText.setText(String.valueOf(expense.getExpenseAmount()));
        descEdittext.setText(expense.getExpenseDesc());
    }

    private void updateLabel() {
        datePicker.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}