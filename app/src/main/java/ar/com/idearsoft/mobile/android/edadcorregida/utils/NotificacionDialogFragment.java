package ar.com.idearsoft.mobile.android.edadcorregida.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by Juanjo on 11/9/2016.
 */
public class NotificacionDialogFragment extends DialogFragment {

    private String titulo = "";
    private String mensaje = "";
    private String textoAceptar = "";

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTextoAceptar() {
        return textoAceptar;
    }

    public void setTextoAceptar(String textoAceptar) {
        this.textoAceptar = textoAceptar;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(getMensaje())
                .setTitle(getTitulo())
                .setPositiveButton(getTextoAceptar(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }
}
