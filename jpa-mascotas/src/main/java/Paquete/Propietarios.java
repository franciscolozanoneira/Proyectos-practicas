package Paquete;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

    /*
    3. Ampliad el apartado 1 para que el CRUD incluya más de una entidad relacionadas entre ellas con relaciones OneToMany.
    */


@Entity // <-- Indica que es una entidad JPA
//@Table(name = "propietarios") //  Nombre exacto de la tabla en la base de datos
public class Propietarios  {
    @Id // Define que este atributo es la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // Indica que el valor se genera automáticamente en la base de datos (AUTO_INCREMENT)

//    @Column(name = "id") // Nombre de la columna en la tabla
    private Long id;


//    @Column(name = "nombre", nullable = false, length = 50)// Nombre de la columna, no puede ser nulo, longitud máxima 50
    private String nombre;

    @OneToMany(mappedBy = "propietario", cascade = CascadeType.ALL)

    /*
        OneToMany: Un propietario puede tener muchas mascotas.
        mappedBy = "propietario": indica que la relación está definida en la clase Mascotas en el atributo 'propietario'.
        cascade = ALL: operaciones en propietario se propagan a las mascotas.
    */

    private List<Mascotas> mascotas= new ArrayList<>(); // Inicializamos para evitar NullPointerException

    // Constructor completo

    public Propietarios(String nombre) {
        this.nombre = nombre;
    }
    // Constructor vacío necesario para JPA

    public Propietarios() {}


    // Getters y Setters

    public Long getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public List<Mascotas> getMascotas() {
        return mascotas;
    }
    public void setMascotas(List<Mascotas> mascotas) {
        this.mascotas = mascotas;
    }

    @Override
    public String toString() {

        return "Propietario [id=" + id +
                ", nombre=" + nombre + "]";
    }
}
