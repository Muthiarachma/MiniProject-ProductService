package com.MiniProjek.controllers;


import com.MiniProjek.models.dto.ProductDTO;
//import com.MiniProjek.models.dto.SearchDataDTO;
//import com.MiniProjek.models.dto.TampungDTO;
import com.MiniProjek.models.entities.Product;
//import com.MiniProjek.helper.CSVHelper;
import com.MiniProjek.models.entities.ResponseMessage;
import com.MiniProjek.helper.CSVHelper;
import com.MiniProjek.helper.ExcelHelper;
import com.MiniProjek.services.ProductServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private ModelMapper modelMapper;


    private ProductDTO convertToDTO (Product product){
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        return productDTO;
    }

    private Product convertToEntity (ProductDTO productDTO){
        Product product = modelMapper.map (productDTO, Product.class);
        return product;
    }

    @PostMapping
    public ProductDTO save (@RequestBody ProductDTO productDTO){
        Product product = convertToEntity(productDTO);
        Product product1 = productService.save(product);
        return convertToDTO(product1);
    }

    @PutMapping
    public ProductDTO update (@RequestBody ProductDTO productDTO){
        Product product = convertToEntity(productDTO);
        Product product1 = productService.save(product);
        return convertToDTO(product1);
    }

    @GetMapping
    public List<ProductDTO> findAll() {
        List<Product> productList = productService.findAll();
        return productList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProductDTO findOne(@PathVariable("id") Long id) {
        return convertToDTO(productService.findOne(id));
    }

    @DeleteMapping("/{id}")
    public void removeOne(@PathVariable("id") Long id) {
        productService.removeOne(id);
    }

    @GetMapping("/search/category/{idCategory}")
    public List<ProductDTO> getProductByCategory(@PathVariable("idCategory") Long idCategory) {
        List<Product> productList = productService.findByCategory(idCategory);
        return productList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @GetMapping("/order-price")
    public List<ProductDTO> findProductByPrice(Double price){
        List<Product> productList = productService.findProductByPrice(price);
        return productList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @PostMapping("/search/namelike")
    public List<ProductDTO> getProductByNameLike(@RequestParam ("name") String name) {
        List<Product> productList = productService.findByProductNameLike(name);
        return productList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (CSVHelper.hasCSVFormat(file)) {
            try {
                productService.saveProductCsv(file);
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<InputStreamResource> getFile (@PathVariable String fileName) {
        InputStreamResource file = new InputStreamResource(productService.load());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }

    @PostMapping("/uploadExcel")
    public ResponseEntity<ResponseMessage> uploadFileExcel (@RequestParam("file") MultipartFile file) {
        String message = "";

        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                productService.saveExcelToDb(file);
                message = "Upload the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }

        message = "Please upload an excel file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }

    @GetMapping("/downloadExcel/{filename:.+}")
    public ResponseEntity<InputStreamResource> downloadFileExcel(@PathVariable String filename) {
        InputStreamResource file = new InputStreamResource(productService.loadExcel());


        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
}
