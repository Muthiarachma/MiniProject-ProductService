package com.MiniProjek.models.entities;

import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@Entity
@Table(name ="category_try")
//@SQLDelete(sql = "UPDATE category_try SET deleted = true WHERE idCategory=?")
//@Where(clause = "deleted=false")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategory;

    @Column(length = 100, nullable = false, unique = true)
    private String nameCategory;

//    private boolean deleted = Boolean.FALSE; //False = not deleted, TRUE = deleted
}
