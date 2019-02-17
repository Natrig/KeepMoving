package rus.app.keepmoving.Util;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import rus.app.keepmoving.R;

public class KPDatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current day as the default values for the picker
        int year = 1997;
        int month = 0;
        int day = 1;

        return new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, this, year, month, day);
    }

    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        EditText dateInput = (EditText) getActivity().findViewById(R.id.yearInput);
        Calendar c = new GregorianCalendar();
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        dateInput.setText(sdf.format(c.getTime()));
    }
}