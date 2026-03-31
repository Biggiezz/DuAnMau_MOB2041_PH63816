package com.example.DuAnMau_PH63816.common;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OpenDatePicker {
    public static void openDatePicker(Context context, TextView textView) {
        Calendar calendar = Calendar.getInstance();
        String currentText = textView.getText().toString().trim();
        if (!currentText.isEmpty()) {
            Date parsedDate = parseDate(currentText);
            if (parsedDate != null) {
                calendar.setTime(parsedDate);
            }
        }
        new DatePickerDialog(
                context,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    textView.setText(formatDate(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    public static Date parseDate(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String normalized = value.trim().replace('/', '-');
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            simpleDateFormat.setLenient(false);
            return simpleDateFormat.parse(normalized);
        } catch (ParseException exception) {
            return null;
        }
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        return simpleDateFormat.format(date);
    }
}
