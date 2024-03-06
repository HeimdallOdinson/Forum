package com.Esliceu.Forum.forum.Repositories;

import com.Esliceu.Forum.forum.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriesRepo extends JpaRepository<Category,Long > {

    Optional<Category> findByslug(String categorySlug);

    List<Category> findBySlugContaining(String slug);

    void deleteBySlug(String categorySlug);
}
