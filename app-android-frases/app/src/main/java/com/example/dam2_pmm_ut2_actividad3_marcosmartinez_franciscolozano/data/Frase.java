/**
 * Clase que representa una Frase, conteniendo su identificador único,
 * el texto de la frase en si, y el autor de dicha frase.
 */
package com.example.dam2_pmm_ut2_actividad3_marcosmartinez_franciscolozano.data;

public class Frase {

    /**
     * Identificador único de la frase
     */
    private long id;

    /**
     * Contenido textual de la frase
     */
    private String texto;

    /**
     * Nombre del autor de la frase
     */
    private String autor;

    /**
     * Constructor por defecto de la clase Frase.
     */
    public Frase() {}

    /**
     * Constructor completo para crear una instancia de Frase con todos sus atributos inicializados
     * @param id Identificador único de la frase
     * @param texto Contenido textual de la frase
     * @param autor Nombre del autor de la frase
     */
    public Frase(long id, String texto, String autor) {
        this.id = id;
        this.texto = texto;
        this.autor = autor;
    }

    /**
     * Constructor para crear una instancia de Frase sin especificar su ID
     * @param texto Contenido textual de la frase
     * @param autor Nombre del autor de la frase
     */
    public Frase(String texto, String autor) {
        this.texto = texto;
        this.autor = autor;
    }

    /**
     * Obtiene el identificador único de la frase
     * @return El ID de la frase
     */
    public long getId() { return id; }

    /**
     * Establece el identificador único de la frase
     * @param id El nuevo ID de la frase
     */
    public void setId(long id) { this.id = id; }

    /**
     * Obtiene el contenido textual de la frase
     * @return El texto de la frase
     */
    public String getTexto() { return texto; }

    /**
     * Establece el contenido textual de la frase
     * @param texto El nuevo texto de la frase
     */
    public void setTexto(String texto) { this.texto = texto; }

    /**
     * Obtiene el nombre del autor de la frase
     * @return El nombre del autor
     */
    public String getAutor() { return autor; }

    /**
     * Establece el nombre del autor de la frase
     * @param autor El nuevo nombre del autor
     */
    public void setAutor(String autor) { this.autor = autor; }
}