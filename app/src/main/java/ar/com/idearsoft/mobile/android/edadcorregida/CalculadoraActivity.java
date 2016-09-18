package ar.com.idearsoft.mobile.android.edadcorregida;

//import android.icu.text.SimpleDateFormat;
//import android.icu.util.Calendar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ar.com.idearsoft.mobile.android.edadcorregida.utils.NotificacionDialogFragment;

public class CalculadoraActivity extends AppCompatActivity {

    private static final String TAG = "CalculadoraActivity";

    private DatePicker datePicker;
    private Calendar calendar;
    private EditText fechaNacimiento, fechaControl, edadGestacional, terminoGestacional;
    private SimpleDateFormat sdf;
    private int anio, mes, dia;
    private boolean fechaNacSeleccionada = true;
    private android.support.v4.app.FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fm = getSupportFragmentManager();

        calendar = Calendar.getInstance();
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        sdf = new SimpleDateFormat("dd/MM/yyyy");

        fechaNacimiento = (EditText) findViewById(R.id.activity_calculadora_edittext_fecha_nacimiento);
        fechaControl = (EditText) findViewById(R.id.activity_calculadora_edittext_fecha_control);
        edadGestacional = (EditText) findViewById(R.id.activity_calculadora_edittext_edad_gestacional);
        terminoGestacional = (EditText) findViewById(R.id.activity_calculadora_edittext_termino_gestacional);

        fechaNacimiento.setText(sdf.format(calendar.getTime()));
        fechaControl.setText(sdf.format(calendar.getTime()));

        fechaNacimiento.clearFocus();
        fechaControl.clearFocus();
        edadGestacional.requestFocus();
        terminoGestacional.clearFocus();

        fechaNacimiento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //Log.i(TAG, "Click en fecha de nacimiento.");
                if (hasFocus) {
                    fechaNacSeleccionada = true;
                    showDialog(777);
                }
            }
        });

        fechaControl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //Log.i(TAG, "Click en fecha de control.");
                if (hasFocus) {
                    fechaNacSeleccionada = false;
                    showDialog(777);
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificacionDialogFragment ndf = new NotificacionDialogFragment();
                ndf.setTitulo("Error");
                ndf.setTextoAceptar("Aceptar");
                switch (validarDatos()) {
                    case -1:
                        //Toast.makeText(CalculadoraActivity.this, "[Error]: La fecha de control debe ser posterior a la fecha de nacimiento.", Toast.LENGTH_LONG).show();
                        ndf.setMensaje("La fecha de control debe ser posterior a la fecha de nacimiento.");
                        ndf.show(fm, "tagErrorFechas");
                        break;
                    case -2:
                        //Toast.makeText(CalculadoraActivity.this, "[Error]: Edad gestacional fuera de rango.", Toast.LENGTH_LONG).show();
                        ndf.setMensaje("Edad gestacional fuera de rango.");
                        ndf.show(fm, "tagErrorEdadGestacional");
                        break;
                    case -3:
                        //Toast.makeText(CalculadoraActivity.this, "[Error]: Termino gestacional fuera de rango.", Toast.LENGTH_LONG).show();
                        ndf.setMensaje("Termino gestacional fuera de rango.");
                        ndf.show(fm, "tagErrorTerminoGestacional");
                        break;
                    case 1:
                        //Toast.makeText(CalculadoraActivity.this, "[Notificación]: Calculando...", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(CalculadoraActivity.this, ResultadoActivity.class);
                        i.putExtra("FechaNacimiento", fechaNacimiento.getText().toString());
                        i.putExtra("FechaControl", fechaControl.getText().toString());
                        i.putExtra("EdadGestacional", edadGestacional.getText().toString());
                        i.putExtra("TerminoGestacional", terminoGestacional.getText().toString());
                        startActivity(i);
                        break;
                }
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 777) {
            return new DatePickerDialog(this, onDateSelected, anio, mes, dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener onDateSelected = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            //showDate(arg1, arg2+1, arg3);
            anio = arg1;
            mes = arg2 + 1;
            dia = arg3;
            String anioStr = String.valueOf(anio);
            String mesStr = String.valueOf(mes);
            String diaStr = String.valueOf(dia);
            if (mes < 10) {
                mesStr = "0" + mesStr;
            }
            if (dia < 10) {
                diaStr = "0" + diaStr;
            }
            String fechaStr = diaStr + "/" + mesStr + "/" + anioStr;
            if (fechaNacSeleccionada) {
                fechaNacimiento.setText(fechaStr);
            } else {
                fechaControl.setText(fechaStr);
            }
        }
    };

    private int validarDatos() {
        //Valido fechaNacimiento y fechaControl
        Date fNacimiento = null;
        try {
            fNacimiento = sdf.parse(fechaNacimiento.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date fControl = null;
        try {
            fControl = sdf.parse(fechaControl.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int fComparacion = 2;
        try {
            fComparacion = fNacimiento.compareTo(fControl);
        } catch (Exception ex) {

        }
        //Log.i(TAG, "Fecha de nacimiento: " + sdf.format(fNacimiento));
        //Log.i(TAG, "Fecha de control: " + sdf.format(fControl));
        //Log.i(TAG, "Resultado comparación de fecha nacimiento y fecha control: " + String.valueOf(fComparacion));
        if (fNacimiento == null || fControl == null || fComparacion > -1) {
            return -1;
        }

        //Valido edad gestacional
        if (edadGestacional.getText().toString().trim().length() < 1) {
            return -2;
        }
        int eG = -1;
        try {
            eG = Integer.parseInt(edadGestacional.getText().toString().trim());
        } catch (Exception ex) {

        }
        if (eG < 20 || eG > 36) {
            return -2;
        }

        //Valido termino gestacionl
        if (terminoGestacional.getText().toString().trim().length() < 1) {
            return -3;
        }
        int tG = -1;
        try {
            tG = Integer.parseInt(terminoGestacional.getText().toString().trim());
        } catch (Exception ex) {

        }
        if (tG < 37 || tG > 40) {
            return -3;
        }

        return 1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calculadora, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_info) {
            NotificacionDialogFragment ndf = new NotificacionDialogFragment();
            ndf.setTitulo(getResources().getString(R.string.global_info_app_titulo));
            ndf.setTextoAceptar("Aceptar");
            ndf.setMensaje(getResources().getString(R.string.global_info_app_texto));
            ndf.show(fm, "tagInformacionApp");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
