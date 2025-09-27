package com.archtech.store.services;

import com.archtech.store.model.*;
import com.archtech.store.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    public final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAllProducts() {
        return this.repository.findAll();
    }

    public Optional<Product> getProduct(long id) {
        return this.repository.findById(id);
    }

    public Product createProduct(Product product) {
        return this.repository.save(product);
    }

    public Product updateProduct(long id, Product product) {
        product.setId(id);
        return this.repository.save(product);
    }

    public void deleteProduct(long id) {
        this.repository.deleteById(id);
    }

}