package com.example.dam2_pmm_ut2_actividad3_marcosmartinez_franciscolozano.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dam2_pmm_ut2_actividad3_marcosmartinez_franciscolozano.R;
import com.example.dam2_pmm_ut2_actividad3_marcosmartinez_franciscolozano.data.Frase;

import java.util.List;

public class FrasesAdapter extends RecyclerView.Adapter<FrasesAdapter.ViewHolder> {

    // Lista que contendrá las frases a mostrar en el RecyclerView
    private List<Frase> lista;

    // Constructor del adaptador: recibe la lista inicial de frases
    public FrasesAdapter(List<Frase> lista) {
        this.lista = lista;
    }

    // Métodito para actualizar la lista de frases y notificar al adaptador que los datos cambiaron
    public void actualizar(List<Frase> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged(); // Refresca el RecyclerView mostrando los nuevos datos
    }

    @NonNull
    @Override
    public FrasesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater se usa para "inflar" un XML y convertirlo en un objeto View que Java puede usar
        // parent.getContext() obtiene el contexto actual de la vista padre (normalmente la Activity)
        View vista = LayoutInflater.from(parent.getContext())
                // Inflamos el layout item_frase.xml para cada item del RecyclerView
                // parent es el ViewGroup que contendrá esta vista
                // false indica que no queremos adjuntarlo todavía, RecyclerView lo hará
                .inflate(R.layout.item_frase, parent, false);

        // Creamos un ViewHolder pasándole la vista inflada
        // El ViewHolder mantiene las referencias a los elementos internos del layout (TextView)
        return new ViewHolder(vista);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // "position" es la posición actual del item que se está mostrando
        // Obtenemos la frase correspondiente a esta posición de la lista
        Frase fraseActual = lista.get(position);

        // Ponemos el texto de la frase dentro del TextView "texto" del ViewHolder
        holder.texto.setText(fraseActual.getTexto());

        // Obtenemos el autor de la frase
        String autor = fraseActual.getAutor();

        // Comprobamos si el autor es nulo o está vacío
        if (autor == null || autor.trim().isEmpty()) {
            // Si no hay autor, mostramos un valor por defecto
            holder.autor.setText("- desconocido");
        } else {
            // Si hay autor, lo mostramos con un guion delante
            holder.autor.setText("- " + autor);
        }
    }

    @Override
    public int getItemCount() {
        // Devuelve la cantidad de elementos que tiene la lista (cuántos items mostrar)
        return lista.size();
    }

    // Clase interna ViewHolder que mantiene las referencias de los elementos de cada item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView texto; // TextView que mostrará la frase
        TextView autor; // TextView que mostrará el autor de la frase

        public ViewHolder(View itemView) {
            super(itemView);

            // Enlazamos los TextView del XML con las variables Java
            texto = itemView.findViewById(R.id.TextoFrase);
            autor = itemView.findViewById(R.id.FraseAutor);
        }

        @Override
        public String toString() {
            // Métodito opcional que devuelve una representación en String del ViewHolder
            return autor.getText() + "-" + texto.getText() + " ";
        }
    }
}
