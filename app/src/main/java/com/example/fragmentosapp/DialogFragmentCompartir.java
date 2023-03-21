package com.example.fragmentosapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DialogFragmentCompartir extends DialogFragment{

    public String mensaje;

    public DialogFragmentCompartir(String mensaje){
        this.mensaje= mensaje;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mensaje);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                respuestaCompartir.Confirmar(DialogFragmentCompartir.this);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                respuestaCompartir.Cancelar(DialogFragmentCompartir.this);
            }
        });
        return builder.create();
    }

    public interface RespuestasCompartir{
        public void Confirmar(DialogFragment dialog);
        public void Cancelar(DialogFragment dialog);
    }
    private RespuestasCompartir respuestaCompartir;

    public void  ProcesarRespuestaCompartir(RespuestasCompartir r){
        respuestaCompartir = r;
    }




}
