package com.MiniProjek.services;

import com.MiniProjek.models.entities.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;


public interface ProductService {
    public Product save (Product product);
    public void removeOne (Long id);
    public List<Product> findAll();
    public Product findOne(Long id);

    public List<Product> findByProductNameLike(String name);
    public List<Product> findByCategory(Long idCategory);
    public List<Product> findProductByPrice(Double price);
    public void saveProductCsv (MultipartFile file);
    public ByteArrayInputStream load();

}
