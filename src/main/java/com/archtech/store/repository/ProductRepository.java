package com.archtech.store.repository;

import com.archtech.store.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Spring Data JPA automatically gives you CRUD methods
}
