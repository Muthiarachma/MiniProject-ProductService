package com.MiniProjek.repository;

import com.MiniProjek.models.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.websocket.server.PathParam;
import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Long> {

    @Query("SELECT p FROM Product p WHERE p.name LIKE :name")
    public List<Product> findProductByNameLike(@PathParam("name") String name);

    @Query("SELECT p FROM Product p WHERE p.category.idCategory = :idCategory")
    public List<Product> findProductByCategory(@PathParam("idCategory") Long idCategory);

    @Query("SELECT p FROM Product p ORDER BY p.price ASC")
    public List<Product> findProductByPrice(Double price);

}
