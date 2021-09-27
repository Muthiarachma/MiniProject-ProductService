package com.MiniProjek.models.entities;

import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "product_try")
@Data
//@SQLDelete(sql = "UPDATE product_try SET deleted = true WHERE id=?")
//@Where(clause = "deleted=false")
public class Product{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", length = 100)
    private String name;

    @Column(name = "product_desc", length = 500)
    private String description;

    private Double price;

    @Column(name = "category_id")
    private Long idCategory;
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    @ManyToOne
    private Category category;

//    private boolean deleted = Boolean.FALSE;

}
