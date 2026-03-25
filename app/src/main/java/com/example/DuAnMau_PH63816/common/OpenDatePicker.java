package com.example.DuAnMau_PH63816.common;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OpenDatePicker {
    public static void openDatePicker(Context context, TextView textView) {
        Calendar calendar = Calendar.getInstance();
        String currentText = textView.getText().toString().trim();
        try {
            if (!currentText.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                calendar.setTime(sdf.parse(currentText));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        new DatePickerDialog(
                context,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    String format = "dd-MM-yyyy";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
                    textView.setText(simpleDateFormat.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
}
