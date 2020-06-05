package info.devram.dailyexpenses.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import info.devram.dailyexpenses.R;

public class SaveDataDialog extends DialogFragment {

    private Spinner spinner;
    private EditText datePicker;
    private Calendar myCalendar;
    private ArrayAdapter<CharSequence> adapter;
    private DatePickerDialog.OnDateSetListener date;
    private AlertDialog.Builder builder;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();


        View view = inflater.inflate(R.layout.modal,null);

        builder.setView(view);

        spinner = view.findViewById(R.id.spinner);

        //Log.d("spinner", "onCreate: " + spinner);



        adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.expense_type,android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        datePicker = view.findViewById(R.id.edit_text_expense);

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
                // TODO Auto-generated method stub
                Log.i("date", "onClick: " + myCalendar);
                new DatePickerDialog(v.getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        return builder.create();
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA_FRENCH);

        datePicker.setText(sdf.format(myCalendar.getTime()));
    }
}
