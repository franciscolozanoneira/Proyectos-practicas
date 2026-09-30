package Paquete;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/*

1.Prueba de concepto: elegid una base de datos de los retos de temas anteriores, del curso pasado o de las propuestas en el curso
(tema 2 - Bases de datos de ejemplos-20241021 Archivo ) y realizar una aplicación (o reutilizad las de los retos anteriores) que mediante EclipseLink
realice un CRUD sobre una de las entidades de dicha base de datos haciendo el correspondiente mapeo a objetos de Java. En este caso dejad que cree las
tablas el propio EclipseLink sin especificar @Tabla ni @Column.

2.Prueba completa: explorad las distintas posibilidades de hacer que la aplicación cree las tablas, que solo las modifique ante cambios o que se
limite a usar las tablas y datos ya existentes. Cambia mediante @Table y @Column el mapeo para que se corresponda exactamente con los nombres de la
base de datos original.

*/

public class PuebasMascotasJPA {
	 public static void main(String[] args) {

	    	//Insertamos en el persistence.xml como llamamos a la unidad

	        EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadMascotas");
	        EntityManager em = emf.createEntityManager();

		 try {
			 	em.getTransaction().begin();

			 	// Crear propietarios

			 	Propietarios propietario1 = new Propietarios("Luis");
			 	Propietarios propietario2 = new Propietarios("Pepe");
			 	Propietarios propietario3 = new Propietarios("María");
			 	Propietarios propietario4 = new Propietarios("Jose");

			 	em.persist(propietario1);
			 	em.persist(propietario2);
			 	em.persist(propietario3);
			 	em.persist(propietario4);

			 	//Creamos los vetrinarios

			 	Veterinarios veterinario1 = new Veterinarios("Dra.Ramirez");
			 	Veterinarios veterinario2 = new Veterinarios("Dr.Coscollano");
			 	Veterinarios veterinario3 = new Veterinarios("Dr.Quispe");

			 	em.persist(veterinario1);
			 	em.persist(veterinario2);
			 	em.persist(veterinario3);


	        	// creamos a las mascotas haciendo referencia a los propietarios declarados arriba

	        	Mascotas mascota1 = new Mascotas("Miki", propietario1,"Perro", "Macho", "19/09/2005", "03/05/2020");
	        	Mascotas mascota2 = new Mascotas("Lucho", propietario2,"Gato", "Macho", "20/12/2008", "01/08/2014");
	        	Mascotas mascota3 = new Mascotas("Nikama", propietario3,"Caballo", "Hembra", "10/09/2003", "15/02/2025");
	        	Mascotas mascota4 = new Mascotas("Hermenegilda", propietario4,"Huron", "Hembra", "12/07/2008", "25/06/2013");

				/*
				* Asociamos las mascotas a sus correspondientes veterinarios para
				* realiar asi la relacion (ManytoMany)
				*/

				/*
				* Cadavez que queramos relacionar unos veterinarios con alguna mascota en concreto usamos la siguiente forma:
				* creamos un a lista de en este caso Veterinarios y le damos un nombre para asignarlo luego a una mascota en concreto,
				*  despues añadimos a la lista nueva de verterinariosmasc1 el numerode veterinarios que queremos asignarle, y despues se lo asignamos a una mascota
				*/

			 	List<Veterinarios>veterinariosmasc1= new ArrayList<>();
			 	veterinariosmasc1.add(veterinario1);
			 	mascota1.setVeterinarios(veterinariosmasc1);

			 	List<Veterinarios>veterinariosmasc2= new ArrayList<>();
			 	veterinariosmasc2.add(veterinario3);
			 	veterinariosmasc2.add(veterinario2);
			 	mascota2.setVeterinarios(veterinariosmasc2);

			 	List<Veterinarios>veterinariosmasc3= new ArrayList<>();
			 	veterinariosmasc3.add(veterinario3);
			 	mascota3.setVeterinarios(veterinariosmasc3);

			 	List<Veterinarios>veterinariosmasc4= new ArrayList<>();
			 	veterinariosmasc4.add(veterinario1);
			 	mascota4.setVeterinarios(veterinariosmasc4);


			 	/*
			 	* Asignamos lo mismo, pero al reves ahora es el turno de las mascotas con sus veterinarios para que
			 	* asi tenerlos asignados
			 	*
			 	* */

			 	veterinario1.getMascotas().add(mascota1);
			 	veterinario1.getMascotas().add(mascota4);

			 	veterinario2.getMascotas().add(mascota2);

			 	veterinario3.getMascotas().add(mascota2);
			 	veterinario3.getMascotas().add(mascota3);

	            // hacemos persistentes a las mascotas

	            em.persist(mascota1);
	            em.persist(mascota2);
	            em.persist(mascota3);
	            em.persist(mascota4);

                em.flush(); // fuerza la generación de ID
                Long idMascota4 = mascota4.getId();
                Long idMascota3 = mascota3.getId();
                Long idMascota2 = mascota2.getId();
                Long idMascota1 = mascota1.getId();

                 Mascotas modificarMascota4 = em.find(Mascotas.class, idMascota4);
                 if(modificarMascota4 != null) {
                     modificarMascota4.setNombre("Pepa");
                 }
	            //modificar la mascota4

	            //Mascotas modificarMascota4 = em.find(Mascotas.class, 4L); // el find busca por ID no por otro campo
                 // if(modificarMascota4 != null) {
                    //  modificarMascota4.setNombre("Pepa");
                 // } else {
                    // System.out.println("No existe la mascota con ID 4");
                 // }

	        	// em.merge(mascota4); no necesitamos usar el merge porque JPA sincroniza cualquier cambio en objetos al usar commit

				// eliminar mascotas y comprobamos que no existan

	           	Mascotas eliminarmascota3 = em.find(Mascotas.class, idMascota3);// el find busca por ID no por otro campo

			 	/*
        		 * Hay que tener muy en cuenta que en JPA hay que hacer los begin y commits, para que se guarden en BBDD los valores modificados
        		 */

        		em.remove(eliminarmascota3);
    			System.out.println("mascota3 en principio fue eliminada correctamente");

        		if(em.find(Mascotas.class, idMascota3) == null) {
        			System.out.println("Nikama ya no existe");
        		} else {
					System.out.println("Algo salio mal");
				}

	       	     // si todoa salio bien se guarda la information usando el commit

	       	     em.getTransaction().commit();
				 System.out.println("Todo bien :)");
			} catch (Exception e) {

	            e.printStackTrace();

				// si salieron las cosas mal se vuelve tras con el rollback para no guardar información errónea

	            if (em.getTransaction().isActive()) {
	                em.getTransaction().rollback();
	            } System.out.println("Todo mal :(");
	        } finally {

				// cerramos el factory y el manager

	            em.close();
	            emf.close();
	        }
	    }
	}