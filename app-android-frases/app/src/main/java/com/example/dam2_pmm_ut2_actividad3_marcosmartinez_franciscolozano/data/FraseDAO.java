/**
 * Clase Data Access Object (DAO) que proporciona métodos para interactuar
 * con la tabla de frases de la base de datos SQLite.
 * Extiende de {@link FraseDBHelper} para gestionar la conexión y creación
 * de la base de datos.
 */
package com.example.dam2_pmm_ut2_actividad3_marcosmartinez_franciscolozano.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class FraseDAO extends FraseDBHelper{

    /**
     * Contexto de la aplicación, necesaria para inicializar el ayudante de la BD
     */
    private Context contexto;

    /**
     * Constructor de la clase FraseDAO
     * Llama al constructor de la clase padre (FraseDBHelper) para inicializar
     * la gestión de la base de datos y almacena el contexto
     * @param contexto El contexto de la aplicación
     */
    public FraseDAO(@Nullable Context contexto) {
        super(contexto);
        this.contexto = contexto;
    }

    /**
     * Recupera todas las frases almacenadas en la base de datos
     * @return Una {@link ArrayList} de objetos {@link Frase} que contiene todos
     * los registros de la tabla
     * Devuelve una lista vacía si no hay frases
     */
    public ArrayList<Frase> mostrarFrases() {

        ArrayList<Frase> listaFrases = new ArrayList<>();
        // Obtener una instancia de la base de datos en modo lectura
        SQLiteDatabase database = getReadableDatabase();
        Frase frase = null;

        // Ejecutar la consulta SQL para obtener todos los registros
        // DATABASE_TABLA debería ser una constante definida en FraseDBHelper o una clase relacionada
        Cursor cursorFrases = database.rawQuery("SELECT * FROM "+ DATABASE_TABLA, null);

        // Si el cursor tiene resultados (moveToFirst devuelve true)
        if (cursorFrases.moveToFirst()){
            // Iterar sobre cada fila del cursor
            do {
                frase = new Frase();
                // Asignar los valores del cursor a un nuevo objeto Frase
                // Se asume el orden de las columnas: ID (0), Autor (1), Texto (2)
                frase.setId(cursorFrases.getInt(0));
                frase.setAutor(cursorFrases.getString(1));
                frase.setTexto(cursorFrases.getString(2));
                listaFrases.add(frase);
            } while (cursorFrases.moveToNext()); // Moverse a la siguiente fila
        }
        // Cerrar el cursor para liberar recursos
        cursorFrases.close();

        return listaFrases;
    }

    /**
     * Inserta una nueva frase y su autor asociado en la base de datos
     * @param autor El nombre del autor de la frase
     * @param frase El contenido textual de la frase
     */
    public void insertarFrase(String autor, String frase) {

        // Uso de try-with-resources para asegurar que el ayudante de la BD se cierre correctamente
        try (FraseDBHelper ayudante = new FraseDBHelper(contexto)) {

            // Obtener la base de datos en modo escritura.
            SQLiteDatabase database = ayudante.getWritableDatabase();
            // Utilizar ContentValues para almacenar los pares clave-valor de las columnas
            ContentValues valores = new ContentValues();

            valores.put("autor",autor);
            valores.put("frase",frase); // Se asume que el nombre de la columna es "frase"

            // Insertar el nuevo registro en la tabla
            database.insert(DATABASE_TABLA, null, valores);
        } // El ayudante de la BD se cierra automáticamente aquí
    }

    /**
     * Elimina todos los registros de la base de datos
     */
    public void eliminarFrases() {

        // Uso de try-with-resources para asegurar que el ayudante de la BD se cierre correctamente
        try (FraseDBHelper ayudante = new FraseDBHelper(contexto)) {

            // Obtener la base de datos en modo escritura
            SQLiteDatabase database = ayudante.getWritableDatabase();

            // Eliminar todas las filas. El whereClause y whereArgs son null para eliminar todito
            database.delete(DATABASE_TABLA, null, null);
        } // El ayudante de la BD se cierra automáticamente aquí
    }
}