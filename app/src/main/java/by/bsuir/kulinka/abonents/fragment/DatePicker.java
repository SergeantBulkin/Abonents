package by.bsuir.kulinka.abonents.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    //----------------------------------------------------------------------------------------------
    //Поле для ссылки EditText
    private EditText endDateEditText;
    //----------------------------------------------------------------------------------------------
    public DatePicker(EditText endDateEditText)
    {
        this.endDateEditText = endDateEditText;
    }
    //----------------------------------------------------------------------------------------------
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(requireActivity(), this, year, month, day);
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth)
    {
        endDateEditText.setText("");

        //Показать читаемую дату
        endDateEditText.append(getNormalDate(dayOfMonth, month, year));
    }
    //----------------------------------------------------------------------------------------------
    //Метод для получения читаемой даты
    private String getNormalDate(int day, int month, int year)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyy", Locale.US);
        Calendar calendar = new GregorianCalendar(year, month, day);
        return dateFormat.format(calendar.getTime());
    }
    //----------------------------------------------------------------------------------------------
}
