/**
 * Clase ayudante (Helper) para la gestión de la base de datos SQLite
 * Extiende de {@link SQLiteOpenHelper} para manejar automáticamente
 * la creación, apertura y actualización de la base de datos de frases
 */
package com.example.dam2_pmm_ut2_actividad3_marcosmartinez_franciscolozano.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class FraseDBHelper extends SQLiteOpenHelper {

    /**
     * Nombre del archivo de la base de datos
     */
    private static final String DATABASE_NOMBRE = "frasesMotivadoras.db";

    /**
     * Versión de la base de datos
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Nombre de la tabla principal donde se almacenarán las frases
     * Es 'protected' para que sea accesible desde {@link FraseDAO}
     */
    protected static final String DATABASE_TABLA = "frases";

    /**
     * Constructor de la clase FraseDBHelper
     * @param context El contexto de la aplicación (e.g., Activity o Application)
     */
    public FraseDBHelper(@Nullable Context context) {
        // Llama al constructor de la clase padre (SQLiteOpenHelper)
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }

    /**
     * Llamado cuando la base de datos es creada por primera vez
     * Aquí se definen y se ejecutan las sentencias SQL para crear las tablas
     * @param db La instancia de la base de datos
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Sentencia SQL para crear la tabla 'frases'
        // Define tres columnas: id (clave primaria autoincrementable), autor (texto) y frase (texto no nulo)
        String crearQuery = "CREATE TABLE " + DATABASE_TABLA + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "autor TEXT," +
                "frase TEXT NOT NULL)";

        // Ejecutar la sentencia de creación de la tabla
        db.execSQL(crearQuery);
    }

    /**
     * Llamado cuando la versión de la base de datos ha sido incrementada
     * Contiene la lógica para actualizar el esquema de la base de datos
     * En este caso, simplemente elimina la tabla existente y la recrea
     * @param db La instancia de la base de datos
     * @param oldVersion El número de la versión antigua
     * @param newVersion El número de la versión nueva
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar la tabla existente si existe
        db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TABLA);
        // Recrear la tabla llamando a onCreate
        onCreate(db);
    }
}