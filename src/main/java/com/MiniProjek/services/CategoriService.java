package com.MiniProjek.services;

import com.MiniProjek.models.entities.Category;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface CategoriService {
    public Category save (Category category);
    public Category findOne (Long idCategory);
    public List<Category> findAll();
    public void removeOne (Long idCategory);

    public List<Category> findByNameCategoryStartingWith (String nameCategory);
    public List<Category> OrderByNameCategoryAsc ();
    public void saveCategoryCSV (MultipartFile file);
    public ByteArrayInputStream load();
}
