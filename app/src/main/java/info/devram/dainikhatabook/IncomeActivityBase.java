package info.devram.dainikhatabook;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import info.devram.dainikhatabook.Models.Income;
import info.devram.dainikhatabook.ui.BaseAddActivity;

public class IncomeActivityBase extends BaseAddActivity {

    private static final String TAG = "IncomeActivity";

    private EditText datePicker;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private ArrayAdapter<CharSequence> adapter;

    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Add Incomes");

        adapter = ArrayAdapter
                .createFromResource(this,
                        R.array.income_type, android.R.layout.simple_spinner_item);

        final Spinner spinner = findViewById(R.id.addNew_spinner);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        setupBase();

        Button addNew = findViewById(R.id.add_new_btn);

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = spinner.getSelectedItem().toString();
                parseData();
                Intent resultIntent = getIntent();
                Income income = new Income();
                income.setIncomeType(type);
                income.setIncomeAmount(selectedAmount);
                income.setIncomeDate(parsedDate);
                income.setIncomeDesc(selectedDesc);
                resultIntent.putExtra(Income.class.getSimpleName(),income);
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


}