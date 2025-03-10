package com.yunha.flexforumback.forum.repository;

import com.yunha.flexforumback.forum.dto.CategoryDTO;
import com.yunha.flexforumback.forum.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT new com.yunha.flexforumback.forum.dto.CategoryDTO(c.categoryCode, c.name, c.enable) from Category c")
    List<CategoryDTO> findAllCategory();
}
