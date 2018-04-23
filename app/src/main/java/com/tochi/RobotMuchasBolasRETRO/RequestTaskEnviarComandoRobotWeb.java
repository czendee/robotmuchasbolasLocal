package com.tochi.RobotMuchasBolasRETRO;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
/**
 * Created by 813743 on 25/11/2017.
 */
public class RequestTaskEnviarComandoRobotWeb extends AsyncTask<TextView, String, String> {


    TextView t;
    String result = "fail";
    String nombreRutina=null;
    String secuencia=null;
    String comando=null;
    String tiempo=null;

    @Override
    protected String doInBackground(TextView... params) {
        System.out.println("doInBackground - 1");
        this.t = params[0];
        System.out.println("doInBackground - 2");
        return AddNuevaComandoRobot(this.nombreRutina,this.secuencia,this.comando, this.tiempo);


    }

    final String AddNuevaComandoRobot(
            String nombreRutina,
            String secuencia,
            String comando,
            String tiempo
    )
    {
        System.out.println("AddNuevaComandoRobotWeb - 1");

        Constants constantValues=new Constants();
        //Create JSONObject here
        JSONObject jsonParam = new JSONObject();
        try{


            jsonParam.put("nombrerobot", nombreRutina);
            jsonParam.put("secuencia", secuencia);
            jsonParam.put("comando", comando);
            jsonParam.put("tiempo", tiempo);

        }catch(Exception e){
            System.out.println("AddNuevaComandoRobotWeb - 12. error de json");
            e.printStackTrace();
        }
        //esto se usaba para cuando se mandan parametros
        try{
            nombreRutina= URLEncoder.encode(nombreRutina, "UTF-8");
            secuencia= URLEncoder.encode(secuencia, "UTF-8");
            comando= URLEncoder.encode(comando, "UTF-8");
            tiempo= URLEncoder.encode(tiempo, "UTF-8");


        }catch(UnsupportedEncodingException e){
            System.out.println("AddNuevaComandoRobotWeb - 12 error");
            e.printStackTrace();
        }


        String urlStr = constantValues.URL_ADD_COMANDO_ROBOT;

        BufferedReader inStream = null;



        try{

            System.out.println("AddNuevaComandoRobotWeb - sql:"+urlStr);
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            wr.write(jsonParam.toString());
            wr.flush();

            try {

                //display what returns the POST request

                StringBuilder sb = new StringBuilder();
                int HttpResult = urlConnection.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {

                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    //readStream(in);

                    inStream = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    StringBuffer buffer = new StringBuffer("");
                    String line = "";
                    System.out.println("AddNuevaComandoRobotWeb - 7");
                    String NL = System.getProperty("line.separator");
                    System.out.println("AddNuevaComandoRobotWeb - 8");
                    while ((line = inStream.readLine()) != null) {
                        buffer.append(line + NL);
                    }
                    System.out.println("AddNuevaComandoRobotWeb - 9");
                    inStream.close();
                    System.out.println("AddNuevaComandoRobotWeb - 10");
                    result = buffer.toString();
                    System.out.println("GetSomething - 11");
                } else {
                    System.out.println(urlConnection.getResponseMessage());
                }



            } finally {
                urlConnection.disconnect();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("AddNuevaComandoRobotWeb - 12 error");
            e.printStackTrace();
        } finally {
            System.out.println("AddNuevaComandoRobotWeb - 13");
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



        System.out.println("GetSomething - 14");
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println("AddNuevaComandoRobotWeb onPostExecute - 10");
//        t.setText(result);
        System.out.println("AddNuevaComandoRobotWeb onPostExecute - 20");
    }


}
