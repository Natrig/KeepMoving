package rus.app.keepmoving.Util;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import rus.app.keepmoving.R;

public class KPDatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public Date startFrom;
    public Date minDate;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current day as the default values for the picker
        int year = 1997;
        int month = 0;
        int day = 1;

        if (startFrom != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(startFrom);

            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth, this, year, month, day);

        if (minDate != null) {
            dpd.getDatePicker().setMinDate(minDate.getTime());
        }

        return dpd;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        EditText dateInput = (EditText) getActivity().findViewById(R.id.yearInput);

        Calendar c = new GregorianCalendar();
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        dateInput.setText(sdf.format(c.getTime()));
    }
}