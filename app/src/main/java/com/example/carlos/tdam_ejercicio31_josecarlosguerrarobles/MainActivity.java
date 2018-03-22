package com.example.carlos.tdam_ejercicio31_josecarlosguerrarobles;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements AsyncResponse{
    ConexionWeb con;
    TextView txtTemperatura, txtPresion, txtHumedad, txtAmanecer, txtPuesta, txtCoordenadas, txtClima;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtTemperatura = findViewById(R.id.txtTemperatura);
        txtPresion = findViewById(R.id.txtPresion);
        txtHumedad = findViewById(R.id.txtHumedad);
        txtAmanecer = findViewById(R.id.txtAmanecer);
        txtPuesta = findViewById(R.id.txtPuesta);
        txtCoordenadas = findViewById(R.id.txtCoordenadas);
        txtClima = findViewById(R.id.txtClima);
        fnWeatherConection();
    }

    @Override
    public void procesarRespuesta(String r) {
        try{
            JSONObject object = new JSONObject(r);
            JSONArray weather = object.getJSONArray("weather");

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < weather.length(); i++) {
                JSONObject objeto = weather.getJSONObject(i);
                String clima = objeto.getString("main");
                String des = objeto.getString("description");
                stringBuilder.append(clima+" : "+des+"         ");
            }
            JSONObject  main= object.getJSONObject("main");
            JSONObject  sys= object.getJSONObject("sys");
            JSONObject  coord= object.getJSONObject("coord");
            txtTemperatura.setText(""+main.getString("temp")+"*F");
            txtPresion.setText(""+main.getString("pressure"));
            txtHumedad.setText(main.getString("humidity"));

            txtAmanecer.setText(sys.getString("sunrise"));
            txtPuesta.setText(sys.getString("sunset"));

            txtCoordenadas.setText(coord.getString("lon")+","+coord.getString("lat"));

            txtClima.setText(""+stringBuilder);

        }catch (JSONException e){
            System.out.println(""+e);
        }

}

    public void fnWeatherConection(){
        try {
            con = new ConexionWeb(MainActivity.this);
            URL direcciopn = new URL("http://api.openweathermap.org/data/2.5/weather?q=Tepic,mx&APPID=c320e4eebb618b6e8e56062e83d8f23c");
            con.execute(direcciopn);
        } catch (MalformedURLException e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
