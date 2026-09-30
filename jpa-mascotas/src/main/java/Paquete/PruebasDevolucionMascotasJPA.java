package Paquete;

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

public class PruebasDevolucionMascotasJPA {

	 public static void main(String[] args) {
	    	
	    	//Leer
			 /*
            EntityManagerFactory:
            - Crea y configura el entorno de JPA.
            - Se construye a partir del nombre de la unidad de persistencia (persistence.xml).
            - Es pesado, por eso se crea solo una vez por aplicación.
        	*/

	        EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadMascotas");

			/*
            EntityManager:
            - Objeto principal para interactuar con la base de datos.
            - Se usa para ejecutar consultas, insertar, borrar, actualizar y leer entidades.
            - Es ligero y se crea cada vez que lo necesitemos.
        	*/

	        EntityManager em = emf.createEntityManager();

	        try {

				/*
                Las lecturas simples NO siempre requieren una transacción,
                pero EclipseLink permite leer dentro de una transacción sin problema.
                Aquí comenzamos una transacción por seguridad.
            	*/

	            em.getTransaction().begin();

	            // Consulta JPQL moderna
				/*
                Consulta JPQL:
                - JPQL usa nombres de clases y atributos, no nombres de tablas o columnas.
                - "SELECT p FROM Mascotas p" significa:
                  "Selecciona todas las entidades Mascotas y llámalas p".
                - Mascotas.class indica el tipo de retorno.
            	*/

	            List<Mascotas> mascota = em
	                    .createQuery("SELECT p FROM Mascotas p", Mascotas.class)
	                    .getResultList();

				// Recorremos la lista y mostramos cada mascota.
				// El métodotoString() de Mascotas determina cómo se muestra cada objeto.

	            for (Mascotas p : mascota) {

	               //  System.out.println("Nombre: " + p.getNombre()); con esto solo devuelve el nombre pero si

					System.out.println(p);// deberia devolver todos los parametros del toString
	            }

				/*
                NO es necesario commit si solo estamos leyendo.
                commit() solo se usa si hay cambios en la base de datos:
                - insertar
                - actualizar
                - eliminar
                Como aquí solo hacemos SELECT, lo comentamos.
            	*/
	            //em.getTransaction().commit(); esto no es necesario

				/*
                Búsqueda con find():
                - Busca una entidad por su clave primaria.
                - El primer parámetro es la clase de la entidad.
                - El segundo es el valor de la clave primaria.
                - Si no existe, devuelve null.
            	*/

	            Mascotas mascota2 = em.find(Mascotas.class, 2L);

	            //En el find siempre se debe de buscar por la clase primaria

				System.out.println("\nMascota con ID: 2");
	            System.out.println(mascota2);

	        } catch (Exception e) {

				// Muestra cualquier error ocurrido durante la ejecución.

	            e.printStackTrace();

				// Si la transacción estaba activa, se revierte para evitar errores en la BD.

	            if (em.getTransaction().isActive()) {
	                em.getTransaction().rollback();
	            }
	        } finally {

				/*
                Es MUY importante cerrar los recursos JPA:
                - EntityManager: cierra la sesión con la BD.
                - EntityManagerFactory: libera la configuración y conexiones abiertas.
            	*/

	            em.close();
	            emf.close();
	        }
	    }
	}