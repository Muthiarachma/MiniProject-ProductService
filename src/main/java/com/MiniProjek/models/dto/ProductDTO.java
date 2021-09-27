package com.MiniProjek.models.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

@Data
public class ProductDTO {

    private Long id;

    private String name;

    private String description;

    private Double price;

    private Long idCategory;

    private String nameCategory;


}
