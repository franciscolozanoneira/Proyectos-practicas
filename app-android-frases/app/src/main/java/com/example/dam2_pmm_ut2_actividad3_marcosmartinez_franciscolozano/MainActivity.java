/**
 * Actividad principal de la aplicación, encargada de la interfaz de usuario
 * y la gestión de la lógica de presentación.
 * Muestra frases motivadoras en un RecyclerView, permitiendo al usuario
 * descargarlas, mostrarlas/ocultarlas y eliminarlas de la base de datos local.
 */
package com.example.dam2_pmm_ut2_actividad3_marcosmartinez_franciscolozano;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast; // Importación no utilizada en el código proporcionado, pero útil para mensajes

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dam2_pmm_ut2_actividad3_marcosmartinez_franciscolozano.data.Frase;
import com.example.dam2_pmm_ut2_actividad3_marcosmartinez_franciscolozano.data.FraseDAO;
import com.example.dam2_pmm_ut2_actividad3_marcosmartinez_franciscolozano.net.HttpUtils;
import com.example.dam2_pmm_ut2_actividad3_marcosmartinez_franciscolozano.ui.FrasesAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /**
     * Componente RecyclerView para mostrar la lista de frases
     */
    private RecyclerView listaFrases;

    /**
     * Adaptador para el RecyclerView, gestiona cómo se muestran los datos de {@link Frase}
     */
    private FrasesAdapter adaptador;


    /**
     * Metodito llamado cuando se crea la actividad por primera vez
     * Realiza la inicialización de la interfaz de usuario, el RecyclerView y los listeners de botones
     * @param savedInstanceState Si la actividad se está recreando a partir de un estado guardado,
     * este es el estado
     * En caso contrario, es nulo
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Habilita el modo EdgeToEdge para el manejo de los bordes de la pantalla
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Manejo de Insets para asegurar que el contenido se dibuje correctamente
        // alrededor de las barras del sistema (barra de estado, barra de navegación)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialización del RecyclerView y su LayoutManager
        listaFrases = findViewById(R.id.recycler);
        listaFrases.setLayoutManager(new LinearLayoutManager(this));

        // Inicialización de los botones de la interfaz.
        Button botonDescargar = findViewById(R.id.btnDescargar);
        Button botonMostrar = findViewById(R.id.btnMostrar);
        Button botonEliminar = findViewById(R.id.btnEliminar);

        // Listener para el botón de Descargar
        botonDescargar.setOnClickListener(v -> {
            try {
                // Llama al método estático para descargar una frase (asumiendo que HttpUtils maneja la descarga en un hilo secundario).
                HttpUtils.descargarFrase(this);
            } catch (Exception e) {
                // Manejo de excepciones durante la descarga
                // En un entorno real, se debería mostrar un mensaje de error al usuario
                throw new RuntimeException(e);
            }
        });

        // Listener para el botón Mostrar/Ocultar Frases
        botonMostrar.setOnClickListener(v -> {
            String textoBoton = (String) botonMostrar.getText();
            if (textoBoton.equals("Mostrar Frases")){
                // Si el texto es "Mostrar Frases", se cargan y visualizan los datos
                mostrarFrases();
                botonMostrar.setText("Ocultar Frases");
            } else {
                // Si el texto es "Ocultar Frases", se limpia la vista
                borrarFrasesView();
                botonMostrar.setText("Mostrar Frases");
            }
        });

        // Listener para el botón Eliminar Frases
        botonEliminar.setOnClickListener(v -> {
            // Instancia del DAO para interactuar con la base de datos
            FraseDAO fraseDAO = new FraseDAO(this);
            // Llama al método para eliminar todos los registros.
            fraseDAO.eliminarFrases();
            // Limpia la vista del RecyclerView.
            borrarFrasesView();
            // Restablece el texto del botón "Mostrar Frases" si estaba en "Ocultar Frases"
            String textoBoton = (String) botonMostrar.getText();
            if (textoBoton.equals("Ocultar Frases")){
                botonMostrar.setText("Mostrar Frases");
            }
        });

    }

    /**
     * Recupera la lista de frases desde la base de datos y la establece
     * como fuente de datos para el adaptador del RecyclerView
     */
    private void mostrarFrases() {
        // Uso de try-with-resources para asegurar que el FraseDAO (y su conexión DB) se cierren correctamente
        try (FraseDAO fraseDAO = new FraseDAO(MainActivity.this)) {
            // Crea un nuevo adaptador con la lista de frases recuperada
            adaptador = new FrasesAdapter(fraseDAO.mostrarFrases());
            // Asigna el adaptador al RecyclerView
            listaFrases.setAdapter(adaptador);
        }
    }

    /**
     * Limpia la lista de frases mostrada en el RecyclerView,
     * reemplazando los datos del adaptador con una lista vacía
     */
    public void borrarFrasesView() {
        ArrayList<Frase> listaVacia = new ArrayList<>();

        // Se crea un nuevo adaptador o se actualiza el existente con la lista vacía
        adaptador = new FrasesAdapter(listaVacia);
        adaptador.actualizar(listaVacia);
        listaFrases.setAdapter(adaptador);
    }


    /**
     * Guarda el estado de la actividad (en particular, el texto del botón Mostrar/Ocultar)
     * antes de que pueda ser destruida por el sistema (ej. por un cambio de orientación)
     * @param outState Bundle en el que se guardarán los datos.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Button btnMostrar = findViewById(R.id.btnMostrar);
        String valorBtn = (String) btnMostrar.getText();
        outState.putString("valorBtn",valorBtn);
    }

    /**
     * Restaura el estado de la actividad después de que ha sido recreada.
     * En particular, restaura el texto del botón y recarga las frases si estaban visibles
     * @param savedInstanceState Bundle que contiene los datos guardados previamente
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String valorBtn = savedInstanceState.getString("valorBtn");

        // Si el botón estaba en estado "Ocultar Frases" (es decir, las frases estaban visibles),
        // se restaura el texto y se llama a mostrarFrases() para recargar la lista
        if (valorBtn.equals("Ocultar Frases")) {
            Button btnMostrar = findViewById(R.id.btnMostrar);
            btnMostrar.setText(valorBtn);
            mostrarFrases();
        }
    }

}