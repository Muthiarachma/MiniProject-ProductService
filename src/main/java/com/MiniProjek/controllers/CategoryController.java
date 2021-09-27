package com.MiniProjek.controllers;

import com.MiniProjek.models.dto.CategoryDTO;
import com.MiniProjek.models.entities.Category;
import com.MiniProjek.models.entities.ResponseMessage;
import com.MiniProjek.helper.CSVHelper;
import com.MiniProjek.services.CategoryServiceImpl;
import com.MiniProjek.helper.ExcelHelper;
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
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryServiceImpl;

    @Autowired
    private ModelMapper modelMapper;

    private CategoryDTO convertToDTO (Category category){
        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
        return categoryDTO;
    }

    private Category convertToEntity (CategoryDTO categoryDTO){
        Category category = modelMapper.map (categoryDTO, Category.class);
        return category;
    }

    @PostMapping
    public CategoryDTO save (@RequestBody CategoryDTO categoryDTO){
        Category category = convertToEntity(categoryDTO);
        Category category1 = categoryServiceImpl.save(category);
        return convertToDTO(category1);
    }

     @PutMapping
     public CategoryDTO update (@RequestBody CategoryDTO categoryDTO){
         Category category = convertToEntity(categoryDTO);
         Category category1 = categoryServiceImpl.save(category);
         return convertToDTO(category1);
     }

     @GetMapping("/{idCategory}")
    public CategoryDTO findId (@PathVariable("idCategory") Long idCategory){
        return convertToDTO(categoryServiceImpl.findOne(idCategory));
     }

     @GetMapping
    public List<CategoryDTO> findAll (){
         List<Category> categoryList = categoryServiceImpl.findAll();
         return categoryList.stream().map(this::convertToDTO).collect(Collectors.toList());
     }

     @DeleteMapping("/{idCategory}")
    public void delete (@PathVariable("idCategory") Long idCategory){
        categoryServiceImpl.removeOne(idCategory);
     }

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (CSVHelper.hasCSVFormat(file)) {
            try {
                categoryServiceImpl.saveCategoryCSV (file);
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
        InputStreamResource file = new InputStreamResource(categoryServiceImpl.load());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }

    @GetMapping("/search/namestartwith")
    public List<CategoryDTO> findByNameStartingWith(@RequestParam ("nameCategory") String nameCategory){
        List<Category> categoryList = categoryServiceImpl.findByNameCategoryStartingWith(nameCategory);
        return categoryList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @GetMapping("/order")
    public List<Category> OrderByNameAsc (){
        return categoryServiceImpl.OrderByNameCategoryAsc();
    }

    @PostMapping("/uploadExcel")
    public ResponseEntity<ResponseMessage> uploadFileExcel (@RequestParam("file") MultipartFile file) {
        String message = "";

        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                categoryServiceImpl.saveExcelToDb(file);
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
        InputStreamResource file = new InputStreamResource(categoryServiceImpl.loadExcel());


        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

}
