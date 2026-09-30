# Proyectos-practicas

Proyectos desarrollados durante el ciclo de **Técnico Superior en Desarrollo de Aplicaciones Multiplataforma (DAM)**, cursado en el IES Francisco de Goya (Madrid) entre 2024 y 2026.

![Java](https://img.shields.io/badge/Java-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?logo=springboot&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?logo=android&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white)
![MariaDB](https://img.shields.io/badge/MariaDB-003545?logo=mariadb&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?logo=gradle&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white)

## Proyectos

| Proyecto | Qué demuestra | Tecnologías | Trabajo |
|---|---|---|---|
| [API REST con Spring Boot](api-rest-spring-boot/ProyectoCRUDRepository) | CRUD completo, arquitectura en capas y persistencia con JPA | Java, Spring Boot, Spring Data JPA, MySQL, Gradle | En pareja |
| [Subasta en tiempo real](subasta-tiempo-real-java) | Servidor concurrente, sincronización y comunicación por sockets | Java, Sockets TCP, Threads, Swing | Individual |
| [App Android de frases](app-android-frases) | Consumo de API HTTP, base de datos local y listados | Java, Android, SQLite, RecyclerView, JSON | En pareja |
| [Persistencia con JPA](jpa-mascotas) | Modelo de datos con relaciones y transacciones | Java, JPA, EclipseLink, MariaDB, Maven | En pareja |

---

## 1. API REST con Spring Boot

API REST con operaciones CRUD completas sobre una base de datos MySQL.

- Endpoints para **GET, POST, PUT, PATCH y DELETE**, con códigos HTTP adecuados (200, 201, 204 y 404).
- Arquitectura en capas: **Controller → Service → Repository**.
- Persistencia con **Spring Data JPA** (`CrudRepository`) y build con **Gradle**.
- Inyección de dependencias por constructor.

**Cómo ejecutarlo**

1. Necesitas JDK 17 o superior y un servidor MySQL en marcha.
2. Crea la base de datos y configura la conexión (URL, usuario y contraseña) en `src/main/resources/application.properties`. Las credenciales no se incluyen en el repositorio.
3. Desde la carpeta del proyecto:
   ```bash
   ./gradlew bootRun
   ```
   En Windows: `gradlew.bat bootRun`.

## 2. Subasta en tiempo real

Sistema cliente-servidor de subastas con varios usuarios conectados a la vez.

- **Servidor TCP concurrente**: un hilo por cliente.
- Estado compartido (puja máxima) protegido con **sincronización** para evitar condiciones de carrera.
- **Difusión (broadcast)** de cada nueva puja a todos los clientes conectados.
- Protocolo propio con mensajes serializables tipados: `LOGIN`, `PUJA`, `UPDATE` y `RECHAZADO`.
- Cliente gráfico con **Swing**, con actualización segura de la interfaz.

**Cómo ejecutarlo**

1. Ejecuta primero `ServidorSubasta` (escucha en el puerto 6000).
2. Ejecuta `ClienteSubasta` varias veces, una por cada usuario, e introduce un nombre.
3. Realiza pujas desde los distintos clientes y observa cómo se actualiza el precio en todos.

## 3. App Android de frases

Aplicación Android que descarga datos de una API y los guarda en local.

- Consumo de una **API HTTP en formato JSON**, en un hilo secundario para no bloquear la interfaz.
- Almacenamiento en **SQLite** con `SQLiteOpenHelper` y patrón **DAO**.
- Listado con **RecyclerView** y layouts distintos para vertical y horizontal.

**Cómo ejecutarlo**

1. Abre la carpeta `app-android-frases` con **Android Studio**.
2. Sincroniza Gradle y ejecuta la app en un emulador o en un móvil.
3. Necesita conexión a internet para descargar los datos.

## 4. Persistencia con JPA

Modelo de datos de una clínica veterinaria: **propietarios, mascotas y veterinarios**.

- Entidades con relaciones `@OneToMany`, `@ManyToOne` y `@ManyToMany`.
- Operaciones CRUD con `EntityManager` y **transacciones** (`begin`, `commit`, `rollback`).
- Consultas con **JPQL**.
- **EclipseLink** como proveedor JPA sobre **MariaDB**, con Maven.

**Cómo ejecutarlo**

1. Necesitas JDK 17 o superior y un servidor MariaDB.
2. Crea la base de datos y configura la conexión en `src/main/resources/META-INF/persistence.xml`. Las credenciales no se incluyen en el repositorio.
3. Compila con `mvn compile` y ejecuta la clase de pruebas desde tu IDE.

---

## Otros

La carpeta [Practica Módulos Python avanzados](Practica%20M%C3%B3dulos%20Python%20avanzados) recoge ejercicios de clase de Python.

## Notas

- Los proyectos marcados como "En pareja" se hicieron con compañeros del ciclo, trabajando gran parte del tiempo de forma simultánea y con Git.
- Son trabajos académicos. Las credenciales de bases de datos se han eliminado y deben configurarse en local.

## Contacto

**Francisco Lozano Neira** · Desarrollador Java Junior · Madrid
📧 franciscolozanoneira@gmail.com
