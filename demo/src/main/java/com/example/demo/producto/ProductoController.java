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