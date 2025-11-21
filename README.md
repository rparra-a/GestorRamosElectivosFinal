### PROYECTO FINAL: SISTEMA DE GESTIÓN DEL PROCESO DE SELECCIÓN DE ASIGNATURAS ELECTIVAS

### Desarrollado por Valentina Cartagena, Romina Parra y Bernarda Rodríguez.

#### Desarrollar un sistema automatizado para la organización y gestión del proceso de selección de asignaturas electivas en los niveles de 3ro y 4to medio, utilizando herramientas y técnicas de Java y Spring Boot.

Este proyecto implementa un CRUD completo para gestionar el proceso, utilizando una base de datos que contiene una muestra representativa de estudiantes, es decir, la simulación de información que podría ser entregada por estudiantes de 3ro y 4to medio para llevar a cabo la organización de sus preferencias.

#### 🎯 Objetivos de Práctica

* **Spring Security:** módulo de seguridad central del proyecto, su rol fundamental es definir las políticas de acceso y la gestión de la autenticación de los usuarios.
* **Spring Boot:** Uso del framework para construir aplicaciones REST.
* **Verbos y Códigos HTTP:** Implementación correcta de métodos HTTP (POST, GET, PUT, DELETE) y sus códigos de respuesta.
* **Persistencia de Datos con JPA:** Mapeo de entidades y uso de Spring Data JPA para interactuar con la base de datos.
* **Programación orientada a objetos** Implementación de una estructura a base de clases y métodos simples y abstractos.

#### 📐 Arquitectura de Capas del Proyecto (Spring Boot)  

El proyecto sigue una estructura ordenada para la programación de distintas partes y funciones, así como capas de modelo y de servicio, las cuales son utilizadas posteriormente para aplicar el CRUD.

#### 1. Capa de Presentación / Controladores (Controller)
Propósito: Es la capa de entrada de la aplicación. Maneja las peticiones HTTP entrantes (entrantes) y devuelve las respuestas HTTP (salientes). 

__Responsabilidad:__  
* Mapear las URLs (endpoints) a métodos específicos de Java (ej: GET /tareas).
* Delegar la lógica de negocio a la capa de Servicio.
* Formatear la respuesta (ej: devolver un objeto Tarea con código 201 Created).
* Clases anotadas con @RestController y métodos con @GetMapping, @PostMapping, @PutMapping, @DeleteMapping.
* Entidades: Utiliza DTOs (Data Transfer Objects) para la comunicación.

  #### 2. Capa de Lógica de Negocio / Servicio (Service)
  Propósito: Contiene toda la lógica de negocio de la aplicación y coordina las acciones.

  __Responsabilidad:__ 
* Implementar las reglas de negocio (ej: verificar que una postulación no se pueda eliminar si no ha finalizado")
* Manejar las transacciones.
* Actuar como intermediario entre el Controlador y la capa de Persistencia.
* Llamar a los métodos de la capa de Repositorio para manipular los datos.
* Clases anotadas con @Service y a menudo @Transactional.

#### 3) Capa de Acceso a Datos / Repositorio (Repository)
Propósito: Es la capa responsable de comunicarse directamente con la base de datos.  

__Responsabilidad:__ 
* Ejecutar las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) en las tablas.
* Mapear las filas de la base de datos a objetos Java (Entidades) y viceversa.
* Es responsable de traducir las operaciones del Service a consultas SQL (a través de Hibernate)
* Gestionar la comunicación de bajo nivel (conexiones, sesiones, etc.).
* Interfaces anotadas con @Repository que extienden de JpaRepository (Spring Data JPA).

#### 4)  Capa de Persistencia (Database) Model (Modelo / Entidad)
Propósito: El almacén físico de los datos.  

__Responsabilidad:__ 
* Define las clases que serán mapeadas a las tablas de la BD (ej: Tarea.java)
* Incluye anotaciones de JPA (@Entity, @Id, @Table, @Column)
* Almacenar y recuperar los datos según las instrucciones SQL generadas por la capa de Repositorio (JPA/Hibernate).
* Representa el estado actual de un registro en la base de datos.<
* Tecnologías: PostgreSQL.

#### 5)  Capa de Comunicación DTO (Data Transfer Object)
Propósito: Define el formato de intercambio de datos.

__Responsabilidad:__ 
* Define las estructuras de datos que se usan para enviar y recibir información a través de la API (JSON).
* Su objetivo es evitar exponer la Entidad (Model) directamente.

#### 6)  Capa config (Seguridad)
Propósito: Su objetivo es centralizar la lógica de autenticación y autorización, definiendo quién es el usuario (autenticación) y qué puede hacer (autorización) dentro del sistema.

__Responsabilidad:__ 
* Codificación de Contraseñas: Define el Bean PasswordEncoder, utilizando BCryptPasswordEncoder. Esto garantiza que las contraseñas de los usuarios se almacenen de forma segura y cifrada (hash) en la base de datos, cumpliendo con las mejores prácticas de seguridad.
  
* Filtro de Seguridad Web: Configura el SecurityFilterChain (el filtro de seguridad) de Spring Security. Esto incluye la desactivación de CSRF (común en APIs REST), el uso de autenticación HTTP Basic, y la definición de las reglas de acceso por roles.

#### 🔄 Flujo de Datos 

Ejemplo de flujo de los datos al crear una postulación:

* El cliente envía los datos en formato DTO (JSON) al Controller: [Cliente / Postman] -->|1. POST /api/v1/postulaciones| B(PostulacionController)
* El Controller valida la entrada (si el DTO es válido) y lo pasa al Service. {PostulacionService}
* El Service aplica la lógica, convierte el DTO a un objeto Model y llama al método save() del Repository. 
* Persistencia: El Repository utiliza el Model para persistir los datos en la base de datos. Guarda el Objeto Postulacion 
* La respuesta (el objeto Model persistido) regresa al Service.
* El Service convierte el Model de regreso a un DTO de respuesta.
* El Controller envía el DTO de respuesta al cliente. Responde 201 Created + DTO de Respuesta.

### Configuración del Proyecto (pom.xml summary)
El archivo pom.xml utiliza la versión 3.5.6 de spring-boot-starter-parent y está configurado para usar Java 21. 
Las dependencias clave son:
* spring-boot-starter-web: Para la construcción de aplicaciones web y RESTful.
* spring-boot-starter-data-jpa: Para la persistencia de datos usando JPA y Hibernate.
* postgresql: Driver para la conexión a la base de datos PostgreSQL.
* spring-boot-starter-test: Para pruebas unitarias y de integración.

## 🛠️ Entidades Principales
1. Usuario: Clase abstracta que representa a un estudiante o funcionario de la escuela encargado de revisar el proceso de postulación.

* id (int): Clave primaria autoincremental.

* nombre (String): Nombre del usuario (no puede ser nulo).

* correo (String): Correo electrónico del usuario.

* contraseña (String): Contraseña para ingresar ala plataforma y revisar el estado de una postulación, entre otros.

* rol (String): Rol del usuario, puede ser "Estudiante" o "Administrador".
  
   
2. Estudiante: Hereda de la clase usuario y representa a un estudiante en capacidad de postular a un electivo.

* curso (String): Nivel y letra del curso al que pertenece, puede ser 3ro A, 3ro B, 4to A, 4to B.

* postulaciones (List<Postulacion>): Coleccion de objetos de tipo Postulación, que contiene las potulaciones a electivos realizadas por el estudiante.
  

3. Administrador: Hereda de la clase usuario y representa a un administrador del programa.

* cargo (string): Especificación de cargo del administrador.
  

4. Profesor: Clase padre, representa a un profesor de un ramo electivo.

* id (int): Clave primaria autoincremental.

* nombre (String): Nombre del profesor (no puede ser nulo).

* especialidad (String): Especialidad del profesor, relacionada a la asignatura que imparte.
  

5. Electivo: Clase relacionada con Profesor, quien imparte al electivo representado en una instancia determinada.

* id (int): Clave primaria autoincremental.

* nombre (String): Nombre del electivo (no puede ser nulo).

* descripción (String): Descripción que responde a la pregunta, ¿de qué trata el electivo?.

* cupos (int): Cupos disponibles para estudiantes interesados en el electivo.

* profesor (Profesor): Objeto de Profesor, quien imparte el electivo.

* postulaciones (List<Postulacion>): Coleccion de objetos de tipo Postulación, que contiene las potulaciones a ese electivo, las cuales han sido realizadas por diferentes estudiantes.
  

6. Postulación: Clase de las postulaciones de los estudiantes.

* id (int): Clave primaria autoincremental.

* estudiante (Estudiante): Objeto Estudiante, quien realiza la postulación.

* electivo (Electivo): Objeto del electivo de la postulación.

* fecha (Date): Fecha en que se realizó la postulación.

* estado (Estado): Estado de la postulación, categoría Estado.
  

7. Estado: Posibles estados de postulaciones, pudiendo ser PENDIENTE, ACEPTADA o RECHAZADA.

🗺️ Endpoints de la API _La API expone los siguientes endpoints bajo la ruta base /api/tareas:

* Estudiantes: 
GET /api/estudiantes
POST /api/estudiantes
* Electivos: 
GET /api/electivos
POST /api/electivos
GET /api/electivos/buscar?nombre=...
* Postulaciones: 
GET /api/postulaciones
POST /api/postulaciones
GET /api/postulaciones/estudiante/{id}
* Profesores: 
GET /api/profesores
POST /api/profesores

## 🛠️ Script SQL Entidades Principales

### 📝 Scripts de Inserción de Estados Iniciales (estado) 🚦
Primero, necesitamos asegurarnos de que los posibles estados para las tareas existan en la tabla estado.

-- Inserción de los tres estados posibles para las tareas:
INSERT INTO estado (nombre) VALUES 
('PENDIENTE'), 
('ACEPTADA'), 
('RECHAZADA');

-- NOTA: Asumiendo que 'pendiente' tiene ID 1, 'en progreso' tiene ID 2 y 'completada' tiene ID 3 
-- si la columna 'id' es SERIAL y se insertan en este orden.

Las demás tablas se crean automáticamente, gracias a la configuración del archivo application.properties.


## 🚀 Configuración y Ejecución

Requisitos: 
Java Development Kit (JDK) 21
[Maven/Gradle]

Una base de datos configurada (ej. PostgreSQL, MySQL, o H2 para desarrollo).

1. Clonar el Repositorio
2. Configurar la Base de Datos
Asegúrate de configurar los parámetros de conexión a la base de datos en el archivo de configuración de Spring Boot (ej. application.properties o application.yml).

Nota: Debes precargar los estados iniciales de postulación ("PENDIENTE", "ACEPTADA", "RECHAZADA") en la tabla estados para que la aplicación funcione correctamente.

3. Ejecutar la Aplicación
Si usas Maven:
La aplicación se iniciará por defecto en http://localhost:8080.
------------------------------------------------------------------------------------------

### Ruta de Prueba (Ejemplos de Solicitudes HTTP)

## 📝 Plan de Pruebas: Gestión de Postulaciones a Electivos

Haremos uso de __Postman__ para enviar las peticiones.

### 🔄 Paso 1: Creación de Postulaciones (POST)

Creamos postulaciones iniciales. El servicio debe asignar el estado **PENDIENTE**.

| ID Post. | Estudiante (ID) | Electivo (ID) | Estado Inicial | Petición | Resultado Esperado |
| :---: | :---: | :---: | :---: | :---: | :---: |
| **P1** | E1 (1) | L1 (1) | ❌ PENDIENTE | POST /api/v1/postulaciones?estudianteId=1\&electivoId=1 | 201 Created |
| **P2** | E2 (2) | L2 (2) | ❌ PENDIENTE | POST /api/v1/postulaciones?estudianteId=2\&electivoId=2 | 201 Created |

---

### 🚫 Paso 2: Probar Restricciones de Negocio y Errores (4xx)

Probamos las reglas definidas en el **`PostulacionService`** y manejo de recursos no encontrados.

| Funcionalidad | Petición (Ruta Completa) | Resultado Esperado |
| :---: | :--- | :---: |
| **Postulación Duplicada** | POST /api/v1/postulaciones?estudianteId=1\&electivoId=1 | 400 Bad Request. 

🛠️ Tecnologías Utilizadas

* **Lenguaje Java:** Lenguaje de programación principal
* **Framework Spring Boot :** Para construir la aplicación REST.
* **Persistencia Spring Data JPA:** Abstracción sobre JPA para el acceso a datos.
* **Hibernate:** Implementación de JPA para mapeo objeto-relacional.
* **Maven/Gradle:** Herramienta de construcción y gestión de dependencias.
* **Base de Datos PostgreSQL (PgAdmin):** Sistema de gestión de bases de datos relacional.
* **APIs (Interfaces de Programación de Aplicaciones) Postman:** Para enviar y probar las peticiones HTTP.
* **Sring Swcurity** :Seguridad y el Control de Acceso de la aplicación.

### RECURSOS TECNOLOGICOS:

__1) Spring Boot:__

El sitio web https://start.spring.io/ es una herramienta esencial conocida como Spring Initializr.  
Su propósito principal es generar rápidamente la estructura básica (el esqueleto o boilerplate) como asistente web agiliza la configuración inicial, permitiendo a los desarrolladores comenzar a escribir la lógica de negocio de su aplicación inmediatamente.

#### Funcionalidades Clave:
* Generación de Proyectos: Permite configurar y descargar un proyecto Spring Boot listo para ejecutar en cuestión de segundos.
* Selección de Dependencias (Starters): Es su característica más valiosa.
* Permite seleccionar visualmente las funcionalidades que necesita el proyecto (por ejemplo, Web, JPA para bases de datos, Security, Lombok, etc.).
* Spring Initializr se encarga de añadir las dependencias correctas al archivo de configuración de Maven (pom.xml) o Gradle.
* Configuración Básica: Permite definir:  
Tipo de Proyecto: Maven o Gradle.  
Lenguaje de Programación: Java, Kotlin o Groovy.  
Versión de Spring Boot y la versión de Java.  
Metadatos del proyecto: Nombre del grupo (Group), artefacto (Artifact) y el nombre del paquete.  
Integración con IDEs: los Entornos de Desarrollo Integrado (IDEs) populares como IntelliJ IDEA, Eclipse STS y Visual Studio Code tienen integración directa con Spring Initializr, permitiendo crear proyectos Spring Boot desde la interfaz del IDE.

__Spring Data JPA__ es un módulo (una parte) del proyecto Spring Data que tiene como objetivo simplificar y agilizar el desarrollo de aplicaciones que acceden a bases de datos relacionales, utilizando la especificación JPA (Java Persistence API).  En esencia, Spring Data JPA actúa como una capa de abstracción y automatización sobre JPA.

__2) Postman :__

El sitio web https://web.postman.co/home es una plataforma de desarrollo y pruebas de APIs (Application Programming Interfaces) actuar como un "cliente" para tu API, permitiendo a desarrolladores y testers interactuar con los endpoints sin necesidad de construir una interfaz de usuario compleja o escribir mucho código inicial.
#### Funcionalidades Clave:

* Envío de Solicitudes (Requests) HTTP/HTTPS: Permite enviar peticiones utilizando todos los métodos HTTP comunes como:  
GET (para obtener datos).  
POST (para crear datos).  
PUT/PATCH (para actualizar datos).  
DELETE (para eliminar datos).  

* Facilita la configuración de parámetros de la solicitud, encabezados (headers), datos de autenticación (tokens, claves) y el cuerpo de la solicitud (body), típicamente en formato JSON o XML.
* Pruebas (Testing) de API:
Permite verificar si una API funciona como se espera, enviando la solicitud y examinando la respuesta.  
Ofrece la posibilidad de escribir scripts de prueba automáticos (en JavaScript) para validar el código de estado HTTP (por ejemplo, 200 OK, 201 Created), la estructura de los datos de respuesta y el contenido de la respuesta.  

* Colecciones y Entornos (Collections and Environments):  
Colecciones: Permite agrupar y organizar solicitudes relacionadas en carpetas lógicas. Esto facilita la reutilización y el intercambio de flujos de trabajo de API completos.  
Entornos: Permite definir variables (como URLs base, tokens de autenticación o claves) que cambian según el contexto (desarrollo, prueba, producción). Esto simplifica el cambio entre diferentes configuraciones sin modificar las solicitudes.

__3) PostgreSQL (Postgres)__
PostgreSQL https://www.postgresql.org/ es un sistema de gestión de bases de datos relacional orientado a objetos (ORDBMS) de código abierto. Se le conoce por su solidez, fiabilidad y cumplimiento estricto del estándar SQL. A menudo se le considera una alternativa de código abierto a sistemas comerciales como Oracle o SQL Server, ofreciendo un amplio conjunto de características avanzadas.
#### Funcionalidades Clave:

* Orientado a Objetos y Relacional: Además de las características relacionales estándar, soporta herencia de tablas y tipos de datos definidos por el usuario, lo que le da una ventaja en el manejo de estructuras complejas.

* Concurrencia (MVCC): Utiliza un sistema llamado Control de Concurrencia Multiversión (MVCC), que permite que las operaciones de lectura y escritura se realicen simultáneamente sin necesidad de bloqueos, mejorando el rendimiento en entornos de alta concurrencia.

* Extensibilidad: Ofrece una gran variedad de tipos de datos nativos (incluyendo JSON, XML, direcciones IP, y figuras geométricas) y permite a los usuarios crear sus propias funciones y extensiones, siendo PostGIS (para datos geoespaciales) una de las más populares.

* Cumplimiento parte de ACID: Garantiza la Atomicidad, Consistencia, Aislamiento y Durabilidad de las transacciones, lo cual es fundamental para la integridad de los datos.

* Manejo adecuado de errores, considerando los códigos 404 Not Found (recurso no existe) y 400 Bad Request (fallo de validación).

__4) pgAdmin__
pgAdmin es la plataforma de administración y desarrollo de código abierto más popular y rica en funciones para PostgreSQL.
#### Funcionalidades Clave:
* Gestión de Bases de Datos: Permite crear, modificar y eliminar bases de datos, esquemas, tablas, índices, vistas y otros objetos de PostgreSQL.
+ Herramienta de Consulta (Query Tool): Ofrece un editor SQL avanzado para escribir, ejecutar y depurar consultas.
* Administración: Facilita la gestión de usuarios, roles, permisos y la realización de tareas administrativas como backups (copias de seguridad) y restauración.
* Monitoreo: Proporciona un dashboard (panel de control) para visualizar el estado del servidor, las conexiones activas y el rendimiento de las consultas.

__5) Spring Security__
framework Java que proporciona servicios integrales y extensibles de autenticación y autorización para aplicaciones construidas con Spring Framework o Spring Boot.
#### Funcionalidades Clave:

* Cifrado de Contraseñas: Utiliza BCryptPasswordEncoder para garantizar el almacenamiento seguro de todas las credenciales de usuario.  
* Control de Acceso (Autorización): Establece reglas de acceso por rol (ADMINISTRADOR, PROFESOR, ESTUDIANTE) para cada endpoint de la API, asegurando que solo los usuarios autorizados puedan realizar acciones como crear electivos o postular a ellos.  
* Autenticación HTTP Basic: Configura el mecanismo de inicio de sesión para validar credenciales contra la base de datos del sistema.

###  PROPUESTAS DE MEJORAS

* Trabajar en la seguridad y el control de datos para las clases de administrador y profesor, agregando los RequestDTOs que sean necesarios.
* Solucionar problemas de bucles de serialización JSON para conseguir una API mucho más profesional.
* Reestructurar el código para un manejo de base de datos mucho más limpio, el cual pueda garantizar la aplicación de todos los principios de ACID.
