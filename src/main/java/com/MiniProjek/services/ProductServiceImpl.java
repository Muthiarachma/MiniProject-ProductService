package com.MiniProjek.services;

import com.MiniProjek.helper.CSVHelper;
import com.MiniProjek.helper.ExcelHelper;
import com.MiniProjek.models.entities.Product;
import com.MiniProjek.repository.ProductRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;

    public ProductServiceImpl(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public Product save (Product product) {
        if(product.getId()!=null){
            Product hasil = productRepo.findById(product.getId()).get();
            hasil.setName(product.getName());
            hasil.setPrice(product.getPrice());
            hasil.setDescription(product.getDescription());
            hasil.setIdCategory(product.getIdCategory());
        }
        return productRepo.save(product);
    }

    @Override
    public Product findOne(Long id) {
        Optional<Product> product = productRepo.findById(id);
        if(!product.isPresent()){
            return null;
        }
        return product.get();
    }

    @Override
    public List<Product> findAll() {
        List<Product> productList = new ArrayList<>();
        productList.addAll(productRepo.findAll());
        return productList;
    }

    @Override
    public void removeOne (Long id){
        productRepo.deleteById(id);
    }

    @Override
    public List<Product> findByProductNameLike(String name){
        List<Product> productList = new ArrayList<>();
        productRepo.findProductByNameLike("%"+ name +"%").forEach(productList::add);
        return productList;
    }

    @Override
    public List<Product> findByCategory(Long idCategory){
        return productRepo.findProductByCategory(idCategory);
    }

    @Override
    public List<Product> findProductByPrice(Double price){
        return productRepo.findProductByPrice(price);
    }

    @Override
    public void saveProductCsv (MultipartFile file){
        try {
            List<Product> productList = CSVHelper.csvToProduct (file.getInputStream());
            productRepo.saveAll(productList);
        }catch (IOException e){
            throw new RuntimeException("gagal, " + e.getMessage());
        }
    }

    @Override
    public ByteArrayInputStream load() {
        List<Product> productList = productRepo.findAll();

        ByteArrayInputStream in = CSVHelper.dbToCSVProduct(productList);
        return in;
    }

    public void saveExcelToDb(MultipartFile file) {
        try {
            List<Product> productList = ExcelHelper.excelTOProduct(file.getInputStream());
            productRepo.saveAll(productList);
        } catch (IOException e) {
            throw new RuntimeException("Fail! -> message = " + e.getMessage());
        }
    }

    // Load Data to Excel File
    public ByteArrayInputStream loadExcel () {
        try {
            List<Product> productList = productRepo.findAll();
            ByteArrayInputStream in = ExcelHelper.productToExcel(productList);
            return in;

        } catch (IOException e) {
            throw new RuntimeException("Fail! -> message = " + e.getMessage());
        }
    }
}