# Spring Boot JPA

## Paso 0: 
Creara una base de datos en Postgres ejecutando el la terminal
```bash
psql -U postgres
```


```sql
CREATE USER chochos WITH PASSWORD ‘chochos’;
CREATE DATABASE chochos OWNER chochos;
```

# Paso1:

Agregar dependencias: busca el archivo Pom.xml y agrega las siguientes dependencias
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>
  
  <dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
  </dependency>
<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<version>1.18.34</version>
			<scope>provided</scope>
		</dependency>
```

## Paso2 

Abrir el archivo ```/workspaces/empty/demo/src/main/resources/application.properties ```

Agregar configuración Postgres
```
spring.application.name=demo
spring.datasource.url= 'jdbc:postgresql://localhost:5432/chochos'
spring.datasource.username= chochos
spring.datasource.password= chochos
spring.jpa.hibernate.ddl-auto= update
spring.jpa.properties.hibernate.dialect= org.hibernate.dialect.PostgreSQLDialect
```
## Paso3
Generar el modelo ```demo/src/main/java/com/example/demo/model/product.java```
```java
package com.example.demo.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name="producto")
@Entity
public class product {
    @Id
    private long id;

    private String nombre;
  
    private String descripcion;
    private String lote;
    private float precioUnitario;
    private LocalDate fechaCaducidad;

    public product(long id, String nombre, String descripcion, String lote, float precioUnitario,
            LocalDate fechaCaducidad) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.lote = lote;
        this.precioUnitario = precioUnitario;
        this.fechaCaducidad = fechaCaducidad;
    }
    
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getLote() {
        return lote;
    }
    public void setLote(String lote) {
        this.lote = lote;
    }
    public float getPrecioUnitario() {
        return precioUnitario;
    }
    public void setPrecioUnitario(float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }
    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

}
```

## Paso 4
Crea el repositorio ```demo/src/main/java/com/example/demo/repo/productoRepo.java``` (Interface)

```java
package com.example.demo.repo;

import org.hibernate.mapping.List;
import org.springframework.data.jpa.repository.JpaRepository;


import com.example.demo.model.product;
import com.example.demo.producto.Producto;


public interface productoRepo extends JpaRepository <product,Long>{  

}

```


## Psoo 5 
Crea el controlador ```demo/src/main/java/com/example/demo/producto/Producto.java```

```java
package com.example.demo.producto;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.product;
import com.example.demo.repo.productoRepo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class Producto {
@Autowired
productoRepo repo;
@GetMapping("productos")
  public List<product> findAllProductos() {
    return this.repo.findAll();
  }

@PostMapping("producto")
  public product addProducto(@RequestBody product producto) {
    return repo.save(producto);
  }
}


```

## Paso 6 
Intenta en una nueva consola o terminal ejecutar el siguiente comando:
```cmd
curl -s -X POST localhost:8080/producto \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Creatina", "descripcion":"Creatina de 500g holixlab", "lote":"12345", "precioUnitario":500.5, "fechaCaducidad":"2026-05-04"}'
```

## Paso 7

Revis la base datos utilizandoselo la extensión de vscode
abre la plicacion en un navegador y añade /productos al final

