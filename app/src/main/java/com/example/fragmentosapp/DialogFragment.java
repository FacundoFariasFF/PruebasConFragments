package com.example.fragmentosapp;



import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDate;

public class DialogFragment extends androidx.fragment.app.DialogFragment {
    boolean[] checks = new boolean[7];
    LocalDate diaActual= LocalDate.now();
    public  DialogFragment(){

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Cotizaciones del dia: "+diaActual +"."+ '\n'+ "Seleccione las que desea ver.");

            View selector = getLayoutInflater().inflate(R.layout.fragment_dialog, null);
            builder.setView(selector);
            String[] monedas = {"Dolar Oficial", "Dolar Blue", "Dolar Soja", "Dolar Contado Con liqui", "Dolar Bolsa",
                    "Bitcoin", "Dolar Turista"};
            //boolean[] checks = new boolean[7];
        builder.setMultiChoiceItems(monedas, checks, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                //*String todo = "";
                for (int i=0;i<checks.length;i++){
                    if (checks[i]){

                        // todo=todo+monedas[i]+"-";
                    }
                }
            }
        });

        builder.setPositiveButton("confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                respuestas.confirmar(DialogFragment.this);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                respuestas.cancelar(DialogFragment.this);

            }
        });
        return builder.create();
    }

    public interface Respuestas{
        public void confirmar(DialogFragment dialog);
        public void cancelar(DialogFragment dialog);

    }

    private Respuestas respuestas;

    public void ProcesarRespuesta(Respuestas r){

        respuestas = r;
    }





}
