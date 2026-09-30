package com.example.dam2_pmm_ut2_actividad3_marcosmartinez_franciscolozano.net;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.example.dam2_pmm_ut2_actividad3_marcosmartinez_franciscolozano.data.FraseDAO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils extends Thread{
    private static final String URL = "https://zenquotes.io/api/random";
    public static void descargarFrase(Context contexto) throws Exception {
        new Thread(() -> {
            // Looper.prepare (sacado del LogCat) sirve para poder avisar al hilo de que vamos a usar un Toast
            Looper.prepare();
            try {
                URL url = new URL(URL);
                // Abrir una conexión HTTP hacia esa URL.
                // El resultado es un HttpURLConnection que permite configurar y enviar la petición.
                HttpURLConnection conexion = (HttpURLConnection) url.openConnection();

                // Indicamos que la petición será de tipo GET (solicitar datos al servidor).
                conexion.setRequestMethod("GET");
                // Tiempo máximo para establecer la conexión (en milisegundos).
                // Si el servidor no responde en 3 segundos, lanza un error de timeout.
                conexion.setConnectTimeout(3000);
                // Tiempo máximo para leer la respuesta del servidor.
                // Si tarda más de 5 segundos leyendo datos, también lanza timeout.
                conexion.setReadTimeout(5000);

                conexion.connect();

                // Si el servidor no responde OK, lanzamos un mensaje de espera
                if (conexion.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Toast.makeText(contexto, "Espere un momento para la siguiente peticion...", Toast.LENGTH_SHORT).show();
                }

                // Leer la respuesta del servidor
                // Aquí almacenaremos todito el texto que el servidor envíe como respuesta.
                StringBuilder respuesta = new StringBuilder();

                // Abrimos un BufferedReader para leer la respuesta del servidor línea por línea.
                // El try-with-resources garantiza que el lector se cierre automáticamente.
                try (BufferedReader lector = new BufferedReader(new InputStreamReader(conexion.getInputStream()))) {
                    String linea;
                    // Leemos cada línea que envía el servidor hasta que no haya más.
                    // Cada línea se va agregando al StringBuilder 'respuesta'.
                    while ((linea = lector.readLine()) != null) {
                        respuesta.append(linea);
                    }
                }

                // Convertir la respuesta completa (que está en un StringBuilder) a un texto normal
                // y luego interpretarlo como un arreglo JSON (JSONArray).
                // Esto significa que la respuesta del servidor debe tener un formato del tipo:
                // [ { "clave": "valor", ... } ]
                JSONArray arr = new JSONArray(respuesta.toString());
                // Obtener el primer objeto JSON dentro del arreglo.
                // Si la respuesta trae varios elementos, aquí tomamos solo el que está en la posición 0.
                // Ese objeto es el que contiene los datos que queremos leer.
                JSONObject obj = arr.getJSONObject(0);
                // Extrae del objeto JSON el valor asociado a la clave "a" (autor).
                // Si la clave no existe, devuelve una cadena vacía en lugar de lanzar un error.
                String autor = obj.optString("a", "");
                // Extrae del objeto JSON el valor asociado a la clave  "q" (texto)..
                // Si la clave no existe, devuelve una cadena vacía en lugar de lanzar un error.
                String texto = obj.optString("q", "");

                conexion.disconnect();

                // Generamos la frase con los datos y la insertamos en la BBDD
                FraseDAO fraseDAO = new FraseDAO(contexto);
                fraseDAO.insertarFrase(autor,texto);

                // catch vacio para que en caso de error no haga nada
            } catch (Exception e) {}
        }).start();
    }
}