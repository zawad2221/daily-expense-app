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

public class ExpenseActivity extends AppCompatActivity {

    private static final String TAG = "ExpenseActivity";

    private EditText datePicker;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;    private ArrayAdapter<CharSequence> adapter;
    private String myFormat;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Add Expense");

        adapter = ArrayAdapter
                .createFromResource(this,
                        R.array.expense_type, android.R.layout.simple_spinner_item);

        final Spinner spinner = findViewById(R.id.addNew_spinner);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        datePicker = findViewById(R.id.edit_text_date);
        Button addNew = findViewById(R.id.add_new_btn);
        final EditText amountEditText = findViewById(R.id.edit_text_amount);
        final EditText descEdittext = findViewById(R.id.edit_text_desc);
        myFormat = "dd/MM/yy";
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });


        addNew.setOnClickListener(new View.OnClickListener() {
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
                Expense expense = new Expense();
                expense.setExpenseType(type);
                expense.setExpenseAmount(amount);
                expense.setExpenseDate(checkedDate);
                expense.setExpenseDesc(desc);
                resultIntent.putExtra(Expense.class.getSimpleName(),expense);
                setResult(1,resultIntent);
                finish();
            }
        });

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

    private void updateLabel() {

        datePicker.setText(sdf.format(myCalendar.getTime()));
    }
}