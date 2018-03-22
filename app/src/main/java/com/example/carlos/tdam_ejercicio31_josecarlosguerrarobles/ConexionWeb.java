package com.example.carlos.tdam_ejercicio31_josecarlosguerrarobles;


import android.os.AsyncTask;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ConexionWeb extends AsyncTask<URL,String,String> {
    List<String[]> variables; //vector dinamico
    AsyncResponse delegado;


    public ConexionWeb(AsyncResponse p){
        delegado = p;
        variables = new ArrayList<>();
    }


    public void agregarVariables(String nombreVariable, String contenidoVariable){
        String[] temporal ={nombreVariable,contenidoVariable};
        variables.add(temporal);
    }
    private String generarCadenaPost(){
        String post="";
        try {
            for (int i = 0; i < variables.size(); i++) {
                String[] temporal = variables.get(i);
                post += temporal[0] + "=" + URLEncoder.encode(temporal[1], "UTF-8") + " ";
            }
        }catch (Exception e){

        }
        post=post.trim();
        post = post.replaceAll(" ","&");
        return post;
    }
    @Override
    protected String doInBackground(URL... urls)
    {
        String POST = generarCadenaPost();
        String respuesta="";
        HttpURLConnection conexion = null;
        try{
            conexion = (HttpURLConnection) urls[0].openConnection();

            conexion.setDoOutput(true);
            conexion.setFixedLengthStreamingMode(POST.length());
            publishProgress("Enviando datos...");
            conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            OutputStream flujoSalida = new BufferedOutputStream(conexion.getOutputStream());
            flujoSalida.write(POST.getBytes());
            flujoSalida.flush();
            flujoSalida.close();

            if (conexion.getResponseCode()==200){
                publishProgress("recibiendo respuesta del servidor");
                InputStreamReader entrada = new InputStreamReader(conexion.getInputStream(),"UTF-8");
                BufferedReader flujoEntrada = new BufferedReader(entrada);
                String temp = "";
                do{
                    temp = flujoEntrada.readLine();
                    if (temp !=null){
                        respuesta+=temp;
                    }
                }while (temp!=null);
                flujoEntrada.close();

            }else{
                return "ERROR_400";
            }
        }catch (UnknownHostException e){

            respuesta = "ERROR_404";
        }catch (IOException e){
            respuesta = "ERROR_405";
        }finally {

            if (conexion !=null){
                conexion.disconnect();
            }
        }
        return respuesta;
    }
    public void onPostExecute(String r){

        delegado.procesarRespuesta(r);
    }
}
