package Paquete;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

/*

1.Prueba de concepto: elegid una base de datos de los retos de temas anteriores, del curso pasado o de las propuestas en el curso
(tema 2 - Bases de datos de ejemplos-20241021 Archivo ) y realizar una aplicación (o reutilizad las de los retos anteriores) que mediante EclipseLink
realice un CRUD sobre una de las entidades de dicha base de datos haciendo el correspondiente mapeo a objetos de Java. En este caso dejad que cree las
tablas el propio EclipseLink sin especificar @Tabla ni @Column.

2.Prueba completa: explorad las distintas posibilidades de hacer que la aplicación cree las tablas, que solo las modifique ante cambios o que se
limite a usar las tablas y datos ya existentes. Cambia mediante @Table y @Column el mapeo para que se corresponda exactamente con los nombres de la
base de datos original.

*/


// necesario para que el persistence.xml sepa que esto es una entidad necesaria

@Entity // Indica que esta clase es una entidad JPA
// @Table(name = "mascotas") // Nombre exacto de la tabla en la base de datos
public class Mascotas  {

	@Id // Clave primaria
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Autogenerada la BD crea sola el ID de cada uno
//	@Column(name = "id") // Nombre de la columna
	private Long id;

	//Se recomienda ponerle el getId si el id no es autogenerado

	public Long getId() {
		return id;
	}

	//atributos

//	@Column(name = "nombre", length = 20)
	private String nombre;
	/*
		3.Ampliad el apartado 1 para que el CRUD incluya más de una entidad relacionadas entre ellas con relaciones OneToMany.
	*/
	@ManyToOne // Muchas mascotas pueden pertenecer a un propietario
//	@JoinColumn(name = "propietario_id")

    /*
        JoinColumn: define la columna de la clave foránea en la tabla 'mascotas'.
        Será la referencia al propietario correspondiente.
    */

	private Propietarios propietario;

//	@Column(name = "especie", length = 20) //Mapea el atributo 'nombre' a la columna 'nombre' de la tabla Longitud máxima de 20 caracteres
	private String especie;

//	@Column(name = "sexo", length = 1)
	private String sexo;

//	@Column(name = "nacimiento")
	private LocalDate nacimiento;

//	@Column(name = "fallecimiento")
	private LocalDate fallecimiento;
	/*
        4.Añadid alguna relación ManyToMany.

*/
	// Lado dueño de la relación ManyToMany.
	// Esta entidad define y controla la tabla intermedia en la base de datos a través de @JoinTable.
	// Cualquier cambio en la relación (agregar o eliminar elementos) se gestiona desde aquí.

	@ManyToMany(fetch = FetchType.EAGER) // Muchas mascotas pueden tener muchos veterinarios y muchos veterinarios pueden atender muchas mascotas
//	@JoinTable(
//			name = "mascotas_veterinarios",
//			joinColumns = @JoinColumn(name = "mascotas_id"),
//			inverseJoinColumns = @JoinColumn(name = "veterinarios_id")
//	)
	private List<Veterinarios> veterinarios= new ArrayList<>(); // Inicializamos para evitar NullPointerException

	// constructor mascotas completo

	public Mascotas(String nombre, Propietarios propietario, String especie, String sexo, String nacimiento, String fallecimiento) {
		super();
		this.nombre=nombre;
		this.propietario=propietario;
		this.especie=especie;
		this.sexo=sexo;
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		//como tiene que estar vivo para poder meterlo no he visto necesario usar
		// una funcionalidad parecidad a la de abajao para ponelo a null

		this.nacimiento=LocalDate.parse(nacimiento,formato);

		/*
		Cambio en fallecimieto por si alguna mascota aun no ha fallecido poder ponerlo a null
		Si la cadena 'fallecimiento' es null o está vacía, se asigna null.
		true  -> la cadena es nula o vacía, se asigna null
		 De lo contrario, se convierte la cadena a LocalDate usando el formato "dd/MM/yyyy"
		false -> la cadena contiene una fecha, se convierte a LocalDate
		*/

		this.fallecimiento=(fallecimiento == null || fallecimiento.isEmpty()) ? null : LocalDate.parse(fallecimiento,formato);
	}
		
	// construcctor vacio

	public Mascotas() {}

	//################################################Getter y Setter##########################################################//

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Propietarios getPropietario() {
		return propietario;
	}

	public void setPropietario(Propietarios propietario) {
		this.propietario = propietario;
	}

	public String getEspecie() {
		return especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public LocalDate getNacimiento() {

		return nacimiento;
	}

	public void setNacimiento(LocalDate nacimiento) {
		this.nacimiento = nacimiento;
	}

	public LocalDate getFallecimiento() {
		return fallecimiento;
	}

	public void setFallecimiento(LocalDate fallecimiento) {
		this.fallecimiento = fallecimiento;
	}
	public List<Veterinarios> getVeterinarios() {
		return veterinarios;
	}

	public void setVeterinarios(List<Veterinarios> veterinarios) {
		this.veterinarios = veterinarios;
	}

	@Override
	public String toString() {
		return "Mascotas [ Nombre: " + nombre +
				"\nPropietario: " + propietario +
				"\nEspecie: " + especie +
				"\nSexo:" + sexo +
				"\nNacimiento: " + nacimiento +
				"\nFallecimiento: " + fallecimiento +
				"\nVetrinario: " + veterinarios + "]";
	}
}
