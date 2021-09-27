package com.MiniProjek.services;

import com.MiniProjek.helper.CSVHelper;
import com.MiniProjek.helper.ExcelHelper;
import com.MiniProjek.models.entities.Category;
import com.MiniProjek.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.TransactionScoped;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@TransactionScoped
public class CategoryServiceImpl implements CategoriService{

    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public Category save (Category category){
        if(category.getIdCategory()!=null){
            Category hasil = categoryRepo.findById(category.getIdCategory()).get();
            hasil.setNameCategory(category.getNameCategory());
            category = hasil;
        }
        return categoryRepo.save(category);
    }

    @Override
    public Category findOne (Long idCategory) {
        Optional<Category> category = categoryRepo.findById(idCategory);
        if (category.isPresent()) {
            return category.get();
        }
        return null;
    }

    @Override
    public List<Category> findAll(){
        List<Category> categoryList = new ArrayList<>();
        categoryList.addAll(categoryRepo.findAll());
        return categoryList;
    }

    @Override
    public void removeOne (Long idCategory){
        categoryRepo.deleteById(idCategory);
    }

    @Override
    public List<Category> findByNameCategoryStartingWith (String nameCategory){
        return categoryRepo.findByNameCategoryStartingWith(nameCategory);
    }

    @Override
    public List<Category> OrderByNameCategoryAsc (){
        return categoryRepo.OrderByNameCategoryAsc();
    }

    @Override
    public void saveCategoryCSV (MultipartFile file){
        try {
            List<Category> categoryList = CSVHelper.csvToCategory (file.getInputStream());
            categoryRepo.saveAll(categoryList);
        }catch (IOException e){
            throw new RuntimeException("gagal, " + e.getMessage());
        }
    }
    @Override
    public ByteArrayInputStream load() {
        List<Category> categoryList = categoryRepo.findAll();

        ByteArrayInputStream in = CSVHelper.dbToCSVCategory(categoryList);
        return in;
    }

    // Store File Data to Database
    public void saveExcelToDb(MultipartFile file) {
        try {
            List<Category> categoryList = ExcelHelper.excelToCategory(file.getInputStream());
            categoryRepo.saveAll(categoryList);
        } catch (IOException e) {
            throw new RuntimeException("Fail! -> message = " + e.getMessage());
        }
    }

    // Load Data to Excel File
    public ByteArrayInputStream loadExcel () {
        try {
            List<Category> categoryList = categoryRepo.findAll();
            ByteArrayInputStream in = ExcelHelper.categoryToExcel(categoryList);
            return in;

        } catch (IOException e) {
            throw new RuntimeException("Fail! -> message = " + e.getMessage());
        }
    }
}
