package com.example.fragmentosapp;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class DialogCalendario extends DialogFragment {
    static LocalDate fechaSeleccionada;
    public DialogCalendario(){
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendario = Calendar.getInstance();
        int year = calendario.get(Calendar.YEAR);
        int month = calendario.get(Calendar.MONTH);
        int dayOfMonth = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                fechaSeleccionada = LocalDate.of(year,(month + 1),dayOfMonth);
                respuestasCalendario.ConfirmarCalendario(DialogCalendario.this);
            }
        },year,month,dayOfMonth);

        /*dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Seleccionar",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        respuestasCalendario.ConfirmarCalendario(DialogCalendario.this);
                    }
                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        respuestasCalendario.CancelarCalendario(DialogCalendario.this);
                    }
                });*/

        //calendario.set(2023, 2, 1);//Year,Mounth -1,Day
        calendario.set(2002, 3, 9);//Year,Mounth -1,Day
        dialog.getDatePicker().setMinDate(calendario.getTimeInMillis());
        calendario.set(year,month,dayOfMonth);
        dialog.getDatePicker().setMaxDate(calendario.getTimeInMillis());

        dialog.show();
        return dialog;
    }

    public interface RespuestasCalendario{
        public void ConfirmarCalendario(DialogFragment dialogCalendario);
        public void CancelarCalendario(DialogFragment dialogCalendario);
    }
    private RespuestasCalendario respuestasCalendario;

    public void ProcesarRespuestaCalendario(RespuestasCalendario r){
        respuestasCalendario=r;
    }

}
