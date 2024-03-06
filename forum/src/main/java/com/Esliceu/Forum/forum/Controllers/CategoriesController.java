package com.Esliceu.Forum.forum.Controllers;

import com.Esliceu.Forum.forum.DTO.Request.CategoriesRequestDTO;
import com.Esliceu.Forum.forum.DTO.Response.CategoriesResponseDTO;
import com.Esliceu.Forum.forum.Exceptions.ActionNotAllowedException;
import com.Esliceu.Forum.forum.Model.User;
import com.Esliceu.Forum.forum.Services.CategoriesService;
import com.Esliceu.Forum.forum.Services.TopicService;
import com.Esliceu.Forum.forum.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoriesController {
    @Autowired
    CategoriesService categoriesService;
    @Autowired
    TopicService topicService;
    @Autowired
    UserService userService;
    @GetMapping("/categories")
    @CrossOrigin
    public List<CategoriesResponseDTO> getCategories (HttpServletResponse resp){
        return categoriesService.retrieveAllCategories();
    }
    @PostMapping("/categories")
    @CrossOrigin
    public CategoriesResponseDTO crearCategoria(HttpServletResponse resp,
                                                @RequestBody CategoriesRequestDTO categoriesRequest){
        String title= categoriesRequest.title();
        String description = categoriesRequest.description();
        System.out.println(title);
        System.out.println(description);
        return categoriesService.saveCategory(title, description);

    }
    @GetMapping("/categories/{categorySlug}")
    @CrossOrigin
    public CategoriesResponseDTO getCategoryInfo(@PathVariable String categorySlug, HttpServletResponse resp){

        CategoriesResponseDTO categoryResponse = categoriesService.findCategoryDTO(categorySlug);
        return categoryResponse;
    }
    @PutMapping("/categories/{categorySlug}")
    @CrossOrigin
    public CategoriesResponseDTO modifyCategory(@PathVariable String categorySlug,
                                                @RequestBody CategoriesRequestDTO categoryRequest,
                                                HttpServletRequest request,
                                                HttpServletResponse response){
        String token = request.getHeader("Authorization");
        User user = userService.getUserFromToken(token);
        if(user.getRole().equals("user")){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            throw new ActionNotAllowedException("You are not allowed to do this");

        }
        return categoriesService.modifyCategory(categoryRequest.title(),categoryRequest.description(), categorySlug);
    }
    @DeleteMapping("/categories/{categorySlug}")
    @CrossOrigin
    public boolean deleteCategory(@PathVariable String categorySlug, HttpServletResponse response,
                                  HttpServletRequest request){
        String token = request.getHeader("Authorization");
        User user = userService.getUserFromToken(token);
        if(user.getRole().equals("user")){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            throw new ActionNotAllowedException("You are not allowed to do this");

        }
        System.out.println("El slug es "+categorySlug);
        categoriesService.deleteCategory(categorySlug);
        return true;
    }



}
