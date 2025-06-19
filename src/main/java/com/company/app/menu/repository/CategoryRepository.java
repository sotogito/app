package com.company.app.menu.repository;

import com.company.app.menu.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {


    @Query(value="SELECT category_code, category_name, ref_category_code FROM tbl_category WHERE ref_category_code IS NOT NULL"
            , nativeQuery = true)
    List<Category> findAllSubCategory();

    // 위의 Native Query 를 쿼리 메소드로 대체
    List<Category> findByRefCategoryCodeIsNotNullOrderByCategoryCodeDesc();






}
