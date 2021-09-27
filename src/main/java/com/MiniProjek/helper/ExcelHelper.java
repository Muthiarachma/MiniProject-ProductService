package com.MiniProjek.helper;

import com.MiniProjek.models.entities.Category;
import com.MiniProjek.models.entities.Product;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static ByteArrayInputStream productToExcel(List<Product> productList) throws IOException {

        String[] COLUMNs = {"Id", "Name", "Descriptions", "Price", "IdCategory"};
        String SHEET = "Products";

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            CreationHelper createHelper = workbook.getCreationHelper();

            Sheet sheet = workbook.createSheet(SHEET);

            // Row for Header
            Row headerRow = sheet.createRow(0);

            // Header
            for (int col = 0; col < COLUMNs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNs[col]);
            }

            int rowIdx = 1;
            for (Product product : productList) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getDescription());
                row.createCell(3).setCellValue(product.getPrice());
                row.createCell(4).setCellValue(product.getIdCategory());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public static List<Product> excelTOProduct(InputStream is) {
        String SHEET = "Products";

        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<Product> productList = new ArrayList<Product>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                Product product = new Product();

                int cellIndex = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIndex) {
                        case 0:
                            product.setId((long) currentCell.getNumericCellValue());
                            break;

                        case 1:
                            product.setName(currentCell.getStringCellValue());
                            break;

                        case 2:
                            product.setDescription(currentCell.getStringCellValue());
                            break;

                        case 3:
                            product.setPrice((double) currentCell.getNumericCellValue());
                            break;

                        case 4:
                            product.setIdCategory((long) currentCell.getNumericCellValue());
                            break;

                        default:
                            break;
                    }
                    cellIndex++;
                }

                productList.add(product);
            }
            // Close WorkBook
            workbook.close();
            return productList;
        } catch (
                IOException e) {
            throw new RuntimeException("FAIL! -> message = " + e.getMessage());
        }

    }

    public static ByteArrayInputStream categoryToExcel(List<Category> categoryList) throws IOException {
        String[] COLUMNs = {"IdCatgory", "NameCategory"};
        String SHEET = "Category";

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet(SHEET);

            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < COLUMNs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNs[col]);
            }

            int rowIdx = 1;
            for (Category category : categoryList) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(category.getIdCategory());
                row.createCell(1).setCellValue(category.getNameCategory());
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public static List<Category> excelToCategory(InputStream is) {
        String SHEET = "Category";

        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<Category> categoryList = new ArrayList<Category>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellInRow = currentRow.iterator();

                Category category = new Category();

                int cellIndex = 0;
                while (cellInRow.hasNext()) {
                    Cell currentCell = cellInRow.next();

                    switch (cellIndex) {
                        case 0:
                            category.setIdCategory((long) currentCell.getNumericCellValue());
                            break;

                        case 1:
                            category.setNameCategory(currentCell.getStringCellValue());
                            break;

                        default:
                            break;
                    }

                    cellIndex++;
                }
                categoryList.add(category);
            }
            workbook.close();
            return categoryList;
        } catch (IOException e) {
            throw new RuntimeException("FAIL! -> message = " + e.getMessage());
        }
    }
}
