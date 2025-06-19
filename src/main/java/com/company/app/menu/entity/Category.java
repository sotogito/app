package com.company.app.menu.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

@Entity(name="category")
@Table(name="tbl_category")
public class Category {
    @Id
    @Column(name="category_code")
    private Integer categoryCode;
    @Column(name="category_name")
    private String categoryName;
    @Column(name="ref_category_code")
    private Integer refCategoryCode;
}
