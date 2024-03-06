package com.Esliceu.Forum.forum.Controllers;

import com.Esliceu.Forum.forum.DTO.Request.TopicRequestDTO;
import com.Esliceu.Forum.forum.DTO.Response.TopicResponseDTO;
import com.Esliceu.Forum.forum.Exceptions.ActionNotAllowedException;
import com.Esliceu.Forum.forum.Model.*;
import com.Esliceu.Forum.forum.Services.CategoriesService;
import com.Esliceu.Forum.forum.Services.TopicService;
import com.Esliceu.Forum.forum.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TopicController {
    @Autowired
    TopicService topicService;
    @Autowired
    CategoriesService categoryService;
    @Autowired
    UserService userService;
    @GetMapping("/categories/{categorySlug}/topics")
    @CrossOrigin
    public List<TopicResponseDTO> getTopicsFromCategory(@PathVariable String categorySlug,
                                                        HttpServletResponse resp,
                                                        HttpServletRequest request){
        String token = request.getHeader("Authorization");
        return topicService.findAllTopicsFromCategory(categorySlug, token);
    }
    @PostMapping("/topics")
    @CrossOrigin
    public TopicResponseDTO createTopic(@RequestBody TopicRequestDTO topicRequest, HttpServletRequest req){
        String token = req.getHeader("Authorization");
        User u = userService.getUserFromToken(token);
        Permissions permissions= new Permissions();
        permissions.setRoot(u.getRole());

        u.setPermissions(permissions);
        System.out.println("El usuario es :"+u.toString());
        Category categoryOfTopic=categoryService.findCategory(topicRequest.getCategory());
        Topic createdTopic =
                topicService.createTopic(categoryOfTopic,topicRequest.getTitle(),topicRequest.getContent(),token);

        TopicResponseDTO responseDTO = topicService.castToDTO(createdTopic, u);

        return responseDTO;
    }


    @DeleteMapping("/topics/{topicID}")
    @CrossOrigin
    public boolean deleteTopic(@PathVariable Long topicID, HttpServletRequest request, HttpServletResponse response){
        String token = request.getHeader("Authorization");
        User user = userService.getUserFromToken(token);
        Topic topic = topicService.findTopicByID(topicID);
        if(user.getRole().equals("user")&& !topic.getUser().equals(user)){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            throw new ActionNotAllowedException("You are not allowed to delete this topic");

        }
        topicService.deleteTopic(topic);
        return true;
    }
    @PutMapping("/topics/{topicID}")
    @CrossOrigin
    public TopicResponseDTO modifyTopic(@PathVariable Long topicID,@RequestBody TopicRequestDTO topicRequest,
                                        HttpServletRequest request, HttpServletResponse response){
        String token = request.getHeader("Authorization");
        User user = userService.getUserFromToken(token);
        Topic topic = topicService.findTopicByID(topicID);
        Category category = categoryService.findCategory(topicRequest.getCategory());
        if(user.getRole().equals("user") && !topic.getUser().equals(user)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            throw new ActionNotAllowedException("You are not allowed to delete this topic");
        }
        topic.setCategory(category);
        topic.setContent(topicRequest.getContent());
        topic.setTitle(topicRequest.getTitle());

        topicService.modifyTopic(topic);
        return topicService.castToDTO(topic,user);
    }
}
