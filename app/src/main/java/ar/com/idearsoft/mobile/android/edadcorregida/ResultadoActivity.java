package ar.com.idearsoft.mobile.android.edadcorregida;

//import android.icu.text.SimpleDateFormat;
//import android.icu.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import ar.com.idearsoft.mobile.android.edadcorregida.utils.NotificacionDialogFragment;

public class ResultadoActivity extends AppCompatActivity {

    private static final String TAG = "ResultadoActivity";
    private SimpleDateFormat sdf;
    private TextView aniosCronologicoTextView, mesesCronologicoTextView, semanasCronologicoTextView, diasCronologicoTextView;
    private TextView aniosCorregidoTextView, mesesCorregidoTextView, semanasCorregidoTextView, diasCorregidoTextView;
    private android.support.v4.app.FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fm = getSupportFragmentManager();

        Intent i = getIntent();
        String fechaNacimientoStr = i.getStringExtra("FechaNacimiento");
        String fechaControlStr = i.getStringExtra("FechaControl");
        String edadGestacionalStr = i.getStringExtra("EdadGestacional");
        String terminoGestacionalStr = i.getStringExtra("TerminoGestacional");

        sdf = new SimpleDateFormat("dd/MM/yyyy");

        aniosCronologicoTextView = (TextView) findViewById(R.id.activity_resultado_edad_cronologica_anios);
        mesesCronologicoTextView = (TextView) findViewById(R.id.activity_resultado_edad_cronologica_meses);
        semanasCronologicoTextView = (TextView) findViewById(R.id.activity_resultado_edad_cronologica_semanas);
        diasCronologicoTextView = (TextView) findViewById(R.id.activity_resultado_edad_cronologica_dias);

        aniosCorregidoTextView = (TextView) findViewById(R.id.activity_resultado_edad_corregida_anios);
        mesesCorregidoTextView = (TextView) findViewById(R.id.activity_resultado_edad_corregida_meses);
        semanasCorregidoTextView = (TextView) findViewById(R.id.activity_resultado_edad_corregida_semanas);
        diasCorregidoTextView = (TextView) findViewById(R.id.activity_resultado_edad_corregida_dias);

        calcularEdadCorregida(fechaNacimientoStr, fechaControlStr, edadGestacionalStr, terminoGestacionalStr);

    }

    public int dias(String date1Str,String date2Str)
    {
        Date date1 = null;
        Date date2 = null;
        try{
            date1 = sdf.parse(date1Str);
            date2 = sdf.parse(date2Str);
        }catch(Exception e)
        {
            e.printStackTrace();
            return -777;
        }
        Log.i(TAG, "Date 1 Str: " + date1Str);
        Log.i(TAG, "Date 1: " + String.valueOf(date1.getTime()));
        Log.i(TAG, "Date 2 Str: " + date2Str);
        Log.i(TAG, "Date 2: " + String.valueOf(date2.getTime()));
        return (int) ((date2.getTime()-date1.getTime())/(24*60*60*1000));
    }

    private void calcularEdadCorregida(String fNacStr, String fConStr, String eGesStr, String tGesStr) {
        int diasCronologico = dias(fNacStr, fConStr);
        if (diasCronologico == -777) {
            Toast.makeText(ResultadoActivity.this, "[Error]: Ocurrio un error en el cálculo.", Toast.LENGTH_LONG).show();
            return;
        }
        Log.i(TAG, "[Edad Cronológica]: Total días = " + String.valueOf(diasCronologico));
        int diasCronologicoCopia = diasCronologico;
        int aniosCronologico = diasCronologico/365;
        diasCronologico -= aniosCronologico*365;
        int mesesCronologico = diasCronologico/30;
        diasCronologico -= mesesCronologico*30;
        int semanasCronologico = diasCronologico/7;
        diasCronologico -= semanasCronologico*7;

        aniosCronologicoTextView.setText(String.valueOf(aniosCronologico));
        mesesCronologicoTextView.setText(String.valueOf(mesesCronologico));
        semanasCronologicoTextView.setText(String.valueOf(semanasCronologico));
        diasCronologicoTextView.setText(String.valueOf(diasCronologico));

        int eGesInt = -1;
        int tGesInt = -1;

        try {
            eGesInt = Integer.parseInt(eGesStr)*7;
            tGesInt = Integer.parseInt(tGesStr)*7;
        } catch (Exception ex) {
            return;
        }

        int difPrematuroDias = tGesInt - eGesInt;

        int diasCorregida = diasCronologicoCopia - difPrematuroDias;
        int aniosCorregida = diasCorregida/365;
        diasCorregida -= aniosCorregida*365;
        int mesesCorregida = diasCorregida/30;
        diasCorregida -= mesesCorregida*30;
        int semanasCorregida = diasCorregida/7;
        diasCorregida -= semanasCorregida*7;

        aniosCorregidoTextView.setText(String.valueOf(aniosCorregida));
        mesesCorregidoTextView.setText(String.valueOf(mesesCorregida));
        semanasCorregidoTextView.setText(String.valueOf(semanasCorregida));
        diasCorregidoTextView.setText(String.valueOf(diasCorregida));

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
