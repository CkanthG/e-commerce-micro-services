package com.sree.ecommerce.repositories;

import com.sree.ecommerce.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
