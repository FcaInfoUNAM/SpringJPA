# Spring Boot con JPA

## Entorno de desarrollo

Abre el proyecto en el contenedor de desarrollo (Dev Container) para usar el
entorno con Java 21 y PostgreSQL. La aplicación puede conectarse al servicio de
la base de datos mediante el host `db` y el puerto `5432`. El contenedor de
PostgreSQL usa inicialmente la base de datos y las credenciales `postgres`.

Para iniciar la aplicación desde la raíz del repositorio:

```bash
cd demo
./mvnw spring-boot:run
```

VS Code puede detectar y reenviar el puerto `8080` de Spring Boot. Para
conectarte a PostgreSQL desde una herramienta instalada en el equipo local,
agrega `"forwardPorts": [5432]` a `devcontainer.json`; dentro del Dev Container,
la aplicación se conecta directamente al host `db`.

## Conceptos básicos

### ¿Qué es JPA?

JPA (Java Persistence API) es una especificación de Java para guardar, consultar,
actualizar y eliminar datos usando objetos en lugar de escribir manualmente cada
consulta SQL. Las clases del modelo se relacionan con tablas de la base de datos
mediante anotaciones como `@Entity` y `@Id`. Hibernate es una implementación de
JPA; Spring Data JPA facilita su uso, por ejemplo, mediante repositorios como
`JpaRepository`.

### ¿Qué es un controlador REST?

Un controlador REST es una clase que expone operaciones de una aplicación
mediante direcciones URL y métodos HTTP, como `GET` y `POST`. En Spring, la
anotación `@RestController` indica que los métodos de la clase atienden
solicitudes web y que sus resultados se devuelven normalmente como datos JSON.
En este ejemplo, el controlador permitirá consultar y registrar productos.

## Instrucciones

### Paso 1: crear la base de datos

Desde una terminal del Dev Container, conéctate al servidor PostgreSQL:

```bash
psql -h db -U postgres -d postgres
```

Cuando se solicite la contraseña, escribe `postgres`. En la consola de
PostgreSQL, crea un usuario y una base de datos para la aplicación:

```sql
CREATE USER chochos WITH PASSWORD 'chochos';
CREATE DATABASE chochos OWNER chochos;
```

Si ejecutas la aplicación directamente en el equipo local, usa `localhost` como
host de PostgreSQL en lugar de `db`.

### Paso 2: agregar dependencias

Abre `demo/pom.xml`. Dentro de la sección `<dependencies>`, agrega las
dependencias que aún no estén incluidas:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>

<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.34</version>
    <scope>provided</scope>
</dependency>
```

El proyecto también necesita `spring-boot-starter-web` para crear el controlador
REST. Si todavía no aparece en el archivo, agrégalo:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### Paso 3: configurar la conexión

Abre `demo/src/main/resources/application.properties` y configura la conexión
a PostgreSQL. Dentro del Dev Container, usa `db` como host:

```properties
spring.application.name=demo
spring.datasource.url=jdbc:postgresql://db:5432/chochos
spring.datasource.username=chochos
spring.datasource.password=chochos
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

Si ejecutas Spring Boot fuera del Dev Container, cambia `db` por `localhost`.
La opción `ddl-auto=update` permite que Hibernate actualice el esquema de la
base de datos a partir de las entidades.

### Paso 4: crear la entidad

Crea `demo/src/main/java/com/example/demo/model/Producto.java`. La anotación
`@Entity` indica que la clase se guardará en la base de datos, y `@Id` identifica
su clave primaria:

```java
package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private String lote;
    private BigDecimal precioUnitario;
    private LocalDate fechaCaducidad;
}
```

### Paso 5: crear el repositorio

Crea `demo/src/main/java/com/example/demo/repo/ProductoRepository.java`.
`JpaRepository` proporciona operaciones comunes para consultar, guardar,
actualizar y eliminar entidades:

```java
package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
```

### Paso 6: crear el controlador REST

Crea `demo/src/main/java/com/example/demo/producto/ProductoController.java`.
Este controlador ofrece una ruta para consultar los productos y otra para
registrar uno:

```java
package com.example.demo.producto;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Producto;
import com.example.demo.repo.ProductoRepository;

@RestController
public class ProductoController {
    private final ProductoRepository repo;

    public ProductoController(ProductoRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/productos")
    public List<Producto> findAllProductos() {
        return repo.findAll();
    }

    @PostMapping("/productos")
    public Producto addProducto(@RequestBody Producto producto) {
        return repo.save(producto);
    }
}
```

### Paso 7: probar la API

Con la aplicación en ejecución, abre otra terminal y envía una solicitud para
registrar un producto:

```bash
curl -X POST http://localhost:8080/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Creatina","descripcion":"Creatina de 500 g","lote":"12345","precioUnitario":500.50,"fechaCaducidad":"2027-05-04"}'
```

Para consultar los productos guardados, abre
`http://localhost:8080/productos` en el navegador o ejecuta:

```bash
curl http://localhost:8080/productos
```

También puedes conectarte a la base de datos con una extensión de PostgreSQL
para VS Code. Si configuraste el reenvío del puerto `5432`, usa el host
`localhost`; de lo contrario, conéctate desde el Dev Container usando el host
`db`. En ambos casos, usa el puerto `5432`, la base de datos `chochos` y el
usuario y la contraseña `chochos`.

### Paso 8: adaptar el ejemplo

Repite los pasos 4 a 7 y adapta el modelo, el repositorio y el controlador a
otra tabla de tu proyecto.

## Información útil

- Si la aplicación sigue ejecutándose y necesitas liberar el puerto `8080`,
  busca el identificador del proceso:

  ```bash
  fuser 8080/tcp
  ```

  Después, termina el proceso usando el identificador que devolvió el comando:

  ```bash
  kill <PID>
  ```

- PostgreSQL se inicia junto con el Dev Container. Si no está disponible,
  comprueba el estado del contenedor antes de intentar iniciar un servicio
  PostgreSQL en el contenedor de desarrollo.
