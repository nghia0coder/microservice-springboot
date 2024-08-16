package com.ecommercialapp.product.repositories;

import com.ecommercialapp.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    @Query("SELECT p FROM Product p WHERE p.id IN :ids ORDER BY p.id")
    List<Product> findAllByIdOrderById(List<Integer> ids);
}
