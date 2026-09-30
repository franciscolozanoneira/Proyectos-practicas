package Paquete;


import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
        4.Añadid alguna relación ManyToMany.

*/

@Entity // Indica que esta clase es una entidad JPA
//@Table(name = "veterinarios")
public class Veterinarios  {

    @Id // Clave primaria

    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autogenerada la BD crea sola el ID de cada uno

//    @Column(name = "id")
    private Long id;

    //atributos
//    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    // Relación ManyToMany inversa. Esta entidad no es la dueña de la relación.
    // 'mappedBy = "veterinarios"' indica que la propiedad 'veterinarios' en la otra entidad
    // es la que controla la tabla intermedia en la base de datos.

    @ManyToMany(mappedBy = "veterinarios") // Muchas mascotas pueden tener muchos veterinarios y muchos veterinarios pueden atender muchas mascotas

    // Lista de mascotas asociadas a este veterinario.
    // Relación ManyToMany inversa: la entidad Mascotas es la dueña de la relación.
    // Aquí solo se refleja qué mascotas están vinculadas con este veterinario.

    private List<Mascotas>mascotas= new ArrayList<>(); // Inicializamos para evitar NullPointerException

    // Constructor que permite crear un veterinario indicando su nombre.
    // Útil para inicializar objetos rápidamente desde el código.

    public  Veterinarios(String nombre){
        this.nombre=nombre;
    }

    // Constructor vacío requerido por JPA.
    // JPA necesita un constructor sin parámetros para poder crear instancias mediante

    public Veterinarios(){}

    // Permite consultar el identificador único del veterinario.

    public Long getId() {
        return id;
    }

    // Setter de la clave primaria 'id'.
    // Normalmente no se usa cuando el ID es autogenerado, pero se incluye por compatibilidad.

    public void setId(Long id) {
        this.id = id;
    }

    // Devuelve el nombre del veterinario.

    public String getNombre() {
        return nombre;
    }

    // Permite modificar el nombre del veterinario.

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Permite obtener todas las mascotas atendidas por este veterinario.

    public List<Mascotas> getMascotas() {
        return mascotas;
    }

    // Permite asignar o actualizar la lista completa de mascotas vinculadas.

    public void setMascotas(List<Mascotas> mascotas) {
        this.mascotas = mascotas;
    }

    // Representación en forma de cadena del veterinario.
    // Usado para la depuración o para mostrar información en pantalla.

    @Override
    public String toString() {
        return "Veterinario [Id:  "+ id+
                ", Nombre: "+ nombre + "]";
    }
}

