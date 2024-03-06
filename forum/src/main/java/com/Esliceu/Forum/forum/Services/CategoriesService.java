package com.Esliceu.Forum.forum.Services;

import com.Esliceu.Forum.forum.DTO.Response.CategoriesResponseDTO;
import com.Esliceu.Forum.forum.Model.Category;
import com.Esliceu.Forum.forum.Repositories.CategoriesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoriesService {
    @Autowired
    CategoriesRepo categoriesRepo;
    public List<CategoriesResponseDTO> retrieveAllCategories() {
        return categoriesRepo.findAll()
                .stream()
                .map(category -> new CategoriesResponseDTO(category.getCategoryID(),
                        category.getTitle(),
                        category.getSlug(),
                        category.getDescription(),
                        category.getColor(),
                                0)
                        ).collect(Collectors.toList());
    }
    public List<String> retrieveCategoriesNames(){
        return categoriesRepo.findAll()
                .stream().map(Category::getTitle)
                .collect(Collectors.toList());
    }
    public CategoriesResponseDTO findCategoryDTO(String categorySlug) {
        Optional<Category> category =categoriesRepo.findByslug(categorySlug);
        if(category.isPresent()){
            return new CategoriesResponseDTO(category.get().getCategoryID(),
                    category.get().getTitle(),
                    category.get().getSlug(),
                    category.get().getDescription(),
                    category.get().getColor(),
                    0);
        }
            throw new RuntimeException("No category");


    }

    public Category findCategory(String categorySlug){
        return categoriesRepo.findByslug(categorySlug).get();
    }
    //MODIFICABLE SI HAY QUE ACTUALIZAR EL SLUG
    public CategoriesResponseDTO modifyCategory(String title, String description, String categorySlug){
        Category category = categoriesRepo.findByslug(categorySlug).get();
        category.setTitle(title);
        category.setDescription(description);
        categoriesRepo.save(category);
        return new CategoriesResponseDTO(category.getCategoryID(),category.getTitle(),category.getSlug(),
                category.getDescription(),category.getColor(),0);
    }
    public CategoriesResponseDTO saveCategory(String title, String description) {
        Category newCategory = new Category();
        newCategory.setTitle(title);
        newCategory.setDescription(description);


        //revisar que no exista otra categoria con ese slug
            //prepararelSlug
        String slug = title.toLowerCase().replaceAll("\\s", "");
        newCategory.setSlug(generateSlug(slug));

        Long id = categoriesRepo.save(newCategory).getCategoryID();
        System.out.println(id);
        return new CategoriesResponseDTO(id,title, newCategory.getSlug(), description,null,0);
    }


    private List<String> findSlugs(String slug){
        return categoriesRepo.findBySlugContaining(slug).stream().map(Category::getSlug)
                .collect(Collectors.toList());
    }

    public void deleteCategory(String categorySlug) {
        //categoriesRepo.deleteBySlug(categorySlug);
        Category category = categoriesRepo.findByslug(categorySlug).get();
        System.out.println(category.toString());
        categoriesRepo.delete(category);
    }

    private String generateSlug(String slug){
        if(categoriesRepo.findByslug(slug).isPresent()){
            List<String> existingSlugs = findSlugs(slug);
            int contador = 1;
            while (existingSlugs.contains(slug + "-" + contador)) {
                contador++;
            }
            slug = slug + "-" + contador;
        }
        return slug;
    }
}
